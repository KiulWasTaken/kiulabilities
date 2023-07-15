package kiul.kiulabilities.gamelogic.Events;

import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MovingAbilityItems implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (p.getGameMode() != GameMode.CREATIVE) {
            ItemStack currentItem = e.getClick() == ClickType.NUMBER_KEY ?
                    e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) : e.getCurrentItem();

            if (!e.getView().getTitle().equalsIgnoreCase("crafting")) {
                if (currentItem != null && currentItem.hasItemMeta()) {
                    for (AbilityItemNames names : AbilityItemNames.values()) {
                        String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(names.getDisplayName()));
                        if (ChatColor.stripColor(currentItem.getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                            e.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        }

    }
}
