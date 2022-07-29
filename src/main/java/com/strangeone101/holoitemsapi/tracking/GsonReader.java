package com.strangeone101.holoitemsapi.tracking;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.strangeone101.holoitemsapi.item.BlockAbility;
import com.strangeone101.holoitemsapi.item.CustomItemManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class GsonReader extends JsonReader {

    public GsonReader(final File file) throws IOException {
        super(new BufferedReader(new FileReader(file)));
    }

    public void readBlocks(
            final Map<TrackedBlock, BlockAbility> trackedBlocks,
            final Map<TrackedBlock, BlockAbility> invalidBlocks) throws IOException {
        if (peek() == JsonToken.NULL) {
            return;
        }

        final var loadedBukkitWorlds = Bukkit.getWorlds().stream().map(World::getUID).toList();

        beginObject();
        while (hasNext()) {
            final var worldKeyString = nextName();
            final UUID worldKey;
            try {
                worldKey = UUID.fromString(worldKeyString);
            } catch (IllegalArgumentException e) {
                throw new IOException("Failed to read UUID: " + worldKeyString);
            }

            beginObject();
            while (hasNext()) {
                final var chunkKeyString = nextName();
                final long chunkKey;
                try {
                    chunkKey = Long.parseLong(chunkKeyString);
                } catch (NumberFormatException e) {
                    throw new IOException("Failed to read Long: " + chunkKeyString);
                }

                beginObject();
                while (hasNext()) {
                    final var blockKeyString = nextName();
                    final int blockKey;
                    try {
                        blockKey = Integer.parseInt(blockKeyString);
                    } catch (NumberFormatException e) {
                        throw new IOException("Failed to read Integer: " + blockKeyString);
                    }

                    final var block = new TrackedBlock(worldKey, chunkKey, blockKey);
                    final var blockAbility = CustomItemManager.getCustomBlock((short) nextInt());

                    if (loadedBukkitWorlds.contains(worldKey)) {
                        trackedBlocks.put(block, blockAbility);
                    } else {
                        invalidBlocks.put(block, blockAbility);
                    }
                }
                endObject();
            }
            endObject();
        }
        endObject();
    }
}
