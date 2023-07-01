package kiul.kiulabilities;

import kiul.kiulabilities.CommandMethods.SupplyDrop;
import kiul.kiulabilities.gamelogic.*;
import kiul.kiulabilities.gamelogic.abilities.*;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public final class Kiulabilities extends JavaPlugin {

    public static List<UUID> ABILITYUSED = new ArrayList<>();

    @Override
    public void onEnable() {

        registerGlow();

        ultimatePointsConfig.setup();
        ultimatePointsConfig.save();

        this.saveDefaultConfig();
        saveConfig();

        getCommand("test").setExecutor(new Commands());
        getCommand("giveultpoint").setExecutor(new Commands());
        getCommand("resetactionbar").setExecutor(new Commands());
        getCommand("spawnlootcrate").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new Commands(), this);
        getServer().getPluginManager().registerEvents(new ultimatePointsListeners(), this);
        getServer().getPluginManager().registerEvents(new menuClickListener(), this);
        getServer().getPluginManager().registerEvents(new DroppingAbilitys(), this);
        getServer().getPluginManager().registerEvents(new SupplyDrop(), this);

        /** ABILITIES */
        getServer().getPluginManager().registerEvents(new Catalyst(), this);
        getServer().getPluginManager().registerEvents(new Discharge(),this);
        getServer().getPluginManager().registerEvents(new Spectre(), this);
        getServer().getPluginManager().registerEvents(new Ignition(), this);
        getServer().getPluginManager().registerEvents(new Artificer(), this);
        getServer().getPluginManager().registerEvents(new Featherweight(), this);
        getServer().getPluginManager().registerEvents(new Stealth(), this);
        getServer().getPluginManager().registerEvents(new Tracker(), this);
        getServer().getPluginManager().registerEvents(new Frozone(), this);
        /***/
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            GlintEnchantment glow = new GlintEnchantment(new NamespacedKey(this, "glow"));
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
