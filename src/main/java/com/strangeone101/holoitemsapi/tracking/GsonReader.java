package com.strangeone101.holoitemsapi.tracking;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.strangeone101.holoitemsapi.item.BlockAbility;
import com.strangeone101.holoitemsapi.item.CustomItemManager;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GsonReader extends JsonReader {

    public GsonReader(final File file) throws FileNotFoundException {
        super(new BufferedReader(new FileReader(file)));
    }

    public TrackedWorld nextTrackedWorld() throws IOException {
        if (peek() == JsonToken.NULL) {
            nextNull();
            return null;
        }

        var trackedChunks = new Long2ObjectOpenHashMap<TrackedChunk>();

        beginObject();
        while(hasNext()) {
            try {
                var chunkKey = Long.parseLong(nextName());
                var trackedChunk = nextTrackedChunk();
                trackedChunks.put(chunkKey, trackedChunk);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid number format!");
            }
        }
        endObject();

        return trackedChunks.isEmpty() ? null : new TrackedWorld(trackedChunks);
    }

    public TrackedChunk nextTrackedChunk() throws IOException {
        if (peek() == JsonToken.NULL) {
            nextNull();
            return null;
        }

        var trackedBlocks = new Int2ObjectOpenHashMap<BlockAbility>();

        beginObject();
        while (hasNext()) {
            try {
                var blockKey = Integer.parseInt(nextName());
                var ability = CustomItemManager.getCustomBlock(Short.parseShort(nextString()));
                trackedBlocks.put(blockKey, ability);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid number format!");
            }
        }
        endObject();

        return trackedBlocks.isEmpty() ? null : new TrackedChunk(trackedBlocks);
    }
}
