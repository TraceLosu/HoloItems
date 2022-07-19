package com.strangeone101.holoitemsapi.tracking;

import com.google.gson.stream.JsonToken;
import com.strangeone101.holoitemsapi.block.Placeable;
import com.strangeone101.holoitemsapi.item.CustomItemManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Big shoutout to Flo0 (a.k.a 7Smiley7 on Spigot) for giving permission on copying the concept of tracking blocks.
 * @see <a href="https://www.spigotmc.org/threads/tracking-blocks-that-were-placed-by-players.500216/">Resource thread</a>
 */
public class TrackingManager {

    public static final String FILENAME = "tracker.json";

    private final Map<UUID, TrackedWorld> trackedWorldMap;
    private final HoloItemsRevamp plugin;

    public TrackingManager(HoloItemsRevamp plugin) {
        this.trackedWorldMap = new Object2ObjectOpenHashMap<>();
        this.plugin = plugin;
    }

    public void loadTrackedWorlds() {
        try {
            final var file = new File(plugin.getDataFolder(), FILENAME);

            if (!file.exists())
                return;

            if (!trackedWorldMap.isEmpty())
                throw new IllegalStateException("Tracker already has data!");

            final var reader = new GsonReader(file);

            if (reader.peek() == JsonToken.NULL) {
                reader.close();
                return;
            }

            reader.beginObject();
            while (reader.hasNext()) {
                var uidString = reader.nextName();
                UUID worldUUID;
                try {
                    worldUUID = UUID.fromString(uidString);
                    if (Bukkit.getWorlds().stream().map(World::getUID).noneMatch((uniqueId) -> uniqueId.equals(worldUUID))) {
                        plugin.getLogger().warning("World with UUID " + worldUUID + " does not exist! " +
                            "Storing in invalid folder and discarding!");
                        var trackedWorld = reader.nextTrackedWorld();
                        saveInvalidWorlds(worldUUID, trackedWorld);
                        continue;
                    }
                } catch (IllegalArgumentException e) {
                    reader.close();
                    throw new IOException("Unrecognized UUID!");
                }

                var trackedWorld = reader.nextTrackedWorld();

                trackedWorldMap.put(worldUUID, trackedWorld);
            }
            reader.endObject();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveTrackedWorlds() {
        try {
            final var file = new File(plugin.getDataFolder(), FILENAME);
            final var writer = new GsonWriter(file);

            if (trackedWorldMap.isEmpty()) {
                writer.nullValue();
                writer.close();
                return;
            }

            writer.beginObject();
            for (var entry : trackedWorldMap.entrySet()) {
                writer.name(entry.getKey().toString());
                writer.value(entry.getValue());
            }
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveInvalidWorlds(UUID worldId, TrackedWorld trackedWorld) {
        final var invalidFolder = new File(plugin.getDataFolder(), "invalid-worlds");
        if (!invalidFolder.isDirectory())
            invalidFolder.mkdir();

        try {
            final var file = new File(invalidFolder, worldId.toString() + ".json");
            final var writer = new GsonWriter(file);

            writer.beginObject();
            writer.name(worldId.toString());
            writer.value(trackedWorld);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void track(final Block block, final short identifier) {
        var trackedWorld = getTrackedWorldOf(block);

        if (trackedWorld == null)
            trackedWorld = initWorld(block.getWorld());

        trackedWorld.add(block, identifier);
    }

    public void untrack(final Block block) {
        var trackedWorld = getTrackedWorldOf(block);
        if (trackedWorld == null)
            return;

        trackedWorld.remove(block);

        if (trackedWorld.isEmpty())
            trackedWorldMap.remove(block.getWorld().getUID());
    }

    public void move(final Block from, final Block to) {
        var identifier = getIdentifier(from);
        untrack(from);
        track(to, identifier);
    }

    public boolean isTracked(final Block block) {
        var trackedWorld = getTrackedWorldOf(block);

        if (trackedWorld == null)
            return false;

        return trackedWorld.isTracked(block);
    }

    public short getIdentifier(final Block block) {
        var trackedWorld = getTrackedWorldOf(block);
        return trackedWorld.get(block);
    }

    public Placeable getCustomBlock(final Block block) {
        return CustomItemManager.getCustomBlock(getIdentifier(block));
    }

    private TrackedWorld initWorld(final World world) {
        final var trackedWorld = new TrackedWorld(null);
        trackedWorldMap.put(world.getUID(), trackedWorld);
        return trackedWorld;
    }

    private TrackedWorld getTrackedWorldOf(final Block block) {
        return trackedWorldMap.get(block.getWorld().getUID());
    }

    private TrackedWorld getTrackedWorldOf(final Chunk chunk) {
        return trackedWorldMap.get(chunk.getWorld().getUID());
    }
}
