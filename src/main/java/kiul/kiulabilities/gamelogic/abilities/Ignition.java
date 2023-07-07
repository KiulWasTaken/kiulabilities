package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ColoredText;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Ignition implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    String configname = AbilityItemNames.IGNITION.name(); /** CHANGE 'ARTIFICER'*/

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.IGNITION.getLabel())); /** CHANGE 'ARTIFICER'*/

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
                            double t = Math.PI / 4;
                            Location loc = p.getLocation().add(0, -1, 0);

                            public void run() {
                                t = t + 0.1 * Math.PI;
                                for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 32) {
                                    double x = t * Math.cos(theta);
                                    double y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
                                    double z = t * Math.sin(theta);
                                    loc.add(x, y, z);
                                    p.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0, 0);
                                    if (loc.getBlock().getType() == Material.AIR) {
                                        if (loc.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                                            deletefire(p, loc.getBlock());
                                            loc.getBlock().setType(Material.FIRE);
                                        }
                                    }
                                    loc.subtract(x, y, z);

                                    theta = theta + Math.PI / 64;

                                    x = t * Math.cos(theta);
                                    y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
                                    z = t * Math.sin(theta);
                                    loc.add(x, y, z);
                                    p.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 1, 0, 0, 0, 0);
                                    if (loc.getBlock().getType() == Material.AIR) {
                                        if (loc.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                                            deletefire(p, loc.getBlock());
                                            loc.getBlock().setType(Material.FIRE);
                                        }
                                    }
                                    loc.subtract(x, y, z);
                                }
                                if (t > 10) {
                                    this.cancel();
                                }
                            }

                        }.runTaskTimer(plugin, 0, 1);

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

                                fireballrain(p);

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
    public void secondaryhitentity (EntityDamageByEntityEvent e) {

        Entity damagedentity = e.getEntity();

        if (e.getDamager() instanceof Player p) {
            if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
                if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                    if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {

                        /** SECONDARY - CODE START >> */

                        damagedentity.setFireTicks(60);

                        p.getWorld().spawn(damagedentity.getLocation().add(0.5, 1.8, 0.5), Firework.class, (firework) -> {

                            FireworkMeta fireworkMeta = firework.getFireworkMeta();
                            fireworkMeta.setPower(3);
                            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).build());
                            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.RED).trail(true).build());
                            firework.setFireworkMeta(fireworkMeta);
                            firework.detonate();
                        });

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
    public void firedamagecheck (EntityDamageEvent e) {

        if (e.getEntity() instanceof  Player p) {
            if (AbilityExtras.itemcheck(p, itemname) == true) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {

                    e.setDamage(0);

                }
            }
        }
    }

    /**
    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                    if (p.getLocation().getBlock().getType() != Material.FIRE) {

                        Location loc = p.getLocation();

                        if (loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.GRASS || loc.getBlock().getType() == Material.TALL_GRASS
                        || loc.getBlock().getType() == Material.FERN) {
                            loc.getBlock().setType(Material.FIRE);
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (loc.getBlock().getType() == Material.FIRE) {
                                    loc.getBlock().setType(Material.AIR);
                                    p.getWorld().spawnParticle(Particle.CLOUD, loc.add(0,0.8,0), 2, 0.5, 0.5,0.5, 0);
                                }
                            }
                        }.runTaskLater(plugin, 20);

                    }
                }
            }
        }
    } */

    public void deletefire (Player p, Block loc) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (loc.getLocation().getBlock().getType() == Material.FIRE) {
                    loc.breakNaturally();
                    p.getWorld().spawnParticle(Particle.CLOUD, loc.getLocation().add(0.5, 0.8, 0.5), 1, 0.3, 0.3, 0.3, 0);
                }
            }
        }.runTaskLater(plugin, 20);

    }

    public void fireballrain (Player p) {

        AtomicInteger RainLength = new AtomicInteger(150);

        new BukkitRunnable() {
            public void run() {
                Random random = new Random();

                World world = p.getWorld();

                double x = (int) world.getWorldBorder().getCenter().getX() - world.getWorldBorder().getSize() / 2 + random.nextInt((int) world.getWorldBorder().getSize());
                int y = 350;
                double z = (int) world.getWorldBorder().getCenter().getZ() - world.getWorldBorder().getSize() / 2 + random.nextInt((int) world.getWorldBorder().getSize());


                Location randomLocation = new Location(world, x, y, z);

                Fireball fireball = (Fireball) world.spawnEntity(randomLocation, EntityType.FIREBALL);

                fireball.setYield(3);
                fireball.setIsIncendiary(false);
                fireball.setVelocity(new Vector());

                Vector vec = new Vector(0, -3, 0);

                fireball.setDirection(vec);

                RainLength.getAndDecrement();

                if (RainLength.get() <= 0) {
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 0L, 1L);

    }

}



