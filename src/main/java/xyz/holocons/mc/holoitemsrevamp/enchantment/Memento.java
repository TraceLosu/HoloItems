package xyz.holocons.mc.holoitemsrevamp.enchantment;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.strangeone101.holoitemsapi.enchantment.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import com.strangeone101.holoitemsapi.enchantment.ability.BlockPlace;
import com.strangeone101.holoitemsapi.enchantment.ability.PlayerDeath;
import xyz.holocons.mc.holoitemsrevamp.integration.Integrations;

public class Memento extends CustomEnchantment implements PlayerDeath, BlockPlace {

    public Memento(HoloItemsRevamp plugin) {
        super(plugin, "memento");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return true;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return item.getType() == Material.ENDER_CHEST;
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text()
            .color(NamedTextColor.DARK_PURPLE)
            .decoration(TextDecoration.ITALIC, false)
            .append(Component.text("Memento"))
            .build();
    }

    @Override
    public int getCostMultiplier() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void run(PlayerDeathEvent event, ItemStack itemStack) {
        final var location = event.getPlayer().getLocation();

        // Don't do anything if keepInv is already on for this event
        if (event.getKeepInventory() || !Integrations.WORLDGUARD.canUseEnchantment(location, Memento.class)) {
            return;
        }

        // Set keepInv and keepExp on for this event
        event.setKeepInventory(true);
        event.setKeepLevel(true);

        // Clear corpse items to prevent dupes
        event.setShouldDropExperience(false);
        event.getDrops().clear();

        // Remove a Memento
        itemStack.subtract();
    }

    @Override
    public void run(BlockPlaceEvent event, ItemStack itemStack) {
        event.setCancelled(true);
    }
}
