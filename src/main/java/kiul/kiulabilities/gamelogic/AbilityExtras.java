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

                String str1 = AbilityExtras.progress(nnum2);

                double num = ((double) (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())));
                double num1 = 20 * (num / (secondaryTimer * 1000));
                int num2 = 20 - (int) num1;

                String str2 = AbilityExtras.progress(num2);

                AbilityExtras.TimerActionBar(p, str1, str2);

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

    public static void TimerActionBar (Player p, String primary, String secondary) {

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                "|" + primary + "&f| &6« &8[&2&lP&8] &6- &8[&3&lS&8] &6» &f|" + secondary + "&f|")));

    }

}
