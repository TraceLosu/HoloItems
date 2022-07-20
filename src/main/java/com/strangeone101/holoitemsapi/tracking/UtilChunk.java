package com.strangeone101.holoitemsapi.tracking;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class UtilChunk {

    public static long getChunkKey(final Chunk chunk) {
        return getChunkKey(chunk.getX(), chunk.getZ());
    }

    public static long getChunkKey(final int chunkX, final int chunkZ) {
        return (long) chunkX & 0xFFFFFFFFL | ((long) chunkZ & 0xFFFFFFFFL) << 32;
    }

    public static long getChunkKeyOfBlock(final Block block) {
        return getChunkKey(block.getX() >> 4, block.getZ() >> 4);
    }

    public static int getRelativeChunkPosition(final Block block) {
        final int relX = block.getX() & 0xF;
        final int relZ = block.getZ() & 0xF;
        final int relY = block.getY() & 0xFFFF;
        return relY | relX << 16 | relZ << 24;
    }
}
