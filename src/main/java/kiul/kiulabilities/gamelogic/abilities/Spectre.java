package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ColoredText;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Spectre implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    String configname = AbilityItemNames.SPECTRE.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.SPECTRE.getLabel()));

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

                        AtomicInteger DashAmount = new AtomicInteger(5);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                p.setVelocity(direction);

                                Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(255 - DashAmount.get() * 20, 0, (7 - DashAmount.get()) * 30), Color.fromRGB(255, 255, 255), 1.5F);
                                p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation().add(0,1,0), 15, 0.3, 0.5, 0.3, dustTransition);

                                for (Entity entity : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 5, 5)) {
                                    if (entity != p) {
                                        if (entity instanceof Player hitplayer) {
                                            if (hitplayer.getGameMode() == GameMode.SURVIVAL) {
                                                if (entity.getLocation().add(0.5, 1, 0.5).distance(p.getLocation().add(0.5, 1, 0.5)) <= 2.5) {

                                                    p.setVelocity(p.getVelocity().add(direction.multiply(-0.13)));

                                                    Particle.DustTransition dustTransition1 = new Particle.DustTransition(Color.fromRGB(0, 0, 0), Color.fromRGB(180, 180, 180), 10.0F);
                                                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation().add(0, 1, 0), 25, 0.5, 0.7, 0.5, dustTransition1);

                                                    hitplayers.add(entity);

                                                    for (Entity entity1 : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 10, 5)) {
                                                        if (entity1 != p) {
                                                            if (entity1.getType() != EntityType.DROPPED_ITEM && entity1.getType() != EntityType.ARMOR_STAND) {
                                                                if (entity1.getLocation().add(0.5, 1, 0.5).distance(p.getLocation().add(0.5, 1, 0.5)) <= 3) {

                                                                    repulseplayer(p, entity1, -2, 1);

                                                                    p.getWorld().playSound(entity1.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4F, 0.3F);

                                                                    hitplayers.add(entity1);

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
                                }
                                DashAmount.getAndDecrement();

                                if (DashAmount.get() <= 0) {
                                    p.setVelocity(p.getVelocity().add(direction.multiply(-0.8)));

                                    Particle.DustTransition dustTransition1 = new Particle.DustTransition(Color.fromRGB(0, 0, 0), Color.fromRGB(180, 180, 180), 10.0F);
                                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation().add(0,1,0), 25, 0.5, 0.7, 0.5, dustTransition1);

                                    for (Entity entity1 : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 10, 5)) {
                                        if (entity1 != p) {
                                            if (entity1 instanceof Player hitplayer) {
                                                if (hitplayer.getGameMode() == GameMode.SURVIVAL) {
                                                    if (entity1.getLocation().add(0.5, 1, 0.5).distance(p.getLocation().add(0.5, 1, 0.5)) <= 3) {
                                                        AnyPlayersCheck.add(entity1);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (AnyPlayersCheck.size() > 0) {
                                        for (Entity entity1 : AnyPlayersCheck) {
                                            if (entity1 instanceof Player hitplayer) {
                                                if (hitplayer.getGameMode() == GameMode.SURVIVAL) {

                                                    repulseplayer(p, entity1, -2, 1);

                                                    p.getWorld().playSound(entity1.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4F, 0.3F);

                                                    hitplayers.add(entity1);

                                                }
                                            }
                                        }
                                    } else {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,140,3,true,false,false));
                                    }
                                    cancel();
                                }

                            }
                            }.runTaskTimer(plugin, 0L, 0L);

                        AtomicInteger HitParticleLength = new AtomicInteger(30);

                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                for (Entity entity : hitplayers) {
                                    if (entity.getVelocity().getY() < -0.08 || entity.getVelocity().getY() > -0.06) {
                                        Particle.DustTransition dustTransition1 = new Particle.DustTransition(Color.fromRGB((int) (230 - (30 - HitParticleLength.get()) * 7.5), (int) (220 - (30 - HitParticleLength.get()) * 3.75), (int) (0 + (30 - HitParticleLength.get()) * 7.5)), Color.fromRGB(255, 255, 255), 1.5F);
                                        p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, entity.getLocation().add(0, 1, 0), 2, 0.3, 0.5, 0.3, dustTransition1);
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

                        if (p.getAllowFlight() == true) {

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

        float flyspeed = p.getFlySpeed();

        if (AbilityExtras.itemcheck(p, itemname) == true) {
            if (p.getGameMode() == GameMode.SURVIVAL) {

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

                    p.setFlySpeed(0);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.setFlySpeed(flyspeed);
                        }
                    }.runTaskLater(plugin, secondaryTimer * 20);

                    doublejump(p);

                    /** END << */

                } else {
                    DecimalFormat df = new DecimalFormat("0.00/");
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
            if (p.getGameMode() == GameMode.SURVIVAL) {

                if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {

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

                } else {
                    p.setAllowFlight(false);
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
                killer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,100,1,true,false));
                killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,0,true,false));
                killer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100,0,true,false));

                p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, killer.getLocation().add(0, 1, 0), 20, 0.6, 0.8, 0.6, 0);

                AtomicInteger KillParticleLength = new AtomicInteger(80);

                new BukkitRunnable() {
                    @Override
                    public void run() {

                                Particle.DustTransition dustTransition1 = new Particle.DustTransition(Color.fromRGB(200, (int) (200 - (80 - KillParticleLength.get()) * 1.25), (int) (0 + (80 - KillParticleLength.get()) * 2.5)), Color.fromRGB(255, 255, 255), 1.5F);
                                p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, killer.getLocation().add(0, 1, 0), 2, 0.3, 0.5, 0.3, dustTransition1);

                        KillParticleLength.getAndDecrement();

                        if (KillParticleLength.get() <= 0) {
                            p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, killer.getLocation().add(0, 1, 0), 20, 0.6, 0.8, 0.6, 0);
                            cancel();
                        }

                    }
                }.runTaskTimer(plugin, 0L, 0L);
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

                                List<Entity> hitplayers = new ArrayList<>();

                                for (Entity entity1 : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 300, 400, 300)) {
                                    if (entity1 != p) {
                                        if (entity1 instanceof Player hitplayer) {
                                            if (hitplayer.getGameMode() == GameMode.SURVIVAL) {

                                                repulseplayer(p, entity1, -12, 2);

                                                hitplayers.add(entity1);

                                            }
                                        } else {
                                            repulseplayer(p, entity1, -12, 2);
                                        }
                                    }
                                }

                                AtomicInteger HitParticleLength = new AtomicInteger(40);

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                        for (Entity entity : hitplayers) {
                                            if (entity.getVelocity().getY() < -0.08 || entity.getVelocity().getY() > -0.06) {
                                                Particle.DustTransition dustTransition1 = new Particle.DustTransition(Color.fromRGB((int) (230 - (HitParticleLength.get()) * 5), (int) (220 - (HitParticleLength.get()) * 2.5), (int) (0 + (HitParticleLength.get()) * 5)), Color.fromRGB(255, 255, 255), 1.5F);
                                                p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, entity.getLocation().add(0, 1, 0), 2, 0.3, 0.5, 0.3, dustTransition1);
                                            }
                                        }

                                        HitParticleLength.getAndDecrement();

                                        if (HitParticleLength.get() <= 0) {
                                            cancel();
                                        }

                                    }
                                }.runTaskTimer(plugin, 0L, 0L);

                                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1000F, 1F);

                                /** CODE END << */

                            }
                        }, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()) * 20);

                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String timer = df.format((double) (ultimateTimer * 1000 - (System.currentTimeMillis() - ((Long) ultimateCooldown.get(p.getUniqueId())).longValue())) / 1000);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.RED + " Ultimate ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.RED + ChatColor.ITALIC + timer + "s!");
                    }
                } else {
                    ultimatePointsListeners.CheckUltPoints(p);
                }
            }
        }
    }

    public void doublejump (Player p) {

        double yaw = Math.toRadians(p.getLocation().getYaw());

        double xOffset = -2 * Math.sin(yaw);
        double zOffset = 2 * Math.cos(yaw);

        Vector vec = p.getLocation().add(xOffset, 0, zOffset).toVector().subtract(p.getLocation().toVector()).normalize();

        p.setVelocity(vec.multiply(0.5).add(new Vector(0,0.5,0)));

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.2F, 0.5F);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.2F, 1F);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.2F, 2F);

        Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(122, 122, 122), Color.fromRGB(255, 255, 255), 3.0F);
        p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation().add(0,-0.5,0), 20, 0.7, 0, 0.7, dustTransition);

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

    public void repulseplayer (Player p, Entity entity1, double Strength, double Y) {

        double deltaX = entity1.getLocation().add(0.5,0,0.5).getX() - p.getLocation().add(0.5,0,0.5).getX();
        double deltaZ = entity1.getLocation().add(0.5,0,0.5).getZ() - p.getLocation().add(0.5,0,0.5).getZ();

        double yaw = Math.atan2(-deltaX, deltaZ);

        float yawDegrees = (float) Math.toDegrees(yaw);
        yawDegrees = (yawDegrees < 0) ? yawDegrees + 360 : yawDegrees;

        if (yawDegrees > 180) {
            yawDegrees = 180 - yawDegrees;
            yawDegrees = -160 - yawDegrees;

            float yaw1 = yawDegrees;

            double yawRadians = Math.toRadians(yaw1);

            double x = Math.sin(yawRadians);
            double z = -Math.cos(yawRadians);

            x *= Strength;
            z *= Strength;

            double newX = entity1.getLocation().getX() + x;
            double newY = entity1.getLocation().getY();
            double newZ = entity1.getLocation().getZ() + z;

            Location newLocation = new Location(p.getLocation().getWorld(), newX, newY, newZ);

            Vector vec1 = newLocation.toVector().subtract(entity1.getLocation().toVector()).normalize();

            entity1.setVelocity(vec1.add(new Vector(0, Y, 0)));

        } else {

            float yaw1 = yawDegrees;

            double yawRadians = Math.toRadians(yaw1);

            double x = Math.sin(yawRadians);
            double z = -Math.cos(yawRadians);

            x *= Strength;
            z *= Strength;

            double newX = entity1.getLocation().getX() + x;
            double newY = entity1.getLocation().getY();
            double newZ = entity1.getLocation().getZ() + z;

            Location newLocation = new Location(p.getLocation().getWorld(), newX, newY, newZ);

            Vector vec1 = newLocation.toVector().subtract(entity1.getLocation().toVector()).normalize();

            entity1.setVelocity(vec1.add(new Vector(0, Y, 0)));

        }

    }
}



