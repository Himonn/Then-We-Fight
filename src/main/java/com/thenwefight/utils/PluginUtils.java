package com.thenwefight.utils;

import com.thenwefight.ThenWeFightConfig;
import com.thenwefight.ThenWeFightPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.kit.KitType;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.thenwefight.ThenWeFightPlugin.*;
import static com.thenwefight.ThenWeFightPlugin.IU_6_CUSTOM_DIR;

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

    public void setCustomUnlockImageFilePaths()
    {
        int unlockListLength = rawUnlockList.length;
        int scroll = config.unlockScroll();

        if (unlockListLength >= 1)
        {
            String path1 = rawUnlockList[scroll].split(",")[1];

            if (!isNumeric(path1))
            {
                U_1_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path1);
            }
        }

        if (unlockListLength >= 2)
        {
            String path2 = rawUnlockList[scroll + 1].split(",")[1];

            if (!isNumeric(path2))
            {
                U_2_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path2);
            }
        }

        if (unlockListLength >= 3)
        {
            String path3 = rawUnlockList[scroll + 2].split(",")[1];

            if (!isNumeric(path3))
            {
                U_3_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path3);
            }
        }

        if (unlockListLength >= 4)
        {
            String path4 = rawUnlockList[scroll + 3].split(",")[1];

            if (!isNumeric(path4))
            {
                U_4_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path4);
            }
        }

        if (unlockListLength >= 5)
        {
            String path5 = rawUnlockList[scroll + 4].split(",")[1];

            if (!isNumeric(path5))
            {
                U_5_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path5);
            }
        }

        if (unlockListLength >= 6)
        {
            String path6 = rawUnlockList[scroll + 5].split(",")[1];

            if (!isNumeric(path6))
            {
                U_6_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path6);
            }
        }

        loadCustomUnlockImages();
    }

    public void loadCustomUnlockImages()
    {
        int imageWidth = config.unlockImageWidth();
        int imageHeight = config.unlockImageHeight();

        if (U_1_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.u1Custom = ImageIO.read(U_1_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 1 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + U_1_CUSTOM_DIR.getAbsolutePath());
            plugin.u1Custom = null;
        }

        if (U_2_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.u2Custom = ImageIO.read(U_2_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 2 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + U_2_CUSTOM_DIR.getAbsolutePath());
            plugin.u2Custom = null;
        }

        if (U_3_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.u3Custom = ImageIO.read(U_3_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 3 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + U_3_CUSTOM_DIR.getAbsolutePath());
            plugin.u3Custom = null;
        }

        if (U_4_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.u4Custom = ImageIO.read(U_4_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 4 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + U_4_CUSTOM_DIR.getAbsolutePath());
            plugin.u4Custom = null;
        }

        if (U_5_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.u5Custom = ImageIO.read(U_5_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 5 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + U_5_CUSTOM_DIR.getAbsolutePath());

            plugin.u5Custom = null;
        }

        if (U_6_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.u6Custom = ImageIO.read(U_6_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 6 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + U_6_CUSTOM_DIR.getAbsolutePath());
            plugin.u6Custom = null;
        }
    }

    public void setCustomItemUnlockImageFilePaths()
    {
        int unlockListLength = rawItemUnlockList.length;
        int scroll = config.itemUnlockScroll();

        if (unlockListLength >= 1)
        {
            String path1 = rawItemUnlockList[scroll].split(",")[1];

            if (!isNumeric(path1))
            {
                IU_1_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path1);
            }
        }

        if (unlockListLength >= 2)
        {
            String path2 = rawItemUnlockList[scroll + 1].split(",")[1];

            if (!isNumeric(path2))
            {
                IU_2_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path2);
            }
        }

        if (unlockListLength >= 3)
        {
            String path3 = rawItemUnlockList[scroll + 2].split(",")[1];

            if (!isNumeric(path3))
            {
                IU_3_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path3);
            }
        }

        if (unlockListLength >= 4)
        {
            String path4 = rawItemUnlockList[scroll + 3].split(",")[1];

            if (!isNumeric(path4))
            {
                IU_4_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path4);
            }
        }

        if (unlockListLength >= 5)
        {
            String path5 = rawItemUnlockList[scroll + 4].split(",")[1];

            if (!isNumeric(path5))
            {
                IU_5_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path5);
            }
        }

        if (unlockListLength >= 6)
        {
            String path6 = rawItemUnlockList[scroll + 5].split(",")[1];

            if (!isNumeric(path6))
            {
                IU_6_CUSTOM_DIR = new File(THEN_WE_FIGHT_FOLDER + File.separator + path6);
            }
        }

        loadCustomItemUnlockImages();
    }

    public void loadCustomItemUnlockImages()
    {
        int imageWidth = config.unlockImageWidth();
        int imageHeight = config.unlockImageHeight();

        if (IU_1_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.iu1Custom = ImageIO.read(IU_1_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 1 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + IU_1_CUSTOM_DIR.getAbsolutePath());
            plugin.iu1Custom = null;
        }

        if (IU_2_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.iu2Custom = ImageIO.read(IU_2_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 2 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + IU_2_CUSTOM_DIR.getAbsolutePath());
            plugin.iu2Custom = null;
        }

        if (IU_3_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.iu3Custom = ImageIO.read(IU_3_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 3 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + IU_3_CUSTOM_DIR.getAbsolutePath());
            plugin.iu3Custom = null;
        }

        if (IU_4_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.iu4Custom = ImageIO.read(IU_4_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 4 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + IU_4_CUSTOM_DIR.getAbsolutePath());
            plugin.iu4Custom = null;
        }

        if (IU_5_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.iu5Custom = ImageIO.read(IU_5_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 5 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + IU_5_CUSTOM_DIR.getAbsolutePath());

            plugin.iu5Custom = null;
        }

        if (IU_6_CUSTOM_DIR.exists())
        {
            try
            {
                synchronized (ImageIO.class)
                {
                    plugin.iu6Custom = ImageIO.read(IU_6_CUSTOM_DIR).getScaledInstance(imageWidth, imageHeight, 1);
                }
            }
            catch (Exception e)
            {
                log.error("thenwefightplugin: error setting custom task 6 image", e);
            }
        } else {
            log.error("thenwefightplugin: error cannot find image in " + IU_6_CUSTOM_DIR.getAbsolutePath());
            plugin.iu6Custom = null;
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

    public void updateUnlockList()
    {
        String raw = config.unlockList();
        rawUnlockList = raw.split("\n");
    }

    public void updateItemUnlockList()
    {
        String raw = config.itemUnlockList();
        rawItemUnlockList = raw.split("\n");
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

    public void unlockAllEquipment(MenuEntry entry)
    {
        Player local = client.getLocalPlayer();

        if (local == null)
        {
            return;
        }

        for (KitType kitType : KitType.values())
        {
            if (local.getPlayerComposition() == null)
            {
                continue;
            }

            final int itemId = local.getPlayerComposition().getEquipmentId(kitType);

            if (itemId == -1 || itemId == 6512 || itemId == 0)
            {
                continue;
            }

            if (!unlockedItems.contains(itemId))
            {
                unlockedItems.add(itemId);
            }
        }

        configManager.setConfiguration("thenwefight", "unlockedItems", unlockedItems.toString().replace(" ", "").replace("[", "").replace("]", ""));
    }

    public void lockAllEquipment(MenuEntry entry)
    {
        Player local = client.getLocalPlayer();

        if (local == null)
        {
            return;
        }

        for (KitType kitType : KitType.values())
        {
            if (local.getPlayerComposition() == null)
            {
                continue;
            }

            final int itemId = local.getPlayerComposition().getEquipmentId(kitType);

            if (itemId == -1 || itemId == 6512 || itemId == 0)
            {
                continue;
            }

            if (unlockedItems.contains(itemId))
            {
                unlockedItems.remove((Integer) itemId);
            }
        }

        configManager.setConfiguration("thenwefight", "unlockedItems", unlockedItems.toString().replace(" ", "").replace("[", "").replace("]", ""));
    }

    public List<Integer> stringToIntList(String string)
    {
        return (string == null || string.trim().equals("")) ? Arrays.asList(0) :
                Arrays.stream(string.split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
    }

    public boolean isNumeric(String strNum)
    {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
