package com.strangeone101.holoitemsapi.block;

import com.strangeone101.holoitemsapi.block.ability.BlockDispense;
import com.strangeone101.holoitemsapi.item.CustomItemManager;
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

        if (!CustomItemManager.isCustomItem(itemStack))
            return;

        if (!(CustomItemManager.getCustomItem(itemStack) instanceof Placeable placeable))
            return;

        placeable.onPlace(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getTrackingManager().isTracked(event.getBlock()))
            return;

        var placeable = plugin.getTrackingManager().getCustomBlock(event.getBlock());
        placeable.onBreak(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        if (!plugin.getTrackingManager().isTracked(event.getBlock()))
            return;

        var customBlock = plugin.getTrackingManager().getCustomBlock(event.getBlock());

        if (customBlock instanceof BlockDispense dispensable)
            dispensable.onDispense(event);
    }
}
