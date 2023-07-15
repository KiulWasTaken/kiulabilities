package kiul.kiulabilities.gamelogic.Methods;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.Configs.ultimatePointsConfig;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ultimatePointsListeners implements Listener {


    public static HashMap<UUID,Integer> requiredUltPoints = new HashMap<>();
    public static HashMap<UUID,Integer> maximumUltPoints = new HashMap<>();
    public static void addUltPoint(Player p) {

        int ultPoints = ultimatePointsConfig.get().getInt(p.getUniqueId().toString());
        if (ultPoints < 6) {
            ultimatePointsConfig.get().set(p.getUniqueId().toString(), ultimatePointsConfig.get().getInt(p.getUniqueId().toString()) + 1);
            p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 0.5f);
            ultimatePointsConfig.save();
        }
    }

    public static void useUltPoints (Player p, final int amount) {
        ultimatePointsConfig.save();
        new BukkitRunnable() {
            int repeat = amount;
            int ultPoints = (Integer) ultimatePointsConfig.get().get(p.getUniqueId().toString());
            @Override
            public void run() {
                if (repeat >= 1) {
                    ultimatePointsConfig.get().set(p.getUniqueId().toString(), ultPoints - 1);
                    ultPoints --;
                    ultimatePointsConfig.save();
                    CheckUltPoints(p);
                    p.playSound(p.getLocation(), Sound.ITEM_HONEY_BOTTLE_DRINK,1,2f);
                    repeat--;
                } else {
                    ultimatePointsConfig.save();
                    CheckUltPoints(p);
                    cancel();
                }
            }
        }.runTaskTimer(Kiulabilities.getPlugin(Kiulabilities.class), 0L, 20L);

    }
    public static void CheckUltPoints (Player p) {
        if (ultimatePointsConfig.get().get(p.getUniqueId().toString()) == null) {
            ultimatePointsConfig.get().set(p.getUniqueId().toString(), 0);
        }
        ultimatePointsConfig.save();
    }
    public static int getUltPoints (Player p) {
        ultimatePointsConfig.save();
        if (ultimatePointsConfig.get().get(p.getUniqueId().toString()) == null) {
            ultimatePointsConfig.get().set(p.getUniqueId().toString(), 0);
        }
        return (int) ultimatePointsConfig.get().get(p.getUniqueId().toString()); }

    @EventHandler
    public void onKill (PlayerDeathEvent e) {

        if (e.getEntity().getKiller() instanceof Player) {
            Player p = e.getEntity().getKiller();
            Player killed = e.getEntity();
            if (maximumUltPoints.containsKey(p.getUniqueId()) && requiredUltPoints.containsKey(p.getUniqueId())) {
                if (killed != null) {
                    if (getUltPoints(p) < maximumUltPoints.get(p.getUniqueId())) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Kiulabilities.getPlugin(Kiulabilities.class), new Runnable() {
                            @Override
                            public void run() {
                                addUltPoint(p);
                            }
                        }, 10);
                    }
                }
            }
        }
    }
}
