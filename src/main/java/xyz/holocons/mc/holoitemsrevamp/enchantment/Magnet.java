package xyz.holocons.mc.holoitemsrevamp.enchantment;

import com.destroystokyo.paper.MaterialTags;
import com.strangeone101.holoitemsapi.enchantment.CustomEnchantment;
import com.strangeone101.holoitemsapi.enchantment.ability.BlockBreak;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.integration.Integrations;

public class Magnet extends CustomEnchantment implements BlockBreak {

    private final HoloItemsRevamp plugin;

    public Magnet(HoloItemsRevamp plugin) {
        super(plugin, "magnet");
        this.plugin = plugin;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return MaterialTags.PICKAXES.isTagged(item) || MaterialTags.AXES.isTagged(item)
            || MaterialTags.SHOVELS.isTagged(item) || MaterialTags.HOES.isTagged(item);
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text()
            .color(NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false)
            .append(Component.text("Magnet"))
            .build();
    }

    @Override
    public int getCostMultiplier() {
        return 12;
    }

    @Override
    public void run(BlockBreakEvent event, ItemStack itemStack) {
        final var location = event.getBlock().getLocation().toCenterLocation();
        final var player = event.getPlayer();

        if (!Integrations.WORLDGUARD.canUseEnchantment(location, Magnet.class)) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                final var items = location.getNearbyEntitiesByType(Item.class, 1.5, Item::canPlayerPickup);
                final var itemStacks = items.stream().map(Item::getItemStack).toArray(ItemStack[]::new);
                final var excess = player.getInventory().addItem(itemStacks);
                items.forEach(player::playPickupItemAnimation);
                items.forEach(Item::remove);
                excess.values().forEach(itemStack -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
            }
        }.runTask(plugin);
    }
}
