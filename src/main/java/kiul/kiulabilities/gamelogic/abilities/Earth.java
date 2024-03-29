package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Earth implements Listener {
    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();
    private final ArrayList<Player> voidthing = new ArrayList<>();
    public static ArrayList<Player> primaryTrigger = new ArrayList<>();

    String configname = AbilityItemNames.EARTH.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.EARTH.getDisplayName()));

    @EventHandler
    public void onClick(PlayerInteractEvent e) {


        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore() != null) {
                if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {
                            e.setCancelled(true);
                            primaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

                            // ABILITY CODE START
                            if (p.isOnGround()) {
                                primaryTrigger.add(p);
                                p.setVelocity(new Vector(0, 1, 0));
                                p.spawnParticle(Particle.BLOCK_DUST, p.getLocation(), 5);
                            }
                            //ABILITY CODE END

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
                            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + " Primary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.DARK_AQUA + ChatColor.ITALIC + timer + "s!");                        }
                    } else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {
                            e.setCancelled(true);

                            // ABILITY CODE START

                            //ABILITY CODE END

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
    }

    @EventHandler
    public void ultCheckActivate (PlayerSwapHandItemsEvent e) {

        Player p = (Player) e.getPlayer();
        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                e.setCancelled(true);
                if (ultimatePointsListeners.getUltPoints(p) >= ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId())) {
                    if (!ultimateCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (ultimateCooldown.get(p.getUniqueId())).longValue() > ultimateTimer * 1000)) {
                        ultimateCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        ultimatePointsListeners.useUltPoints(p, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
                            @Override
                            public void run() {
                                // ULTIMATE CODE HERE
                                Location centerLoc = p.getLocation();
                                int radius = 2;
                                int interval = 4;
                                World world = centerLoc.getWorld();
                                double x = centerLoc.getX();
                                double y = centerLoc.getY();
                                double z = centerLoc.getZ();

                                for (double theta = 0; theta <= 2 * Math.PI; theta += interval) {
                                    double dx = radius * Math.cos(theta);
                                    double dz = radius * Math.sin(theta);

                                    world.spawnFallingBlock(new Location(p.getWorld(),x+dx,y,z+dz),Material.DIRT.createBlockData());

                                }

                                radius += interval;
                                if (radius > 10) { // Adjust the maximum radius as needed
                                    cancel(); // Stop the ripple effect
                                }
                                // ULTIMATE CODE END HERE
                            }
                        }, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()) * 20);

                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String timer = df.format((double) (ultimateTimer * 1000 - (System.currentTimeMillis() - ((Long) ultimateCooldown.get(p.getUniqueId())).longValue())) / 1000);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.RED + " Ultimate ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.RED + ChatColor.ITALIC + timer + "s!");                    }
                } else {
                    ultimatePointsListeners.CheckUltPoints(p);
                }
            }
        }
    } @EventHandler
    public void noDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("rock") && primaryTrigger.contains(e.getEntity())) {
            e.setCancelled(true);
        } else {
            e.setDamage(6);
        }
    }
    @EventHandler
    public void primaryStart (EntityDamageEvent e) {
        Player p = (Player) e.getEntity();
        if (primaryTrigger.contains(p) && e.getCause() == EntityDamageEvent.DamageCause.FALL){
            e.setDamage(0);
            TNTPrimed rock = (TNTPrimed) p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
            rock.setMetadata("rock",new FixedMetadataValue(plugin,"beans"));
            rock.setYield(2);
            rock.setFuseTicks(0);


            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {

                    primaryTrigger.remove(p);
                }
            }, 20);

        }
    }
}

