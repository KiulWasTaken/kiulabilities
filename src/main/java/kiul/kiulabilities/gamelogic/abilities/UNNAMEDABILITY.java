package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    private int primaryTimer = 1;
    private int secondaryTimer = 1;
    private int ultimateTimer = 1;

    String itemname = AbilityItemNames.UNNAMED.getLabel();

    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);

                        /** PRIMARY - CODE START >> */

                        List<Entity> hitplayers = new ArrayList<>();
                        List<Entity> AnyPlayersCheck = new ArrayList<>();

                        Vector direction = p.getEyeLocation().getDirection().multiply(1.5);

                        AtomicInteger DashAmount = new AtomicInteger(7);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                p.setVelocity(direction);

                                Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(255, 0, 0), Color.fromRGB(255, 255, 255), 3.0F);
                                p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation().add(0,1,0), 15, 0.3, 0.5, 0.3, dustTransition);

                                for (Entity entity : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 5, 5)) {
                                    if (entity != p) {
                                        if (entity.getType() != EntityType.DROPPED_ITEM && entity.getType() != EntityType.ARMOR_STAND) {
                                            if (entity.getLocation().add(0, 1, 0).distance(p.getLocation().add(0, 1, 0)) <= 1.5) {
                                                Vector vec = entity.getLocation().add(0, 0.5, 0).toVector().subtract(p.getLocation().toVector()).normalize();
                                                entity.setVelocity(vec.multiply(1.5).add(new Vector(0, 0.05, 0)));

                                                p.setVelocity(p.getVelocity().add(direction.multiply(-0.2)));

                                                p.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2F, 0.3F);

                                                Particle.DustTransition dustTransition1 = new Particle.DustTransition(Color.fromRGB(0, 0, 0), Color.fromRGB(180, 180, 180), 10.0F);
                                                p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation().add(0,1,0), 25, 1, 1, 1, dustTransition1);

                                                hitplayers.add(entity);

                                                for (Entity entity1 : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 10, 5)) {
                                                    if (entity1 != p) {
                                                        if (entity1 != entity) {
                                                            if (entity1.getType() != EntityType.DROPPED_ITEM && entity1.getType() != EntityType.ARMOR_STAND) {
                                                                if (entity1.getLocation().add(0, 1, 0).distance(p.getLocation().add(0, 1, 0)) <= 3) {
                                                                    Vector vec1 = entity1.getLocation().add(0, 0.5, 0).toVector().subtract(p.getLocation().toVector()).normalize();
                                                                    entity1.setVelocity(vec1.multiply(1.5).add(new Vector(0, 0.2, 0)));

                                                                    p.getWorld().playSound(entity1.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4F, 0.3F);

                                                                    hitplayers.add(entity1);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                cancel();
                                                break;
                                            }
                                        }
                                    }
                                }
                                DashAmount.getAndDecrement();

                                if (DashAmount.get() <= 0) {
                                    p.setVelocity(p.getVelocity().add(direction.multiply(-0.5)));

                                    Particle.DustTransition dustTransition1 = new Particle.DustTransition(Color.fromRGB(0, 0, 0), Color.fromRGB(180, 180, 180), 10.0F);
                                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation().add(0,1,0), 25, 1, 1, 1, dustTransition1);

                                    for (Entity entity1 : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 10, 5)) {
                                        if (entity1 != p) {
                                            if (entity1.getType() != EntityType.DROPPED_ITEM && entity1.getType() != EntityType.ARMOR_STAND) {
                                                if (entity1.getLocation().add(0, 1, 0).distance(p.getLocation().add(0, 1, 0)) <= 3) {
                                                    AnyPlayersCheck.add(entity1);
                                                }
                                            }
                                        }
                                    }

                                    if (AnyPlayersCheck.size() > 0) {
                                        for (Entity entity1 : AnyPlayersCheck) {
                                            Vector vec1 = entity1.getLocation().add(0, 0.5, 0).toVector().subtract(p.getLocation().toVector()).normalize();
                                            entity1.setVelocity(vec1.multiply(1.5).add(new Vector(0, 0.2, 0)));

                                            p.getWorld().playSound(entity1.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4F, 0.3F);

                                            hitplayers.add(entity1);
                                        }
                                    } else {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,2,true,false,false));
                                    }
                                    cancel();
                                }

                            }
                            }.runTaskTimer(plugin, 0L, 0L);

                        AtomicInteger HitParticleLength = new AtomicInteger(20);

                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                for (Entity entity : hitplayers) {
                                    if (entity.getVelocity().getY() < -0.08 || entity.getVelocity().getY() > -0.06) {
                                        Particle.DustTransition dustTransition1 = new Particle.DustTransition(Color.fromRGB(200, 170, 0), Color.fromRGB(0, 0, 0), 2.0F);
                                        p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, entity.getLocation().add(0, 1, 0), 5, 0.3, 0.5, 0.3, dustTransition1);
                                    }
                                }

                                HitParticleLength.getAndDecrement();

                                if (HitParticleLength.get() <= 0) {
                                    cancel();
                                }

                            }
                        }.runTaskTimer(plugin, 0L, 0L);

                        /** CODE END << */

                        if (secondaryCooldown.isEmpty()) {
                            secondaryCooldown.put(p.getUniqueId(), (long) 0);
                        }
                        primaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

                        if (!Kiulabilities.ABILITYUSED.contains(p.getUniqueId())) {
                            Kiulabilities.ABILITYUSED.add(p.getUniqueId());
                            AbilityExtras.TimerTask(p, primaryTimer, primaryCooldown, secondaryTimer, secondaryCooldown);
                        }
                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String timer = df.format((double) (primaryTimer * 1000 - (System.currentTimeMillis() - ((Long) primaryCooldown.get(p.getUniqueId())).longValue())) / 1000);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + " Primary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.DARK_AQUA + ChatColor.ITALIC + timer + "s!");
                    }
                } else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {

                        /** SECONDARY - CODE START >> */

                        doublejump(p);

                        /** CODE END << */

                        if (primaryCooldown.isEmpty()) {
                            primaryCooldown.put(p.getUniqueId(), (long) 0);
                        }
                        secondaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
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

    @EventHandler
    public void onDeath(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();

        if (AbilityExtras.itemcheck(p, itemname) == true) {
            if (p.getGameMode() != GameMode.CREATIVE) {

                e.setCancelled(true);

                if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {

                    if (primaryCooldown.isEmpty()) {
                        primaryCooldown.put(p.getUniqueId(), (long) 0);
                    }
                    secondaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

                    if (!Kiulabilities.ABILITYUSED.contains(p.getUniqueId())) {
                        Kiulabilities.ABILITYUSED.add(p.getUniqueId());
                        AbilityExtras.TimerTask(p, primaryTimer, primaryCooldown, secondaryTimer, secondaryCooldown);
                    }

                    /** CODE >> */

                    doublejump(p);

                    /** END << */

                } else {
                    DecimalFormat df = new DecimalFormat("0.00");
                    String timer = df.format((double) (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())) / 1000);
                    p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.LIGHT_PURPLE + " Secondary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + timer + "s!");
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if (AbilityExtras.itemcheck(p, itemname) == true) {
            if (p.getGameMode() != GameMode.CREATIVE) {

                if (p.getVelocity().getY() < -0.7) {
                    p.setAllowFlight(false);
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.setAllowFlight(true);
                        }
                    }.runTaskLater(plugin, 1);
                }

            }
        }
    }

    /** PASSIVE ABILITY >> */

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        if (e.getEntity().getKiller() instanceof Player) {

            Player p = e.getEntity();
            Player killer = e.getEntity().getKiller();

            if (AbilityExtras.itemcheck(killer, itemname) == true) {
                killer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,160,1,true,false));
            }

        }
    }

    /** << */

    @EventHandler
    public void ultCheckActivate (PlayerSwapHandItemsEvent e) {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                e.setCancelled(true);
                if (ultimatePointsListeners.getUltPoints(p) >= ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId())) {
                    if (!ultimateCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (ultimateCooldown.get(p.getUniqueId())).longValue() > ultimateTimer * 1000)) {
                        ultimateCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        ultimatePointsListeners.useUltPoints(p, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {

                                /** ULTIMATE - CODE START >> */



                                /** CODE END << */

                            }
                        }, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()) * 20);

                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String timer = df.format((double) (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())) / 1000);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.RED + " Ultimate ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.RED + ChatColor.ITALIC + timer + "s!");
                    }
                } else {
                    ultimatePointsListeners.CheckUltPoints(p);
                }
            }
        }
    }

    public void doublejump (Player p) {

        if (p.getLocation().getPitch() != -90) {
            Location playerLocation = p.getLocation();
            Vector direction = playerLocation.getDirection();

            double deltaX = direction.getX() * 2;
            double deltaZ = direction.getZ() * 2;

            Location blockLocation = playerLocation.clone().add(deltaX, 0, deltaZ);
            Vector vec1 = blockLocation.toVector().subtract(p.getLocation().toVector()).normalize();

            p.setVelocity(vec1.multiply(0.5).add(new Vector(0, 0.5, 0)));
        } else {
            p.setVelocity(new Vector(0,0.8,0));
        }

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.2F, 0.5F);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.2F, 1F);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.2F, 2F);

        Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(122, 122, 122), Color.fromRGB(255, 255, 255), 3.0F);
        p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation().add(0,-0.5,0), 20, 0.7, 0, 0.7, dustTransition);


        p.setAllowFlight(false);
        p.setFlying(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                p.setAllowFlight(true);
            }
        }.runTaskLater(plugin, secondaryTimer * 20);

        AtomicInteger ParticleLength = new AtomicInteger(15);

        new BukkitRunnable() {
            @Override
            public void run() {

                p.getWorld().spawnParticle(Particle.TOTEM,p.getLocation(),1,0.5,0,0.5,0);

                ParticleLength.getAndDecrement();

                if (ParticleLength.get() <= 0) {
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 0L, 0L);

    }
}



