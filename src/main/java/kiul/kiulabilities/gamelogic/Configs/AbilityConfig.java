package kiul.kiulabilities.gamelogic.Configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AbilityConfig {

        private static File file;
        private static FileConfiguration abilityFile;

        public static void setup() {
            file = new File(Bukkit.getServer().getPluginManager().getPlugin("Kiul-Abilities-V2").getDataFolder(), "abilityconfig.yml");

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {

                }
            }
            abilityFile = YamlConfiguration.loadConfiguration(file);
        }

        public static FileConfiguration get() {
            return abilityFile;
        }



        public static void save(){
            try {
                abilityFile.save(file);
            } catch (IOException e) {
                System.out.println("Failed to save, abilityFile.");
            }
        }

        public static void reload(){
            abilityFile = YamlConfiguration.loadConfiguration(file);
        }


        
}
