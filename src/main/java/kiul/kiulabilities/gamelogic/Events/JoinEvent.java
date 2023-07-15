package kiul.kiulabilities.gamelogic.Events;

import kiul.kiulabilities.gamelogic.Configs.AbilityConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onClick(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        AbilityConfig.get().addDefault(p.getUniqueId().toString(), true);

        AbilityConfig.save();

    }
}
