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
class ThenWeFightUnlockOverlay extends Overlay {
    @Inject
    private ItemManager itemManager;

    private final Client client;
    private final ThenWeFightConfig config;
    private final ThenWeFightPlugin plugin;

    private static final Font FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 16);
    private static final String TITLE = "Unlock Overlay";


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

        renderSlot(graphics, overlay, 1);
        renderSlot(graphics, overlay, 2);
        renderSlot(graphics, overlay, 3);
        renderSlot(graphics, overlay, 4);
        renderSlot(graphics, overlay, 5);
        renderSlot(graphics, overlay, 6);

        return null;
    }

    public void renderSlot(Graphics2D graphics, Point overlay, int slot)
    {
        boolean unlocked = false;
        String title = null;
        Point titlePoint = null;
        boolean useItemImage = true;
        int itemId = 0;
        int iamgeWidth = config.unlockImageWidth();
        int imageHeight = config.unlockImageHeight();
        Image itemImage = null;
        Point imagePoint = null;
        String price = null;
        Point pricePoint = null;

        switch(slot)
        {
            case 1:
                if (!config.slot1Title().equals(""))
                {
                    if (plugin.getU1Custom() != null)
                    {
                        useItemImage = config.slot1UseItem();
                    }

                    unlocked = config.slot1Unlocked();
                    title = config.slot1Title();
                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot1Title())/ 2) + 90, overlay.y + graphics.getFontMetrics().getHeight() + 97);
                    itemId = config.slot1Id();
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU1Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 88 - (iamgeWidth / 2), overlay.y + 72 - (imageHeight / 2));
                    price = config.slot1Price();
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot1Price())/ 2) + 90, overlay.y + graphics.getFontMetrics().getHeight() + 122);
                }
                break;
            case 2:
                if (!config.slot2Title().equals(""))
                {
                    if (plugin.getU2Custom() != null)
                    {
                        useItemImage = config.slot2UseItem();
                    }

                    unlocked = config.slot2Unlocked();
                    title = config.slot2Title();
                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot2Title())/ 2) + 244, overlay.y + graphics.getFontMetrics().getHeight() + 97);
                    itemId = config.slot2Id();
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU2Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 242 - (iamgeWidth / 2), overlay.y + 72 - (imageHeight / 2));
                    price = config.slot2Price();
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot2Price())/ 2) + 244, overlay.y + graphics.getFontMetrics().getHeight() + 122);
                }
                break;
            case 3:
                if (!config.slot3Title().equals(""))
                {
                    if (plugin.getU3Custom() != null)
                    {
                        useItemImage = config.slot3UseItem();
                    }

                    unlocked = config.slot3Unlocked();
                    title = config.slot3Title();
                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot3Title())/ 2) + 398, overlay.y + graphics.getFontMetrics().getHeight() + 97);
                    itemId = config.slot3Id();
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU3Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 396 - (iamgeWidth / 2), overlay.y + 72 - (imageHeight / 2));
                    price = config.slot3Price();
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot3Price())/ 2) + 398, overlay.y + graphics.getFontMetrics().getHeight() + 122);
                }
                break;
            case 4:
                if (!config.slot4Title().equals(""))
                {
                    if (plugin.getU4Custom() != null)
                    {
                        useItemImage = config.slot4UseItem();
                    }

                    unlocked = config.slot4Unlocked();
                    title = config.slot4Title();
                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot4Title())/ 2) + 90, overlay.y + graphics.getFontMetrics().getHeight() + 227);
                    itemId = config.slot4Id();
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU4Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 88 - (iamgeWidth / 2), overlay.y + 202 - (imageHeight / 2));
                    price = config.slot4Price();
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot4Price())/ 2) + 90, overlay.y + graphics.getFontMetrics().getHeight() + 252);
                }
                break;
            case 5:
                if (!config.slot5Title().equals(""))
                {
                    if (plugin.getU5Custom() != null)
                    {
                        useItemImage = config.slot5UseItem();
                    }

                    unlocked = config.slot5Unlocked();
                    title = config.slot5Title();
                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot5Title())/ 2) + 244, overlay.y + graphics.getFontMetrics().getHeight() + 227);
                    itemId = config.slot5Id();
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU5Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 242 - (iamgeWidth / 2), overlay.y + 202 - (imageHeight / 2));
                    price = config.slot5Price();
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot5Price())/ 2) + 244, overlay.y + graphics.getFontMetrics().getHeight() + 252);
                }
                break;
            case 6:
                if (!config.slot6Title().equals(""))
                {
                    if (plugin.getU6Custom() != null)
                    {
                        useItemImage = config.slot6UseItem();
                    }

                    unlocked = config.slot6Unlocked();
                    title = config.slot6Title();
                    titlePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot6Title())/ 2) + 398, overlay.y + graphics.getFontMetrics().getHeight() + 227);
                    itemId = config.slot6Id();
                    itemImage = (useItemImage ? itemManager.getImage(itemId) : plugin.getU6Custom()).getScaledInstance(iamgeWidth, imageHeight, 1);
                    imagePoint = new Point(overlay.x + 396 - (iamgeWidth / 2), overlay.y + 202 - (imageHeight / 2));
                    price = config.slot6Price();
                    pricePoint = new Point(overlay.x - (graphics.getFontMetrics().stringWidth(config.slot6Price())/ 2) + 398, overlay.y + graphics.getFontMetrics().getHeight() + 252);
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
