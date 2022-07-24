package xyz.holocons.mc.holoitemsrevamp.item;

import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import com.strangeone101.holoitemsapi.enchantment.EnchantManager;
import com.strangeone101.holoitemsapi.enchantment.Enchantable;
import com.strangeone101.holoitemsapi.item.CustomItem;
import com.strangeone101.holoitemsapi.recipe.RecipeManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class UnlimitedKrisWorksItem {
  private final static String name = "unlimited_kris_works";
  private final static Material material = Material.GOLDEN_SWORD;
  private final staic Component displayName = Component.txt("Unlimited Kris Works", NamedTextColor.GOLD);
  private final static List<Component> lore = List.of(
      Component.text("Right click to throw a sword", NamedTextColor.GOLD)
  );

  private final EnchantManager enchantManager;

  public UnlimitedKrisWorksItem(HoloItemsRevamp plugin) {
    super(plugin, name, material, displayName, lore);
    this.enchantManager = plugin.getEnchantManager();
    this.register();
    this.registerRecipe();
  }

  private void registerRecipe() {
    ShapedRecipe recipe = new ShapedRecipe(getKey(), buildStack(null));
    recipe.shape(
        "A  ",
        " A ",
        "B  "
        );
    recipe.setIngredient('A', Material.NETHER_STAR);
    recipe.setIngredient('B', Material.LIGHTNING_ROD);
    RecipeManager.registerRecipe(recipe);
  }

  @Override
  public @NotNull Enchantment getEnchantment() {
    return Enchantment.getByKey(getKey());
  }

  @Override
  public ItemStack applyEnchantment(ItemStack itemStack) {
    var enchantedStack = itemstack.clone();
    var enchantedMeta = enchantedstack.hasItemMeta() ? enchantedStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(enchantedstack.getType());
    if(enchantedMeta.addEnchant(getEnchantment(), 1, false)) {
      enchantedStack.setItemMeta(enchantedMeta);
      enchantManager.removeCustomEnchantmentLore(enchantedStack);
      enchantManager.applyCustomEnchantmentLore(enchantedStack);
      return enchantedStack;
    } else {
      return null;
    }
  }
}
