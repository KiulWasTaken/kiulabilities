package kiul.kiulabilities.gamelogic.Commands;

import kiul.kiulabilities.CommandMethods.SupplyDrop;
import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.Configs.AbilityConfig;
import kiul.kiulabilities.gamelogic.Configs.ultimatePointsConfig;
import kiul.kiulabilities.gamelogic.Methods.AbilityMenu;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PattyEventCommands implements TabExecutor, Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (senderCheck(sender) == true) {

            if (label.equalsIgnoreCase("spawnlootcrate")) { /** /spawnlootcrate */
                int repeat = 1;
                int arg = 0;
                if (args.length == 1) {
                    try {
                        if (Integer.parseInt(args[0]) > 0) {
                            arg = Integer.parseInt(args[0]);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
                repeat = repeat + arg;
                for (Player allplayers : Bukkit.getOnlinePlayers()) {
                    SupplyDrop.SpawnLootCrate(allplayers, repeat);
                    break;
                }
            } else if (label.equalsIgnoreCase("opengui")) { /** /opengui */
                if (args.length == 1) {

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

                    if (offlinePlayer.getPlayer() != null) {

                        AbilityMenu.menuInventory(offlinePlayer.getPlayer());

                    }

                }
            } else if (label.equalsIgnoreCase("setconfig")) {
                if (args.length == 2) {

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

                    if (offlinePlayer.getPlayer() != null) {
                        switch (args[1]) {
                            case "false":
                                AbilityConfig.get().set(offlinePlayer.getUniqueId().toString(), false);
                                break;
                            case "true":
                                AbilityConfig.get().set(offlinePlayer.getUniqueId().toString(), true);
                                break;
                        }
                        AbilityConfig.save();
                    }

                }
            } else if (label.equalsIgnoreCase("resetactionbar")) { /** /resetactionbar */
                if (args.length == 1) {

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

                    if (offlinePlayer.getPlayer() != null) {
                        offlinePlayer.getPlayer().setMetadata("reset", new FixedMetadataValue(plugin, "pat"));

                        ultimatePointsConfig.get().set(offlinePlayer.getPlayer().getUniqueId().toString(), 0);
                    }

                }
            }
        }
        if (sender instanceof Player p) {
            if (label.equalsIgnoreCase("abilitymenu")) { /** /abilitymenu */

                AbilityMenu.menuInventory(p);

            }
        }
        return false;
    }

    public static Boolean senderCheck (CommandSender sender) {

        if (sender instanceof ConsoleCommandSender) {
            return true;
        } else if (sender instanceof Player p) {
            if (p.isOp()) {
                return true;
            } else {
                p.sendMessage("fail, not an operator!");
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (command.getName().equals("setconfig")) {
            if (args.length == 2) {
                List<String> arguments = new ArrayList<>();

                arguments.add("true");
                arguments.add("false");

                return arguments;
            }
        }
        return null;
    }
}
