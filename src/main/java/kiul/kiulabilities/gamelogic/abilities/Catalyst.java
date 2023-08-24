package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.Methods.StatusEffects;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.SculkVein;
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
import java.util.*;

public class Catalyst implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    String configname = AbilityItemNames.CATALYST.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.CATALYST.getDisplayName()));

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
                        spread(spreadCenter.getLocation(),0,0,10,1,600);

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
                        }, 1);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                midLeftBottom.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,midRightBottom.getLocation(),5,1,1,1, 0.0005);
                                midRightBottom.setType(Material.SCULK);
                                p.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP,midLeftBottom.getLocation(),5,1,1,1, 0.0005);
                            }
                        }, 2);
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
                        }, 3);
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
                        }, 4);
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
                        }, 5);

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
                        p.getWorld().playSound(p.getLocation(),Sound.BLOCK_SCULK_SENSOR_CLICKING,1,1);
                        spread(p.getLocation().add(0,-1,0),0,2,15,1,600);

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
            spread(e.getEntity().getLocation().add(0,-1,0),0, 1,5,2,600);
            StatusEffects.root((Player) e.getEntity(),1);
        }
    }

    @EventHandler
    public void getHurtOnSculkEvent (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p && AbilityExtras.itemcheck(p,itemname) && e.getEntity().isOnGround()) {
            spread(e.getEntity().getLocation().add(0,-1,0),0,1,5,1,600);
        }
    }


    @EventHandler
    public void dismissOnSculkCrouch (PlayerToggleSneakEvent e) {
        if (e.getPlayer().isSneaking() && AbilityExtras.itemcheck(e.getPlayer(),itemname) && isCharged) {
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
        if (e.getEntity().getKiller() != null && AbilityExtras.itemcheck(e.getEntity().getKiller(),itemname) && e.getEntity().getKiller() instanceof Player && e.getEntity().isOnGround()) {
            spread(e.getEntity().getLocation().add(0,-1,0),0,2,10,2,600);
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
                    if (!p.getActivePotionEffects().contains(PotionEffectType.INCREASE_DAMAGE)) {
                        isCharged = false;
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 160L, 20L);
        }
    }


    public void spread (Location loc, int delay, int timer, int length, int speed, int revertAfter) {
        Random random = new Random();
        int rint = random.nextInt(12);
        List<Block> check = new ArrayList<>();
        if (timer < length) {
            check.add(loc.getBlock().getRelative(BlockFace.NORTH));
            check.add(loc.getBlock().getRelative(BlockFace.EAST));
            check.add(loc.getBlock().getRelative(BlockFace.SOUTH));
            check.add(loc.getBlock().getRelative(BlockFace.WEST));
            check.add(loc.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.UP));
            check.add(loc.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.UP));
            check.add(loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP));
            check.add(loc.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.UP));
            check.add(loc.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN));
            check.add(loc.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN));
            check.add(loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN));
            check.add(loc.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN));
            for (Block block1 : check) {
                if (block1.getType() != Material.SCULK && block1.getType().isOccluding() == true && block1.getRelative(BlockFace.UP).getType().isOccluding() == false) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        Block block = loc.getBlock();

                        @Override
                        public void run() {
                            switch (rint) {
                                case 0:
                                    block = block.getRelative(BlockFace.NORTH);
                                    break;
                                case 1: {
                                    block = block.getRelative(BlockFace.EAST);
                                    break;
                                }
                                case 2: {
                                    block = block.getRelative(BlockFace.SOUTH);
                                    break;
                                }
                                case 3: {
                                    block = block.getRelative(BlockFace.WEST);
                                    break;
                                }
                                case 4: {
                                    block = block.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN);
                                    break;
                                }
                                case 5: {
                                    block = block.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN);
                                    break;
                                }
                                case 6: {
                                    block = block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN);
                                    break;
                                }
                                case 7: {
                                    block = block.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN);
                                    break;
                                }
                                case 8: {
                                    block = block.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP);
                                    break;
                                }
                                case 9: {
                                    block = block.getRelative(BlockFace.EAST).getRelative(BlockFace.UP);
                                    break;
                                }
                                case 10: {
                                    block = block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP);
                                    break;
                                }
                                case 11: {
                                    block = block.getRelative(BlockFace.WEST).getRelative(BlockFace.UP);
                                    break;
                                }
                            }

                            if (block.getType() != Material.SCULK && block.getType() != Material.AIR && block.getType().isOccluding() == true && block.getRelative(BlockFace.UP).getType().isOccluding() == false) {

                                if (block.getRelative(BlockFace.UP).getType().isOccluding() == false) {
                                    block.getRelative(BlockFace.UP).setType(Material.AIR);
                                }

                                Material mat = block.getType();
                                BlockData blockData = block.getBlockData();

                                block.setType(Material.SCULK);

                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {

                                        if (block.getType() == Material.SCULK) {
                                            block.setType(mat);
                                            block.setBlockData(blockData);
                                            block.getLocation().getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_SCULK_CATALYST_BLOOM, 0.5F, 2F);
                                            block.getLocation().getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_SCULK_CHARGE, 0.5F, 2F);
                                            block.getLocation().getWorld().spawnParticle(Particle.SCULK_CHARGE_POP, block.getLocation().clone().add(0.5, 1, 0.5), 5, 0.5, 0, 0.5, 0);
                                        }

                                    }
                                }, revertAfter * 20);

                                block.getLocation().getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_SCULK_CHARGE, 0.3F, 1F);
                                block.getLocation().getWorld().spawnParticle(Particle.SCULK_CHARGE_POP, block.getLocation().clone().add(0.5, 1.1, 0.5), 5, 0.5, 0, 0.5, 0);
                                block.getLocation().getWorld().spawnParticle(Particle.SCULK_CHARGE, block.getLocation().clone().add(0.5, 1.1, 0.5), 5, 0.5, 0, 0.5, 0,1F);
                                block.getLocation().getWorld().spawnParticle(Particle.SCULK_SOUL, block.getLocation().clone().add(0.5, 1, 0.5), 5, 0.5, 0, 0.5, 0);

                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType().isOccluding() == false && block.getRelative(BlockFace.NORTH).getType() != Material.SCULK && block.getRelative(BlockFace.NORTH).getType() != Material.AIR && block.getRelative(BlockFace.NORTH).getType().isOccluding() == true) {
                                            block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setType(Material.AIR);
                                            block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setType(Material.SCULK_VEIN);
                                            if (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType() == Material.SCULK_VEIN) {
                                                SculkVein sculkVein = (SculkVein) block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getBlockData();
                                                sculkVein.setFace(BlockFace.DOWN, true);
                                                block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setBlockData(sculkVein);
                                            }
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                @Override
                                                public void run() {

                                                    if (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType() == Material.SCULK_VEIN) {
                                                        block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setType(Material.AIR);
                                                    }

                                                }
                                            }, revertAfter * 20);
                                        }
                                        if (block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType().isOccluding() == false && block.getRelative(BlockFace.EAST).getType() != Material.SCULK && block.getRelative(BlockFace.EAST).getType() != Material.AIR && block.getRelative(BlockFace.EAST).getType().isOccluding() == true) {
                                            block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setType(Material.AIR);
                                            block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setType(Material.SCULK_VEIN);
                                            if (block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType() == Material.SCULK_VEIN) {
                                                SculkVein sculkVein = (SculkVein) block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getBlockData();
                                                sculkVein.setFace(BlockFace.DOWN, true);
                                                block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setBlockData(sculkVein);
                                            }
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                @Override
                                                public void run() {

                                                    if (block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType() == Material.SCULK_VEIN) {
                                                        block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setType(Material.AIR);
                                                    }

                                                }
                                            }, revertAfter * 20);
                                        }
                                        if (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType().isOccluding() == false && block.getRelative(BlockFace.SOUTH).getType() != Material.SCULK && block.getRelative(BlockFace.SOUTH).getType() != Material.AIR && block.getRelative(BlockFace.SOUTH).getType().isOccluding() == true) {
                                            block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setType(Material.AIR);
                                            block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setType(Material.SCULK_VEIN);
                                            if (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType() == Material.SCULK_VEIN) {
                                                SculkVein sculkVein = (SculkVein) block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getBlockData();
                                                sculkVein.setFace(BlockFace.DOWN, true);
                                                block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setBlockData(sculkVein);
                                            }
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                @Override
                                                public void run() {

                                                    if (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType() == Material.SCULK_VEIN) {
                                                        block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setType(Material.AIR);
                                                    }

                                                }
                                            }, revertAfter * 20);
                                        }
                                        if (block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType().isOccluding() == false && block.getRelative(BlockFace.WEST).getType() != Material.SCULK && block.getRelative(BlockFace.WEST).getType() != Material.AIR && block.getRelative(BlockFace.WEST).getType().isOccluding() == true) {
                                            block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setType(Material.AIR);
                                            block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setType(Material.SCULK_VEIN);
                                            if (block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType() == Material.SCULK_VEIN) {
                                                SculkVein sculkVein = (SculkVein) block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getBlockData();
                                                sculkVein.setFace(BlockFace.DOWN, true);
                                                block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setBlockData(sculkVein);
                                            }
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                @Override
                                                public void run() {

                                                    if (block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType() == Material.SCULK_VEIN) {
                                                        block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setType(Material.AIR);
                                                    }

                                                }
                                            }, revertAfter * 20);
                                        }
                                    }
                                }, 5);

                                spread(block.getLocation(), speed, timer + 1, length, speed, revertAfter);

                            } else {
                                spread(loc, 1, timer, length, 1, revertAfter);
                            }
                        }
                    }, delay);
                    break;
                }
            }
        }
    }
// fuck you intellij
    @EventHandler
    public void Passive (PlayerMoveEvent e) {
        ArrayList<Player> preventInfiniteRepeatingTask = new ArrayList<>();

        if (!preventInfiniteRepeatingTask.contains(e.getPlayer()) && AbilityExtras.itemcheck(e.getPlayer(),itemname)) {
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



