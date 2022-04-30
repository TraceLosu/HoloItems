package xyz.holocons.mc.holoitemsrevamp.enchantment;

import com.destroystokyo.paper.MaterialTags;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.Util;
import xyz.holocons.mc.holoitemsrevamp.ability.BlockBreak;
import xyz.holocons.mc.holoitemsrevamp.enchant.CustomEnchantment;
import net.kyori.adventure.text.Component;

import java.util.HashMap;


public class Plow extends CustomEnchantment implements BlockBreak {

    private final HoloItemsRevamp plugin;
    private final HashMap<Player, Integer> plowTimer = new HashMap<>();

    public Plow(HoloItemsRevamp plugin){
        super(plugin, "plow");
        this.plugin = plugin;

    }

    @Override
    public int getMaxLevel(){
        return 1;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item){
        return MaterialTags.SHOVELS.isTagged(item);
    }

    @Override
    public @NotNull Component displayName(int level){
        return Component.text()
            .color(NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false)
            .append(Component.text("Plow"))
            .build();
    }

    @Override
    public int getItemStackCost(ItemStack itemStack){
        int levels = switch (Util.getOreLevel(itemStack.getType())){
            case NETHERITE_INGOT -> 9;
            case DIAMOND -> 7;
            case IRON_INGOT -> 5;
            case GOLD_INGOT -> 3;
            case OAK_PLANKS -> 1;
            default -> 0;
        };

        for (var entry: itemStack.getEnchantments().entrySet()){
            levels += switch (entry.getKey().getRarity()){
                case VERY_RARE -> 4 * entry.getValue();
                case RARE -> 3 * entry.getValue();
                case UNCOMMON -> 2 * entry.getValue();
                default -> 1 * entry.getValue();
            };
        }

        return levels;
    }

    @Override
    public final void run(BlockBreakEvent event, ItemStack itemStack){
        final var player = event.getPlayer();
        final var playerplowTimer = plowTimer.get(player);

        // If the player is not in the plowTimer map
        // and the broken block is snow, add the player to the map
        if (playerplowTimer == null){
            if (event.getBlock().getType() != Material.SNOW){
                return;
            } else {
                plowTimer.put(player, 20);
            }

        // If the player is in the plowTimer map
        // and the broken block is snow, reset duration to 20.
        // If the block is not a snow block, cancel the event.
        } else {
            if (event.getBlock().getType() == Material.SNOW){
                plowTimer.put(player, 20);
            } else {
                event.setCancelled(true);
            }
        }

        // Check every tick if the player has a value of 0 in plowTimer.
        // If so, remove the player from the map and cancel this task.
        // If not, decrease the value by 1.
        new BukkitRunnable(){
            @Override
            public void run(){
                if (plowTimer.get(player) == null){
                    cancel();
                    return;
                } else if (plowTimer.get(player) <= 0){
                    plowTimer.remove(player);
                    cancel();
                    return;
                }
                plowTimer.replace(player, plowTimer.get(player) - 1);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
