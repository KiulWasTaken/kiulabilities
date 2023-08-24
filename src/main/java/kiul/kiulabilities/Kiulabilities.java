package kiul.kiulabilities;

import kiul.kiulabilities.CommandMethods.SupplyDrop;
import kiul.kiulabilities.gamelogic.*;
import kiul.kiulabilities.gamelogic.Commands.OperatorCommands;
import kiul.kiulabilities.gamelogic.Commands.PattyEventCommands;
import kiul.kiulabilities.gamelogic.Configs.AbilityConfig;
import kiul.kiulabilities.gamelogic.Configs.ultimatePointsConfig;
import kiul.kiulabilities.gamelogic.Events.DroppingAbilitys;
import kiul.kiulabilities.gamelogic.Events.InventoryEvent;
import kiul.kiulabilities.gamelogic.Events.JoinEvent;
import kiul.kiulabilities.gamelogic.Events.MovingAbilityItems;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import kiul.kiulabilities.gamelogic.abilities.*;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
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

        AbilityConfig.setup();
        AbilityConfig.get().options().copyDefaults(true);
        AbilityConfig.save();

        this.saveDefaultConfig();
        saveConfig();

        getCommand("test").setExecutor(new OperatorCommands());
        getCommand("giveultpoint").setExecutor(new OperatorCommands());
        getCommand("resetactionbar").setExecutor(new PattyEventCommands());
        getCommand("spawnlootcrate").setExecutor(new PattyEventCommands());
        getCommand("setconfig").setExecutor(new PattyEventCommands());
        getCommand("opengui").setExecutor(new PattyEventCommands());
        getCommand("abilitymenu").setExecutor(new PattyEventCommands());
        getServer().getPluginManager().registerEvents(new OperatorCommands(), this);
        getServer().getPluginManager().registerEvents(new ultimatePointsListeners(), this);
        getServer().getPluginManager().registerEvents(new DroppingAbilitys(), this);
        getServer().getPluginManager().registerEvents(new SupplyDrop(), this);
        getServer().getPluginManager().registerEvents(new MovingAbilityItems(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryEvent(), this);

        /** ABILITIES */
        getServer().getPluginManager().registerEvents(new Catalyst(), this);
        getServer().getPluginManager().registerEvents(new Spectre(), this);
        getServer().getPluginManager().registerEvents(new Ignition(), this);
        getServer().getPluginManager().registerEvents(new Artificer(), this);
        getServer().getPluginManager().registerEvents(new Featherweight(), this);
        getServer().getPluginManager().registerEvents(new Stealth(), this);
        getServer().getPluginManager().registerEvents(new Tracker(), this);
        getServer().getPluginManager().registerEvents(new Frozone(), this);
        getServer().getPluginManager().registerEvents(new Gecko(), this);
        getServer().getPluginManager().registerEvents(new Cello(),this);
        getServer().getPluginManager().registerEvents(new Discharge(),this);
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
