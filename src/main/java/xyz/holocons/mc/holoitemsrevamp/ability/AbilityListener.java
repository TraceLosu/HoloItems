package xyz.holocons.mc.holoitemsrevamp.ability;

import org.bukkit.Material;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityListener implements Listener {

    /**
     * Handles BlockBreak enchantments.
     *
     * @param event The BlockBreakEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final var itemStack = event.getPlayer().getInventory().getItemInMainHand();

        itemStack.getEnchantments().keySet().forEach(enchantment -> {
            if (enchantment instanceof BlockBreak ability) {
                ability.run(event, itemStack);
            }
        });
    }

    /**
     * Handles BlockPlace enchantments.
     *
     * @param event The BlockPlaceEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        final var itemStack = event.getItemInHand();

        itemStack.getEnchantments().keySet().forEach(enchantment -> {
            if (enchantment instanceof BlockPlace ability) {
                ability.run(event, itemStack);
            }
        });
    }

    /**
     * Handles PlayerDeath enchantments.
     *
     * @param event The PlayerDeathEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final var playerInventory = event.getPlayer().getInventory();

        for (final var itemStack : playerInventory) {
            itemStack.getEnchantments().keySet().forEach(enchantment -> {
                if (enchantment instanceof PlayerDeath ability) {
                    ability.run(event, itemStack);
                }
            });
        }
    }

    /**
     * Handles PlayerInteract enchantments.
     *
     * @param event The PlayerInteractEvent
     */
    @EventHandler(ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isBlockInHand() || !event.getAction().isRightClick() || !event.hasItem()) {
            return;
        }
        final var itemStack = switch (event.getHand()) {
            case HAND -> event.getPlayer().getInventory().getItemInMainHand();
            case OFF_HAND -> event.getPlayer().getInventory().getItemInOffHand();
            default -> new ItemStack(Material.AIR);
        };

        itemStack.getEnchantments().keySet().forEach(enchantment -> {
            if (enchantment instanceof PlayerInteract ability) {
                ability.run(event, itemStack);
            }
        });
    }

    /**
     * Handles ProjectileLaunch enchantments.
     *
     * @param event The ProjectileLaunchEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof ThrowableProjectile throwableProjectile)) {
            return;
        }
        final var itemStack = throwableProjectile.getItem();

        itemStack.getEnchantments().keySet().forEach(enchantment -> {
            if (enchantment instanceof ProjectileLaunch ability) {
                ability.run(event, itemStack);
            }
        });
    }
}
