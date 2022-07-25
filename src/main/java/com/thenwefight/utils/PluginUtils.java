package com.thenwefight.utils;

import com.thenwefight.ThenWeFightConfig;
import com.thenwefight.ThenWeFightPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.thenwefight.ThenWeFightPlugin.*;

@Slf4j
public class PluginUtils {

    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ThenWeFightPlugin plugin;
    @Inject
    private ThenWeFightConfig config;
    @Inject
    private ConfigManager configManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private GameUtils gameUtils;

    public void loadCustomImages()
    {
        if (U_1_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.u1Custom = ImageIO.read(U_1_CUSTOM_DIR);
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
                    plugin.u2Custom = ImageIO.read(U_2_CUSTOM_DIR);
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
                    plugin.u3Custom = ImageIO.read(U_3_CUSTOM_DIR);
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
                    plugin.u4Custom = ImageIO.read(U_4_CUSTOM_DIR);
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
                    plugin.u5Custom = ImageIO.read(U_5_CUSTOM_DIR);
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
                    plugin.u6Custom = ImageIO.read(U_6_CUSTOM_DIR);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 6 image", e);
            }
        }
    }

    public void loadResources()
    {
        try {
            plugin.background = ImageUtil.loadImageResource(plugin.getClass(), BACKGROUND_PATH);
            plugin.debugBackground = ImageUtil.loadImageResource(plugin.getClass(), DEBUG_PATH);
            plugin.runOrb = ImageUtil.loadImageResource(plugin.getClass(), RUN_PATH);
            plugin.specOrb = ImageUtil.loadImageResource(plugin.getClass(), SPEC_PATH);
            plugin.healthOrb = ImageUtil.loadImageResource(plugin.getClass(), HEALTH_PATH);
            plugin.prayerOrb = ImageUtil.loadImageResource(plugin.getClass(), PRAYER_PATH);
        } catch (Exception e){
            log.error("thenwefightplugin, error loading image resources", e);
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
                gameObjects.addAll(gameUtils.getGameObjects(s));
                wallObjects.addAll(gameUtils.getWallObjects(s));
                groundObjects.addAll(gameUtils.getGroundObjects(s));
            }
        }

        if (config.lockBanks())
        {
            for (String s : BANK_OBJECT_NAMES)
            {
                gameObjects.addAll(gameUtils.getGameObjects(s));
                wallObjects.addAll(gameUtils.getWallObjects(s));
                groundObjects.addAll(gameUtils.getGroundObjects(s));
            }
        }

        if (config.lockUnderground())
        {
            for (String s : UNDERGROUND_OBJECT_NAMES)
            {
                gameObjects.addAll(gameUtils.getGameObjects(s));
                wallObjects.addAll(gameUtils.getWallObjects(s));
                groundObjects.addAll(gameUtils.getGroundObjects(s));
            }
        }

        if (config.lockDoors())
        {
            for (String s : DOOR_OBJECT_NAMES)
            {
                gameObjects.addAll(gameUtils.getGameObjects(s));
                wallObjects.addAll(gameUtils.getWallObjects(s));
                groundObjects.addAll(gameUtils.getGroundObjects(s));
            }
        }

        if (config.lockStairs())
        {
            for (String s : STAIRS_OBJECT_NAMES)
            {
                gameObjects.addAll(gameUtils.getGameObjects(s));
                wallObjects.addAll(gameUtils.getWallObjects(s));
                groundObjects.addAll(gameUtils.getGroundObjects(s));
            }
        }
    }

    public void updateNpcs()
    {
        npcs.clear();

        if (config.unlockNpcs())
        {
            npcs.addAll(gameUtils.getExcludedNpcs(unlockedNpcs));
        }

        if (config.lockBanks())
        {
            npcs.addAll(gameUtils.getIncludedNpcs(BANK_NPC_NAMES));
        }
    }

    public void unlockAllItems(MenuEntry entry)
    {
        for (Widget w : gameUtils.getAllItems())
        {
            int id = w.getItemId();

            if (id == -1 || id == 6512 || id == 0)
            {
                continue;
            }

            if (!unlockedItems.contains(id))
            {
                unlockedItems.add(id);
            }
        }

        configManager.setConfiguration("thenwefight", "unlockedItems", unlockedItems.toString().replace(" ", "").replace("[", "").replace("]", ""));
    }

    public void lockAllItems(MenuEntry entry)
    {
        for (Widget w : gameUtils.getAllItems())
        {
            int id = w.getItemId();

            if (id == -1 || id == 6512 || id == 0)
            {
                continue;
            }

            if (unlockedItems.contains(id))
            {
                unlockedItems.remove((Integer) id);
            }
        }

        configManager.setConfiguration("thenwefight", "unlockedItems", unlockedItems.toString().replace(" ", "").replace("[", "").replace("]", ""));
    }

    public void unlockAllFood(MenuEntry entry)
    {
        for (Widget w : gameUtils.getAllItems())
        {
            int id = w.getItemId();

            if (id == -1 || id == 6512 || id == 0)
            {
                continue;
            }

            ItemComposition ic = itemManager.getItemComposition(id);

            if (ic != null)
            {
                for (String s : ic.getInventoryActions())
                {
                    if (s == null || s.equals(""))
                    {
                        continue;
                    }

                    if ((EAT_OPTIONS.contains(s.toLowerCase()) || DRINK_OPTIONS.contains(s.toLowerCase())) && !unlockedItems.contains(id))
                    {
                        unlockedItems.add(id);
                    }
                }
            }
        }

        configManager.setConfiguration("thenwefight", "unlockedItems", unlockedItems.toString().replace(" ", "").replace("[", "").replace("]", ""));
    }

    public void lockAllFood(MenuEntry entry)
    {
        for (Widget w : gameUtils.getAllItems())
        {
            int id = w.getItemId();

            if (id == -1 || id == 6512 || id == 0)
            {
                continue;
            }

            ItemComposition ic = itemManager.getItemComposition(id);

            if (ic != null)
            {
                for (String s : ic.getInventoryActions())
                {
                    if (s == null || s.equals(""))
                    {
                        continue;
                    }

                    if ((EAT_OPTIONS.contains(s.toLowerCase()) || DRINK_OPTIONS.contains(s.toLowerCase())) && unlockedItems.contains(id))
                    {
                        unlockedItems.remove((Integer) id);
                    }
                }
            }
        }

        configManager.setConfiguration("thenwefight", "unlockedItems", unlockedItems.toString().replace(" ", "").replace("[", "").replace("]", ""));
    }

    public List<Integer> stringToIntList(String string)
    {
        return (string == null || string.trim().equals("")) ? Arrays.asList(0) :
                Arrays.stream(string.split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
    }

}
