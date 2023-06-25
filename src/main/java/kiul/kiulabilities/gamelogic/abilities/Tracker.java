package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ColoredText;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class Tracker implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

     static boolean ultimateEnabled = false;
    static boolean toggle = false;

    String configname = AbilityItemNames.TRACKER.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.TRACKER.getLabel()));

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
                            roger.setOwner(p);
                            roger.setAngry(true);
                            roger.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2, false, false));
                            roger.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(4);
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

                            if (players.size() > 0) {
                                Arrays.sort(nearbyPlayers.toArray());
                                roger.setTarget(players.get(nearbyPlayers.get(0)));
                                roger.setVelocity((players.get(nearbyPlayers.get(0)).getLocation().add(0, 1, 0).toVector().subtract(roger.getLocation().toVector())).normalize().multiply(0.8));
                                roger.setCustomName(p.getDisplayName() + "'s Bloodhound");
                                roger.setMetadata("roger", new FixedMetadataValue(plugin, "pat"));
                            }

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
                                // ULT CODE START
                                new BukkitRunnable() {
                                    public void run() {
                                            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {

                                                if (onlinePlayers != p) {

                                                    Vector direction = onlinePlayers.getEyeLocation().toVector().subtract(p.getEyeLocation().toVector()).normalize();
                                                    double distance = 1.5; // Adjust as needed
                                                    Location particleLocation = p.getEyeLocation().add(direction.multiply(distance));

                                                    int distanceBetween = (int) p.getEyeLocation().distance(onlinePlayers.getEyeLocation());
                                                    if (distanceBetween / 10 >= 1) {
                                                        int scaledValue = 255 / (distanceBetween / 10);
                                                        p.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 3, new Particle.DustOptions(Color.fromRGB(scaledValue, 0, 0), 1));
                                                    } else {
                                                        int scaledValue = 255;
                                                        p.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 3, new Particle.DustOptions(Color.fromRGB(scaledValue, 0, 0), 1));
                                                    }
                                                }
                                            }
                                    }
                                }.runTaskTimer(plugin, 0L, 5L);
                                //fuck you intellij




                                // ULT CODE END
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
            if (roger.hasMetadata("roger")) {
                p.getWorld().createExplosion(roger.getLocation(),1,false);
                roger.remove();
            }
        }

    }

    @EventHandler
    public void combatPassive (PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();

        if (killer.hasMetadata("tracker")) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 0));
        }
    }







    @EventHandler
    public void berriesDontHurt (EntityDamageByBlockEvent e) {
        if (e.getEntity().hasMetadata("tracker") && e.getDamager().getType() == Material.SWEET_BERRY_BUSH) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void healPassive (PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        ArrayList<Player> preventInfiniteRepeatingTask = new ArrayList<>();
        if (p.getInventory().getItemInMainHand() !=null && p.getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
            if (!preventInfiniteRepeatingTask.contains(e.getPlayer())) {
                preventInfiniteRepeatingTask.add(e.getPlayer());
                new BukkitRunnable() {
                    public void run() {
                        if (p.isSneaking()) {
                            for (Entity entity : p.getNearbyEntities(2, 2, 2)) {
                                if (entity instanceof Player) {
                                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15, 1));
                                }
                            }
                            Location playerLocation = p.getLocation();
                            double radius = 2; // Radius of the cylinder
                            int height = 2; // Height of the cylinder (1.5 blocks = 3 halves)
                            int density = 10; // Density of particles (adjust as needed)

                            for (int yOffset = 0; yOffset < height; yOffset++) {
                                double y = playerLocation.getY() + yOffset + 0.5; // Add 0.5 to center the cylinder

                                for (int i = 0; i < 360; i += density) {
                                    double angle = Math.toRadians(i);
                                    double x = playerLocation.getX() + radius * Math.cos(angle);
                                    double z = playerLocation.getZ() + radius * Math.sin(angle);

                                    Location particleLocation = new Location(playerLocation.getWorld(), x, y, z);
                                    playerLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1,new Particle.DustOptions(Color.LIME,1));
                                }
                            }
                        } else {
                            preventInfiniteRepeatingTask.remove(e.getPlayer());
                            cancel();
                        }

                    }
                }.runTaskTimer(plugin, 0L, 5L);
            }
        }
    }
}

