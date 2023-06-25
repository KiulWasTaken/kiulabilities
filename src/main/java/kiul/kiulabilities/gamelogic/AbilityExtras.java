package kiul.kiulabilities.gamelogic;

import kiul.kiulabilities.Kiulabilities;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class AbilityExtras {

    public static Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    public static void TimerTask (Player p, Integer primaryTimer, HashMap primaryCooldown, Integer secondaryTimer, HashMap secondaryCooldown) {

        new BukkitRunnable() {
            public void run() {

                double nnum = ((double) (primaryTimer * 1000 - (System.currentTimeMillis() - ((Long) primaryCooldown.get(p.getUniqueId())).longValue())));
                double nnum1 = 20 * (nnum / (primaryTimer * 1000));
                int nnum2 = 20 - (int) nnum1;

                String primary = AbilityExtras.progress(nnum2);

                double num = ((double) (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())));
                double num1 = 20 * (num / (secondaryTimer * 1000));
                int num2 = 20 - (int) num1;

                String secondary = AbilityExtras.progress(num2);

                int ultpoints = ultimatePointsListeners.getUltPoints(p);
                int requiredpoints = ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId());
                int maximumpoints = ultimatePointsListeners.maximumUltPoints.get(p.getUniqueId());

                String ultimate = progressultimate(ultpoints, requiredpoints, maximumpoints);

                AbilityExtras.TimerActionBar(p, primary, ultimate, secondary);

            }
        }.runTaskTimer(plugin, 0L, 3L);

    }

    public static String progress (Integer nnum2) {

        if (nnum2 >= 0 && nnum2 <= 20) {
            String secondary = ChatColor.translateAlternateColorCodes('&', "&a" + String.valueOf("|").repeat(nnum2) + "&c" + String.valueOf("|").repeat(20 - nnum2));

            return secondary;
        } else {
            String secondary = ChatColor.translateAlternateColorCodes('&', "&6" + String.valueOf("|").repeat(20));

            return secondary;
        }
    }

    public static String progressultimate (Integer ultpoints, Integer requiredpoints, Integer maximumpoints) {

        if (maximumpoints <= requiredpoints) {

            if (ultpoints < requiredpoints) {
                String ultimate = ChatColor.translateAlternateColorCodes('&', "■".repeat(ultpoints) + "□".repeat(maximumpoints - ultpoints));
                return ultimate;
            } else {
                String ultimate = ChatColor.translateAlternateColorCodes('&', "■".repeat(maximumpoints));
                return ultimate;
            }

        } else {

            if (ultpoints > requiredpoints) {
                String ultimate = ChatColor.translateAlternateColorCodes('&', "■".repeat(requiredpoints) + "&8▏&7" + "■".repeat(ultpoints - requiredpoints) + "□".repeat(maximumpoints - ultpoints));
                return ultimate;
            } else {
                String ultimate = ChatColor.translateAlternateColorCodes('&', "■".repeat(ultpoints) + "□".repeat(requiredpoints - ultpoints) + "&8&l▏&7" + "□".repeat(maximumpoints - requiredpoints));
                return ultimate;
            }

        }
    }

    public static boolean itemcheck (Player p, String itemname) {

        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null && is.hasItemMeta()) {
                if (ChatColor.stripColor(is.getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void TimerActionBar (Player p, String primary, String ultimate, String secondary) {

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                "|" + primary + "&f| &6« &8[&2&lP&8] &6<&7" + ultimate + "&6> &8[&3&lS&8] &6» &f|" + secondary + "&f|")));

    }

    /***/

    public static String displayname (String configname) {

        String name = Kiulabilities.getPlugin(Kiulabilities.class).getConfig().getString("Abilities." + configname);

        return name;
    }
}
