package com.thenwefight.overlay;

import com.thenwefight.ThenWeFightConfig;
import com.thenwefight.ThenWeFightPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class ThenWeFightItemOverlay extends WidgetItemOverlay {

    private final ItemManager itemManager;
    private final ThenWeFightPlugin plugin;
    private final ThenWeFightConfig config;

    @Inject
    ThenWeFightItemOverlay(ItemManager itemManager, ThenWeFightPlugin plugin, ThenWeFightConfig config)
    {
        this.itemManager = itemManager;
        this.plugin = plugin;
        this.config = config;
        showOnEquipment();
        showOnInventory();
        showOnBank();
        showOnInterfaces(
                WidgetID.CHAMBERS_OF_XERIC_STORAGE_UNIT_INVENTORY_GROUP_ID,
                WidgetID.CHAMBERS_OF_XERIC_STORAGE_UNIT_PRIVATE_GROUP_ID,
                WidgetID.CHAMBERS_OF_XERIC_STORAGE_UNIT_SHARED_GROUP_ID,
                WidgetID.GRAVESTONE_GROUP_ID
        );
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
    {
        if ((config.unlockItems() && !ThenWeFightPlugin.unlockedItems.contains(itemId))
            || config.lockCoins() && itemId == 995)
        {
            Rectangle bounds = widgetItem.getCanvasBounds();

            final BufferedImage outline = itemManager.getItemOutline(itemId, widgetItem.getQuantity(), config.uiOverlayColour());
            graphics.drawImage(outline, (int) bounds.getX(), (int) bounds.getY(), null);

            final Image image = ImageUtil.fillImage(itemManager.getImage(itemId, widgetItem.getQuantity(), false), config.uiOverlayColour());
            float opacity = (config.opacity() == 0 ? 0 : config.opacity() / 255f);
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            graphics.drawImage(image, (int) bounds.getX(), (int) bounds.getY(), null);
        }
    }
}