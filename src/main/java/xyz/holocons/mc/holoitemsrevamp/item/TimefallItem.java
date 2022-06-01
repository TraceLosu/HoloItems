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
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.enchant.EnchantManager;

import java.util.List;

public class TimefallItem extends CustomItem implements Enchantable {

    private final static String name = "timefall";
    private final static Material material = Material.POWDER_SNOW_BUCKET;
    private final static String displayName = ChatColor.BLUE + "Timefall";
    private final static List<String> lore = List.of(
        "Accelerate oxidation"
    );

    private final EnchantManager enchantManager;

    public TimefallItem(HoloItemsRevamp plugin) {
        super(name, material, displayName, lore);
        this.enchantManager = plugin.getEnchantManager();
        this.setStackable(false);
        this.register();
        this.registerRecipe();
    }

    @Override
    public ItemStack buildStack(Player player) {
        return applyEnchantment(super.buildStack(player));
    }

    private void registerRecipe() {
        final var recipe = new ShapedRecipe(getKey(), buildStack(null));
        recipe.shape(
            "ABA",
            "BCB",
            "ABA"
        );
        recipe.setIngredient('A', Material.SNOW);
        recipe.setIngredient('B', Material.BONE_BLOCK);
        recipe.setIngredient('C', Material.BUCKET);
        RecipeManager.registerRecipe(recipe);
    }

    @Override
    public @NotNull Enchantment getEnchantment() {
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
