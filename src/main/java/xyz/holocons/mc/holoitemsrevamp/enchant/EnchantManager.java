package xyz.holocons.mc.holoitemsrevamp.enchant;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.enchantment.Magnet;
import xyz.holocons.mc.holoitemsrevamp.enchantment.Plow;
import xyz.holocons.mc.holoitemsrevamp.enchantment.TideRider;

public class EnchantManager {

    private final Set<CustomEnchantment> customEnchantments;
    private final Map<Pair<Enchantment, Integer>, Component> customEnchantmentDisplayNames;

    public EnchantManager(HoloItemsRevamp plugin) throws ReflectiveOperationException {
        try {
            final Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new ReflectiveOperationException(e);
        }
        this.customEnchantments = buildCustomEnchantments(plugin);
        customEnchantments.forEach(Enchantment::registerEnchantment);
        Enchantment.stopAcceptingRegistrations();

        // Key is a pair of Enchantment and level, value is the display name for that pair
        this.customEnchantmentDisplayNames = customEnchantments.stream()
            .<Pair<Enchantment, Integer>>mapMulti((customEnchantment, mapper) -> {
                IntStream.rangeClosed(customEnchantment.getStartLevel(), customEnchantment.getMaxLevel()).forEach(level -> {
                    mapper.accept(Pair.of(customEnchantment, level));
                });
            })
            .collect(Collectors.toMap(Function.identity(), EnchantManager::displayNameFromEnchantmentPair));

        plugin.getServer().getPluginManager().registerEvents(new EnchantListener(plugin, this), plugin);
    }

    private static Component displayNameFromEnchantmentPair(Pair<Enchantment, Integer> enchantmentPair) {
        return enchantmentPair.left().displayName(enchantmentPair.right());
    }

    /**
     * Applies the display names of all custom enchantments present on an itemstack
     * to the itemstack as lore. If any custom enchantment lore is already on the given
     * itemstack, {@code EnchantManager#removeCustomEnchantmentLore} should be
     * done first.
     * @param itemStack An ItemStack that has custom enchantments
     * @return The same ItemStack with custom enchantment lore applied
     */
    public void applyCustomEnchantmentLore(ItemStack itemStack) {
        final var enchantmentLore = itemStack.getEnchantments()
            .entrySet()
            .stream()
            .map(entry -> customEnchantmentDisplayNames.get(Pair.of(entry.getKey(), entry.getValue())))
            .filter(Objects::nonNull);
        final var oldLore = itemStack.lore();
        final var newLore = oldLore == null ? enchantmentLore.toList() : Stream.concat(enchantmentLore, oldLore.stream()).toList();
        itemStack.lore(newLore.isEmpty() ? null : newLore);
    }

    /**
     * Removes all custom enchantment lore from the given itemstack.
     * @param itemStack An ItemStack without custom enchantments
     * @return The same ItemStack with all custom enchantment lore removed
     */
    public void removeCustomEnchantmentLore(ItemStack itemStack) {
        final var oldLore = itemStack.lore();
        if (oldLore != null) {
            final var newLore = oldLore.stream()
                .filter(loreComponent -> !customEnchantmentDisplayNames.values().contains(loreComponent))
                .toList();
            itemStack.lore(newLore.isEmpty() ? null : newLore);
        }
    }

    public List<String> getCustomEnchantmentNames() {
        return customEnchantments.stream().map(CustomEnchantment::getName).toList();
    }

    /**
     * Gets a custom enchantment from the plugin by class.
     * @param enchantmentCls The class of the enchantment
     * @return Resulting CustomEnchantment, or null if not found
     */
    @Nullable
    public <E extends CustomEnchantment> E getCustomEnchantment(@NotNull Class<E> enchantmentCls) {
        return enchantmentCls.cast(customEnchantments.stream().filter(enchantmentCls::isInstance).findAny().orElse(null));
    }

    private static Set<CustomEnchantment> buildCustomEnchantments(HoloItemsRevamp plugin) {
        return Set.of(
            new Magnet(plugin),
            new TideRider(plugin),
            new Plow(plugin)
        );
    }
}
