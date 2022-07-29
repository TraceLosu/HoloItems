package xyz.holocons.mc.holoitemsrevamp.enchantment;

import com.strangeone101.holoitemsapi.enchantment.CustomEnchantment;
import com.strangeone101.holoitemsapi.enchantment.EnchantmentAbility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.integration.Integrations;

public class TideRider extends CustomEnchantment implements EnchantmentAbility {

    private final HoloItemsRevamp plugin;

    public TideRider(HoloItemsRevamp plugin) {
        super(plugin, "tide_rider");
        this.plugin = plugin;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return !other.equals(Enchantment.MENDING) && !other.equals(Enchantment.VANISHING_CURSE);
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return item.getType() == Material.TRIDENT;
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text("Tide Rider", NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public int getCostMultiplier() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, ItemStack itemStack) {
        final var player = event.getPlayer();
        final var world = player.getWorld();

        if (!Integrations.WORLDGUARD.canUseEnchantment(player.getLocation(), TideRider.class)) {
            return;
        }

        if (itemStack.getItemMeta() instanceof Damageable damageable) {
            final var damage = damageable.getDamage() + 1;
            if (damage >= itemStack.getType().getMaxDurability()) {
                return;
            }
            damageable.setDamage(damage);
            itemStack.setItemMeta(damageable);
        }

        player.playSound(player, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 0.3F, 0.7F);

        new BukkitRunnable() {
            int elapsedTicks = 0;

            @Override
            public void run() {
                if (!player.isValid() || !player.isHandRaised() || elapsedTicks >= 1200) {
                    cancel();
                    if (itemStack.getItemMeta() instanceof Damageable damageable) {
                        damageable.setDamage(damageable.getDamage() + 1);
                        itemStack.setItemMeta(damageable);
                    }
                }
                elapsedTicks++;

                final var location = player.getLocation();

                if (elapsedTicks % 2 != 0) {
                    world.spawnParticle(Particle.WATER_WAKE, location, 80, 0.2, 0.0, 0.2);
                }

                final var direction = location.getDirection().setY(0.0001).normalize();
                final var belowBlock = location.getBlock().getRelative(BlockFace.DOWN, 1);
                final var frontBlock = world.getBlockAt(location.add(direction));

                double multiplier = 1.0;
                if (player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                    multiplier += 0.33;
                }
                if (belowBlock.getType() == Material.SOUL_SAND) {
                    multiplier -= 0.33;
                }
                final var boots = player.getInventory().getBoots();
                if (boots != null && boots.getType() != Material.AIR) {
                    multiplier += 0.11 * boots.getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
                    if (belowBlock.getType() == Material.SOUL_SAND) {
                        multiplier += 0.33 * boots.getEnchantmentLevel(Enchantment.SOUL_SPEED);
                    }
                }

                double yVelocity;
                if (frontBlock.isPassable() && !frontBlock.isLiquid()) {
                    if (belowBlock.isPassable() && !belowBlock.isLiquid()) {
                        yVelocity = -0.5;
                    } else {
                        yVelocity = 0;
                    }
                } else if (!belowBlock.isPassable() || belowBlock.isLiquid()) {
                    yVelocity = 0.5;
                } else {
                    yVelocity = 0;
                }

                player.setVelocity(player.getVelocity().add(direction).normalize().multiply(multiplier).setY(yVelocity));
                player.setFallDistance(0);
            }
        }.runTaskTimer(plugin, 2, 1);
    }

    @Override
    public void onProjectileLaunch(ProjectileLaunchEvent event, ItemStack itemStack) {
        event.setCancelled(true);
    }
}
