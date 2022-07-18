package com.strangeone101.holoitemsapi.enchantment.ability;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface PlayerInteract extends Ability {

    void run(PlayerInteractEvent event, ItemStack itemStack);
}
