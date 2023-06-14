package kiul.kiulabilities;

import kiul.kiulabilities.gamelogic.DroppingAbilitys;
import kiul.kiulabilities.gamelogic.abilities.*;
import kiul.kiulabilities.gamelogic.menuClickListener;
import kiul.kiulabilities.gamelogic.ultimatePointsConfig;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public final class Kiulabilities extends JavaPlugin {

    public static List<UUID> ABILITYUSED = new ArrayList<>();

    @Override
    public void onEnable() {

        // Plugin startup logic
        ultimatePointsConfig.setup();
        ultimatePointsConfig.save();
        getCommand("test").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new Stealth(), this);
        getServer().getPluginManager().registerEvents(new Tracker(), this);
        getServer().getPluginManager().registerEvents(new Commands(), this);
        getServer().getPluginManager().registerEvents(new ultimatePointsListeners(), this);
        getServer().getPluginManager().registerEvents(new menuClickListener(), this);
        getServer().getPluginManager().registerEvents(new Artificer(), this);
        getServer().getPluginManager().registerEvents(new Featherweight(), this);
        getServer().getPluginManager().registerEvents(new DroppingAbilitys(), this);

        getServer().getPluginManager().registerEvents(new UNNAMEDABILITY(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
