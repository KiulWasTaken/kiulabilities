package kiul.kiulabilities.gamelogic.Events;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.Configs.AbilityConfig;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Select your ability!")) {

            e.setCancelled(true);

            if (AbilityConfig.get().getBoolean(p.getUniqueId().toString()) == false && p.getGameMode() != GameMode.ADVENTURE) {
                if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {

                    for (AbilityItemNames names : AbilityItemNames.values()) {
                        String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(names.getDisplayName()));
                        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {

                            p.getInventory().addItem(e.getCurrentItem());

                            AbilityConfig.get().set(p.getUniqueId().toString(), true);

                            AbilityConfig.save();

                            p.closeInventory();

                            AbilityItemNames abilityItemNames = names;
                            ultimatePointsListeners.maximumUltPoints.put(p.getUniqueId(), abilityItemNames.getMaxPoints());
                            ultimatePointsListeners.requiredUltPoints.put(p.getUniqueId(), abilityItemNames.getRequiredPoints());

                            break;
                        }
                    }

                }
            } else {

                p.sendMessage(ColoredText.translateHexCodes("&7[&6»&7] &4You've already chosen an ability!"));

            }
        }

    }
}
