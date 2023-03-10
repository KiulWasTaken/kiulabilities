package kiul.kiulabilities.gamelogic;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ultimatePointsConfig {

        private static File file;
        private static FileConfiguration ultpointsFile;

        public static void setup() {
            file = new File(Bukkit.getServer().getPluginManager().getPlugin("Kiul-Abilities-V2").getDataFolder(), "ultpoints.yml");

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {

                }
            }
            ultpointsFile = YamlConfiguration.loadConfiguration(file);
        }

        public static FileConfiguration get() {
            return ultpointsFile;
        }



        public static void save(){
            try {
                ultpointsFile.save(file);
            } catch (IOException e) {
                System.out.println("Failed to save, ultpointsFile.");
            }
        }

        public static void reload(){
            ultpointsFile = YamlConfiguration.loadConfiguration(file);
        }


        
}
