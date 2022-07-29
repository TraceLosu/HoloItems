package com.strangeone101.holoitemsapi.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlockInventoryHolder;

import com.strangeone101.holoitemsapi.tracking.TrackingManager;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;

public class BlockListener implements Listener {

    private final TrackingManager trackingManager;

    public BlockListener(HoloItemsRevamp plugin) {
        this.trackingManager = plugin.getTrackingManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        var itemStack = event.getItemInHand();

        if (!(CustomItemManager.getCustomItem(itemStack) instanceof BlockAbility ability)) {
            return;
        }

        ability.onBlockPlace(event, event.getBlock().getState());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!trackingManager.isTracked(event.getBlock())) {
            return;
        }

        trackingManager.getBlockAbility(event.getBlock())
                .onBlockBreak(event, event.getBlock().getState());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        if (!trackingManager.isTracked(event.getBlock())) {
            return;
        }

        trackingManager.getBlockAbility(event.getBlock())
                .onBlockDispense(event, event.getBlock().getState());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(true) instanceof BlockInventoryHolder blockInventoryHolder)
                || !trackingManager.isTracked(blockInventoryHolder.getBlock())) {
            return;
        }

        trackingManager.getBlockAbility(blockInventoryHolder.getBlock())
                .onInventoryClose(event, blockInventoryHolder.getBlock().getState());
    }

    @EventHandler
    public void onPlayerChunkLoad(PlayerChunkLoadEvent event) {
        trackingManager.getTrackedBlocks(event.getWorld().getUID(), event.getChunk().getChunkKey())
                .forEach(entry -> entry.getValue().onPlayerChunkLoad(event, entry.getKey().blockState()));
    }
}
