package kiul.kiulabilities.gamelogic;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.List;

public class DroppingAbilitys implements Listener {

    @EventHandler
    public void onClick(PlayerDropItemEvent e) {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            for (String names : AbilityItemNames.abilitys) {
                if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(names)) {
                    e.setCancelled(true);
                    break;
                }
            }
        }

    }
}
