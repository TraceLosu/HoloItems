package com.strangeone101.holoitemsapi.tracking;

import com.strangeone101.holoitemsapi.item.BlockAbility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.block.Block;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Shoutout to Flo0 (a.k.a 7Smiley7 on Spigot) for sharing the block tracking
 * concept which initially guided the design of our own system.
 * 
 * @see <a href=
 *      "https://www.spigotmc.org/threads/tracking-blocks-that-were-placed-by-players.500216/">Resource
 *      thread</a>
 */
public class TrackingManager {

    public static final String FILENAME = "blocks.json";

    private final Logger logger;
    private final File dataFolder;
    private final Map<TrackedBlock, BlockAbility> trackedBlocks;

    public TrackingManager(HoloItemsRevamp plugin) {
        this.logger = plugin.getLogger();
        this.dataFolder = plugin.getDataFolder();
        this.trackedBlocks = new Object2ObjectOpenHashMap<>();
    }

    public void loadTrackedBlocks() {
        final var file = new File(dataFolder, FILENAME);
        if (!file.exists()) {
            return;
        }

        if (!trackedBlocks.isEmpty()) {
            throw new IllegalStateException("Block tracker already has data!");
        }

        final Map<TrackedBlock, BlockAbility> invalidBlocks = new Object2ObjectOpenHashMap<>();

        try {
            final var reader = new GsonReader(file);
            reader.readBlocks(trackedBlocks, invalidBlocks);
            reader.close();
        } catch (IOException e) {
            final var invalidBlocksFolder = new File(dataFolder, "invalid-blocks");
            invalidBlocksFolder.mkdir();

            final var invalidBlocksFile = new File(invalidBlocksFolder, Instant.now().toString() + ".json");
            file.renameTo(invalidBlocksFile);
            throw new RuntimeException(e);
        }

        if (!invalidBlocks.isEmpty()) {
            logger.warning("Invalid blocks will be discarded!");

            final var invalidBlocksFolder = new File(dataFolder, "invalid-blocks");
            invalidBlocksFolder.mkdir();

            final var invalidBlocksFile = new File(invalidBlocksFolder, Instant.now().toString() + ".json");

            try {
                final var writer = new GsonWriter(invalidBlocksFile);
                writer.writeBlocks(invalidBlocks);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveTrackedBlocks() {
        dataFolder.mkdir();

        final var file = new File(dataFolder, FILENAME);

        try {
            final var writer = new GsonWriter(file);
            writer.writeBlocks(trackedBlocks);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void track(final Block block, final BlockAbility ability) {
        trackedBlocks.put(new TrackedBlock(block), ability);
    }

    public void untrack(final Block block) {
        trackedBlocks.remove(new TrackedBlock(block));
    }

    public void move(final Block from, final Block to) {
        final var ability = getBlockAbility(from);
        untrack(from);
        track(to, ability);
    }

    public boolean isTracked(final Block block) {
        return trackedBlocks.containsKey(new TrackedBlock(block));
    }

    public BlockAbility getBlockAbility(final Block block) {
        return trackedBlocks.get(new TrackedBlock(block));
    }
}
