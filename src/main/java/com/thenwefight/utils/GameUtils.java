package com.thenwefight.utils;

import com.thenwefight.ThenWeFightConfig;
import com.thenwefight.ThenWeFightPlugin;
import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.thenwefight.ThenWeFightPlugin.*;

public class GameUtils {

    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ThenWeFightPlugin plugin;
    @Inject
    private ThenWeFightConfig config;

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
    public ObjectComposition getObjectComposition(int id)
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

    public Collection<Widget> getAllItems()
    {
        Widget inventory = client.getWidget(WidgetInfo.INVENTORY);

        if (inventory == null)
        {
            return null;
        }

        if (isWidgetInventoryContainer(inventory) && inventory.isHidden())
        {
            refreshInventory();
        }

        Widget[] inventoryItems = inventory.getChildren();

        if (inventoryItems == null || inventoryItems.length == 0)
        {
            return null;
        }

        return Arrays.stream(inventoryItems).collect(Collectors.toList());
    }

    public boolean isWidgetInventoryContainer(Widget widget)
    {
        Widget inventory = client.getWidget(WidgetInfo.INVENTORY);
        return widget != null && inventory != null && widget.getId() == inventory.getId();
    }

    public void refreshInventory()
    {
        if (!client.isClientThread())
        {
            clientThread.invokeLater(() -> client.runScript(6009, WidgetInfo.INVENTORY.getId(), 28, 1, -1));
            return;
        }

        client.runScript(6009, WidgetInfo.INVENTORY.getId(), 28, 1, -1);
    }
}
