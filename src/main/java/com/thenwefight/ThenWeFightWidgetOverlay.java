package com.thenwefight;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.geom.Rectangle2D;

@Slf4j
@Singleton
class ThenWeFightWidgetOverlay extends Overlay {

    @Inject
    private Client client;
    private final ThenWeFightConfig config;
    private final ThenWeFightPlugin plugin;

    @Inject
    private ThenWeFightWidgetOverlay(final Client client, final ThenWeFightConfig config, final ThenWeFightPlugin plugin) {
        super(plugin);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }


    @Override
    public Dimension render(Graphics2D graphics)
    {
        Player local = client.getLocalPlayer();

        if (plugin == null || local == null)
        {
            return null;
        }

        if (config.lockWidgets())
        {
            for (int i : ThenWeFightPlugin.lockedWidgets)
            {
                Widget custom = client.getWidget(i);

                if (custom != null && !custom.isHidden())
                {
                    renderWidgetOverlay(graphics, custom, config.overlayColour());
                }
            }
        }

        if (config.lockRunEnergy())
        {
            Widget runOrb = client.getWidget(WidgetInfo.MINIMAP_RUN_ORB);

            if (runOrb != null && !runOrb.isHidden())
            {
                renderImageLocation(graphics, runOrb.getBounds().getLocation(), plugin.getRunOrb());
            }
        }

        if (config.lockPrayers())
        {
            Widget prayOrb = client.getWidget(WidgetInfo.MINIMAP_PRAYER_ORB);

            if (prayOrb != null && !prayOrb.isHidden())
            {
                renderImageLocation(graphics, prayOrb.getBounds().getLocation(), plugin.getPrayerOrb());
            }

            Widget prayerTab = client.getWidget(35454980);

            if (prayerTab != null && !prayerTab.isHidden())
            {
                Widget[] prayers = prayerTab.getStaticChildren();

                if (prayers != null)
                {
                    for (Widget prayer : prayers)
                    {
                        if (!prayer.isHidden())
                        {
                            renderWidgetOverlay(graphics, prayer, config.overlayColour());
                        }
                    }
                }
            }
        }

        if (config.lockSpec())
        {
            Widget specOrb = client.getWidget(WidgetInfo.MINIMAP_SPEC_ORB);

            if (specOrb != null && !specOrb.isHidden())
            {
                renderImageLocation(graphics, specOrb.getBounds().getLocation(), plugin.getSpecOrb());
            }

            Widget specBar = client.getWidget(38862888);

            if (specBar != null && !specBar.isHidden())
            {
                renderWidgetOverlay(graphics, specBar, config.overlayColour());
            }
        }

        if (config.lockHpOrb())
        {
            Widget hpOrb = client.getWidget(WidgetInfo.MINIMAP_HEALTH_ORB);

            if (hpOrb != null && !hpOrb.isHidden())
            {
                renderImageLocation(graphics, hpOrb.getBounds().getLocation(), plugin.getHealthOrb());
            }
        }

        if (config.lockTeles())
        {
            Widget spellsTab = client.getWidget(14286851);

            if (spellsTab != null && !spellsTab.isHidden())
            {
                Widget[] spells = spellsTab.getStaticChildren();

                if (spells != null)
                {
                    for (Widget spell : spells)
                    {
                        if (!spell.isHidden() && spell.getName().toLowerCase().contains("tele"))
                        {
                            renderWidgetOverlay(graphics, spell, config.overlayColour());
                        }
                    }
                }
            }
        }

        Widget skillsTab = client.getWidget(WidgetInfo.SKILLS_CONTAINER);

        if (skillsTab != null && !skillsTab.isHidden())
        {
            Widget[] skills = skillsTab.getStaticChildren();

            if (skills != null)
            {
                for (Widget skill : skills)
                {
                    if (skill.isHidden() || skill.getId() == 20971548)
                    {
                        continue;
                    }

                    if ((config.lockAttack() && skill.getId() == 20971521)
                        || (config.lockStrength() && skill.getId() == 20971522)
                        || (config.lockDefence() && skill.getId() == 20971523)
                        || (config.lockRange() && skill.getId() == 20971524)
                        || (config.lockPrayer() && skill.getId() == 20971525)
                        || (config.lockMagic() && skill.getId() == 20971526)
                        || (config.lockRunecrafting() && skill.getId() == 20971527)
                        || (config.lockConstruction() && skill.getId() == 20971528)
                        || (config.lockHitpoints() && skill.getId() == 20971529)
                        || (config.lockAgility() && skill.getId() == 20971530)
                        || (config.lockHerblore() && skill.getId() == 20971531)
                        || (config.lockThieving() && skill.getId() == 20971532)
                        || (config.lockCrafting() && skill.getId() == 20971533)
                        || (config.lockFletching() && skill.getId() == 20971534)
                        || (config.lockSlayer() && skill.getId() == 20971535)
                        || (config.lockHunter() && skill.getId() == 20971536)
                        || (config.lockMining() && skill.getId() == 20971537)
                        || (config.lockSmithing() && skill.getId() == 20971538)
                        || (config.lockFishing() && skill.getId() == 20971539)
                        || (config.lockCooking() && skill.getId() == 20971540)
                        || (config.lockFiremaking() && skill.getId() == 20971541)
                        || (config.lockWoodcutting() && skill.getId() == 20971542)
                        || (config.lockFarming() && skill.getId() == 20971543))
                    {
                        renderWidgetOverlay(graphics, skill, config.overlayColour());
                    }
                }
            }
        }

        if (config.lockEmotes())
        {
            Widget emoteTab = client.getWidget(WidgetInfo.EMOTE_CONTAINER);

            if (emoteTab != null && !emoteTab.isHidden())
            {
                renderWidgetOverlay(graphics, emoteTab, config.overlayColour());
            }
        }

        if (config.lockHopping())
        {
            Widget worldList = client.getWidget(4521984);

            if (worldList != null && !worldList.isHidden())
            {
                renderWidgetOverlay(graphics, worldList, config.overlayColour());
            }

            Widget switchWorlds = client.getWidget(11927559);

            if (switchWorlds != null && !switchWorlds.isHidden())
            {
                renderWidgetOverlay(graphics, switchWorlds, config.overlayColour());
            }
        }

        return null;
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

    private void renderWidgetOverlay(Graphics2D graphics, Widget widget, Color color)
    {
        Rectangle2D bounds = widget.getBounds();

        if (bounds != null)
        {
            renderPolyFill(graphics, color, bounds, true);
        }
    }

    public static void renderImageLocation(Graphics2D graphics, Point loc, Image image)
    {
        graphics.drawImage(image, loc.x, loc.y, null);
    }
}
