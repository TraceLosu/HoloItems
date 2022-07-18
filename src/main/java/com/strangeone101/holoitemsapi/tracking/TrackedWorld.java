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

    protected short get(final Block block) {
        return this.getChunkOf(block).get(block);
    }

    protected boolean isTracked(final Block block) {
        var trackedChunk = this.getChunkOf(block);

        if (trackedChunk == null)
            return false;

        return trackedChunk.isTracked(block);
    }

    protected void add(final Block block, final short identifier) {
        var trackedChunk = this.getChunkOf(block);

        if (trackedChunk == null)
            trackedChunk = initChunk(block.getChunk());

        trackedChunk.add(block, identifier);
    }

    protected void remove(final Block block) {
        final var trackedChunk = this.getChunkOf(block);
        if (trackedChunk == null)
            return;

        trackedChunk.remove(block);

        if (trackedChunk.isEmpty())
            chunkMap.remove(UtilChunk.getChunkKeyOfBlock(block));
    }

    protected boolean isEmpty() {
        return chunkMap.isEmpty();
    }

    protected TrackedChunk initChunk(final Chunk chunk) {
        final TrackedChunk trackedChunk = new TrackedChunk(null);
        this.chunkMap.put(UtilChunk.getChunkKey(chunk), trackedChunk);
        return trackedChunk;
    }

    public Long2ObjectMap<TrackedChunk> getChunkMap() {
        return chunkMap;
    }

    private TrackedChunk getChunkOf(final Block block) {
        return this.chunkMap.get(UtilChunk.getChunkKeyOfBlock(block));
    }

}
