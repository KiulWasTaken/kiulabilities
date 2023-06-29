package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.StatusEffects;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Catalyst implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    String configname = AbilityItemNames.CATALYST.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    private int primaryMode = 0;

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.CATALYST.getLabel()));

    boolean isCharged = false;
    boolean isUltimateActive = false;

    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname) && p.isOnGround()) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);

                        /** PRIMARY - CODE START >> */


                        Location inFront = p.getLocation().add(p.getLocation().getDirection().normalize().multiply(2));
                        inFront.setY(p.getLocation().add(0,-1,0).getY());
                        Block spreadCenter = inFront.getBlock();
                        spread(spreadCenter,p,2);

                        Block backLeft = getLocationRelative(1.0,2.2,0.0, p.getLocation()).getBlock();
                        Block backRight = getLocationRelative(1.0,0.0,2.2, p.getLocation()).getBlock();

                        Block midLeftBottom = getLocationRelative(2.0,1.6,0.0, p.getLocation()).getBlock();
                        Block midLeftTop = midLeftBottom.getRelative(BlockFace.UP);
                        Block midRightBottom = getLocationRelative(2.0,0.0,1.6, p.getLocation()).getBlock();
                        Block midRightTop = midRightBottom.getRelative(BlockFace.UP);

                        Block frontBottomMid = getLocationRelative(3.0,0.0,0.0, p.getLocation()).getBlock();
                        Block frontBottomLeft = getLocationRelative(3.0,1.0,0.0, p.getLocation()).getBlock();
                        Block frontBottomRight = getLocationRelative(3.0,0.0,1.0, p.getLocation()).getBlock();
                        Block frontMiddleMiddle = frontBottomMid.getRelative(BlockFace.UP);
                        Block frontMiddleRight = frontBottomRight.getRelative(BlockFace.UP);
                        Block frontMiddleLeft = frontBottomLeft.getRelative(BlockFace.UP);
                        Block frontTopLeft = frontMiddleLeft.getRelative(BlockFace.UP);
                        Block frontTopRight = frontMiddleRight.getRelative(BlockFace.UP);
                        Block frontTopMiddle = frontMiddleMiddle.getRelative(BlockFace.UP);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                backRight.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,backRight.getLocation(),5,1,1,1, 0.0005);
                                backLeft.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,backLeft.getLocation(),5,1,1,1, 0.0005);

                            }
                        }, 2);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                midLeftBottom.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,midRightBottom.getLocation(),5,1,1,1, 0.0005);
                                midRightBottom.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,midLeftBottom.getLocation(),5,1,1,1, 0.0005);
                            }
                        }, 4);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                midLeftTop.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,midLeftTop.getLocation(),5,1,1,1, 0.0005);
                                midRightTop.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,midRightTop.getLocation(),5,1,1,1, 0.0005);
                                frontBottomLeft.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontBottomLeft.getLocation(),5,1,1,1, 0.0005);
                                frontBottomRight.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontBottomRight.getLocation(),5,1,1,1, 0.0005);

                            }
                        }, 6);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                frontBottomMid.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontBottomMid.getLocation(),5,1,1,1, 0.0005);
                                frontMiddleLeft.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontMiddleLeft.getLocation(),5,1,1,1, 0.0005);
                                frontMiddleRight.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontMiddleRight.getLocation(),5,1,1,1, 0.0005);
                            }
                        }, 8);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                frontMiddleMiddle.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontMiddleMiddle.getLocation(),5,1,1,1, 0.0005);
                                frontTopLeft.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontTopLeft.getLocation(),5,1,1,1, 0.0005);
                                frontTopMiddle.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontTopMiddle.getLocation(),5,1,1,1, 0.0005);
                                frontTopRight.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,frontTopRight.getLocation(),5,1,1,1, 0.0005);
                            }
                        }, 10);
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

                        for (Entity nearbyEntities : p.getNearbyEntities(9,9,9)) {
                            if (nearbyEntities instanceof Player) {
                                EvokerFangs evokerFangs = (EvokerFangs) nearbyEntities.getWorld().spawnEntity(nearbyEntities.getLocation(), EntityType.EVOKER_FANGS);
                                evokerFangs.setOwner(p);
                                evokerFangs.setMetadata("spreadfang", new FixedMetadataValue(plugin, "pat"));
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
                                isUltimateActive = true;
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
    public void noDropsForSpreadZombie (EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("spread")) {
            for (int i = 0; i < e.getDrops().size(); i++) {
                e.getDrops().set(i,null);
                e.setDroppedExp(0);
            }
        }
    }

    @EventHandler
    public void spreadFromFangHit (EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.EVOKER_FANGS && e.getDamager().hasMetadata("spreadfang")) {
            EvokerFangs evokerFangs = (EvokerFangs) e.getDamager();
            spread(e.getEntity().getLocation().add(0,-1,0).getBlock(),(Player) evokerFangs.getOwner(), 1);
            StatusEffects.root((Player) e.getEntity(),1);
        }
    }

    @EventHandler
    public void getHurtOnSculkEvent (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getEntity().hasMetadata("catalyst") && e.getEntity().isOnGround()) {
            spread(e.getEntity().getLocation().add(0,-1,0).getBlock(),(Player) e.getEntity(),1);
        }
    }


    @EventHandler
    public void dismissOnSculkCrouch (PlayerToggleSneakEvent e) {
        if (e.getPlayer().isSneaking() && e.getPlayer().hasMetadata("catalyst") && isCharged) {
            e.getPlayer().getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,e.getPlayer().getLocation().add(0,1,0),10,1,1,1,0.0005);
            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                onlinePlayers.hidePlayer(plugin,e.getPlayer());
            }
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,80,0,true,false));

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.showPlayer(plugin,e.getPlayer());
                    }
                }
            }, 80);

        }
    }

    @EventHandler
    public void catalystKillEvent (PlayerDeathEvent e) {
        if (e.getEntity().getKiller().hasMetadata("catalyst") && e.getEntity().getKiller() instanceof Player && e.getEntity().isOnGround()) {
            spread(e.getEntity().getLocation().add(0,-1,0).getBlock(),e.getEntity().getKiller(),2);
        }

        if (isUltimateActive) {
            Player p = e.getEntity().getKiller();
            isCharged = true;
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,160,0,true,false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,160,1,true,false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,160,1,true,false));

            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.getGameMode() != GameMode.SPECTATOR && p.getLocation().add(0,-1,0).getBlock().getType() == Material.SCULK || p.getLocation().add(0,-1,0).getBlock().getType() == Material.SCULK_CATALYST) {
                    EvokerFangs evokerFangs = (EvokerFangs) players.getWorld().spawnEntity(players.getLocation(), EntityType.EVOKER_FANGS);
                    evokerFangs.setOwner(p);
                    evokerFangs.setMetadata("spreadfang", new FixedMetadataValue(plugin, "pat"));
                }
            }
            new BukkitRunnable() {
                public void run() {
                    if (p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() <= 0 ) {
                        isCharged = false;
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 160L, 20L);

        }
    }


    public void spread (Block spreadCenter,Player p, int amount) {
        World world = spreadCenter.getWorld();

        spreadCenter.setType(Material.SCULK_CATALYST);
        if ( spreadCenter.getRelative(BlockFace.NORTH).getType() != Material.AIR) {
            spreadCenter.getRelative(BlockFace.NORTH).setType(Material.SCULK);
        }
        if ( spreadCenter.getRelative(BlockFace.EAST).getType() != Material.AIR) {
            spreadCenter.getRelative(BlockFace.EAST).setType(Material.SCULK);
        }
        if ( spreadCenter.getRelative(BlockFace.SOUTH).getType() != Material.AIR) {
            spreadCenter.getRelative(BlockFace.SOUTH).setType(Material.SCULK);
        }
        if ( spreadCenter.getRelative(BlockFace.WEST).getType() != Material.AIR) {
            spreadCenter.getRelative(BlockFace.WEST).setType(Material.SCULK);

        }
        for (int i = 0; i < amount; i++) {
            Zombie triggerSpread = (Zombie) world.spawnEntity(spreadCenter.getLocation(), EntityType.ZOMBIE);
            triggerSpread.setBaby();
            triggerSpread.setInvisible(true);
            triggerSpread.setGravity(false);
            triggerSpread.setSilent(true);
            triggerSpread.setMetadata("spread", new FixedMetadataValue(plugin, "pat"));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Kiulabilities.getPlugin(Kiulabilities.class), new Runnable() {
                @Override
                public void run() {
                    triggerSpread.damage(20, p);
                    spreadCenter.getState().update();
                }
            }, 1);
        }

    }
