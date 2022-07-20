package com.strangeone101.holoitemsapi.tracking;

import org.bukkit.block.Block;

public class UtilChunk {

    public static int getRelativeChunkPosition(final Block block) {
        final int relX = block.getX() & 0xF;
        final int relZ = block.getZ() & 0xF;
        final int relY = block.getY() & 0xFFFF;
        return relY | relX << 16 | relZ << 24;
    }
}
