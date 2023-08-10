package kiul.kiulabilities.gamelogic.Methods;

import kiul.kiulabilities.Kiulabilities;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.Arrays;
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

                String primary = ColoredText.translateHexCodes(Arrays.toString(AbilityExtras.progress(nnum2)));
                primary = primary.replace("F", "");
                primary = primary.replace("[", "");
                primary = primary.replace("]", "");
                primary = primary.replace(",", "");
                primary = primary.replace(" ", "");

                double num = ((double) (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())));
                double num1 = 20 * (num / (secondaryTimer * 1000));
                int num2 = 20 - (int) num1;

                String secondary = ColoredText.translateHexCodes(Arrays.toString(AbilityExtras.progress(num2)));
                secondary = secondary.replace("F", "");
                secondary = secondary.replace("[", "");
                secondary = secondary.replace("]", "");
                secondary = secondary.replace(",", "");
                secondary = secondary.replace(" ", "");


                int ultpoints = ultimatePointsListeners.getUltPoints(p);
                int requiredpoints = 0;
                int maximumpoints = 0;
                if (ultimatePointsListeners.maximumUltPoints.containsKey(p.getUniqueId())) {
                    requiredpoints = ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId());
                    maximumpoints = ultimatePointsListeners.maximumUltPoints.get(p.getUniqueId());
                }

                String ultimate = progressultimate(ultpoints, requiredpoints, maximumpoints);

                AbilityExtras.TimerActionBar(p, primary, ultimate, secondary);

                if (p.hasMetadata("reset")) {
                    Kiulabilities.ABILITYUSED.remove(p.getUniqueId());
                    p.removeMetadata("reset", plugin);
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 0L, 3L);

    }

    public static String[] progress (Integer nnum2) {

        if (nnum2 >= 0 && nnum2 <= 20) {
            String secondary = "|".repeat(nnum2);
            String secondary1 = ColoredText.translateHexCodes("&#919191|".repeat(20 - nnum2));

            secondary = matrixFormatter(secondary);

            return new String[] {secondary ,secondary1};
        } else {
            String secondary = ColoredText.translateHexCodes("&#48ff00|&#46f600|&#43ee00|&#41e500|&#3edc00|&#3cd300|&#39cb00|&#37c200|&#35b900|&#32b000|&#32b000|&#35b900|&#37c200|&#39cb00|&#3cd300|&#3edc00|&#41e500|&#43ee00|&#46f600|&#48ff00|");

            return new String[] {secondary};
        }
    }

    public static String matrixFormatter(String x) {
        List<String> chars = Arrays.asList(x.split(""));
        String result = chars.get(0);
        for (int i = 1; i < chars.size(); i++) {
            Color color = new java.awt.Color(120 + (i * 6),120 + (i * 6),50);
            int rgba = (color.getRGB() << 8) | color.getAlpha();
            String str = String.format("&#%08X", rgba);
            result += str + chars.get(i);
        }
        return net.md_5.bungee.api.ChatColor.of(new java.awt.Color(120,120,50)) + result;
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
                String ultimate = ChatColor.translateAlternateColorCodes('&', "■".repeat(requiredpoints) + "&8▏&5" + "■".repeat(ultpoints - requiredpoints) + "□".repeat(maximumpoints - ultpoints));
                return ultimate;
            } else {
                String ultimate = ChatColor.translateAlternateColorCodes('&', "■".repeat(ultpoints) + "□".repeat(requiredpoints - ultpoints) + "&8&l▏&5" + "□".repeat(maximumpoints - requiredpoints));
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

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                "|" + primary + "&f| &6« &8[&2&lP&8] &6<&5" + ultimate + "&6> &8[&3&lS&8] &6» &f|" + secondary + "&f|")));

    }

    /***/

    public static String displayname (String configname) {

        String name = Kiulabilities.getPlugin(Kiulabilities.class).getConfig().getString("Abilities." + configname);

        return name;
    }
    public static Entity getNearestEntity(Location targetLocation) {
        double nearestDistanceSquared = Double.MAX_VALUE;
        Entity nearestEntity = null;

        for (Entity entity : targetLocation.getWorld().getEntities()) {
            if (entity.equals(targetLocation)) {
                continue; // Skip the target location itself if it's an entity
            }

            double distanceSquared = entity.getLocation().distanceSquared(targetLocation);
            if (distanceSquared < nearestDistanceSquared) {
                nearestDistanceSquared = distanceSquared;
                nearestEntity = entity;
            }
        }

        return nearestEntity;
    }
}

