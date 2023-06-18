package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class Tracker implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    private int primaryTimer = 1;
    private int secondaryTimer = 1;
    private int ultimateTimer = 1;

    String itemname = AbilityItemNames.TRACKER.getLabel();

    @EventHandler
    public void onClick(PlayerInteractEvent e) {


        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore() != null) {
                if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {
                            e.setCancelled(true);

                            // ABILITY CODE START

                            Wolf roger = (Wolf) p.getWorld().spawnEntity(p.getLocation().add(0, 1, 0), EntityType.WOLF);
                            roger.setAngry(true);
                            roger.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2, false, false));
                            roger.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6);
                            roger.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(100);
                            Location user = p.getLocation();
                            ArrayList<Double> nearbyPlayers = new ArrayList<>();
                            HashMap<Double, Player> players = new HashMap<>();


                            for (Player nearby : Bukkit.getOnlinePlayers()) {
                                if (nearby != p) {
                                    nearbyPlayers.add(user.distance(nearby.getLocation()));
                                    players.put(user.distance(nearby.getLocation()), nearby);
                                }
                            }

                            Arrays.sort(nearbyPlayers.toArray());
                            roger.setTarget(players.get(nearbyPlayers.get(0)));
                            roger.setVelocity((players.get(nearbyPlayers.get(0)).getLocation().add(0, 1, 0).toVector().subtract(roger.getLocation().toVector())).normalize().multiply(0.8));
                            roger.setCustomName(p.getDisplayName() + "'s Bloodhound Roger");

                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    if (!roger.isDead()) {
                                        roger.remove();
                                    }
                                }
                            }, 200);

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

                            for (Block nearbyBlocks : getBlocks(p.getLocation().getBlock(), 2)) {
                                if (nearbyBlocks.getType() == Material.GRASS) {
                                    nearbyBlocks.setType(Material.SWEET_BERRY_BUSH);
                                    BlockState state = nearbyBlocks.getState();
                                    Ageable ageable = (Ageable) state.getBlockData();
                                    ageable.setAge(2);
                                    state.setBlockData(ageable);
                                    state.update();


                                }
                            }
                            Location center = p.getLocation().add(0, 1, 0); // replace world, x, y, z with your desired values
                            double radius = 2;

                            for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 16) {
                                double x = radius * Math.cos(theta);
                                double z = radius * Math.sin(theta);
                                Location loc = center.clone().add(x, 0, z);
                                loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 1);
                            }
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ITEM_BONE_MEAL_USE, 1, 1);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    for (Block nearbyBlocks : getBlocks(p.getLocation().getBlock(), 3)) {
                                        if (nearbyBlocks.getType() == Material.GRASS) {
                                            nearbyBlocks.setType(Material.SWEET_BERRY_BUSH);
                                            BlockState state = nearbyBlocks.getState();
                                            Ageable ageable = (Ageable) state.getBlockData();
                                            ageable.setAge(2);
                                            state.setBlockData(ageable);
                                            state.update();


                                        }
                                    }
                                    Location center = p.getLocation().add(0, 0.5, 0); // replace world, x, y, z with your desired values
                                    double radius = 3;

                                    for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 16) {
                                        double x = radius * Math.cos(theta);
                                        double z = radius * Math.sin(theta);
                                        Location loc = center.clone().add(x, 0, z);
                                        loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 1);
                                    }
                                    p.getLocation().getWorld().playSound(p.getLocation(), Sound.ITEM_BONE_MEAL_USE, 1, 1);
                                }
                            }, 10);


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

                            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.LIGHT_PURPLE + " Secondary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())) + "ms!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void ultCheckActivate(PlayerSwapHandItemsEvent e) {

        Player p = (Player) e.getPlayer();
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
                                // ULT CODE
                            }
                        }, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()) * 20);

                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String timer = df.format((double) (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())) / 1000);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.RED + " Ultimate ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.RED + ChatColor.ITALIC + timer + "s!");                    }
                } else {
                    ultimatePointsListeners.CheckUltPoints(p);
                }
            }
        }
    }

    @EventHandler
    public void dontEatIt(PlayerItemConsumeEvent e) {

        Player p = (Player) e.getPlayer();
        if (e.getItem().getType() == Material.SWEET_BERRIES) {
            e.setCancelled(true);
        }


    }

    public List<Block> getBlocks(Block start, int radius){
        if (radius < 0) {
            return new ArrayList<Block>(0);
        }
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<Block>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        return blocks;
    }

    @EventHandler
    public void rogerExplode(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Wolf) {
            Wolf roger = (Wolf) e.getDamager();
            Player p = (Player) e.getEntity();
            if (roger.hasMetadata("ROGER")) {
                p.getWorld().createExplosion(roger.getLocation(),1,false);
                roger.remove();
            }
        }

    }
}
