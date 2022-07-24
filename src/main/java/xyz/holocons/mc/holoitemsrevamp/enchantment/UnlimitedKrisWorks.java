package xyz.holocons.mc.holoitemsrevamp.enchantment;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import com.strangeone101.holoitemsapi.enchantment.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.ability.PlayerInteract;
import xyz.holocons.mc.holoitemsrevamp.ability.ProjectileLaunch;

public class UnlimitedKrisWorks extends CustomEnchantment implements PlayerInteract{
  
  private final HoloItemsRevamp plugin;

  public UnlimitedKrisWorks(HoloItemsRevamp plugin) {
    super(plugin, "unlimited_kris_works");
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
    return item.getType() == Material.GOLDEN_SWORD;
  }

  @Override
  public @NotNull Component displayName(int level) {
    return Component.text("Unlimited Kris Works", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false);
  }

  @Override
  public int getCostMultiplier() {
    return Integer.MAX_VALUE;
  }

  @Override
  public void run(PlayerInteractEvent event, ItemStack itemStack) {

    // If not right-clicked on air, return
    if(!event.getAction().isRightClick() && event.getAction() != Action.RIGHT_CLICK_AIR) {
      return;
    }

  }
}
