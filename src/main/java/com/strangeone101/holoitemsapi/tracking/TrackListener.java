package com.strangeone101.holoitemsapi.tracking;

import com.strangeone101.holoitemsapi.block.Placeable;
import com.strangeone101.holoitemsapi.item.CustomItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;

public class TrackListener implements Listener {

    private final HoloItemsRevamp plugin;

    public TrackListener(HoloItemsRevamp plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(final BlockPlaceEvent event) {
        var itemStack = event.getItemInHand();

        if (!CustomItemManager.isCustomItem(itemStack))
            return;

        if (!(CustomItemManager.getCustomItem(itemStack) instanceof Placeable placeable))
            return;

        plugin.getTrackingManager().track(event.getBlockPlaced(), placeable.getIdentifier());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(final BlockBreakEvent event) {
        plugin.getTrackingManager().untrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplode(final BlockExplodeEvent event) {
        event.blockList().forEach(plugin.getTrackingManager()::untrack);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBurn(final BlockBurnEvent event) {
        plugin.getTrackingManager().untrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFade(final BlockFadeEvent event) {
        plugin.getTrackingManager().untrack(event.getBlock());
    }

    //TODO handle piston movement (maybe outright disable them when used against custom blocks?)
}
