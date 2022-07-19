package xyz.holocons.mc.holoitemsrevamp.item;

import com.strangeone101.holoitemsapi.block.Placeable;
import com.strangeone101.holoitemsapi.block.ability.BlockDispense;
import com.strangeone101.holoitemsapi.item.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.Util;

import java.util.List;

public class DummyBlockBlock extends CustomItem implements Placeable, BlockDispense {

    private final static String name = "dummy_block";
    private final static Material material = Material.DROPPER;
    private final static Component displayName = Component.text("Dummy Block", NamedTextColor.GOLD);
    private final static List<Component> lore = List.of(
        Component.text("A dummy custom block used for testing.", NamedTextColor.AQUA)
            .decoration(TextDecoration.ITALIC, false)
    );

    private final HoloItemsRevamp plugin;

    public DummyBlockBlock(HoloItemsRevamp plugin) {
        super(plugin, name, material, displayName, lore);
        register();

        this.plugin = plugin;
    }

    @Override
    public short getIdentifier() {
        return 0;
    }

    @Override
    public void onPlace(BlockPlaceEvent event) {
        event.getPlayer().sendMessage(Component.text("You placed a custom block!"));
    }

    @Override
    public void onBreak(BlockBreakEvent event) {
        event.getPlayer().sendMessage(Component.text("You broke a custom block!"));

        // Handle custom block drops
        new BukkitRunnable() {
            @Override
            public void run() {
                var customBlockStack = buildStack(null);
                var location = event.getBlock().getLocation();

                var items = location.getNearbyEntitiesByType(Item.class, 1.5, Item::canPlayerPickup);
                var droppedCustomBlock = items.stream()
                    .filter((item -> {
                        var drop = item.getItemStack();

                        return drop.getType() == customBlockStack.getType() && drop.hasItemMeta() &&
                            drop.getItemMeta().displayName().equals(customBlockStack.getItemMeta().displayName());
                    }))
                    .findAny();

                if (droppedCustomBlock.isPresent()) {
                    droppedCustomBlock.get().setItemStack(customBlockStack);
                } else {
                    location.getWorld().dropItemNaturally(location, customBlockStack);
                }
            }
        }.runTask(plugin);
    }

    @Override
    public void onDispense(BlockDispenseEvent event) {
        var itemStack = new ItemStack(Material.PAPER);
        var itemMeta = itemStack.getItemMeta();

        itemMeta.displayName(Component.text("Current Tick", NamedTextColor.GOLD));
        itemMeta.lore(List.of(Component.text(Util.currentTimeTicks())));

        itemStack.setItemMeta(itemMeta);

        event.setItem(itemStack);
    }
}
