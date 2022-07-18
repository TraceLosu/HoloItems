package com.strangeone101.holoitemsapi.tracking;

import com.google.gson.stream.JsonWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GsonWriter extends JsonWriter {

    public GsonWriter(final File file) throws IOException {
        super(new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8)));
    }

    public void value(TrackedWorld value) throws IOException {
        if (value == null) {
            nullValue();
            return;
        }

        beginObject();
        for (var entry : value.getChunkMap().long2ObjectEntrySet()) {
            name(String.valueOf(entry.getLongKey()));
            value(entry.getValue());
        }
        endObject();
    }

    public void value(TrackedChunk value) throws IOException {
        if (value == null) {
            nullValue();
            return;
        }

        beginObject();
        for (var entry : value.getTrackedBlocks().int2ShortEntrySet()) {
            name(String.valueOf(entry.getIntKey()));
            value(entry.getShortValue());
        }
        endObject();
    }
}
