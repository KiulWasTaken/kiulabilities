package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class UNNAMEDABILITY implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private int primaryTimer = 1;
    private int secondaryTimer = 1;

    String itemname = AbilityItemNames.UNNAMEDABILITY;

    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);

                        if (secondaryCooldown.isEmpty()) {
                            secondaryCooldown.put(p.getUniqueId(), (long) 0);
                        }
                        primaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

                        if (!Kiulabilities.ABILITYUSED.contains(p.getUniqueId())) {
                            Kiulabilities.ABILITYUSED.add(p.getUniqueId());
                            AbilityExtras.TimerTask(p, primaryTimer, primaryCooldown, secondaryTimer, secondaryCooldown);
                        }

                        /** PRIMARY - CODE START >> */

                        Vector direction = p.getEyeLocation().getDirection().multiply(1.5);

                        AtomicInteger DashAmount = new AtomicInteger(7);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                p.setVelocity(direction);

                                for (Entity entity : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 5, 5)) {
                                    if (entity != p) {
                                        if (entity.getType() != EntityType.DROPPED_ITEM) {
                                            if (entity.getLocation().add(0, 1, 0).distance(p.getLocation().add(0, 1, 0)) <= 1.5) {
                                                Vector vec = entity.getLocation().add(0, 0.5, 0).toVector().subtract(p.getLocation().toVector()).normalize();
                                                entity.setVelocity(vec.multiply(1.5).add(new Vector(0, 0.05, 0)));
                                                p.sendMessage(entity.getLocation().add(0, 1, 0).distance(p.getLocation().add(0, 1, 0)) + "");
                                                p.setVelocity(new Vector(0, 0, 0));

                                                for (Entity entity1 : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 10, 5)) {
                                                    if (entity1 != p) {
                                                        if (entity1 != entity) {
                                                            if (entity1.getType() != EntityType.DROPPED_ITEM) {
                                                                if (entity1.getLocation().add(0, 1, 0).distance(p.getLocation().add(0, 1, 0)) <= 3) {
                                                                    Vector vec1 = entity1.getLocation().add(0, 0.5, 0).toVector().subtract(p.getLocation().toVector()).normalize();
                                                                    entity1.setVelocity(vec1.multiply(1.5).add(new Vector(0, 0.2, 0)));
                                                                    p.sendMessage(ChatColor.GRAY + "" + entity.getLocation().add(0, 1, 0).distance(p.getLocation().add(0, 1, 0)) + "");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                cancel();
                                            }
                                        }
                                    }
                                }
                                DashAmount.getAndDecrement();

                                if (DashAmount.get() <= 0) {
                                    cancel();
                                }

                            }
                            }.runTaskTimer(plugin, 0L, 0L);

                        /** CODE END << */

                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String timer = df.format((double) (primaryTimer * 1000 - (System.currentTimeMillis() - ((Long) primaryCooldown.get(p.getUniqueId())).longValue())) / 1000);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + " Primary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.DARK_AQUA + ChatColor.ITALIC + timer + "s!");
                    }
                } else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {

                        if (primaryCooldown.isEmpty()) {
                            primaryCooldown.put(p.getUniqueId(), (long) 0);
                        }
                        secondaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

                        /** SECONDARY - CODE START >> */



                        /** CODE END << */

                        if (!Kiulabilities.ABILITYUSED.contains(p.getUniqueId())) {
                            Kiulabilities.ABILITYUSED.add(p.getUniqueId());
                            AbilityExtras.TimerTask(p, primaryTimer, primaryCooldown, secondaryTimer, secondaryCooldown);
                        }

                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String timer = df.format((double) (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())) / 1000);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.LIGHT_PURPLE + " Secondary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + timer + "s!");
                    }
                }
            }
        }
    }
}



