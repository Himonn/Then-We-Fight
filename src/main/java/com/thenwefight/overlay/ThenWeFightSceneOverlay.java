package com.thenwefight.overlay;

import com.google.common.base.Strings;
import com.thenwefight.ThenWeFightConfig;
import com.thenwefight.ThenWeFightPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Slf4j
@Singleton
public
class ThenWeFightSceneOverlay extends Overlay {

    @Inject
    private Client client;
    private final ThenWeFightConfig config;
    private final ThenWeFightPlugin plugin;

    @Inject
    private ThenWeFightSceneOverlay(final Client client, final ThenWeFightConfig config, final ThenWeFightPlugin plugin) {
        super(plugin);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }


    @Override
    public Dimension render(Graphics2D graphics)
    {
        Player local = client.getLocalPlayer();

        if (plugin == null || local == null)
        {
            return null;
        }

        for (NPC n : ThenWeFightPlugin.npcs)
        {
            renderNpcOverlay(graphics, config.gameOverlayColour(), n);
        }

        for (GameObject g : ThenWeFightPlugin.gameObjects)
        {
            renderGameObject(graphics, config.gameOverlayColour(), g);
        }

        for (GroundObject go : ThenWeFightPlugin.groundObjects)
        {
            renderGroundObject(graphics, config.gameOverlayColour(), go);
        }

        for (WallObject wo : ThenWeFightPlugin.wallObjects)
        {
            renderWallObject(graphics, config.gameOverlayColour(), wo);
        }

        return null;
    }

    private void renderWorldArea(final Graphics2D graphics, WorldArea area, Color color)
    {
        WorldPoint sw = area.toWorldPoint();
        int width = area.getWidth();
        int height = area.getHeight();
        boolean widthEven = width % 2 == 0;
        boolean heightEven = height % 2 == 0;

        WorldPoint center = new WorldPoint(sw.dx(width / 2).getX(), sw.dy(height / 2).getY(), sw.getPlane());

        LocalPoint local = LocalPoint.fromWorld(client, center);

        if (local == null)
        {
            return;
        }

        if (!widthEven)
        {
            local = new LocalPoint(local.getX() + (Perspective.LOCAL_HALF_TILE_SIZE), local.getY());
            width++;
        } else {
            width++;
        }

        if (!heightEven)
        {
            local = new LocalPoint(local.getX(), local.getY() + (Perspective.LOCAL_HALF_TILE_SIZE));
            height++;
        } else {
            height++;
        }

//        rendering center tiles for debugging
//        renderWorldPoint(graphics, center, Color.GREEN);
//        renderTile(graphics, local, Color.BLUE, 1);

        Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, local, width, height, area.getPlane(), 100);

        renderPoly(graphics, color, tilePoly, false);
    }

    private void renderNpcText(Graphics2D graphics, NPC npc, String text, Color color)
    {
        Point txtLoc = npc.getCanvasTextLocation(graphics, text, npc.getLogicalHeight() - 70);

        if (npc.getConvexHull() == null || txtLoc == null)
        {
            return;
        }

        renderTextLocation(graphics, txtLoc, text, color);
    }

    private void renderWorldPoint(Graphics2D graphics, WorldPoint worldPoint, Color color)
    {
        LocalPoint lp = LocalPoint.fromWorld(client, worldPoint);
        if (lp != null)
        {
            renderTile(graphics, lp, color, 1);
        }
    }

    private void renderWorldPointText(Graphics2D graphics, WorldPoint worldPoint, String text, Color color)
    {
        LocalPoint lp = LocalPoint.fromWorld(client, worldPoint);
        if (lp != null)
        {
            Point point = Perspective.localToCanvas(client, lp, worldPoint.getPlane());
            if (point != null)
            {
                int textWidth = graphics.getFontMetrics().stringWidth(text);
                int textHeight = graphics.getFontMetrics().getAscent();

                Point centerPoint = new Point(point.getX() - textWidth / 2, point.getY() + textHeight / 2);

                renderTextLocation(graphics, centerPoint, text, color);
            }
        }
    }

    private void renderTile(final Graphics2D graphics, final LocalPoint dest, final Color color, final double borderWidth)
    {
        if (dest == null)
        {
            return;
        }

        final Polygon poly = Perspective.getCanvasTilePoly(client, dest);

        if (poly == null)
        {
            return;
        }

        renderPoly(graphics, color, poly, true);
    }

    public static void renderTextLocation(Graphics2D graphics, Point txtLoc, String text, Color color)
    {
        if (Strings.isNullOrEmpty(text))
        {
            return;
        }

        int x = (int) txtLoc.getX();
        int y = (int) txtLoc.getY();

        graphics.setColor(Color.BLACK);
        graphics.drawString(text, x + 1, y + 1);

        graphics.setColor(color);
        graphics.drawString(text, x, y);
    }

    private void renderNpcOverlay(Graphics2D graphics, Color color, NPC actor)
    {
        NPCComposition npcComposition = actor.getTransformedComposition();
        if (npcComposition == null || !npcComposition.isInteractible()
                || (actor.isDead()))
        {
            return;
        }
        Shape objectClickbox = actor.getConvexHull();
        renderPolyFill(graphics, color, objectClickbox, true);
    }

    private void renderPoly(Graphics2D graphics, Color color, Shape polygon, boolean fill)
    {
        if (polygon != null)
        {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke((float) 1.2));
            graphics.draw(polygon);
            graphics.setColor(ColorUtil.colorWithAlpha(color, 20));
            if (fill)
            {
                graphics.fill(polygon);
            }
        }
    }

    private void renderPolyFill(Graphics2D graphics, Color color, Shape polygon, boolean fill)
    {
        if (polygon != null)
        {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke((float) 1.2));
            graphics.draw(polygon);
            graphics.setColor(ColorUtil.colorWithAlpha(color, config.opacity()));
            graphics.fill(polygon);
        }
    }

    private void renderGroundObject(Graphics2D graphics, Color color, GroundObject object)
    {
        Shape shape = object.getConvexHull();

        if (shape != null)
        {
            renderPolyFill(graphics, color, shape, true);
        }
    }

    private void renderWallObject(Graphics2D graphics, Color color, WallObject object)
    {
        Shape shape = object.getConvexHull();

        if (shape != null)
        {
            renderPolyFill(graphics, color, shape, true);
        }
    }

    private void renderGameObject(Graphics2D graphics, Color color, GameObject object)
    {
        Shape shape = object.getConvexHull();

        if (shape != null)
        {
            renderPolyFill(graphics, color, shape, true);
        }
    }
}
