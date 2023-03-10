package kiul.kiulabilities;

import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class Commands implements CommandExecutor, Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("test")) {
            ArrayList<String> lore = new ArrayList<>();
            switch (args[0]) {
                case "stealth":
                    ultimatePointsListeners.maximumUltPoints.put(p.getUniqueId(),6);
                    ultimatePointsListeners.requiredUltPoints.put(p.getUniqueId(),2);
                    ItemStack stealthTrigger = new ItemStack(Material.PRISMARINE_CRYSTALS);
                    ItemMeta stealthTriggerMeta = stealthTrigger.getItemMeta();
                    lore.add(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Become intangible and invisible for a short time");
                    lore.add(ChatColor.WHITE + "Left-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Shortsight players in a radius directly in front of you for a short time");
                    stealthTriggerMeta.setLore(lore);
                    stealthTriggerMeta.setDisplayName(ChatColor.WHITE + "Stealth Ability Item");
                    p.setMetadata("stealth", new FixedMetadataValue(plugin, "pat"));
                    stealthTrigger.setItemMeta(stealthTriggerMeta);
                    p.getInventory().addItem(stealthTrigger);
                break;
                case "tracker":
                    ultimatePointsListeners.maximumUltPoints.put(p.getUniqueId(),3);
                    ultimatePointsListeners.requiredUltPoints.put(p.getUniqueId(),3);
                    ItemStack trackerTrigger = new ItemStack(Material.SWEET_BERRIES);
                    ItemMeta trackerTriggerMeta = trackerTrigger.getItemMeta();
                    lore.add(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Summon a wolf that hunts down and stuns the nearest player");
                    lore.add(ChatColor.WHITE + "Left-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Grow a defensive cluster of sweet berry bushes in nearby grass");
                    trackerTriggerMeta.setLore(lore);
                    trackerTriggerMeta.setDisplayName(ChatColor.WHITE + "Tracker Ability Item");
                    p.setMetadata("tracker", new FixedMetadataValue(plugin, "pat"));
                    trackerTrigger.setItemMeta(trackerTriggerMeta);
                    p.getInventory().addItem(trackerTrigger);
                    break;
            }
        }
    return false;}
}
