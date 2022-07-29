package com.strangeone101.holoitemsapi.tracking;

import com.strangeone101.holoitemsapi.item.BlockAbility;
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

public class TrackingListener implements Listener {

    private final HoloItemsRevamp plugin;

    public TrackingListener(HoloItemsRevamp plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final var itemStack = event.getItemInHand();

        if (!(CustomItemManager.getCustomItem(itemStack) instanceof BlockAbility ability))
            return;

        plugin.getTrackingManager().track(event.getBlockPlaced(), ability);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        plugin.getTrackingManager().untrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockExplode(final BlockExplodeEvent event) {
        event.blockList().forEach(plugin.getTrackingManager()::untrack);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBurn(final BlockBurnEvent event) {
        plugin.getTrackingManager().untrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockFade(final BlockFadeEvent event) {
        plugin.getTrackingManager().untrack(event.getBlock());
    }

    //TODO handle piston movement (maybe outright disable them when used against custom blocks?)
}
