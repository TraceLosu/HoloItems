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

    protected short get(final Block block) {
        return this.trackedBlocks.get(UtilChunk.getRelativeChunkPosition(block));
    }

    protected void add(final Block block, final short identifier) {
        this.trackedBlocks.put(UtilChunk.getRelativeChunkPosition(block), identifier);
    }

    protected void remove(final Block block) {
        this.trackedBlocks.remove(UtilChunk.getRelativeChunkPosition(block));
    }

    protected boolean isTracked(final Block block) {
        return this.trackedBlocks.containsKey(UtilChunk.getRelativeChunkPosition(block));
    }

    protected boolean isEmpty() {
        return this.trackedBlocks.isEmpty();
    }

    public Int2ShortMap getTrackedBlocks() {
        return trackedBlocks;
    }

}
