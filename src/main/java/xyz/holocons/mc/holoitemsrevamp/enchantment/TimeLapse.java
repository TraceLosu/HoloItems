package xyz.holocons.mc.holoitemsrevamp.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.ability.PlayerInteract;
import xyz.holocons.mc.holoitemsrevamp.enchant.CustomEnchantment;

import java.util.Map;

public class TimeLapse extends CustomEnchantment implements PlayerInteract {

    private final HoloItemsRevamp plugin;

    public TimeLapse(HoloItemsRevamp plugin) {
        super(plugin, "time_lapse");
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
        return item.getType() == Material.CLOCK;
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text("Time Lapse", NamedTextColor.GOLD)
            .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public int getItemStackCost(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void run(PlayerInteractEvent event, ItemStack itemStack) {

        // If not right-clicked or a container, return
        final var blockState = event.getClickedBlock().getState();
        if(!event.getAction().isRightClick() || !(blockState instanceof Container)){
            return;
        }

        Container container = (Container) blockState;

        Map<ItemStack, Material> metals = Map.of(
            new ItemStack(Material.COPPER_INGOT),    Material.RAW_COPPER,
            new ItemStack(Material.IRON_INGOT),      Material.RAW_IRON,
            new ItemStack(Material.GOLD_INGOT),      Material.RAW_GOLD,
            new ItemStack(Material.NETHERITE_SCRAP), Material.ANCIENT_DEBRIS,
            new ItemStack(Material.IRON_BLOCK),      Material.RAW_IRON_BLOCK,
            new ItemStack(Material.GOLD_BLOCK),      Material.RAW_GOLD_BLOCK,
            new ItemStack(Material.COPPER_BLOCK),    Material.RAW_COPPER_BLOCK
        );


        // Iterate over the container and if an item exists and its type is one of the above, replace it.
        boolean consumeItem = false;
        for (ItemStack item : container.getInventory()) {
            if (item == null) {
                continue;
            }
            for (ItemStack metal : metals.keySet()) {
                    if (item.isSimilar(metal)) {
                        item.setType(metals.get(metal));
                        consumeItem = true;
                    }
                }

        }

        // Deletes item if it was used.
        if(consumeItem){
            event.getItem().setAmount(0);
        }


    }
}

