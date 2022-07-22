package xyz.holocons.mc.holoitemsrevamp;

import com.strangeone101.holoitemsapi.Keys;
import com.strangeone101.holoitemsapi.enchantment.AnvilListener;
import com.strangeone101.holoitemsapi.enchantment.EnchantManager;
import com.strangeone101.holoitemsapi.enchantment.EnchantmentListener;
import com.strangeone101.holoitemsapi.item.BlockListener;
import com.strangeone101.holoitemsapi.recipe.CraftListener;
import com.strangeone101.holoitemsapi.tracking.TrackingListener;
import com.strangeone101.holoitemsapi.tracking.TrackingManager;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.holocons.mc.holoitemsrevamp.collection.CollectionManager;
import xyz.holocons.mc.holoitemsrevamp.command.MainCommand;
import xyz.holocons.mc.holoitemsrevamp.integration.Integrations;

public final class HoloItemsRevamp extends JavaPlugin {

    private CollectionManager collectionManager;
    private EnchantManager enchantManager;
    private TrackingManager trackingManager;

    @Override
    public void onLoad() {
        Keys.fillKeys(this);

        this.enchantManager = new EnchantManager(this);
        this.collectionManager = new CollectionManager(this);
        this.trackingManager = new TrackingManager(this);

        Integrations.onLoad();
    }

    @Override
    public void onEnable() {
        Integrations.onEnable();

        trackingManager.loadTrackedWorlds();

        getServer().getPluginManager().registerEvents(new EnchantmentListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new TrackingListener(this), this);

        getCommand("holoitems").setExecutor(new MainCommand(this));
        getLogger().info("HoloItems-Revamped [ON]");
    }

    @Override
    public void onDisable() {
        trackingManager.saveTrackedWorlds();
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public EnchantManager getEnchantManager() {
        return enchantManager;
    }

    public TrackingManager getTrackingManager() {
        return trackingManager;
    }
}
