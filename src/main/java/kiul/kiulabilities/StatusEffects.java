package kiul.kiulabilities;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class StatusEffects implements Listener {

    public static ArrayList<Player> preventMove = new ArrayList<>();


    static void stun(Player p, int time) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, time, 1, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 1, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time, 1, true, false));
    }

    static void root(Player p, int time) {
        Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

        preventMove.add(p);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                preventMove.remove(p);
            }
        }, time * 20);
    }

    static void shield(Player p, int level, int time) {
        Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

        p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, time, level, true, false));


    }

    static void bleed(Player p, int time) {
        Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

        p.setSaturatedRegenRate(-1);
        p.setUnsaturatedRegenRate(-1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.setSaturatedRegenRate(10);
                p.setUnsaturatedRegenRate(80);
            }
        }, time * 20);
    }

    private int taskId;


    @EventHandler
    public void preventMoveWhenRooted(PlayerVelocityEvent e) {
        if (preventMove.contains(e.getPlayer())) {
            e.setCancelled(true);

        }
    }
}

