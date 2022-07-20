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
        final var trackedChunk = getTrackedChunk(block);
        return Objects.nonNull(trackedChunk) && trackedChunk.isTracked(block);
    }

    protected void add(final Block block, final short identifier) {
        var trackedChunk = getTrackedChunk(block);
        if (Objects.isNull(trackedChunk)) {
            trackedChunk = new TrackedChunk(null);
            chunkMap.put(getChunkKey(block), trackedChunk);
        }

        trackedChunk.add(block, identifier);
    }

    protected void remove(final Block block) {
        final var trackedChunk = getTrackedChunk(block);
        if (Objects.isNull(trackedChunk))
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
}