// fuck you intellij
    @EventHandler
    public void Passive (PlayerMoveEvent e) {
        ArrayList<Player> preventInfiniteRepeatingTask = new ArrayList<>();

        if (!preventInfiniteRepeatingTask.contains(e.getPlayer())) {
            preventInfiniteRepeatingTask.add(e.getPlayer());
            new BukkitRunnable() {
                public void run() {
                    if (e.getPlayer().getLocation().add(0,-1,0).getBlock().getType() == Material.SCULK) {
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,15,0));
                    } else {
                        preventInfiniteRepeatingTask.remove(e.getPlayer());
                        cancel();
                    }

                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }


    public Location getLocationRelative (double forwards, double left, double right, Location center) {

        Vector direction = center.getDirection().setY(0).normalize();


// Scale the direction vector to the desired length
        Vector forwardVector = direction.multiply(forwards);

// Calculate the left vector by rotating the forward vector 90 degrees

        Vector leftVector = new Vector(-forwardVector.getZ(), 0, forwardVector.getX()).normalize().multiply(left);

        Vector rightVector = new Vector(forwardVector.getZ(), 0, -forwardVector.getX()).normalize().multiply(right);

        Vector targetVector = (forwardVector.add(leftVector)).add(rightVector);

        Location targetLocation = center.clone().add(targetVector);

        return targetLocation;

    }
}



