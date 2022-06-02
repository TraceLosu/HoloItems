package xyz.holocons.mc.holoitemsrevamp.ability;

import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public interface PlayerToggleSneak{

    public void run(PlayerToggleSneakEvent event, ItemStack itemStack);
}

