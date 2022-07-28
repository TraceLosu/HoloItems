package com.strangeone101.holoitemsapi.tracking;

import com.google.gson.stream.JsonToken;
import com.strangeone101.holoitemsapi.item.BlockAbility;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;
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

    public static final String FILENAME = "worlds.json";

    private final HoloItemsRevamp plugin;
    private final Map<UUID, TrackedWorld> trackedWorldMap;

    public TrackingManager(HoloItemsRevamp plugin) {
        this.plugin = plugin;
        this.trackedWorldMap = new Object2ObjectOpenHashMap<>();
    }

    public void loadTrackedWorlds() {
        final var loadedBukkitWorlds = Bukkit.getWorlds().stream().map(World::getUID).toList();

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
                final var uidString = reader.nextName();
                final var trackedWorld = reader.nextTrackedWorld();

                UUID worldUUID;
                try {
                    worldUUID = UUID.fromString(uidString);
                } catch (IllegalArgumentException e) {
                    worldUUID = null;
                }

                if (!loadedBukkitWorlds.contains(worldUUID)) {
                    plugin.getLogger().warning("World with ID " + uidString +
                        " does not exist! Storing in invalid worlds folder!");
                    discardInvalidWorld(uidString, trackedWorld);
                    continue;
                }

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
            final var dataFolder = plugin.getDataFolder();
            if (!dataFolder.isDirectory())
                dataFolder.mkdir();

            final var file = new File(dataFolder, FILENAME);
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

    private void discardInvalidWorld(final String worldId, final TrackedWorld trackedWorld) {
        final var invalidWorldsFolder = new File(plugin.getDataFolder(), "invalid-worlds");
        if (!invalidWorldsFolder.isDirectory())
            invalidWorldsFolder.mkdir();

        try {
            final var file = new File(invalidWorldsFolder, worldId + ".json");
            final var writer = new GsonWriter(file);

            writer.beginObject();
            writer.name(worldId);
            writer.value(trackedWorld);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void track(final Block block, final BlockAbility ability) {
        var trackedWorld = getTrackedWorldOf(block);

        if (trackedWorld == null)
            trackedWorld = initWorld(block.getWorld());

        trackedWorld.add(block, ability);
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
        var ability = getCustomBlock(from);
        untrack(from);
        track(to, ability);
    }

    public boolean isTracked(final Block block) {
        var trackedWorld = getTrackedWorldOf(block);

        if (trackedWorld == null)
            return false;

        return trackedWorld.isTracked(block);
    }

    public BlockAbility getCustomBlock(final Block block) {
        var trackedWorld = getTrackedWorldOf(block);
        return trackedWorld.get(block);
    }

    private TrackedWorld initWorld(final World world) {
        final var trackedWorld = new TrackedWorld(null);
        trackedWorldMap.put(world.getUID(), trackedWorld);
        return trackedWorld;
    }

    private TrackedWorld getTrackedWorldOf(final Block block) {
        return trackedWorldMap.get(block.getWorld().getUID());
    }
}
