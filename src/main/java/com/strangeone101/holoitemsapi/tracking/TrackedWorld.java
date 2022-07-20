package com.strangeone101.holoitemsapi.tracking;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TrackedWorld {

    private final Long2ObjectMap<TrackedChunk> chunkMap;

    protected TrackedWorld(@Nullable Long2ObjectMap<TrackedChunk> trackedChunks) {
        this.chunkMap = Objects.requireNonNullElseGet(trackedChunks, Long2ObjectOpenHashMap::new);
    }

    public Long2ObjectMap<TrackedChunk> getChunkMap() {
        return chunkMap;
    }

    protected boolean isEmpty() {
        return chunkMap.isEmpty();
    }

    protected short get(final Block block) {
        return getTrackedChunk(block).get(block);
    }

    protected boolean isTracked(final Block block) {
        var trackedChunk = getTrackedChunk(block);

        if (trackedChunk == null)
            return false;

        return trackedChunk.isTracked(block);
    }

    protected void add(final Block block, final short identifier) {
        var trackedChunk = getTrackedChunk(block);

        if (trackedChunk == null)
            trackedChunk = initTrackedChunk(block.getChunk());

        trackedChunk.add(block, identifier);
    }

    protected void remove(final Block block) {
        final var trackedChunk = getTrackedChunk(block);
        if (trackedChunk == null)
            return;

        trackedChunk.remove(block);

        if (trackedChunk.isEmpty())
            chunkMap.remove(getChunkKey(block));
    }

    private TrackedChunk getTrackedChunk(final Block block) {
        return chunkMap.get(getChunkKey(block));
    }

    private static long getChunkKey(final Block block) {
        return Chunk.getChunkKey(block.getX() >> 4, block.getZ() >> 4);
    }

    protected TrackedChunk initTrackedChunk(final Chunk chunk) {
        final TrackedChunk trackedChunk = new TrackedChunk(null);
        chunkMap.put(chunk.getChunkKey(), trackedChunk);
        return trackedChunk;
    }
}
