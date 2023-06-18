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

        if (e.getItemDrop().getItemStack().hasItemMeta()) {
            for (AbilityItemNames names : AbilityItemNames.values()) {
                names.toString().replace("_", " ");
                if (ChatColor.stripColor(e.getItemDrop().getItemStack().getItemMeta().getDisplayName()).equalsIgnoreCase(names.toString())) {
                    e.setCancelled(true);
                    break;
                }
            }
        }

    }
}
