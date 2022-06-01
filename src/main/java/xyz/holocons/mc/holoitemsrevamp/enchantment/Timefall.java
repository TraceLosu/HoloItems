package xyz.holocons.mc.holoitemsrevamp.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.ability.PlayerInteract;
import xyz.holocons.mc.holoitemsrevamp.enchant.CustomEnchantment;

import java.util.HashMap;
import java.util.Map;

public class Timefall extends CustomEnchantment implements PlayerInteract {

    private final HoloItemsRevamp plugin;

    public Timefall(HoloItemsRevamp plugin) {
        super(plugin, "timefall");
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
        return item.getType() == Material.POWDER_SNOW_BUCKET;
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text("Timefall", NamedTextColor.GOLD)
            .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public int getItemStackCost(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void run(PlayerInteractEvent event, ItemStack itemStack) {

        // If not right-clicked or a container, return
        if(!event.getAction().isRightClick() || event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }

        final Map<Material, Material> age = new HashMap<>() {
        };
        age.put(Material.COPPER_BLOCK,                Material.EXPOSED_COPPER);
        age.put(Material.CUT_COPPER,                  Material.EXPOSED_CUT_COPPER);
        age.put(Material.CUT_COPPER_SLAB,             Material.EXPOSED_CUT_COPPER_SLAB);
        age.put(Material.CUT_COPPER_STAIRS,           Material.EXPOSED_CUT_COPPER_STAIRS);
        age.put(Material.EXPOSED_COPPER,              Material.WEATHERED_COPPER);
        age.put(Material.EXPOSED_CUT_COPPER,          Material.WEATHERED_CUT_COPPER);
        age.put(Material.EXPOSED_CUT_COPPER_SLAB,     Material.WEATHERED_CUT_COPPER_SLAB);
        age.put(Material.EXPOSED_CUT_COPPER_STAIRS,   Material.WEATHERED_CUT_COPPER_STAIRS);
        age.put(Material.WEATHERED_COPPER,            Material.OXIDIZED_COPPER);
        age.put(Material.WEATHERED_CUT_COPPER,        Material.OXIDIZED_CUT_COPPER);
        age.put(Material.WEATHERED_CUT_COPPER_SLAB,   Material.OXIDIZED_CUT_COPPER_SLAB);
        age.put(Material.WEATHERED_CUT_COPPER_STAIRS, Material.OXIDIZED_CUT_COPPER_STAIRS);


        ItemStack item = event.getItem();
        item.setType(Material.BUCKET);
        item.setItemMeta(new ItemStack(Material.BUCKET).getItemMeta());

        Player player = event.getPlayer();
        player.setFreezeTicks(60);
        Location center = player.getLocation();
        player.getWorld().spawnParticle(Particle.WHITE_ASH, center, 200, 5, 5, 5);

        boolean consumeItem = false;
        for (int x=-5; x<=5; x++) {
            for (int y=-5; y<=5; y++) {
                for(int z=-5; z<=5; z++) {
                    Block block = center.clone().add(x, y, z).getBlock();
                    Material type = age.get(block.getType());
                    if(type!=null)
                        block.setType(type);
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

