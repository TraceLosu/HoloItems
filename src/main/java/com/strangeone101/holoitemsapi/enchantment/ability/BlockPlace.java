package com.strangeone101.holoitemsapi.enchantment.ability;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public interface BlockPlace extends Ability {

    void run(BlockPlaceEvent event, ItemStack itemStack);
}
