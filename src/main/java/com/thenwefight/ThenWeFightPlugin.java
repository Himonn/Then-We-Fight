package com.thenwefight;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@PluginDescriptor(
		name = "Then We Fight",
		description = "Then We Fight Gamemode Plugin"
)
@Slf4j
public class ThenWeFightPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ClientThread clientThread;
	@Inject
	private ThenWeFightConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ConfigManager configManager;
	@Inject
	private ThenWeFightUnlockOverlay unlockOverlay;
	@Inject
	private ThenWeFightItemOverlay itemOverlay;
	@Inject
	private ThenWeFightSceneOverlay sceneOverlay;
	@Inject
	private ThenWeFightWidgetOverlay widgetOverlay;
	@Inject
	private ThenWeFightTaskOverlay taskOverlay;

	public static String[] rawTaskList;

	public static List<Integer> unlockedItems = new ArrayList<>();
	public static List<String> unlockedNpcs = new ArrayList<>();
	public static List<Integer> lockedWidgets = new ArrayList<>();
	public static List<String> lockedObjects = new ArrayList<>();

	public static Collection<String> bankNpcs = Arrays.asList("banker", "grand exchange clerk", "banker tutor");
	public static Collection<String> bankObjects = Arrays.asList("bank booth", "bank chest", "bank deposit box", "bank deposit chest", "grand exchange booth");
	public static Collection<String> underGroundObjects = Arrays.asList("stairs", "staircase", "trapdoor", "ladder", "dark hole", "hole", "rope", "cavern entrance", "dive", "cave", "tunnel entrance");
	public static Collection<String> stairObjects = Arrays.asList("stairs", "staircase");
	public static Collection<String> doorObjects = Arrays.asList("door", "large door");

	public static Collection<GameObject> gameObjects = new ArrayList<>();
	public static Collection<WallObject> wallObjects = new ArrayList<>();
	public static Collection<GroundObject> groundObjects = new ArrayList<>();
	public static Collection<NPC> npcs = new ArrayList<>();

	private static final File U_1_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/1.png");
	private static final File U_2_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/2.png");
	private static final File U_3_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/3.png");
	private static final File U_4_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/4.png");
	private static final File U_5_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/5.png");
	private static final File U_6_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/6.png");

	public static final Collection<Integer> OBJECT_ACTIONS = Arrays.asList(MenuAction.EXAMINE_OBJECT.getId(),
			MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), MenuAction.GAME_OBJECT_SECOND_OPTION.getId(),
			MenuAction.GAME_OBJECT_THIRD_OPTION.getId(), MenuAction.GAME_OBJECT_FOURTH_OPTION.getId(),
			MenuAction.GAME_OBJECT_FIFTH_OPTION.getId());
	public static final Collection<Integer> NPC_ACTIONS = Arrays.asList(MenuAction.EXAMINE_NPC.getId(),
			MenuAction.NPC_FIRST_OPTION.getId(), MenuAction.NPC_SECOND_OPTION.getId(),
			MenuAction.NPC_THIRD_OPTION.getId(), MenuAction.NPC_FOURTH_OPTION.getId(),
			MenuAction.NPC_FIFTH_OPTION.getId());
	public static final Collection<Integer> GROUND_ITEM_ACTIONS = Arrays.asList(MenuAction.EXAMINE_ITEM_GROUND.getId(),
			MenuAction.GROUND_ITEM_FIRST_OPTION.getId(), MenuAction.GROUND_ITEM_SECOND_OPTION.getId(),
			MenuAction.GROUND_ITEM_THIRD_OPTION.getId(), MenuAction.GROUND_ITEM_FOURTH_OPTION.getId(),
			MenuAction.GROUND_ITEM_FIFTH_OPTION.getId());

	public static String BACKGROUND_PATH = "background.png";
	public static String DEBUG_PATH = "debug.png";
	public static String HEALTH_PATH = "health.png";
	public static String PRAYER_PATH = "prayer.png";
	public static String RUN_PATH = "run.png";
	public static String SPEC_PATH = "spec.png";

	@Getter
	private Image background;
	@Getter
	private Image debugBackground;
	@Getter
	private Image runOrb;
	@Getter
	private Image prayerOrb;
	@Getter
	private Image specOrb;
	@Getter
	private Image healthOrb;
	@Getter
	private Image u1Custom;
	@Getter
	private Image u2Custom;
	@Getter
	private Image u3Custom;
	@Getter
	private Image u4Custom;
	@Getter
	private Image u5Custom;
	@Getter
	private Image u6Custom;

	public boolean unlockOverlayVisible = false;
	public boolean taskOverlayVisible = false;

	public int plane = -1;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(itemOverlay);
		overlayManager.add(sceneOverlay);
		overlayManager.add(unlockOverlay);
		overlayManager.add(widgetOverlay);
		overlayManager.add(taskOverlay);

		updateItemList();
		updateNpcList();
		updateWidgetList();
		updateGameObjectList();
		updateTaskList();
		loadResources();

		if (U_1_CUSTOM_DIR.exists())
		{
			try
			{
				synchronized (ImageIO.class)
				{
					u1Custom = ImageIO.read(U_1_CUSTOM_DIR);
				}
			}
			catch (Exception e)
			{
				log.error("thenwefightplugin: error setting custom task 1 image", e);
			}
		}

		if (U_2_CUSTOM_DIR.exists())
		{
			try
			{
				synchronized (ImageIO.class)
				{
					u2Custom = ImageIO.read(U_2_CUSTOM_DIR);
				}
			}
			catch (Exception e)
			{
				log.error("thenwefightplugin: error setting custom task 2 image", e);
			}
		}

		if (U_3_CUSTOM_DIR.exists())
		{
			try
			{
				synchronized (ImageIO.class)
				{
					u3Custom = ImageIO.read(U_3_CUSTOM_DIR);
				}
			}
			catch (Exception e)
			{
				log.error("thenwefightplugin: error setting custom task 3 image", e);
			}
		}

		if (U_4_CUSTOM_DIR.exists())
		{
			try
			{
				synchronized (ImageIO.class)
				{
					u4Custom = ImageIO.read(U_4_CUSTOM_DIR);
				}
			}
			catch (Exception e)
			{
				log.error("thenwefightplugin: error setting custom task 4 image", e);
			}
		}

		if (U_5_CUSTOM_DIR.exists())
		{
			try
			{
				synchronized (ImageIO.class)
				{
					u5Custom = ImageIO.read(U_5_CUSTOM_DIR);
				}
			}
			catch (Exception e)
			{
				log.error("thenwefightplugin: error setting custom task 5 image", e);
			}
		}

		if (U_6_CUSTOM_DIR.exists())
		{
			try
			{
				synchronized (ImageIO.class)
				{
					u6Custom = ImageIO.read(U_6_CUSTOM_DIR);
				}
			}
			catch (Exception e)
			{
				log.error("thenwefightplugin: error setting custom task 6 image", e);
			}
		}

		unlockOverlayVisible = false;
		taskOverlayVisible = false;

		plane = -1;
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(itemOverlay);
		overlayManager.remove(sceneOverlay);
		overlayManager.remove(unlockOverlay);
		overlayManager.remove(widgetOverlay);
		overlayManager.remove(taskOverlay);

		updateItemList();
		updateNpcList();
		updateWidgetList();
		updateGameObjectList();

		unlockOverlayVisible = false;
		taskOverlayVisible = false;

		plane = -1;
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("thenwefight"))
		{
			return;
		}

		if (event.getKey().equals("unlockedItems"))
		{
			updateItemList();
		}

		if (event.getKey().equals("unlockedNpcs")
				|| event.getKey().equals("unlockNpcs")
				|| event.getKey().equals("lockBanks"))
		{
			updateNpcList();
		}

		if (event.getKey().equals("lockedGameObjects")
				|| event.getKey().equals("lockGameObjects")
				|| event.getKey().equals("lockBanks")
				|| event.getKey().equals("lockUnderground")
				|| event.getKey().equals("lockStairs")
				|| event.getKey().equals("lockDoors"))
		{
			updateGameObjectList();
		}

		if (event.getKey().equals("lockedWidgets"))
		{
			updateWidgetList();
		}

		if (event.getKey().equals("taskList"))
		{
			updateTaskList();
		}
	}

	@Provides
	ThenWeFightConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ThenWeFightConfig.class);
	}

	@Subscribe
	private void onMenuEntryAdded(MenuEntryAdded event)
	{
		String target = event.getTarget();
		String cleanTarget = Text.standardize(target);
		String option = event.getOption();
		int type = event.getType();
		int identifier = event.getIdentifier();
		int param1 = event.getActionParam1();
		int param0 = event.getActionParam0();
		Widget widget = event.getMenuEntry().getWidget();

		if (param1 == 10747957 || param1 == 10551356 || param1 == 35913792)
		{
			client.createMenuEntry(-1)
					.setOption(ColorUtil.prependColorTag(unlockOverlayVisible ? "Hide Unlocks" : "View Unlocks", Color.ORANGE))
					.setParam1(10747957)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE);


			client.createMenuEntry(-2)
					.setOption(ColorUtil.prependColorTag(taskOverlayVisible ? "Hide Tasks" : "View Tasks", Color.ORANGE))
					.setParam1(10747957)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE);
		}

        if (param1 == WidgetInfo.INVENTORY.getId()
                && type == MenuAction.CC_OP_LOW_PRIORITY.getId()
                && client.isKeyPressed(KeyCode.KC_SHIFT)
				&& identifier == 10
				&& event.getMenuEntry().getWidget() != null
				&& config.unlockItems())
        {
			boolean unlocked = unlockedItems.contains(event.getMenuEntry().getWidget().getItemId());

			if (widget != null)
			{
				client.createMenuEntry(-1)
						.setOption(ColorUtil.prependColorTag(unlocked ? "Lock": "Unlock", Color.ORANGE))
						.setTarget(target)
						.setIdentifier(identifier)
						.setParam0(widget.getItemId())
						.setParam1(param1)
						.setType(MenuAction.RUNELITE);
			}
        }

		if (type == MenuAction.EXAMINE_NPC.getId()
			&& client.isKeyPressed(KeyCode.KC_SHIFT)
			&& config.unlockNpcs())
		{
			final int id = event.getMenuEntry().getIdentifier();
			final NPC[] cachedNPCs = client.getCachedNPCs();
			final NPC npc = cachedNPCs[id];

			if (npc != null && npc.getName() != null)
			{
				boolean unlocked = unlockedNpcs.contains(npc.getName().toLowerCase());

				client.createMenuEntry(-1)
						.setOption(ColorUtil.prependColorTag(unlocked ? "Lock NPC": "Unlock NPC", Color.ORANGE))
						.setTarget(target)
						.setIdentifier(identifier)
						.setParam0(param0)
						.setParam1(param1)
						.setType(MenuAction.RUNELITE);
			}
		}

		if (type == MenuAction.EXAMINE_OBJECT.getId()
				&& client.isKeyPressed(KeyCode.KC_SHIFT)
				&& config.lockGameObjects())
		{
			final int id = event.getMenuEntry().getIdentifier();

			boolean unlocked = lockedObjects.contains(getGameObjectName(id));

			client.createMenuEntry(-1)
					.setOption(ColorUtil.prependColorTag(unlocked ? "Unlock Object": "Lock Object", Color.ORANGE))
					.setTarget(target)
					.setIdentifier(identifier)
					.setParam0(param0)
					.setParam1(param1)
					.setType(MenuAction.RUNELITE);
		}

		// if youre reading my code for some reason and see this, please continue with your life and pay no attention to it
		if ((config.lockAttacking() && type == MenuAction.NPC_SECOND_OPTION.getId())
				|| (config.unlockItems() && param1 == WidgetInfo.INVENTORY.getId() && !unlockedItems.contains(widget.getItemId()))
				|| (config.lockDrinking() && option.equals("Drink"))
				|| (config.lockEating() && option.equals("Eat"))
				|| (config.lockEating() && option.equals("Consume"))
				|| (config.lockCoins() && type == MenuAction.GROUND_ITEM_THIRD_OPTION.getId() && target.contains("Coins"))
				|| (config.lockTeles() && option.toLowerCase().contains("teleport"))
				|| (config.lockTeles() && target.toLowerCase().contains("teleport"))
				|| (config.lockTeles() && option.toLowerCase().contains("tele"))
				|| (config.lockTeles() && target.toLowerCase().contains("tele"))
				|| (config.lockBanks() && target.toLowerCase().contains("bank"))
				|| (config.lockGameObjects() && lockedObjects.contains(cleanTarget) && OBJECT_ACTIONS.contains(type))
				|| (config.lockBanks() && bankObjects.contains(cleanTarget) && OBJECT_ACTIONS.contains(type))
				|| (config.lockUnderground() && underGroundObjects.contains(cleanTarget) && OBJECT_ACTIONS.contains(type))
				|| (config.lockBanks() && bankNpcs.contains(cleanTarget) && NPC_ACTIONS.contains(type)))
		{
			event.getMenuEntry().setOption(ColorUtil.prependColorTag(option,config.overlayColour()));
			event.getMenuEntry().setTarget(ColorUtil.prependColorTag(Text.removeTags(target),config.overlayColour()));
			event.getMenuEntry().setDeprioritized(true);
		}
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		String target = event.getMenuTarget();
		String cleanTarget = Text.standardize(target);
		String option = event.getMenuOption();
		int type = event.getMenuAction().getId();
		int identifier = event.getId();
		int param1 = event.getParam1();
		int param0 = event.getParam0();
		MenuEntry entry = event.getMenuEntry();
		Widget widget = entry.getWidget();

		if (option.equals("Walk here") && (taskOverlayVisible || unlockOverlayVisible))
		{
			unlockOverlayVisible = false;
			taskOverlayVisible = false;
		}

		if (param1 == 10747957 && option.equals(ColorUtil.prependColorTag("View Unlocks", Color.ORANGE)))
		{
			event.consume();
			unlockOverlayVisible = true;
			taskOverlayVisible = false;
		}

		if (param1 == 10747957 && option.equals(ColorUtil.prependColorTag("Hide Unlocks", Color.ORANGE)))
		{
			event.consume();
			unlockOverlayVisible = false;
		}

		if (param1 == 10747957 && option.equals(ColorUtil.prependColorTag("View Tasks", Color.ORANGE)))
		{
			event.consume();
			taskOverlayVisible = true;
			unlockOverlayVisible = false;
		}

		if (param1 == 10747957 && option.equals(ColorUtil.prependColorTag("Hide Tasks", Color.ORANGE)))
		{
			event.consume();
			taskOverlayVisible = false;
		}

		if (param1 == WidgetInfo.INVENTORY.getId()
				&& type == MenuAction.RUNELITE.getId()
				&& param0 != 0
		)
		{
			if (option.equals(ColorUtil.prependColorTag("Unlock", Color.ORANGE)))
			{
				event.consume();
				unlockedItems.add(param0);
				configManager.setConfiguration("thenwefight", "unlockedItems", unlockedItems.toString().replace(" ", "").replace("[", "").replace("]", ""));
			}

			if (option.equals(ColorUtil.prependColorTag("Lock", Color.ORANGE)))
			{
				event.consume();
				unlockedItems.remove((Integer) param0);
				configManager.setConfiguration("thenwefight", "unlockedItems", unlockedItems.toString().replace(" ", "").replace("[", "").replace("]", ""));
			}
		}

		if (type == MenuAction.RUNELITE.getId() && option.equals(ColorUtil.prependColorTag("Unlock NPC", Color.ORANGE)))
		{
			event.consume();

			final int id = event.getMenuEntry().getIdentifier();
			final NPC[] cachedNPCs = client.getCachedNPCs();
			final NPC npc = cachedNPCs[id];

			if (npc != null && npc.getName() != null)
			{
				unlockedNpcs.add(npc.getName().toLowerCase());
				configManager.setConfiguration("thenwefight", "unlockedNpcs", unlockedNpcs.toString().replace("[", "").replace("]", ""));
			}
		}

		if (type == MenuAction.RUNELITE.getId() && option.equals(ColorUtil.prependColorTag("Lock NPC", Color.ORANGE)))
		{
			event.consume();

			final int id = event.getMenuEntry().getIdentifier();
			final NPC[] cachedNPCs = client.getCachedNPCs();
			final NPC npc = cachedNPCs[id];

			if (npc != null && npc.getName() != null)
			{
				unlockedNpcs.remove(npc.getName().toLowerCase());
				configManager.setConfiguration("thenwefight", "unlockedNpcs", unlockedNpcs.toString().replace("[", "").replace("]", ""));
			}
		}

		if (type == MenuAction.RUNELITE.getId() && option.equals(ColorUtil.prependColorTag("Lock Object", Color.ORANGE)))
		{
			event.consume();
			int id = event.getId();

			lockedObjects.add(getGameObjectName(id));
			configManager.setConfiguration("thenwefight", "lockedGameObjects", lockedObjects.toString().replace("[", "").replace("]", ""));
		}

		if (type == MenuAction.RUNELITE.getId() && option.equals(ColorUtil.prependColorTag("Unlock Object", Color.ORANGE)))
		{
			event.consume();
			int id = event.getId();

			lockedObjects.remove(getGameObjectName(id));
			configManager.setConfiguration("thenwefight", "lockedGameObjects", lockedObjects.toString().replace("[", "").replace("]", ""));
			updateGameObjects();
		}
	}

	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		GameObject object = event.getGameObject();
		ObjectComposition objectComposition = getObjectComposition(object.getId());

		if (objectComposition == null || objectComposition.getName() == null || objectComposition.getName().equals(""))
		{
			return;
		}

		String name = objectComposition.getName().toLowerCase();

		if (config.lockBanks() && bankObjects.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}

		if (config.lockUnderground() && underGroundObjects.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}

		if (config.lockGameObjects() && lockedObjects.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}

		if (config.lockDoors() && doorObjects.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}

		if (config.lockStairs() && stairObjects.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}
	}

	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned event)
	{
		gameObjects.remove(event.getGameObject());
	}

	@Subscribe
	private void onWallObjectSpawned(WallObjectSpawned event)
	{
		WallObject object = event.getWallObject();

		ObjectComposition objectComposition = getObjectComposition(object.getId());

		if (objectComposition == null || objectComposition.getName() == null || objectComposition.getName().equals(""))
		{
			return;
		}

		String name = objectComposition.getName().toLowerCase();

		if (config.lockBanks() && bankObjects.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}

		if (config.lockUnderground() && underGroundObjects.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}

		if (config.lockGameObjects() && lockedObjects.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}

		if (config.lockDoors() && doorObjects.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}

		if (config.lockStairs() && stairObjects.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}
	}

	@Subscribe
	private void onWallObjectDespawned(WallObjectDespawned event)
	{
		wallObjects.remove(event.getWallObject());
	}

	@Subscribe
	private void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		GroundObject object = event.getGroundObject();
		ObjectComposition objectComposition = getObjectComposition(object.getId());

		if (objectComposition == null || objectComposition.getName() == null || objectComposition.getName().equals(""))
		{
			return;
		}

		String name = objectComposition.getName().toLowerCase();

		if (config.lockBanks() && bankObjects.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}

		if (config.lockUnderground() && underGroundObjects.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}

		if (config.lockGameObjects() && lockedObjects.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}

		if (config.lockDoors() && doorObjects.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}

		if (config.lockStairs() && stairObjects.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}
	}

	@Subscribe
	private void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		groundObjects.remove(event.getGroundObject());
	}

	@Subscribe
	private void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();

		if (npc.getName() == null || npc.getName().equals(""))
		{
			return;
		}

		if (config.lockBanks() && bankNpcs.contains(npc.getName().toLowerCase()) && !npcs.contains(npc))
		{
			npcs.add(npc);
		}

		if (config.unlockNpcs() && !unlockedNpcs.contains(npc.getName().toLowerCase()) && !npcs.contains(npc))
		{
			npcs.add(npc);
		}
	}

	@Subscribe
	private void onNpcDespawned(NpcDespawned event)
	{
		npcs.remove(event.getNpc());
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState().equals(GameState.LOGGED_IN))
		{
			updateNpcs();
			updateGameObjects();
		}
	}

	@Subscribe
	private void onGameTick(GameTick event)
	{
		Player local = client.getLocalPlayer();

		if (local == null)
		{
			return;
		}

		int localPlane = local.getWorldLocation().getPlane();

		if (plane == -1 || plane != localPlane)
		{
			plane = localPlane;
			updateGameObjects();
		}
	}

	public void updateItemList()
	{
		unlockedItems.clear();
		unlockedItems.addAll(stringToIntList(config.unlockedItems()));
	}

	public void updateNpcList()
	{
		unlockedNpcs.clear();
		unlockedNpcs.addAll(Text.fromCSV(Text.standardize(config.unlockedNpcs())));

		if (client.getGameState().equals(GameState.LOGGED_IN))
		{
			clientThread.invoke(this::updateNpcs);
		}
	}

	public void updateWidgetList()
	{
		lockedWidgets.clear();
		lockedWidgets.addAll(stringToIntList(config.lockedWidgets()));
	}

	public void updateGameObjectList()
	{
		lockedObjects.clear();
		lockedObjects.addAll(Text.fromCSV(Text.standardize(config.lockedGameObjects())));

		if (client.getGameState().equals(GameState.LOGGED_IN))
		{
			clientThread.invoke(this::updateGameObjects);
		}
	}

	public void updateTaskList()
	{
		String raw = config.taskList();
		rawTaskList = raw.split("\n");
	}

	public void updateGameObjects()
	{
		gameObjects.clear();
		wallObjects.clear();
		groundObjects.clear();

		if (config.lockGameObjects())
		{
			for (String s : lockedObjects)
			{
				gameObjects.addAll(getGameObjects(s));
				wallObjects.addAll(getWallObjects(s));
				groundObjects.addAll(getGroundObjects(s));
			}
		}

		if (config.lockBanks())
		{
			for (String s : bankObjects)
			{
				gameObjects.addAll(getGameObjects(s));
				wallObjects.addAll(getWallObjects(s));
				groundObjects.addAll(getGroundObjects(s));
			}
		}

		if (config.lockUnderground())
		{
			for (String s : underGroundObjects)
			{
				gameObjects.addAll(getGameObjects(s));
				wallObjects.addAll(getWallObjects(s));
				groundObjects.addAll(getGroundObjects(s));
			}
		}

		if (config.lockDoors())
		{
			for (String s : doorObjects)
			{
				gameObjects.addAll(getGameObjects(s));
				wallObjects.addAll(getWallObjects(s));
				groundObjects.addAll(getGroundObjects(s));
			}
		}

		if (config.lockStairs())
		{
			for (String s : stairObjects)
			{
				gameObjects.addAll(getGameObjects(s));
				wallObjects.addAll(getWallObjects(s));
				groundObjects.addAll(getGroundObjects(s));
			}
		}
	}

	public void updateNpcs()
	{
		npcs.clear();

		if (config.unlockNpcs())
		{
			npcs.addAll(getExcludedNpcs(unlockedNpcs));
		}

		if (config.lockBanks())
		{
			npcs.addAll(getIncludedNpcs(bankNpcs));
		}
	}

	public List<Integer> stringToIntList(String string)
	{
		return (string == null || string.trim().equals("")) ? Arrays.asList(0) :
				Arrays.stream(string.split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
	}

	public Collection<NPC> getExcludedNpcs(Collection<String> names)
	{
		Collection<NPC> excludedNpcs = new ArrayList<>();

		for (NPC n : client.getNpcs())
		{
			if (n != null && n.getName() != null && !n.getName().equals("") && !names.contains(n.getName().toLowerCase()) && !npcs.contains(n))
			{
				excludedNpcs.add(n);
			}
		}

		return excludedNpcs;
	}

	public Collection<NPC> getIncludedNpcs(Collection<String> names)
	{
		Collection<NPC> includedNpcs = new ArrayList<>();

		for (NPC n : client.getNpcs())
		{
			if (n != null && n.getName() != null && !n.getName().equals("") && names.contains(n.getName().toLowerCase()) && !npcs.contains(n))
			{
				includedNpcs.add(n);
			}
		}

		return includedNpcs;
	}

	public Collection<GameObject> getGameObjects(String names)
	{
		Player local = client.getLocalPlayer();
		final Scene scene = client.getScene();
		final Tile[][][] tiles = scene.getTiles();
		Collection<GameObject> tileGameObjects = new ArrayList<>();

		for (Tile[] tiles1 : tiles[local.getWorldLocation().getPlane()])
		{
			for (Tile tile : tiles1)
			{
				if (tile == null)
				{
					continue;
				}

				for (GameObject g : tile.getGameObjects())
				{
					if (g == null || g.getWorldLocation().getPlane() != local.getWorldLocation().getPlane())
					{
						continue;
					}

					String name = getGameObjectName(g.getId());

					if (name == null || name.equals(""))
					{
						continue;
					}

					if (names.contains(name) && !tileGameObjects.contains(g) && !gameObjects.contains(g))
					{
						tileGameObjects.add(g);
					}
				}
			}
		}

		return tileGameObjects;
	}

	public Collection<WallObject> getWallObjects(String names)
	{
		Player local = client.getLocalPlayer();
		final Scene scene = client.getScene();
		final Tile[][][] tiles = scene.getTiles();
		Collection<WallObject> tileWallObjects = new ArrayList<>();

		for (Tile[] tiles1 : tiles[local.getWorldLocation().getPlane()])
		{
			for (Tile tile : tiles1)
			{
				if (tile == null)
				{
					continue;
				}

				if (tile.getWallObject() == null || tile.getWallObject().getWorldLocation().getPlane() != local.getWorldLocation().getPlane())
				{
					continue;
				}

				String name = getGameObjectName(tile.getWallObject().getId());

				if (name == null || name.equals(""))
				{
					continue;
				}

				if (names.contains(name) && !tileWallObjects.contains(tile.getWallObject()) && !wallObjects.contains(tile.getWallObject()))
				{
					tileWallObjects.add(tile.getWallObject());
				}
			}
		}

		return tileWallObjects;
	}

	public Collection<GroundObject> getGroundObjects(String names)
	{
		Player local = client.getLocalPlayer();
		final Scene scene = client.getScene();
		final Tile[][][] tiles = scene.getTiles();
		Collection<GroundObject> tileGroundObjects = new ArrayList<>();

		for (Tile[] tiles1 : tiles[local.getWorldLocation().getPlane()])
		{
			for (Tile tile : tiles1)
			{
				if (tile == null || tile.getGroundObject() == null || tile.getGroundObject().getWorldLocation().getPlane() != local.getWorldLocation().getPlane())
				{
					continue;
				}

				String name = getGameObjectName(tile.getGroundObject().getId());

				if (name == null || name.equals(""))
				{
					continue;
				}

				if (names.contains(name) && !tileGroundObjects.contains(tile.getGroundObject()) && !groundObjects.contains(tile.getGroundObject()))
				{
					tileGroundObjects.add(tile.getGroundObject());
				}
			}
		}

		return tileGroundObjects;
	}

	@Nullable
	private ObjectComposition getObjectComposition(int id)
	{
		ObjectComposition objectComposition = client.getObjectDefinition(id);
		return objectComposition.getImpostorIds() == null ? objectComposition : objectComposition.getImpostor();
	}

	public String getGameObjectName(int id)
	{
		ObjectComposition objectComposition = getObjectComposition(id);

		if (objectComposition == null || objectComposition.getName() == null || objectComposition.getName().equals(""))
		{
			return "";
		}

		return objectComposition.getName().toLowerCase();
	}

	public void loadResources()
	{
		try {
			background = ImageUtil.loadImageResource(getClass(), BACKGROUND_PATH);
			debugBackground = ImageUtil.loadImageResource(getClass(), DEBUG_PATH);
			runOrb = ImageUtil.loadImageResource(getClass(), RUN_PATH);
			specOrb = ImageUtil.loadImageResource(getClass(), SPEC_PATH);
			healthOrb = ImageUtil.loadImageResource(getClass(), HEALTH_PATH);
			prayerOrb = ImageUtil.loadImageResource(getClass(), PRAYER_PATH);
		} catch (Exception e){
			log.error("thenwefightplugin, error loading image resources", e);
		}
	}

}
