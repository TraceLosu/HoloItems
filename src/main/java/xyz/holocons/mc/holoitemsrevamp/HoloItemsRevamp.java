package xyz.holocons.mc.holoitemsrevamp;

import org.bukkit.plugin.java.JavaPlugin;

import com.strangeone101.holoitemsapi.Keys;
import com.strangeone101.holoitemsapi.enchantment.AnvilListener;
import com.strangeone101.holoitemsapi.enchantment.EnchantManager;
import com.strangeone101.holoitemsapi.recipe.CraftListener;

import xyz.holocons.mc.holoitemsrevamp.ability.AbilityListener;
import xyz.holocons.mc.holoitemsrevamp.collection.CollectionManager;
import xyz.holocons.mc.holoitemsrevamp.command.MainCommand;
import xyz.holocons.mc.holoitemsrevamp.integration.Integrations;

public final class HoloItemsRevamp extends JavaPlugin {

    private CollectionManager collectionManager;
    private EnchantManager enchantManager;

    @Override
    public void onLoad() {
        Keys.fillKeys(this);

        this.enchantManager = new EnchantManager(this);
        this.collectionManager = new CollectionManager(this);

        Integrations.onLoad();
    }

    @Override
    public void onEnable() {
        Integrations.onEnable();

        getServer().getPluginManager().registerEvents(new AbilityListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);

        getCommand("holoitems").setExecutor(new MainCommand(this));
        getLogger().info("HoloItems-Revamped [ON]");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public EnchantManager getEnchantManager() {
        return enchantManager;
    }
}
