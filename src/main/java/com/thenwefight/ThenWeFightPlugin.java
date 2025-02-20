package com.thenwefight;

import com.google.inject.Provides;
import com.thenwefight.overlay.*;
import com.thenwefight.utils.GameUtils;
import com.thenwefight.utils.PluginUtils;
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
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

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
	private ItemManager itemManager;

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
	@Inject
	private ThenWeFightItemUnlockOverlay itemUnlockOverlay;

	@Inject
	private PluginUtils pluginUtils;
	@Inject
	private GameUtils gameUtils;

	public static String[] rawTaskList;
	public static String[] rawUnlockList;
	public static String[] rawItemUnlockList;

	public static List<Integer> unlockedItems = new ArrayList<>();
	public static List<String> unlockedNpcs = new ArrayList<>();
	public static List<Integer> lockedWidgets = new ArrayList<>();
	public static List<String> lockedObjects = new ArrayList<>();

	public static final Collection<String> BANK_NPC_NAMES = Arrays.asList("banker", "grand exchange clerk", "banker tutor");
	public static final Collection<String> BANK_OBJECT_NAMES = Arrays.asList("bank booth", "bank chest", "bank deposit box", "bank deposit chest", "grand exchange booth");
	public static final Collection<String> UNDERGROUND_OBJECT_NAMES = Arrays.asList("trapdoor", "dark hole", "hole", "rope", "cavern entrance", "dive", "cave", "tunnel entrance");
	public static final Collection<String> TELEPORT_OPTIONS = Arrays.asList("teleport", "tele");
	public static final Collection<String> TELEPORT_TARGETS = Arrays.asList("teleport", "tele");
	public static final Collection<String> STAIRS_OBJECT_NAMES = Arrays.asList("stairs", "staircase");
	public static final Collection<String> DOOR_OBJECT_NAMES = Arrays.asList("door", "large door");
	public static final Collection<String> DROP_OPTIONS = Arrays.asList("drop", "destroy");
	public static final Collection<String> EAT_OPTIONS = Arrays.asList("eat", "consume");
	public static final Collection<String> DRINK_OPTIONS = Arrays.asList("drink");
	public static final Collection<String> THIEVE_OPTIONS = Arrays.asList("pickpocket", "steal");
	public static final Collection<String> WOODCUTTING_OPTIONS = Arrays.asList("chop down");
	public static final Collection<String> FISHING_OPTIONS = Arrays.asList("bait", "fish", "cast-net", "lure");
	public static final Collection<String> FISHING_TARGETS = Arrays.asList("fishing spot", "rod fishing spot");
	public static final Collection<String> SLAYER_NPC_TARGETS = Arrays.asList("turael", "spria", "krystilia", "mazchna", "vannaka", "chaeldar", "konar quo maten", "nieve", "steve", "duradel");

	public static Collection<GameObject> gameObjects = new ArrayList<>();
	public static Collection<WallObject> wallObjects = new ArrayList<>();
	public static Collection<GroundObject> groundObjects = new ArrayList<>();
	public static Collection<NPC> npcs = new ArrayList<>();

	public static final File THEN_WE_FIGHT_FOLDER = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "thenwefight");
	public static File U_1_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/1.png");
	public static File U_2_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/2.png");
	public static File U_3_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/3.png");
	public static File U_4_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/4.png");
	public static File U_5_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/5.png");
	public static File U_6_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/6.png");
	public static File IU_1_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/1.png");
	public static File IU_2_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/2.png");
	public static File IU_3_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/3.png");
	public static File IU_4_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/4.png");
	public static File IU_5_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/5.png");
	public static File IU_6_CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR, "/thenwefight/6.png");

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

	public static final Collection<Integer> QUEST_TAB_PARAMS = Arrays.asList(10747957, 10551356, 35913792);
	public static final Collection<Integer> INVENT_TAB_PARAMS = Arrays.asList(10747958, 10551357, 35913793);
	public static final Collection<Integer> EQUIPMENT_TAB_PARAMS = Arrays.asList(10747959 ,10551358, 35913794);

	public static final String BACKGROUND_PATH = "background.png";
	public static final String DEBUG_PATH = "debug.png";
	public static final String HEALTH_PATH = "health.png";
	public static final String PRAYER_PATH = "prayer.png";
	public static final String RUN_PATH = "run.png";
	public static final String SPEC_PATH = "spec.png";


	public static final String THICK_SKIN_NAME = "thick skin";
	public static final String BURST_OF_STRENGTH_NAME = "burst of strength";
	public static final String CLARITY_OF_THOUGHT_NAME = "clarity of thought";
	public static final String SHARP_EYE_NAME = "sharp eye";
	public static final String MYSTIC_WILL_NAME = "mystic will";
	public static final String ROCK_SKIN_NAME = "rock skin";
	public static final String SUPERHUMAN_STRENGTH_NAME = "superhuman strength";
	public static final String IMPROVED_REFLEXES_NAME = "improved reflexes";
	public static final String RAPID_RESTORE_NAME = "rapid restore";
	public static final String RAPID_HEAL_NAME = "rapid heal";
	public static final String PROTECT_ITEM_NAME = "protect item";
	public static final String HAWK_EYE_NAME = "hawk eye";
	public static final String MYSTIC_LORE_NAME = "mystic lore";
	public static final String STEEL_SKIN_NAME = "steel skin";
	public static final String ULTIMATE_STRENGTH_NAME = "ultimate strength";
	public static final String INCREDIBLE_REFLEXES_NAME = "incredible reflexes";
	public static final String PROTECT_FROM_MAGIC_NAME = "protect from magic";
	public static final String PROTECT_FROM_MISSILES_NAME = "protect from missiles";
	public static final String PROTECT_FROM_MELEE_NAME = "protect from melee";
	public static final String EAGLE_EYE_NAME = "eagle eye";
	public static final String MYSTIC_MIGHT_NAME = "mystic might";
	public static final String RETRIBUTION_NAME = "retribution";
	public static final String REDEMPTION_NAME = "redemption";
	public static final String SMITE_NAME = "smite";
	public static final String PRESERVE_NAME = "preserve";
	public static final String CHIVALRY_NAME = "chivalry";
	public static final String PIETY_NAME = "piety";
	public static final String RIGOUR_NAME = "rigour";
	public static final String AUGURY_NAME = "augury";

	@Getter
	public Image background;
	@Getter
	public Image debugBackground;
	@Getter
	public Image runOrb;
	@Getter
	public Image prayerOrb;
	@Getter
	public Image specOrb;
	@Getter
	public Image healthOrb;
	@Getter
	public Image u1Custom;
	@Getter
	public Image u2Custom;
	@Getter
	public Image u3Custom;
	@Getter
	public Image u4Custom;
	@Getter
	public Image u5Custom;
	@Getter
	public Image u6Custom;
	@Getter
	public Image iu1Custom;
	@Getter
	public Image iu2Custom;
	@Getter
	public Image iu3Custom;
	@Getter
	public Image iu4Custom;
	@Getter
	public Image iu5Custom;
	@Getter
	public Image iu6Custom;

	public boolean unlockOverlayVisible = false;
	public boolean taskOverlayVisible = false;
	public boolean itemUnlockOverlayVisible = false;

	public int plane = -1;

	@Override
	protected void startUp() throws Exception
	{
		if (!THEN_WE_FIGHT_FOLDER.exists())
		{
			THEN_WE_FIGHT_FOLDER.mkdirs();
		}

		overlayManager.add(itemOverlay);
		overlayManager.add(sceneOverlay);
		overlayManager.add(unlockOverlay);
		overlayManager.add(widgetOverlay);
		overlayManager.add(taskOverlay);
		overlayManager.add(itemUnlockOverlay);

		pluginUtils.updateUnlockList();
		pluginUtils.updateItemUnlockList();
		pluginUtils.setCustomUnlockImageFilePaths();
		pluginUtils.setCustomItemUnlockImageFilePaths();
		pluginUtils.updateItemList();
		pluginUtils.updateNpcList();
		pluginUtils.updateWidgetList();
		pluginUtils.updateGameObjectList();
		pluginUtils.updateTaskList();
		pluginUtils.loadResources();


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
		overlayManager.remove(itemUnlockOverlay);

		pluginUtils.updateUnlockList();
		pluginUtils.updateItemUnlockList();
		pluginUtils.setCustomUnlockImageFilePaths();
		pluginUtils.setCustomItemUnlockImageFilePaths();
		pluginUtils.updateItemList();
		pluginUtils.updateNpcList();
		pluginUtils.updateWidgetList();
		pluginUtils.updateGameObjectList();
		pluginUtils.loadResources();

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
			pluginUtils.updateItemList();
		}

		if (event.getKey().equals("unlockedNpcs")
				|| event.getKey().equals("unlockNpcs")
				|| event.getKey().equals("lockBanks"))
		{
			pluginUtils.updateNpcList();
		}

		if (event.getKey().equals("lockedGameObjects")
				|| event.getKey().equals("lockGameObjects")
				|| event.getKey().equals("lockBanks")
				|| event.getKey().equals("lockUnderground")
				|| event.getKey().equals("lockStairs")
				|| event.getKey().equals("lockDoors"))
		{
			pluginUtils.updateGameObjectList();
		}

		if (event.getKey().equals("lockedWidgets"))
		{
			pluginUtils.updateWidgetList();
		}

		if (event.getKey().equals("taskList"))
		{
			pluginUtils.updateTaskList();
		}

		if (event.getKey().equals("unlockList"))
		{
			pluginUtils.updateUnlockList();
			pluginUtils.setCustomUnlockImageFilePaths();
		}

		if (event.getKey().equals("unlockScroll"))
		{
			pluginUtils.setCustomUnlockImageFilePaths();
		}

		if (event.getKey().equals("itemUnlockList"))
		{
			pluginUtils.updateItemUnlockList();
			pluginUtils.setCustomItemUnlockImageFilePaths();
		}

		if (event.getKey().equals("itemUnlockScroll"))
		{
			pluginUtils.setCustomItemUnlockImageFilePaths();
		}

		if (event.getKey().equals("imageWidth") || event.getKey().equals("imageHeight"))
		{
			pluginUtils.setCustomUnlockImageFilePaths();
			pluginUtils.setCustomItemUnlockImageFilePaths();
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
		String cleanOption = Text.standardize(option);
		int type = event.getType();
		int identifier = event.getIdentifier();
		int param1 = event.getActionParam1();
		int param0 = event.getActionParam0();
		Widget widget = event.getMenuEntry().getWidget();
		boolean shiftPressed = client.isKeyPressed(KeyCode.KC_SHIFT);
		boolean unlockedItem = false;
		boolean lockedNpc = false;

		if (widget != null)
		{
			unlockedItem = unlockedItems.contains(widget.getItemId());
		}

		if (QUEST_TAB_PARAMS.contains(param1))
		{
			client.createMenuEntry(-1)
					.setOption(ColorUtil.prependColorTag(unlockOverlayVisible ? "Hide Unlocks" : "View Unlocks", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE);


			client.createMenuEntry(-2)
					.setOption(ColorUtil.prependColorTag(taskOverlayVisible ? "Hide Tasks" : "View Tasks", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE);

			client.createMenuEntry(-3)
					.setOption(ColorUtil.prependColorTag(itemUnlockOverlayVisible ? "Hide Item Unlocks" : "View Item Unlocks", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE);
		}

		if (INVENT_TAB_PARAMS.contains(param1) && config.unlockItems())
		{
			client.createMenuEntry(-1)
					.setOption(ColorUtil.prependColorTag("Lock All Inventory Items", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE)
					.onClick(pluginUtils::lockAllItems);


			client.createMenuEntry(-2)
					.setOption(ColorUtil.prependColorTag("Unlock All Inventory Items", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE)
					.onClick(pluginUtils::unlockAllItems);

			client.createMenuEntry(-3)
					.setOption(ColorUtil.prependColorTag("Lock All Inventory Food", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE)
					.onClick(pluginUtils::lockAllFood);

			client.createMenuEntry(-4)
					.setOption(ColorUtil.prependColorTag("Unlock All Inventory Food", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE)
					.onClick(pluginUtils::unlockAllFood);
		}

		if (EQUIPMENT_TAB_PARAMS.contains(param1) && config.unlockItems())
		{
			client.createMenuEntry(-1)
					.setOption(ColorUtil.prependColorTag("Lock All Worn Items", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE)
					.onClick(pluginUtils::lockAllEquipment);


			client.createMenuEntry(-2)
					.setOption(ColorUtil.prependColorTag("Unlock All Worn Items", Color.ORANGE))
					.setParam1(param1)
					.setDeprioritized(true)
					.setType(MenuAction.RUNELITE)
					.onClick(pluginUtils::unlockAllEquipment);
		}

		if (shiftPressed)
		{
			if (param1 == WidgetInfo.INVENTORY.getId()
					&& type == MenuAction.CC_OP_LOW_PRIORITY.getId()
					&& identifier == 10
					&& widget != null
					&& config.unlockItems())
			{
				client.createMenuEntry(-1)
						.setOption(ColorUtil.prependColorTag(unlockedItem ? "Lock": "Unlock", Color.ORANGE))
						.setTarget(target)
						.setIdentifier(identifier)
						.setParam0(widget.getItemId())
						.setParam1(param1)
						.setType(MenuAction.RUNELITE);
			}

			if (type == MenuAction.EXAMINE_NPC.getId()
					&& config.unlockNpcs())
			{
				final int id = event.getMenuEntry().getIdentifier();
				final NPC npc = client.getTopLevelWorldView().npcs().byIndex(id);

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
					&& config.lockGameObjects())
			{
				final int id = event.getMenuEntry().getIdentifier();

				boolean unlocked = lockedObjects.contains(gameUtils.getGameObjectName(id));

				client.createMenuEntry(-1)
						.setOption(ColorUtil.prependColorTag(unlocked ? "Unlock Object": "Lock Object", Color.ORANGE))
						.setTarget(target)
						.setIdentifier(identifier)
						.setParam0(param0)
						.setParam1(param1)
						.setType(MenuAction.RUNELITE);
			}
		}

		if (config.unlockNpcs() && NPC_ACTIONS.contains(type))
		{
			final NPC npc = client.getTopLevelWorldView().npcs().byIndex(identifier);

			if (npc != null && npc.getName() != null && !unlockedNpcs.contains(npc.getName().toLowerCase()))
			{
				lockedNpc = true;
			}
		}

//		 if youre reading my code for some reason and see this, please continue with your life and pay no attention to it
		if ((config.lockAttacking() && type == MenuAction.NPC_SECOND_OPTION.getId())
				|| (config.unlockItems() && param1 == WidgetInfo.INVENTORY.getId() && widget != null && !unlockedItems.contains(widget.getItemId()))
				|| (config.lockDrinking() && DRINK_OPTIONS.contains(cleanOption))
				|| (config.lockEating() && EAT_OPTIONS.contains(cleanOption))
				|| (config.lockTeles() && option.toLowerCase().contains("teleport"))
				|| (config.lockTeles() && target.toLowerCase().contains("teleport"))
				|| (config.lockTeles() && option.toLowerCase().contains("tele"))
				|| (config.lockTeles() && target.toLowerCase().contains("tele"))
				|| (config.lockBanks() && (BANK_OBJECT_NAMES.contains(cleanTarget) || BANK_NPC_NAMES.contains(cleanTarget)))
				|| (config.lockCoins() && type == MenuAction.GROUND_ITEM_THIRD_OPTION.getId() && cleanTarget.equals("coins"))
				|| (config.unlockItems() && GROUND_ITEM_ACTIONS.contains(type) && lockedObjects.contains(cleanTarget))
				|| (config.lockGameObjects() && OBJECT_ACTIONS.contains(type) && lockedObjects.contains(cleanTarget))
				|| (config.lockBanks() && BANK_OBJECT_NAMES.contains(cleanTarget) && OBJECT_ACTIONS.contains(type))
				|| (config.lockUnderground() && UNDERGROUND_OBJECT_NAMES.contains(cleanTarget) && OBJECT_ACTIONS.contains(type))
				|| (config.lockBanks() && BANK_NPC_NAMES.contains(cleanTarget) && NPC_ACTIONS.contains(type))
				|| (config.lockThieving() && THIEVE_OPTIONS.contains(cleanOption))
				|| (config.lockWoodcutting() && WOODCUTTING_OPTIONS.contains(cleanOption))
				|| (config.lockFishing() && FISHING_TARGETS.contains(cleanTarget))
				|| (config.lockSlayer() && NPC_ACTIONS.contains(type) && SLAYER_NPC_TARGETS.contains(cleanTarget))
				|| lockedNpc)
		{
			event.getMenuEntry().setOption(ColorUtil.prependColorTag(option,config.uiOverlayColour()));
			event.getMenuEntry().setTarget(ColorUtil.prependColorTag(Text.removeTags(target),config.uiOverlayColour()));
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

		if (param1 == WidgetInfo.INVENTORY.getId()
				&& widget != null
				&& config.unlockItems()
				&& !client.isKeyPressed(KeyCode.KC_SHIFT))
		{
			boolean unlocked = unlockedItems.contains(widget.getItemId());

			if (!unlocked)
			{
				event.consume();
				return;
			}
		}

		if (option.equals("Walk here") && (taskOverlayVisible || unlockOverlayVisible || itemUnlockOverlayVisible))
		{
			unlockOverlayVisible = false;
			taskOverlayVisible = false;
			itemUnlockOverlayVisible = false;
		}

		if (QUEST_TAB_PARAMS.contains(param1))
		{
			if (option.equals(ColorUtil.prependColorTag("View Unlocks", Color.ORANGE)))
			{
				event.consume();
				unlockOverlayVisible = true;
				taskOverlayVisible = false;
				itemUnlockOverlayVisible = false;
			}

			if (option.equals(ColorUtil.prependColorTag("Hide Unlocks", Color.ORANGE)))
			{
				event.consume();
				unlockOverlayVisible = false;
			}

			if (option.equals(ColorUtil.prependColorTag("View Tasks", Color.ORANGE)))
			{
				event.consume();
				taskOverlayVisible = true;
				unlockOverlayVisible = false;
				itemUnlockOverlayVisible = false;
			}

			if (option.equals(ColorUtil.prependColorTag("Hide Tasks", Color.ORANGE)))
			{
				event.consume();
				taskOverlayVisible = false;
			}

			if (option.equals(ColorUtil.prependColorTag("View Item Unlocks", Color.ORANGE)))
			{
				event.consume();
				itemUnlockOverlayVisible = true;
				unlockOverlayVisible = false;
				taskOverlayVisible = false;
			}

			if (option.equals(ColorUtil.prependColorTag("Hide Item Unlocks", Color.ORANGE)))
			{
				event.consume();
				itemUnlockOverlayVisible = false;
			}
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

		if (type == MenuAction.RUNELITE.getId())
		{
			if (option.equals(ColorUtil.prependColorTag("Unlock NPC", Color.ORANGE)))
			{
				event.consume();

				final NPC npc = client.getTopLevelWorldView().npcs().byIndex(identifier);

				if (npc != null && npc.getName() != null)
				{
					unlockedNpcs.add(npc.getName().toLowerCase());
					configManager.setConfiguration("thenwefight", "unlockedNpcs", unlockedNpcs.toString().replace("[", "").replace("]", ""));
				}
			}

			if (option.equals(ColorUtil.prependColorTag("Lock NPC", Color.ORANGE)))
			{
				event.consume();

				final NPC npc = client.getTopLevelWorldView().npcs().byIndex(identifier);

				if (npc != null && npc.getName() != null)
				{
					unlockedNpcs.remove(npc.getName().toLowerCase());
					configManager.setConfiguration("thenwefight", "unlockedNpcs", unlockedNpcs.toString().replace("[", "").replace("]", ""));
				}
			}

			if (option.equals(ColorUtil.prependColorTag("Lock Object", Color.ORANGE)))
			{
				event.consume();

				lockedObjects.add(gameUtils.getGameObjectName(identifier));
				configManager.setConfiguration("thenwefight", "lockedGameObjects", lockedObjects.toString().replace("[", "").replace("]", ""));
			}

			if (option.equals(ColorUtil.prependColorTag("Unlock Object", Color.ORANGE)))
			{
				event.consume();

				lockedObjects.remove(gameUtils.getGameObjectName(identifier));
				configManager.setConfiguration("thenwefight", "lockedGameObjects", lockedObjects.toString().replace("[", "").replace("]", ""));
				pluginUtils.updateGameObjects();
			}
		}
	}

	@Subscribe
	private void onMenuOpened(MenuOpened event)
	{
		ArrayList<MenuEntry> entries = new ArrayList<>();

		for (MenuEntry entry : event.getMenuEntries())
		{
			String target = entry.getTarget();
			String cleanTarget = Text.standardize(target);
			String option = entry.getOption();
			String cleanOption = Text.standardize(option);
			int type = entry.getType().getId();
			int identifier = entry.getIdentifier();
			int param1 = entry.getParam1();
			int param0 = entry.getParam0();
			Widget widget = entry.getWidget();
			boolean shiftPressed = client.isKeyPressed(KeyCode.KC_SHIFT);
			boolean lockedNpc = false;

			if (config.unlockNpcs() && NPC_ACTIONS.contains(type))
			{
				final NPC npc = client.getTopLevelWorldView().npcs().byIndex(identifier);

				if (npc != null && npc.getName() != null && !unlockedNpcs.contains(npc.getName().toLowerCase()))
				{
					lockedNpc = true;
				}
			}

			if (!((config.lockAttacking() && type == MenuAction.NPC_SECOND_OPTION.getId())
					|| (config.unlockItems() && param1 == WidgetInfo.INVENTORY.getId() && widget != null && !unlockedItems.contains(widget.getItemId()))
					|| (config.lockDrinking() && DRINK_OPTIONS.contains(cleanOption))
					|| (config.lockEating() && EAT_OPTIONS.contains(cleanOption))
					|| (config.lockTeles() && option.toLowerCase().contains("teleport"))
					|| (config.lockTeles() && target.toLowerCase().contains("teleport"))
					|| (config.lockTeles() && option.toLowerCase().contains("tele"))
					|| (config.lockTeles() && target.toLowerCase().contains("tele"))
					|| (config.lockBanks() && (BANK_OBJECT_NAMES.contains(cleanTarget) || BANK_NPC_NAMES.contains(cleanTarget)))
					|| (config.lockCoins() && type == MenuAction.GROUND_ITEM_THIRD_OPTION.getId() && target.contains("Coins"))
					|| (config.unlockItems() && GROUND_ITEM_ACTIONS.contains(type) && lockedObjects.contains(cleanTarget))
					|| (config.lockGameObjects() && OBJECT_ACTIONS.contains(type) && lockedObjects.contains(cleanTarget))
					|| (config.lockBanks() && BANK_OBJECT_NAMES.contains(cleanTarget) && OBJECT_ACTIONS.contains(type))
					|| (config.lockUnderground() && UNDERGROUND_OBJECT_NAMES.contains(cleanTarget) && OBJECT_ACTIONS.contains(type))
					|| (config.lockBanks() && BANK_NPC_NAMES.contains(cleanTarget) && NPC_ACTIONS.contains(type))
					|| (config.lockThieving() && THIEVE_OPTIONS.contains(cleanOption))
					|| (config.lockThieving() && THIEVE_OPTIONS.contains(cleanOption))
					|| (config.lockWoodcutting() && WOODCUTTING_OPTIONS.contains(cleanOption))
					|| (config.lockFishing() && FISHING_TARGETS.contains(cleanTarget))
					|| (config.lockSlayer() && NPC_ACTIONS.contains(type) && SLAYER_NPC_TARGETS.contains(cleanTarget))
					|| lockedNpc)
					|| shiftPressed)
			{
				entries.add(entry);
			}
		}

		client.setMenuEntries(entries.toArray(new MenuEntry[0]));
	}

	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		GameObject object = event.getGameObject();
		ObjectComposition objectComposition = gameUtils.getObjectComposition(object.getId());

		if (objectComposition == null || objectComposition.getName() == null || objectComposition.getName().equals(""))
		{
			return;
		}

		String name = objectComposition.getName().toLowerCase();

		if (config.lockBanks() && BANK_OBJECT_NAMES.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}

		if (config.lockUnderground() && UNDERGROUND_OBJECT_NAMES.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}

		if (config.lockGameObjects() && lockedObjects.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}

		if (config.lockDoors() && DOOR_OBJECT_NAMES.contains(name) && !gameObjects.contains(object))
		{
			gameObjects.add(object);
		}

		if (config.lockStairs() && STAIRS_OBJECT_NAMES.contains(name) && !gameObjects.contains(object))
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

		ObjectComposition objectComposition = gameUtils.getObjectComposition(object.getId());

		if (objectComposition == null || objectComposition.getName() == null || objectComposition.getName().equals(""))
		{
			return;
		}

		String name = objectComposition.getName().toLowerCase();

		if (config.lockBanks() && BANK_OBJECT_NAMES.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}

		if (config.lockUnderground() && UNDERGROUND_OBJECT_NAMES.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}

		if (config.lockGameObjects() && lockedObjects.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}

		if (config.lockDoors() && DOOR_OBJECT_NAMES.contains(name) && !wallObjects.contains(object))
		{
			wallObjects.add(object);
		}

		if (config.lockStairs() && STAIRS_OBJECT_NAMES.contains(name) && !wallObjects.contains(object))
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
		ObjectComposition objectComposition = gameUtils.getObjectComposition(object.getId());

		if (objectComposition == null || objectComposition.getName() == null || objectComposition.getName().equals(""))
		{
			return;
		}

		String name = objectComposition.getName().toLowerCase();

		if (config.lockBanks() && BANK_OBJECT_NAMES.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}

		if (config.lockUnderground() && UNDERGROUND_OBJECT_NAMES.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}

		if (config.lockGameObjects() && lockedObjects.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}

		if (config.lockDoors() && DOOR_OBJECT_NAMES.contains(name) && !groundObjects.contains(object))
		{
			groundObjects.add(object);
		}

		if (config.lockStairs() && STAIRS_OBJECT_NAMES.contains(name) && !groundObjects.contains(object))
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

		if (config.lockBanks() && BANK_NPC_NAMES.contains(npc.getName().toLowerCase()) && !npcs.contains(npc))
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
			pluginUtils.updateNpcs();
			pluginUtils.updateGameObjects();
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
			pluginUtils.updateGameObjects();
		}
	}

}
