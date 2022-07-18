package com.strangeone101.holoitemsapi.block;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * An interface that represents an enchantment belonging to a custom item that executes code when placed/broken
 */
public interface Placeable {

    default void onPlace(BlockPlaceEvent event) {
    }

    default void onBreak(BlockBreakEvent event) {
    }

    default void onLoad(ChunkLoadEvent event) {
    }

    default void onUnload(ChunkUnloadEvent event) {
    }

    short getIdentifier();
}
