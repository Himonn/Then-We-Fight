package com.thenwefight.overlay;

import com.google.common.base.Strings;
import com.thenwefight.ThenWeFightConfig;
import com.thenwefight.ThenWeFightPlugin;
import com.thenwefight.utils.PluginUtils;
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
class ThenWeFightUnlockOverlay extends Overlay {

    @Inject
    private ItemManager itemManager;
    @Inject
    private PluginUtils pluginUtils;

    private final Client client;
    private final ThenWeFightConfig config;
    private final ThenWeFightPlugin plugin;

    private static final Font FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 16);
    private static final String TITLE = "Then We Fight Unlocks";


    @Inject
    private ThenWeFightUnlockOverlay(final Client client, final ThenWeFightConfig config, final ThenWeFightPlugin plugin) {
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

        if (!plugin.unlockOverlayVisible)
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

        renderSlots(graphics, overlay);

        return null;
    }

    public void renderSlots(Graphics2D graphics, Point overlay)
    {
        for (int i = config.unlockScroll(); i <= ThenWeFightPlugin.rawUnlockList.length; i++)
        {
            if (i >= config.unlockScroll() + 6) {
                break;
            }

            if (i >= ThenWeFightPlugin.rawUnlockList.length || ThenWeFightPlugin.rawUnlockList[i] == null) {
                continue;
            }

            String[] parsed = ThenWeFightPlugin.rawUnlockList[i].split(",");

            if (parsed == null || parsed.length != 4) {
                continue;
            }

            boolean unlocked = parsed[0].equalsIgnoreCase("y");
            boolean useItemImage = pluginUtils.isNumeric(parsed[1]);
            String points = parsed[2] + " Points";
            String title = parsed[3];
            int itemId = -1;

            if (useItemImage)
            {
                itemId = Integer.parseInt(parsed[1]);
            }

            renderSlot(graphics, overlay, i - config.unlockScroll() + 1, unlocked, title, points, useItemImage, itemId);
        }
    }

    public void renderSlot(Graphics2D graphics, Point overlay, int slot, boolean unlocked, String title, String price, boolean useItemImage, int itemId)
    {
        Point titlePoint = null;
        int iamgeWidth = config.unlockImageWidth();
        int imageHeight = config.unlockImageHeight();
        Image itemImage = null;
        Point imagePoint = null;
        Point pricePoint = null;

        switch(slot)
        {
            case 1:
                if (!title.equals(""))
                {
                    if (!useItemImage && plugin.getU1Custom() == null)
                    {
                        useItemImage = true;
                    }

                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(title)/ 2) + 90, overlay.y + graphics.getFontMetrics().getHeight() + 97);
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU1Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 88 - (iamgeWidth / 2), overlay.y + 72 - (imageHeight / 2));
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(price)/ 2) + 90, overlay.y + graphics.getFontMetrics().getHeight() + 122);
                }
                break;
            case 2:
                if (!title.equals(""))
                {
                    if (!useItemImage && plugin.getU2Custom() == null)
                    {
                        useItemImage = true;
                    }

                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(title)/ 2) + 244, overlay.y + graphics.getFontMetrics().getHeight() + 97);
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU2Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 242 - (iamgeWidth / 2), overlay.y + 72 - (imageHeight / 2));
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(price)/ 2) + 244, overlay.y + graphics.getFontMetrics().getHeight() + 122);
                }
                break;
            case 3:
                if (!title.equals(""))
                {
                    if (!useItemImage && plugin.getU3Custom() == null)
                    {
                        useItemImage = true;
                    }

                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(title)/ 2) + 398, overlay.y + graphics.getFontMetrics().getHeight() + 97);
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU3Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 396 - (iamgeWidth / 2), overlay.y + 72 - (imageHeight / 2));
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(price)/ 2) + 398, overlay.y + graphics.getFontMetrics().getHeight() + 122);
                }
                break;
            case 4:
                if (!title.equals(""))
                {
                    if (!useItemImage && plugin.getU4Custom() == null)
                    {
                        useItemImage = true;
                    }

                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(title)/ 2) + 90, overlay.y + graphics.getFontMetrics().getHeight() + 227);
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU4Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 88 - (iamgeWidth / 2), overlay.y + 202 - (imageHeight / 2));
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(price)/ 2) + 90, overlay.y + graphics.getFontMetrics().getHeight() + 252);
                }
                break;
            case 5:
                if (!title.equals(""))
                {
                    if (!useItemImage && plugin.getU5Custom() == null)
                    {
                        useItemImage = true;
                    }

                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(title)/ 2) + 244, overlay.y + graphics.getFontMetrics().getHeight() + 227);
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU5Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 242 - (iamgeWidth / 2), overlay.y + 202 - (imageHeight / 2));
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(price)/ 2) + 244, overlay.y + graphics.getFontMetrics().getHeight() + 252);
                }
                break;
            case 6:
                if (!title.equals(""))
                {
                    if (!useItemImage && plugin.getU6Custom() == null)
                    {
                        useItemImage = true;
                    }

                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(title)/ 2) + 398, overlay.y + graphics.getFontMetrics().getHeight() + 227);
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU6Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 396 - (iamgeWidth / 2), overlay.y + 202 - (imageHeight / 2));
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(price)/ 2) + 398, overlay.y + graphics.getFontMetrics().getHeight() + 252);
                }
                break;
        }

        if (title == null)
        {
            return;
        }

        renderTextLocation(graphics, titlePoint, title, unlocked ? Color.GREEN : Color.YELLOW);
        renderImageLocation(graphics, imagePoint, itemImage);
        renderTextLocation(graphics, pricePoint, price, unlocked ? Color.GREEN : Color.YELLOW);
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
