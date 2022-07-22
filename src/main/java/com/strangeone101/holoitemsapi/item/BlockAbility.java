package com.strangeone101.holoitemsapi.item;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * An interface that represents an enchantment belonging to a custom item that
 * executes code when placed/broken
 */
public interface BlockAbility {

    default void onBlockBreak(BlockBreakEvent event, Block block) {
    }

    default void onBlockDispense(BlockDispenseEvent event, Block block) {
    }

    default void onBlockPlace(BlockPlaceEvent event, Block block) {
    }

    default void onChunkLoad(ChunkLoadEvent event, Block block) {
    }

    default void onChunkUnload(ChunkUnloadEvent event, Block block) {
    }

    short getIdentifier();
}
