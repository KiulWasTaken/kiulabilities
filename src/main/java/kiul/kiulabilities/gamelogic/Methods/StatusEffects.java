package kiul.kiulabilities.gamelogic.Methods;

import kiul.kiulabilities.Kiulabilities;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class StatusEffects implements Listener {

    public static ArrayList<Player> preventMove = new ArrayList<>();


    public static void stun(Player p, int ticks) {
        Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, ticks, 1, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ticks, 1, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, ticks, 1, true, false));


        new BukkitRunnable() {
            int i = ticks;
            public void run() {

                if (i > 0) {
                    i--;
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation().add(0, 0.5, 0), 5, new Particle.DustOptions(Color.YELLOW, 1));

                } else {
                    cancel();
                }


            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public static void root(Player p, int ticks) {
        Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

        preventMove.add(p);

        new BukkitRunnable() {
            int i = ticks;
            public void run() {

                if (i > 0) {
                    i--;
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation().add(0, 0.5, 0), 5, new Particle.DustOptions(Color.PURPLE, 1));

                } else {
                    cancel();
                }


            }
        }.runTaskTimer(plugin, 0L, 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                preventMove.remove(p);
            }
        }, ticks * 20);
    }

    public static void shield(Player p, int level, int ticks) {
        Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
        new BukkitRunnable() {
            int i = ticks;
            public void run() {

                if (i > 0) {
                    i--;
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation().add(0, 0.5, 0), 5, new Particle.DustOptions(Color.TEAL, 1));

                } else {
                    cancel();
                }


            }
        }.runTaskTimer(plugin, 0L, 1L);

        p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, ticks, level, true, false));


    }

    public static void bleed(Player p, int ticks) {
        Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

        p.setSaturatedRegenRate(1000);
        p.setUnsaturatedRegenRate(1000);

        new BukkitRunnable() {
            int i = ticks;
            public void run() {

                if (i > 0) {
                    i--;
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation().add(0, 0.5, 0), 5, new Particle.DustOptions(Color.RED, 1));

                } else {
                    cancel();
                }


            }
        }.runTaskTimer(plugin, 0L, 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.setSaturatedRegenRate(10);
                p.setUnsaturatedRegenRate(80);
            }
        }, ticks);
    }


    @EventHandler
    public void preventMoveWhenRooted(PlayerMoveEvent e) {
        if (preventMove.contains(e.getPlayer())) {
            if (e.getPlayer().isOnGround()) {
                e.setCancelled(true);
                Location to = e.getFrom();
                to.setPitch(e.getTo().getPitch());
                to.setYaw(e.getTo().getYaw());
                e.setTo(to);
            }
        }
    }
}

