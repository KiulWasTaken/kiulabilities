package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ColoredText;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.*;

public class Frozone implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    String configname = AbilityItemNames.FROZONE.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.FROZONE.getLabel()));

    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);

                        /** PRIMARY - CODE START >> */

                        new BukkitRunnable() {
                            Vector dir = p.getLocation().getDirection().normalize();
                            Location loc = p.getLocation().add(p.getLocation().getDirection().multiply(1));
                            double t = 0;
                            @Override
                            public void run() {
                                t += 1;
                                double x = dir.getX() * t;
                                double y = dir.getY() * t + 1.5;
                                double z = dir.getZ() * t;
                                loc.add(x,y,z);
                                p.getWorld().spawnParticle(Particle.BLOCK_DUST, loc, 5,0,0,0, 0, Material.BLUE_ICE.createBlockData());
                                Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(33, 147, 180), Color.fromRGB(255, 255, 255), 2.0F);
                                p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 2, 0, 0, 0, dustTransition);

                                for (Entity entity : loc.getChunk().getEntities()) {
                                    if (entity.getType() != EntityType.DROPPED_ITEM && entity.getType() != EntityType.ARMOR_STAND && entity != p) {
                                        if (entity.getLocation().distance(loc) < 2) {
                                            cancel();
                                            particlehit(p, loc);
                                            break;
                                        }
                                    }
                                }

                                if (t > 20 || loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.WATER && loc.getBlock().getType() != Material.LAVA) {
                                    cancel();
                                    particlehit(p, loc);
                                }

                                loc.subtract(x,y,z);

                            }
                        }.runTaskTimer(plugin, 0L, 1L);

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

                        for (int i = 0; i>=0; i++) {
                            int yaw = (int) ((p.getLocation().getYaw() - 30) + i*2);
                            secondarygetlocation(p, 10, yaw, 8);
                            if (i == 30) {
                                break;
                            }
                        }

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

    public void particlehit (Player p, Location loc) {

            p.getWorld().spawn(loc, Creeper.class, (explosive) -> {
                explosive.setMaxFuseTicks(0);
                explosive.ignite();
                explosive.setInvisible(true);
                explosive.setSilent(true);
                explosive.setMetadata("frozonecreeper", new FixedMetadataValue(plugin, "pat"));
                explosive.setExplosionRadius(6);
                explosive.setCustomName("frozonecreeper");
            });

    }

    @EventHandler
    public void fcreeperexplode (EntityExplodeEvent e) {

        if (e.getEntity() instanceof Creeper creeper) {
            if (creeper.hasMetadata("frozonecreeper")) {

                for (Block block : e.blockList()) {
                    replaceblock(block.getLocation(), block.getType(), block.getBlockData(),2);
                    block.getLocation().getBlock().setType(Material.BLUE_ICE);
                }

                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void fcreeperexplode (EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Creeper creeper) {
            if (creeper.hasMetadata("frozonecreeper")) {
                e.setCancelled(true);
                if (e.getEntity() instanceof Player p) {
                    if (p.getGameMode() == GameMode.SURVIVAL) {
                        if (AbilityExtras.itemcheck(p, itemname) == false) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 5, false, false));
                        }
                    }
                } else if (e.getEntity() instanceof LivingEntity livingEntity) {

                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 5, false, false));

                }
                if (e.getDamager().getLocation().distance(e.getEntity().getLocation()) < 4) {
                    repulseplayer(e.getDamager().getLocation(), e.getEntity(), -3, 0.8);
                }
            }
        }
    }

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

                                        for (double i = 0; i <= Math.PI; i += Math.PI / 13) {
                                            double radius = Math.sin(i);
                                            double y = Math.cos(i) * 4;
                                            for (double a = 0; a < Math.PI * 2; a += Math.PI / 13) {
                                                double x = Math.cos(a) * radius * 4;
                                                double z = Math.sin(a) * radius * 4;
                                                for (Player player : Bukkit.getOnlinePlayers()) {
                                                    if (player != p) {
                                                        Location location = player.getLocation().add(0, 1, 0);
                                                        location.add(x, y, z);
                                                        if (location.getBlock().getType() != Material.BLUE_ICE) {
                                                            replaceblock(location.getBlock().getLocation(), location.getBlock().getLocation().getBlock().getType(), location.getBlock().getBlockData(), 5);
                                                            location.getBlock().setType(Material.BLUE_ICE);
                                                        }
                                                        location.subtract(x, y, z);
                                                    }
                                                }
                                            }
                                        }

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

    @EventHandler
    public void Passiveshift (PlayerToggleSneakEvent e) {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (!p.isSneaking()) {

                        new BukkitRunnable() {
                            public void run() {

                                if (!p.hasMetadata("frozoneno")) {
                                    if (p.isSneaking() && p.getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                                        if (p.getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.AIR) {

                                            p.getLocation().clone().add(0, -1, 0).getBlock().setType(Material.PACKED_ICE);

                                            replaceblock(p.getLocation().clone().add(0, -1, 0), p.getLocation().clone().add(0,-1,0).getBlock().getType(), p.getLocation().clone().add(0,-1,0).getBlock().getBlockData(), 0);

                                        }
                                    } else {
                                        cancel();
                                    }
                                }

                            }
                        }.runTaskTimer(plugin, 0L, 1L);

                }
            }
        }
    }

    @EventHandler
    public void Passiveshift (EntityDamageEvent e) {

        if (e.getEntity() instanceof Player p) {

            p.setMetadata("frozoneno", new FixedMetadataValue(plugin, "pat"));

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {

                    p.removeMetadata("frozoneno", plugin);

                }
            }, 200);

        }

    }

    public void secondarygetlocation (Player p, int numberOfParticles, float yaw1, int distance) {

        double yawRadians = Math.toRadians(yaw1);

        double x = Math.sin(yawRadians);
        double z = -Math.cos(yawRadians);

        x *= distance;
        z *= distance;

        double newX = p.getLocation().getX() - x;
        double newY = p.getLocation().clone().add(0,1,0).getY();
        double newZ = p.getLocation().getZ() - z;

        Location newLocation = new Location(p.getLocation().getWorld(), newX, newY, newZ);

        spawnparticles(p, p.getLocation().clone().add(0,-4,0),newLocation.add(0,-3,0), numberOfParticles);

    }

    public void spawnparticles (Player p, Location startPoint, Location endPoint, int numberOfParticles) {

        double stepSize = 1.0 / (numberOfParticles - 1);

        Location endPoint1 = endPoint.clone();

        for (int i = 0; i < numberOfParticles; i++) {
            double x1 = startPoint.getX() + (endPoint1.getX() - startPoint.getX()) * (i * stepSize);
            double y = startPoint.getY() + (endPoint1.getY() - startPoint.getY()) * (i * stepSize);
            double z1 = startPoint.getZ() + (endPoint1.getZ() - startPoint.getZ()) * (i * stepSize);
            Location particleLocation = new Location(startPoint.getWorld(), x1, y, z1);

            endPoint1.add(0, i / 3, 0);

            //particleLocation.getWorld().spawnParticle(Particle.FLAME, particleLocation, 1, 0, 0, 0, 0);

            int finalI = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (finalI > 5 && finalI < numberOfParticles - 1) {
                        if (particleLocation.getBlock().getType() != Material.PACKED_ICE) {
                            replaceblock(particleLocation, particleLocation.getBlock().getType(), particleLocation.getBlock().getBlockData(), 0);
                            particleLocation.getBlock().setType(Material.PACKED_ICE);
                        }
                        if (particleLocation.clone().add(0,1,0).getBlock().getType() != Material.PACKED_ICE) {
                            replaceblock(particleLocation.clone().add(0, 1, 0), particleLocation.clone().add(0,1,0).getBlock().getType(), particleLocation.clone().add(0,1,0).getBlock().getBlockData(), 0);
                            particleLocation.clone().add(0, 1, 0).getBlock().setType(Material.PACKED_ICE);
                        }
                        if (particleLocation.clone().add(0,2,0).getBlock().getType() != Material.PACKED_ICE) {
                            replaceblock(particleLocation.clone().add(0, 2, 0), particleLocation.clone().add(0,2,0).getBlock().getType(), particleLocation.clone().add(0,2,0).getBlock().getBlockData(), 0);
                            particleLocation.clone().add(0, 2, 0).getBlock().setType(Material.PACKED_ICE);
                            p.getWorld().playSound(particleLocation, Sound.BLOCK_STONE_PLACE, 0.3F, 0.5F);
                        }
                    } else {
                        if (particleLocation.getBlock().getType() != Material.PACKED_ICE) {
                            replaceblock(particleLocation, particleLocation.getBlock().getType(), particleLocation.getBlock().getBlockData(), 0);
                            particleLocation.getBlock().setType(Material.PACKED_ICE);
                        }
                    }
                }
            }, i*2);
        }
    }

    public void replaceblock (Location blockloc, Material material, BlockData blockData, int delay) {

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                blockloc.getBlock().setType(Material.ICE);
            }
        }, delay*20 + 30);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                blockloc.getBlock().setType(Material.LIGHT_BLUE_STAINED_GLASS);
            }
        }, delay*20 + 60);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (material != null && material != Material.GLASS && material != Material.LIGHT_BLUE_STAINED_GLASS && material != Material.PACKED_ICE && material != Material.BLUE_ICE && material != Material.ICE) {
                    blockloc.getBlock().setType(material);
                    blockloc.getBlock().setBlockData(blockData);
                } else {
                    blockloc.getBlock().setType(Material.AIR);
                }
                //blockloc.getWorld().spawnParticle(Particle.CLOUD, blockloc,2, 0.5,0.5,0.5,0);
                //blockloc.getWorld().spawnParticle(Particle.BLOCK_DUST, blockloc, 5,0,0,0, 0, Material.BLUE_ICE.createBlockData());
                Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(33, 147, 180), Color.fromRGB(255, 255, 255), 2.0F);
                blockloc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, blockloc.add(0.5,0.5,0.5), 1, 0.5, 0.5, 0.5, dustTransition);
            }
        }, delay*20 + 70);

    }

    public void repulseplayer (Location startloc, Entity entity1, double Strength, double Y) {

        double deltaX = entity1.getLocation().add(0.5,0,0.5).getX() - startloc.add(0.5,0,0.5).getX();
        double deltaZ = entity1.getLocation().add(0.5,0,0.5).getZ() - startloc.add(0.5,0,0.5).getZ();

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

            Location newLocation = new Location(startloc.getWorld(), newX, newY, newZ);

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

            Location newLocation = new Location(startloc.getWorld(), newX, newY, newZ);

            Vector vec1 = newLocation.toVector().subtract(entity1.getLocation().toVector()).normalize();

            entity1.setVelocity(vec1.add(new Vector(0, Y, 0)));

        }

    }

    @EventHandler
    public void icemelt (BlockFadeEvent e) {

        if (e.getBlock().getType() == Material.ICE) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void icebroken (BlockBreakEvent e) {

        if (e.getBlock().getType() == Material.ICE) {
            if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                e.getBlock().getLocation().getBlock().setType(Material.AIR);
            }
        }

    }
}



