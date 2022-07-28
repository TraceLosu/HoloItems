package com.strangeone101.holoitemsapi.tracking;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import com.strangeone101.holoitemsapi.item.BlockAbility;

import java.util.Objects;

public class TrackedChunk {

    private final Int2ObjectMap<BlockAbility> trackedBlocks;

    protected TrackedChunk(@Nullable final Int2ObjectMap<BlockAbility> trackedBlocks) {
        this.trackedBlocks = Objects.requireNonNullElseGet(trackedBlocks, Int2ObjectOpenHashMap::new);
    }

    public Int2ObjectMap<BlockAbility> getTrackedBlocks() {
        return trackedBlocks;
    }

    protected boolean isEmpty() {
        return trackedBlocks.isEmpty();
    }

    protected BlockAbility get(final Block block) {
        return trackedBlocks.get(getRelativeChunkPosition(block));
    }

    protected boolean isTracked(final Block block) {
        return trackedBlocks.containsKey(getRelativeChunkPosition(block));
    }

    protected void add(final Block block, final BlockAbility ability) {
        trackedBlocks.put(getRelativeChunkPosition(block), ability);
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
