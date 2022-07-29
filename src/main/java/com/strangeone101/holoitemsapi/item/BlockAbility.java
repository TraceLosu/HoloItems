package com.strangeone101.holoitemsapi.item;

import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;

/**
 * An interface that represents an enchantment belonging to a custom item that
 * executes code when placed/broken
 */
public interface BlockAbility {

    default void onBlockBreak(BlockBreakEvent event, BlockState blockState) {
    }

    default void onBlockDispense(BlockDispenseEvent event, BlockState blockState) {
    }

    default void onBlockPlace(BlockPlaceEvent event, BlockState blockState) {
    }

    default void onInventoryClose(InventoryCloseEvent event, BlockState blockState) {
    }

    default void onPlayerChunkLoad(PlayerChunkLoadEvent event, BlockState blockState) {
    }

    short getIdentifier();
}
