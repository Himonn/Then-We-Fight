package com.thenwefight.overlay;

import com.google.common.base.Strings;
import com.thenwefight.ThenWeFightConfig;
import com.thenwefight.ThenWeFightPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Slf4j
@Singleton
public
class ThenWeFightTaskOverlay extends Overlay {
    @Inject
    private ItemManager itemManager;

    private final Client client;
    private final ThenWeFightConfig config;
    private final ThenWeFightPlugin plugin;

    private static final Font FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 16);
    private static final String TITLE = "Task Overlay";


    @Inject
    private ThenWeFightTaskOverlay(final Client client, final ThenWeFightConfig config, final ThenWeFightPlugin plugin) {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.setPriority(OverlayPriority.HIGHEST);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        graphics.setFont(FONT);

        if (!plugin.taskOverlayVisible)
        {
            return null;
        }

        Point overlay = new Point(config.overlayX(), config.overlayY());
        Point title = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(TITLE) / 2)  + 244, overlay.y + graphics.getFontMetrics().getHeight() + 9);
        Point points = new Point(overlay.x + 20, overlay.y + graphics.getFontMetrics().getHeight() + 9);

        if (config.debug())
        {
            if (plugin.getDebugBackground() != null)
            {
                renderImageLocation(graphics, overlay, plugin.getDebugBackground());
            }
        } else {
            if (plugin.getBackground() != null)
            {
                renderImageLocation(graphics, overlay, plugin.getBackground());
            }
        }

        renderTextLocation(graphics, title, TITLE, Color.YELLOW);
        renderTextLocation(graphics, points, config.points(), Color.YELLOW);

        for (int i = config.taskScroll(); i <= ThenWeFightPlugin.rawTaskList.length; i++)
        {
            if (i >= config.taskScroll() + 12)
            {
                break;
            }

            if (i >= ThenWeFightPlugin.rawTaskList.length || ThenWeFightPlugin.rawTaskList[i] == null)
            {
                continue;
            }

            String[] parsed = ThenWeFightPlugin.rawTaskList[i].split(",");

//            if (parsed == null || parsed.length != 3)
            if (parsed == null || parsed.length != 3)
            {
                continue;
            }

            boolean taskCompleted = parsed[0].equalsIgnoreCase("y");
            String taskPoints = parsed[1] + " pts";
            String taskName = parsed[2];
            Color colour = taskCompleted ? Color.GREEN : Color.YELLOW;
            String text = (taskCompleted ? "[x] " : "[  ] ") + taskPoints + " - " + taskName;
            int textLength = text.length();

            while (graphics.getFontMetrics().stringWidth(text) >= 465)
            {
                text = text.substring(0,text.length()-1);
            }

            if (textLength != text.length())
            {
                text = text.substring(0, text.length()-3) + "...";
            }

            Point point = new Point(20 + overlay.x, 40 +(20 * (i - config.taskScroll() + 1)) + overlay.y);

            renderTextLocation(graphics, point, text, colour);
        }

        return null;
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

    public static void renderImageLocation(Graphics2D graphics, Point loc, Image image)
    {
        graphics.drawImage(image, loc.x, loc.y, null);
    }
}
