package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.Methods.StatusEffects;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Cello implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    private final HashMap<Player, Player> bestFriend = new HashMap<>();
    static int mode = 0;

    String configname = AbilityItemNames.CELLO.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.CELLO.getDisplayName()));

    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);

                        /** PRIMARY - CODE START >> */
                            if (getNearestEntityInSight(p, 10) instanceof Player bff) {
                                bestFriend.put(p, bff);
                                Location headLocation = bff.getLocation().clone().add(0, bff.getHeight(), 0);
                                bff.getWorld().spawnParticle(Particle.NOTE, headLocation, 5);
                        } else if (p.getNearbyEntities(10, 10, 10).contains(bestFriend.get(p)) || bestFriend.get(p) != null) {
                                StatusEffects.shield(bestFriend.get(p), 1, 9999999);
                            } else {
                                createExpandingParticleCircle(p, p.getLocation(), Particle.NOTE, 50, 3.5, 100);
                                for (Entity nearby : p.getNearbyEntities(7, 7, 7)) {
                                    if (nearby instanceof Player nearbyPlayer) {
                                        (nearbyPlayer).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0, true, false));
                                    }
                                }
                            }
                        }

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

                        Location infront = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(1));
                        if (mode == 0) { //heal
                            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(infront, EntityType.ARMOR_STAND);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    stand.remove();
                                }
                            }, 50);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (!stand.isDead()) {
                                        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, stand.getLocation(), 5, 0, 0, 0, 1, Material.LIME_WOOL.createBlockData());
                                    } else {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 1L);
                            stand.setInvisible(true);
                            stand.setInvulnerable(true);
                            stand.setSmall(true);
                            moveArmorStand(stand,p);
                        }
                        if (mode == 1) { //fling
                            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(infront, EntityType.ARMOR_STAND);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    stand.remove();
                                }
                            }, 50);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (!stand.isDead()) {
                                        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, stand.getLocation(), 5, 0, 0, 0, 1, Material.GRAY_WOOL.createBlockData());
                                    } else {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 1L);
                            stand.setInvisible(true);
                            stand.setInvulnerable(true);
                            stand.setSmall(true);
                            moveArmorStand(stand, p);
                        }
                        if (mode == 2) { //stun
                            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(infront, EntityType.ARMOR_STAND);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    stand.remove();
                                }
                            }, 50);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (!stand.isDead()) {
                                        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, stand.getLocation(), 5, 0, 0, 0, 1, Material.YELLOW_WOOL.createBlockData());
                                    } else {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 1L);
                            stand.setInvisible(true);
                            stand.setInvulnerable(true);
                            stand.setSmall(true);
                            moveArmorStand(stand, p);
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

    @EventHandler
    public void ultCheckActivate(PlayerSwapHandItemsEvent e) {

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
                        String timer = df.format((double) (ultimateTimer * 1000 - (System.currentTimeMillis() - ((Long) ultimateCooldown.get(p.getUniqueId())).longValue())) / 1000);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.RED + " Ultimate ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.RED + ChatColor.ITALIC + timer + "s!");
                    }
                } else {
                    ultimatePointsListeners.CheckUltPoints(p);
                }
            }
        }
    }

    public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight((Set<Material>) null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (int i = 0; i < sightBlock.size(); i++)
            sight.add(sightBlock.get(i).getLocation());
        for (int i = 0; i < sight.size(); i++) {
            for (int k = 0; k < entities.size(); k++) {
                if (Math.abs(entities.get(k).getLocation().getX() - sight.get(i).getX()) < 1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY() - sight.get(i).getY()) < 1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ() - sight.get(i).getZ()) < 1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null; //Return null/nothing if no entity was found
    }


    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();

        if (event.isSneaking() && AbilityExtras.itemcheck(p,itemname)) {
            cycleMode();
            String modeName = getModeName(mode);
            p.sendTitle("", "Mode: " + modeName);
        }
    }

    private void cycleMode() {
        mode++;
        if (mode > 2) {
            mode = 0;
        }
    }

    private String getModeName(int mode) {
        switch (mode) {
            case 0:
                return ChatColor.GREEN + "Healing";
            case 1:
                return ChatColor.GRAY + "Fling";
            case 2:
                return ChatColor.YELLOW + "Stun";
            default:
                return "Unknown Mode";
        }
    }

    private void moveArmorStand(ArmorStand armorStand, Player p) {
        new BukkitRunnable() {
            private final double speed = 0.5; // Adjust this value to control the speed of movement

            @Override
            public void run() {
                armorStand.setVelocity(armorStand.getLocation().getDirection().multiply(speed).normalize());
                Location currentLocation = armorStand.getLocation();


                // Check for collisions with entities or blocks
                if (checkForCollisions(currentLocation, p)) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private boolean checkForCollisions(Location location, Player p) {
        double tolerance = 0.2; // Adjust this value to control the tolerance for collision detection

        // Check for collisions with entities
        for (Entity entity : location.getWorld().getNearbyEntities(location, tolerance, tolerance, tolerance)) {
            if (entity.getType() != EntityType.ARMOR_STAND && entity != p) {
                for (Entity nearbyEntities : location.getWorld().getNearbyEntities(location, 2, 2, 2)) {
                    if (nearbyEntities instanceof ArmorStand stand) {
                        stand.remove();
                    }
                    if (nearbyEntities instanceof LivingEntity) {
                        switch (mode) {
                            case (0):
                                ((LivingEntity) nearbyEntities).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 2));
                                launchColoredFirework(entity,0, location);
                                break;
                            case (1):
                                sendPlayerAway(nearbyEntities, location, 1);
                                launchColoredFirework(entity,1, location);
                                break;
                            case (2):
                                StatusEffects.stun((Player) nearbyEntities, 200);
                                launchColoredFirework(entity,2, location);
                                break;
                        }

                    }
                }
                return true;
            }
        }

        // Check for collisions with blocks
        Location blockLocation = location.clone().add(0, 0.5, 0);
        if (location.add(0,-0.5,0).getBlock().getType() != Material.AIR || location.add(-0.5,0,0).getBlock().getType() != Material.AIR || location.add(0,0,-0.5).getBlock().getType() != Material.AIR || location.add(0,0.5,0).getBlock().getType() != Material.AIR || location.add(0.5,0,0).getBlock().getType() != Material.AIR || location.add(0,0,0.5).getBlock().getType() != Material.AIR) {
            for (Entity nearbyEntities : location.getWorld().getNearbyEntities(location, 4, 4, 4)) {
                if (nearbyEntities instanceof ArmorStand stand) {
                    stand.remove();
                }
                if (nearbyEntities instanceof LivingEntity) {
                    switch (mode) {
                        case (0):
                            ((LivingEntity) nearbyEntities).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2));
                            launchColoredFirework(nearbyEntities,0, location);
                            break;
                        case (1):
                            sendPlayerAway(nearbyEntities, location, 1);
                            launchColoredFirework(nearbyEntities,1, location);
                            break;
                        case (2):
                            if (nearbyEntities instanceof Player) {
                                StatusEffects.stun((Player) nearbyEntities, 200);
                            }
                            launchColoredFirework(nearbyEntities,2, location);
                            break;
                    }
                }
            }
            return true;
        }

        return false;
    }

    public void createExpandingParticleCircle(Player player, Location center, Particle particleType, int particleCount, double radius, int durationTicks) {
        new BukkitRunnable() {
            private int ticks = 0;

            @Override
            public void run() {
                if (ticks >= durationTicks) {
                    cancel();
                    return;
                }
                double radius = 0.1; // Adjust the expansion speed as needed
                double angleIncrement = Math.PI * 2 / particleCount;
                for (int i = 0; i < particleCount; i++) {
                    double angle = angleIncrement * i;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    double y = center.getY();

                    Location particleLocation = new Location(player.getWorld(), x, y, z);

                    player.spawnParticle(particleType, particleLocation, 1);
                }


                ticks++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void launchColoredFirework(Entity player, int mode, Location location) {
        Firework firework = player.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // Create a firework effect with multiple colors and a trail
        switch (mode) {
            case (0): //heal
                fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).flicker(true).build());
                break;
            case (1): //fling
                fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.GRAY).flicker(true).build());
                break;
            case (2): //stun
                fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).build());
                break;
        }
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }
    public void sendPlayerAway(Entity player, Location sourceLocation, double velocityMultiplier) {
        Vector velocity = player.getLocation().toVector().subtract(sourceLocation.toVector());
        velocity = velocity.normalize().multiply(velocityMultiplier);
        player.setVelocity(velocity);
    }
}

