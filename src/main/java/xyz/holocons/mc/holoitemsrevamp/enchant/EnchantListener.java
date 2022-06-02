package xyz.holocons.mc.holoitemsrevamp.enchant;

import com.strangeone101.holoitemsapi.CustomItemRegistry;
import com.strangeone101.holoitemsapi.interfaces.Enchantable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.ability.BlockBreak;
import xyz.holocons.mc.holoitemsrevamp.ability.PlayerInteract;
import xyz.holocons.mc.holoitemsrevamp.ability.PlayerToggleSneak;
import xyz.holocons.mc.holoitemsrevamp.ability.ProjectileLaunch;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnchantListener implements Listener {

    private final HoloItemsRevamp plugin;
    private final EnchantManager enchantManager;

    public EnchantListener(HoloItemsRevamp plugin, EnchantManager enchantManager) {
        this.plugin = plugin;
        this.enchantManager = enchantManager;
    }

    /**
     * Handles BlockBreak enchantments.
     *
     * @param event The BlockBreakEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final var itemStack = event.getPlayer().getInventory().getItemInMainHand();

        itemStack.getEnchantments().keySet().forEach(enchantment -> {
            if (enchantment instanceof BlockBreak ability) {
                ability.run(event, itemStack);
            }
        });
    }

    /**
     * Handles PlayerInteract enchantments.
     *
     * @param event The PlayerInteractEvent
     */
    @EventHandler(ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isBlockInHand() || !event.getAction().isRightClick() || !event.hasItem()) {
            return;
        }
        final var itemStack = switch (event.getHand()) {
            case HAND -> event.getPlayer().getInventory().getItemInMainHand();
            case OFF_HAND -> event.getPlayer().getInventory().getItemInOffHand();
            default -> new ItemStack(Material.AIR);
        };

        itemStack.getEnchantments().keySet().forEach(enchantment -> {
            if (enchantment instanceof PlayerInteract ability) {
                ability.run(event, itemStack);
            }
        });
    }

    /**
     * Handles ProjectileLaunch enchantments.
     *
     * @param event The ProjectileLaunchEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof ThrowableProjectile throwableProjectile)) {
            return;
        }
        final var itemStack = throwableProjectile.getItem();

        itemStack.getEnchantments().keySet().forEach(enchantment -> {
            if (enchantment instanceof ProjectileLaunch ability) {
                ability.run(event, itemStack);
            }
        });
    }

    /**
     * Handles anvil craftings regarding custom enchantments and custom items.
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPrepareAnvil(PrepareAnvilEvent event) {

        var player = event.getView().getPlayer();

        // Avoid creative bypasses
        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        var base = event.getInventory().getFirstItem();

        if (base == null)
            return;

        var baseMeta = base.hasItemMeta() ? base.getItemMeta() : Bukkit.getItemFactory().getItemMeta(base.getType());
        var addition = event.getInventory().getSecondItem();

        // Only handle events that contain custom enchantments
        if (hasNoCustomEnchants(base) && hasNoCustomEnchants(addition))
            return;

        if (addition == null) {
            var renameText = event.getInventory().getRenameText();
            var displayName = baseMeta.hasDisplayName() ?
                PlainTextComponentSerializer.plainText().serialize(baseMeta.displayName()) : "";
            if (renameText != null && !renameText.equals("")
                && !displayName.equals(renameText)) { // If the player just wants to rename.
                var result = base.clone();
                var resultMeta = baseMeta.clone();

                resultMeta.displayName(Component.text(renameText));
                result.setItemMeta(resultMeta);

                final int levelCost = event.getInventory().getRepairCost();
                final var finalBase = base.clone();

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (!finalBase.equals(event.getInventory().getFirstItem()))
                        return;

                    event.getInventory().setResult(result);
                    event.getInventory().setRepairCost(levelCost);
                    player.setWindowProperty(InventoryView.Property.REPAIR_COST, levelCost);
                });
            }
            return;
        }

        // Only handle recipes that would work in vanilla
        if ((event.getInventory().getResult() == null || event.getInventory().getResult().getType() == Material.AIR)) {
            if (CustomItemRegistry.isCustomItem(addition)) { // Unless that second item is a custom item
                var customItem = CustomItemRegistry.getCustomItem(addition);
                if (customItem instanceof Enchantable enchantable) { // And it must implement the enchantable interface.
                    if (!enchantable.getEnchantment().canEnchantItem(base))
                        return;
                    if (!hasNoConflictEnchants(base.getEnchantments(), enchantable.getEnchantment()))
                        return;
                    var resultItem = enchantable.applyEnchantment(base);

                    if (resultItem == null) // Applying enchantment failed
                        return;

                    var resultItemMeta = resultItem.getItemMeta();
                    int levelCost = ((CustomEnchantment) enchantable.getEnchantment()).getItemStackCost(resultItem);

                    var renameText = event.getInventory().getRenameText();
                    if (renameText != null && !renameText.equals("")) { // The player is trying to rename
                        ++levelCost;
                        resultItemMeta.displayName(Component.text(renameText));
                        resultItem.setItemMeta(resultItemMeta);
                    }

                    event.setResult(resultItem);

                    final int finalLevelCost = capLevelCost(levelCost);
                    final var finalBase = base.clone();
                    final var finalAddition = addition.clone();

                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        if (!finalBase.equals(event.getInventory().getFirstItem()) || !finalAddition.equals(event.getInventory().getSecondItem()))
                            return;

                        event.getInventory().setRepairCost(finalLevelCost);
                        event.getInventory().setResult(resultItem);
                        player.setWindowProperty(InventoryView.Property.REPAIR_COST, finalLevelCost);
                    });
                }
            }
            return;
        }

        var result = event.getInventory().getResult();
        var customEnchants = combineCustomEnchants(base, addition);
        var levelCost = customEnchants.entrySet().stream().reduce(event.getInventory().getRepairCost(),
            (num, entry) -> num + ((CustomEnchantment) entry.getKey()).getItemStackCost(result), Integer::sum);
        result.addEnchantments(customEnchants);

        event.setResult(result);

        final var finalBase = base.clone();
        final var finalAddition = addition.clone();
        final var finalLevelCost = capLevelCost(levelCost);
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!finalBase.equals(event.getInventory().getFirstItem()) || !finalAddition.equals(event.getInventory().getSecondItem()))
                return;

            event.getInventory().setResult(result);
            event.getInventory().setRepairCost(finalLevelCost);
            player.setWindowProperty(InventoryView.Property.REPAIR_COST, finalLevelCost);

        });
    }

    /**
     * Handles PlayerToggleSneak enchantments.
     * Only does armor slots for now.
     *
     * @param event The PlayerToggleSneakEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        final var player = event.getPlayer();
        final var inventory = player.getInventory();

        for (int i = 0; i < inventory.getArmorContents().length; i++) {
            ItemStack item =  player.getInventory().getArmorContents()[i];

            if (item == null){
                return;
            }

            item.getEnchantments().keySet().forEach(enchantment -> {
                if (enchantment instanceof PlayerToggleSneak ability) {
                    ability.run(event, item);
                }
            });
        }


    }



    /**
     * Combines custom enchantments from two items while handling conflicts and levels.
     * @param base The base item
     * @param addition The second item
     * @return A map containing all custom enchants
     */
    private static Map<Enchantment, Integer> combineCustomEnchants(@NotNull ItemStack base, @NotNull ItemStack addition) {
        var additionMeta = addition.getItemMeta();

        var baseEnchantments = base.getEnchantments();

        if (additionMeta instanceof EnchantmentStorageMeta) {
            var additionEnchants = ((EnchantmentStorageMeta) additionMeta).getStoredEnchants().entrySet().stream()
                .filter(entry -> hasNoConflictEnchants(baseEnchantments, entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return combineCustomEnchants(base.getEnchantments(), additionEnchants);
        }

        var additionEnchants = addition.getEnchantments().entrySet().stream()
            .filter(entry -> hasNoConflictEnchants(baseEnchantments, entry.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return combineCustomEnchants(baseEnchantments, additionEnchants);
    }

    private static Map<Enchantment, Integer> combineCustomEnchants(Map<Enchantment, Integer> base, Map<Enchantment, Integer> addition) {
        var combined = Stream.of(base, addition)
            .map(Map::entrySet)
            .flatMap(Set::stream)
            .filter(entry -> entry.getKey() instanceof CustomEnchantment)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) ->
                a.equals(b) ? a + 1 : Integer.max(a, b)
            ));
        var maxLevels = combined.keySet()
            .stream()
            .collect(Collectors.toMap(Function.identity(), Enchantment::getMaxLevel));
        return Stream.of(combined, maxLevels)
            .map(Map::entrySet)
            .flatMap(Set::stream)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) ->
                Integer.min(a, b)
            ));
    }

    private static boolean hasNoConflictEnchants(Map<Enchantment, Integer> enchants, Enchantment filter) {
        return enchants.keySet().stream()
            .noneMatch(filter::conflictsWith);
    }

    private static boolean hasNoCustomEnchants(@Nullable ItemStack itemStack) {
        return itemStack == null || !itemStack.hasItemMeta()
            || itemStack.getEnchantments().keySet().stream()
                .noneMatch(CustomEnchantment.class::isInstance);
    }

    /**
     * method to cap level cost. Any level cost above 39 in anvils results in "Too Expensive" to be shown.
     * @param level The current level cost
     * @return A level lower than 40
     */
    private static int capLevelCost(int level) {
        return Integer.min(level, 39);
    }
}
