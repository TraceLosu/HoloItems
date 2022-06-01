package xyz.holocons.mc.holoitemsrevamp.enchantment;

import com.destroystokyo.paper.MaterialTags;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.ability.PlayerToggleSneak;
import xyz.holocons.mc.holoitemsrevamp.enchant.CustomEnchantment;

public class Backdash extends CustomEnchantment implements PlayerToggleSneak {

    private final HoloItemsRevamp plugin;

    public Backdash(HoloItemsRevamp plugin) {
        super(plugin, "backdash");
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
        return MaterialTags.BOOTS.isTagged(item);
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text("Backdash", NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public int getItemStackCost(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void run(PlayerToggleSneakEvent event, ItemStack itemStack) {
        if (!event.isSneaking()){
            return;
        }
        final var player = event.getPlayer();
        player.setVelocity(player.getLocation().getDirection().setY(0).normalize().multiply(-1));
    }
}

