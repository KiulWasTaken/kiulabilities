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
                String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(names.getLabel()));
                if (ChatColor.stripColor(e.getItemDrop().getItemStack().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                    e.setCancelled(true);
                    break;
                }
            }
        }

    }
}
