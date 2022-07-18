package com.strangeone101.holoitemsapi.enchantment.ability;

import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public interface ProjectileLaunch extends Ability {

    void run(ProjectileLaunchEvent event, ItemStack itemStack);
}
