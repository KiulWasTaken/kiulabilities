package kiul.kiulabilities;

import kiul.kiulabilities.gamelogic.abilities.Stealth;
import kiul.kiulabilities.gamelogic.abilities.Tracker;
import kiul.kiulabilities.gamelogic.menuClickListener;
import kiul.kiulabilities.gamelogic.ultimatePointsConfig;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.plugin.java.JavaPlugin;


public final class Kiulabilities extends JavaPlugin {

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
