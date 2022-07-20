package com.strangeone101.holoitemsapi.tracking;

import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import it.unimi.dsi.fastutil.ints.Int2ShortOpenHashMap;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TrackedChunk {

    private final Int2ShortMap trackedBlocks;

    protected TrackedChunk(@Nullable final Int2ShortMap trackedBlocks) {
        this.trackedBlocks = Objects.requireNonNullElseGet(trackedBlocks, Int2ShortOpenHashMap::new);
    }

    public Int2ShortMap getTrackedBlocks() {
        return trackedBlocks;
    }

    protected boolean isEmpty() {
        return trackedBlocks.isEmpty();
    }

    protected short get(final Block block) {
        return trackedBlocks.get(getRelativeChunkPosition(block));
    }

    protected boolean isTracked(final Block block) {
        return trackedBlocks.containsKey(getRelativeChunkPosition(block));
    }

    protected void add(final Block block, final short identifier) {
        trackedBlocks.put(getRelativeChunkPosition(block), identifier);
    }

    protected void remove(final Block block) {
        trackedBlocks.remove(getRelativeChunkPosition(block));
    }

    private static int getRelativeChunkPosition(final Block block) {
        final int relX = block.getX() & 0xF;
        final int relZ = block.getZ() & 0xF;
        final int relY = block.getY() & 0xFFFF;
        return relY | relX << 16 | relZ << 24;
    }
}
