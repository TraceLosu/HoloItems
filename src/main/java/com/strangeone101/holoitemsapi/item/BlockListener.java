package com.strangeone101.holoitemsapi.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlockInventoryHolder;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;

public class BlockListener implements Listener {

    private final HoloItemsRevamp plugin;

    public BlockListener(HoloItemsRevamp plugin) {
        this.plugin = plugin;
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
        if (!plugin.getTrackingManager().isTracked(event.getBlock())) {
            return;
        }

        final var ability = plugin.getTrackingManager().getCustomBlock(event.getBlock());
        ability.onBlockBreak(event, event.getBlock().getState());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        if (!plugin.getTrackingManager().isTracked(event.getBlock())) {
            return;
        }

        final var ability = plugin.getTrackingManager().getCustomBlock(event.getBlock());
        ability.onBlockDispense(event, event.getBlock().getState());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(true) instanceof BlockInventoryHolder blockInventoryHolder)
                || !plugin.getTrackingManager().isTracked(blockInventoryHolder.getBlock())) {
            return;
        }

        final var ability = plugin.getTrackingManager().getCustomBlock(blockInventoryHolder.getBlock());
        ability.onInventoryClose(event, blockInventoryHolder.getBlock().getState());
    }

    @EventHandler
    public void onPlayerChunkLoad(PlayerChunkLoadEvent event) {
        final var blockStates = event.getChunk()
                .getTileEntities(block -> plugin.getTrackingManager().isTracked(block), true);

        blockStates.forEach(blockState -> plugin.getTrackingManager()
                .getCustomBlock(blockState.getBlock()).onPlayerChunkLoad(event, blockState));
    }
}
