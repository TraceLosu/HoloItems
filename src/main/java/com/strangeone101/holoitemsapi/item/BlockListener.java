package com.strangeone101.holoitemsapi.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;

public class BlockListener implements Listener {

    private final HoloItemsRevamp plugin;

    public BlockListener(HoloItemsRevamp plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        var itemStack = event.getItemInHand();

        if (!(CustomItemManager.getCustomItem(itemStack) instanceof BlockAbility ability))
            return;

        final var block = event.getBlock();
        ability.onBlockPlace(event, block);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getTrackingManager().isTracked(event.getBlock()))
            return;

        final var ability = plugin.getTrackingManager().getCustomBlock(event.getBlock());
        final var block = event.getBlock();
        ability.onBlockBreak(event, block);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        if (!plugin.getTrackingManager().isTracked(event.getBlock()))
            return;

        final var ability = plugin.getTrackingManager().getCustomBlock(event.getBlock());
        final var block = event.getBlock();
        ability.onBlockDispense(event, block);
    }
}
