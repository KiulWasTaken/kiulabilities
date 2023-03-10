package kiul.kiulabilities.gamelogic;

import kiul.kiulabilities.Kiulabilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class menuClickListener implements Listener {
    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    @EventHandler
    public void menuClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ArrayList<String> lore = new ArrayList<>();
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Select an ability!")) {
            if (e.getCurrentItem().getType() == Material.PRISMARINE_CRYSTALS) {
                ultimatePointsListeners.maximumUltPoints.put(p.getUniqueId(),4);
                ultimatePointsListeners.requiredUltPoints.put(p.getUniqueId(),4);
                ItemStack stealthTrigger = new ItemStack(Material.PRISMARINE_CRYSTALS);
                ItemMeta stealthTriggerMeta = stealthTrigger.getItemMeta();
                lore.add(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Become intangible and invisible for a short time");
                lore.add(ChatColor.WHITE + "Left-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Shortsight players in a radius directly in front of you for a short time");
                stealthTriggerMeta.setLore(lore);
                stealthTriggerMeta.setDisplayName(ChatColor.WHITE + "Stealth Ability Item");
                p.setMetadata("stealth", new FixedMetadataValue(plugin, "pat"));
                stealthTrigger.setItemMeta(stealthTriggerMeta);
                p.getInventory().addItem(stealthTrigger);
            }
        }
    }
}
