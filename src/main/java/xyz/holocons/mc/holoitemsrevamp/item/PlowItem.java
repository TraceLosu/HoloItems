package xyz.holocons.mc.holoitemsrevamp.item;

import com.strangeone101.holoitemsapi.CustomItem;
import com.strangeone101.holoitemsapi.interfaces.Enchantable;
import com.strangeone101.holoitemsapi.recipe.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.enchant.EnchantManager;

import java.util.List;

public class PlowItem extends CustomItem implements Enchantable {
    private final static String name = "plow";
    private final static Material material = Material.IRON_SHOVEL;
    private final static String displayName = ChatColor.WHITE + "Plow";
    private final static List<String> lore = List.of(
        "Shovel snow"
    );

    private final EnchantManager enchantManager;

    public PlowItem(HoloItemsRevamp plugin){
        super(name, material, displayName, lore);
        this.enchantManager = plugin.getEnchantManager();
        this.register();
        this.registerRecipe();
    }

    @Override
    public ItemStack buildStack(Player player) {
        return applyEnchantment(super.buildStack(player));
    }

    private void registerRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getKey(), buildStack(null));
        recipe.shape(
            "ABA",
            "ACA",
            "ACA"
        );
        recipe.setIngredient('A', Material.TINTED_GLASS);
        recipe.setIngredient('B', Material.OBSIDIAN);
        recipe.setIngredient('C', Material.STICK);
        RecipeManager.registerRecipe(recipe);
    }

    @Override
    public Enchantment getEnchantment() {
        return Enchantment.getByKey(getKey());
    }

    @Override
    public ItemStack applyEnchantment(ItemStack itemStack) {
        var enchantedStack = itemStack.clone();
        var enchantedMeta = enchantedStack.hasItemMeta() ? enchantedStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(enchantedStack.getType());

        if (enchantedMeta.addEnchant(getEnchantment(), 1, false)) {
            enchantedStack.setItemMeta(enchantedMeta);
            enchantManager.removeCustomEnchantmentLore(enchantedStack);
            enchantManager.applyCustomEnchantmentLore(enchantedStack);
            return enchantedStack;
        } else {
            return null;
        }
    }
}
