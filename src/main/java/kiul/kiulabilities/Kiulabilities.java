package kiul.kiulabilities;

import kiul.kiulabilities.gamelogic.*;
import kiul.kiulabilities.gamelogic.abilities.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public final class Kiulabilities extends JavaPlugin {

    public static List<UUID> ABILITYUSED = new ArrayList<>();

    @Override
    public void onEnable() {

        ultimatePointsConfig.setup();
        ultimatePointsConfig.save();

        this.saveDefaultConfig();

        getCommand("test").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new Stealth(), this);
        getServer().getPluginManager().registerEvents(new Tracker(), this);
        getServer().getPluginManager().registerEvents(new Commands(), this);
        getServer().getPluginManager().registerEvents(new ultimatePointsListeners(), this);
        getServer().getPluginManager().registerEvents(new menuClickListener(), this);
        getServer().getPluginManager().registerEvents(new Artificer(), this);
        getServer().getPluginManager().registerEvents(new Featherweight(), this);
        getServer().getPluginManager().registerEvents(new DroppingAbilitys(), this);
        getServer().getPluginManager().registerEvents(new Catalyst(), this);
        getServer().getPluginManager().registerEvents(new Discharge(),this);
        getServer().getPluginManager().registerEvents(new UNNAMEDABILITY(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
