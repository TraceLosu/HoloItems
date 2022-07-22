package com.strangeone101.holoitemsapi.item;

import org.bukkit.inventory.ItemStack;

import com.strangeone101.holoitemsapi.Keys;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry for managing all custom items
 */
public class CustomItemManager {

    private static int NEXT_ID = 2300;
    private static final int INVALID_ID = 404;

    private static final Map<String, CustomItem> CUSTOM_ITEMS = new HashMap<>();
    private static final Map<Short, BlockAbility> CUSTOM_BLOCKS = new HashMap<>();

    /**
     * Register a custom item
     * @param item The item
     */
    public static void register(CustomItem item) {
        CUSTOM_ITEMS.put(item.getInternalName(), item);

        if (item.getCustomModelID() == 0) {
            item.setCustomModelID(NEXT_ID);

            NEXT_ID++;
            if (NEXT_ID == INVALID_ID) NEXT_ID++;
        }

        if (item instanceof BlockAbility ability) {
            if (CUSTOM_BLOCKS.containsKey(ability.getIdentifier()))
                throw new IllegalArgumentException("Identifier " + ability.getIdentifier() + " has already been registered!");
            CUSTOM_BLOCKS.put(ability.getIdentifier(), ability);
        }
    }

    /**
     * Get a custom item from the item identifier
     * @param id The item identifier
     * @return The custom item
     */
    public static CustomItem getCustomItem(String id) {
        return CUSTOM_ITEMS.get(id);
    }

    /**
     * Is this a custom item?
     * @param stack The itemstack
     * @return True if it's a custom item
     */
    public static boolean isCustomItem(ItemStack stack) {
        return stack != null &&
            stack.hasItemMeta() &&
            Keys.ITEM_ID.has(stack.getItemMeta().getPersistentDataContainer());
    }

    /**
     * Get the CustomItem from the provided stack
     * @param stack The itemstack
     * @return The custom item
     */
    public static CustomItem getCustomItem(ItemStack stack) {
        if (isCustomItem(stack)) {
            String id = Keys.ITEM_ID.get(stack.getItemMeta().getPersistentDataContainer());
            return getCustomItem(id);
        }
        return null;
    }

    public static BlockAbility getCustomBlock(short identifier) {
        return CUSTOM_BLOCKS.get(identifier);
    }

    /**
     * Get all custom items
     * @return The many many items
     */
    public static Map<String, CustomItem> getCustomItems() {
        return CUSTOM_ITEMS;
    }

}
