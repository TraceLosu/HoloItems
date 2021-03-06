package com.strangeone101.holoitemsapi.enchantment;

import com.strangeone101.holoitemsapi.item.CustomItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.packet.PlayerAbilitiesPacket;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnvilListener implements Listener {

    private static final int MAX_REPAIR_COST = Short.MAX_VALUE;

    private final HoloItemsRevamp plugin;

    public AnvilListener(HoloItemsRevamp plugin) {
        this.plugin = plugin;
    }

    /**
     * Makes sure that after a player closes an anvil inventory, their instant build
     * ability gets disabled.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory() instanceof AnvilInventory)
                || !(event.getPlayer() instanceof Player player)
                || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        new PlayerAbilitiesPacket(player, false).sendPacket(player);
    }

    /**
     * Handles anvil craftings regarding custom enchantments and custom items.
     */
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        // Make sure viewer is a player and they don't have creative bypasses
        if (!(event.getView().getPlayer() instanceof Player player) || player.getGameMode() == GameMode.CREATIVE)
            return;

        var inventory = event.getInventory();

        var base = inventory.getFirstItem();
        var addition = inventory.getSecondItem();

        if (base == null || !(base.getItemMeta() instanceof Repairable) || base.getAmount() != 1)
            return;

        // Only handle events that contain custom enchantments
        if (hasNoCustomEnchants(base) && hasNoCustomEnchants(addition))
            return;

        if (CustomItemManager.isCustomItem(base)) { // Disallow players to modify custom items
            event.setResult(null);
            plugin.getServer().getScheduler().runTask(plugin, () -> inventory.setResult(null));
            return;
        }

        inventory.setMaximumRepairCost(MAX_REPAIR_COST);

        // Name handling with base item containing custom enchantments
        if (addition == null) {
            var renameText = inventory.getRenameText();
            var displayName = base.getItemMeta().hasDisplayName()
                    ? PlainTextComponentSerializer.plainText().serialize(base.getItemMeta().displayName())
                    : "";
            if (renameText != null && !renameText.isBlank() && !displayName.equals(renameText)) {
                var result = base.clone();
                var resultMeta = result.getItemMeta();

                resultMeta.displayName(Component.text(renameText));
                result.setItemMeta(resultMeta);

                final int levelCost = inventory.getRepairCost();

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (!base.equals(inventory.getFirstItem()))
                        return;

                    inventory.setResult(result);
                    inventory.setRepairCost(levelCost);
                    player.setWindowProperty(InventoryView.Property.REPAIR_COST, levelCost);
                    new PlayerAbilitiesPacket(player, hasEnoughLevels(player, levelCost)).sendPacket(player);
                });
            }
            return;
        }

        // Enchantment handling if addition item is an enchanted book
        if (addition.getType() == Material.ENCHANTED_BOOK) {
            var customEnchants = combineCustomEnchants(base, addition);
            var result = event.getResult();
            int levelCost = inventory.getRepairCost();

            if (customEnchants.isEmpty())
                return;

            // Enchantment handling if the enchanted book contains only custom enchantments
            if (result == null) {
                result = base.clone();
                levelCost = ((Repairable) base.getItemMeta()).getRepairCost();
            }

            customEnchants.forEach(result::addEnchantment);
            plugin.getEnchantManager().removeCustomEnchantmentLore(result);
            plugin.getEnchantManager().applyCustomEnchantmentLore(result);

            final var finalLevelCost = getCustomEnchantCost(customEnchants, levelCost);
            final var finalResult = result;

            inventory.setResult(finalResult);
            inventory.setRepairCost(finalLevelCost);

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (!base.equals(inventory.getFirstItem()) || !addition.equals(inventory.getSecondItem()))
                    return;

                inventory.setResult(finalResult);
                inventory.setRepairCost(finalLevelCost);
                player.setWindowProperty(InventoryView.Property.REPAIR_COST, finalLevelCost);
                new PlayerAbilitiesPacket(player, hasEnoughLevels(player, finalLevelCost)).sendPacket(player);
            });
            return;
        }

        // Enchantment handling if the addition is not an enchanted book
        if (CustomItemManager.isCustomItem(addition)) {
            // It's a custom item, but not an enchanted book. Should not be used to
            // enchant/repair stuff
            event.setResult(null);
            plugin.getServer().getScheduler().runTask(plugin, () -> inventory.setResult(null));
            return;
        }

        var result = inventory.getResult();
        if (result == null) {
            return;
        }

        var customEnchants = combineCustomEnchants(base, addition);
        var levelCost = getCustomEnchantCost(customEnchants, inventory.getRepairCost());

        customEnchants.forEach(result::addEnchantment);
        plugin.getEnchantManager().removeCustomEnchantmentLore(result);
        plugin.getEnchantManager().applyCustomEnchantmentLore(result);

        event.setResult(result);
        inventory.setRepairCost(levelCost);

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!base.equals(inventory.getFirstItem()) || !addition.equals(inventory.getSecondItem()))
                return;

            inventory.setResult(result);
            inventory.setRepairCost(levelCost);
            player.setWindowProperty(InventoryView.Property.REPAIR_COST, levelCost);
            new PlayerAbilitiesPacket(player, hasEnoughLevels(player, levelCost)).sendPacket(player);
        });
    }

    private static boolean hasEnoughLevels(Player player, int repairCost) {
        return player.getLevel() >= repairCost;
    }

    /**
     * Combines custom enchantments from two items while handling conflicts and
     * levels.
     * 
     * @param base     The base item
     * @param addition The second item
     * @return A map containing all custom enchants
     */
    private static Map<CustomEnchantment, Integer> combineCustomEnchants(
            @NotNull ItemStack base,
            @NotNull ItemStack addition) {
        final var baseEnchantments = EnchantManager.getEnchantments(base);
        final var additionEnchantments = EnchantManager.getEnchantments(addition)
                .entrySet()
                .stream()
                .filter(entry -> hasNoConflictEnchants(baseEnchantments, entry.getKey())
                        && entry.getKey().canEnchantItem(base))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final var combinedEnchantments = Stream
                .of(baseEnchantments, additionEnchantments).<Map.Entry<CustomEnchantment, Integer>>mapMulti(
                        (enchantments, consumer) -> {
                            for (final var entry : enchantments.entrySet()) {
                                if (entry.getKey() instanceof CustomEnchantment enchantment) {
                                    consumer.accept(Map.entry(enchantment, entry.getValue()));
                                }
                            }
                        })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a.equals(b) ? a + 1 : Integer.max(a, b)));
        combinedEnchantments.replaceAll((enchantment, level) -> Integer.min(level, enchantment.getMaxLevel()));

        return combinedEnchantments;
    }

    /**
     * Get the total enchantment cost from the map of enchantments.
     * 
     * @param enchantments Enchantments to calculate level cost
     * @param initialCost  Repair cost before custom enchantments are considered
     * @return Level cost to add enchantments to an item
     */
    private static int getCustomEnchantCost(Map<CustomEnchantment, Integer> enchantments, int initialCost) {
        return enchantments.entrySet().stream()
                .reduce(initialCost, AnvilListener::levelCostAccumulator, Integer::sum);
    }

    private static int levelCostAccumulator(int partialCost, Map.Entry<CustomEnchantment, Integer> nextEnchantment) {
        return partialCost + Integer.min(nextEnchantment.getKey().getCostMultiplier(), MAX_REPAIR_COST)
                * nextEnchantment.getValue();
    }

    private static boolean hasNoConflictEnchants(Map<Enchantment, Integer> enchantments, Enchantment other) {
        return enchantments.keySet().stream().noneMatch(other::conflictsWith);
    }

    private static boolean hasNoCustomEnchants(@Nullable ItemStack itemStack) {
        return itemStack == null || !itemStack.hasItemMeta()
                || EnchantManager.getEnchantments(itemStack).keySet().stream()
                        .noneMatch(entry -> entry instanceof CustomEnchantment);
    }
}
