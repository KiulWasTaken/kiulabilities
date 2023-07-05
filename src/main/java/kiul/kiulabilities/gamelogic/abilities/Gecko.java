package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ColoredText;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Gecko implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    String configname = AbilityItemNames.GECKO.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.GECKO.getLabel()));

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
                                loc.add(x, y, z);
                                p.getWorld().spawnParticle(Particle.BLOCK_DUST, loc, 10, 0.1, 0.1, 0.1, 0, Material.REDSTONE_BLOCK.createBlockData());
                                Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(150, 0, 0), Color.fromRGB(168, 64, 64), 2F);
                                p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 2, 0, 0, 0, dustTransition);

                                drawLine(loc, p.getEyeLocation().add(dir), 1);

                                if (t > 15 || loc.getBlock().getType().isOccluding() == true) {
                                    cancel();
                                    drawLine1(loc, 1, p);
                                }

                                for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1.5, 1.5, 1.5)) {
                                    if (entity.getType() != EntityType.ARMOR_STAND && entity != p) {
                                        cancel();
                                        drawLine1(loc, 1, p);
                                        entity.setVelocity(p.getEyeLocation().toVector().clone().subtract(entity.getLocation().toVector()).normalize().multiply(1.4).add(new Vector(0,0.1,0)));
                                    }
                                }

                                loc.subtract(x, y, z);

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

                        p.setArrowsInBody(0);

                        ItemStack helmet = p.getInventory().getHelmet();
                        ItemStack chestplate = p.getInventory().getChestplate();
                        ItemStack leggings = p.getInventory().getLeggings();
                        ItemStack boots = p.getInventory().getBoots();
                        ItemStack Offhand = p.getInventory().getItemInOffHand();

                        p.getInventory().setHelmet(null);
                        p.getInventory().setChestplate(null);
                        p.getInventory().setLeggings(null);
                        p.getInventory().setBoots(null);
                        p.getInventory().setItemInOffHand(null);

                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 0, true, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 2, true, false));

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Location loc = p.getLocation();
                                new BukkitRunnable() {
                                    public void run() {

                                        if (loc.distance(p.getLocation()) > 0.5) {
                                            cancel();
                                            p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                                            p.removePotionEffect(PotionEffectType.INVISIBILITY);
                                            p.getInventory().setHelmet(helmet);
                                            p.getInventory().setChestplate(chestplate);
                                            p.getInventory().setLeggings(leggings);
                                            p.getInventory().setBoots(boots);
                                            p.getInventory().setItemInOffHand(Offhand);
                                        }

                                    }
                                }.runTaskTimer(plugin, 0, 1L);
                            }
                        }, 20);

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
    public void Passiveshift(PlayerToggleSneakEvent e) {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (!p.isSneaking()) {

                    new BukkitRunnable() {
                        public void run() {

                            List<Block> blocks = new ArrayList<>();
                            blocks.clear();
                            blocks.add(p.getLocation().add(0.35, 0, 0).getBlock());
                            blocks.add(p.getLocation().add(0.35, 0, 0.35).getBlock());
                            blocks.add(p.getLocation().add(0, 0, 0.35).getBlock());
                            blocks.add(p.getLocation().add(-0.35, 0, 0.35).getBlock());
                            blocks.add(p.getLocation().add(-0.35, 0, 0).getBlock());
                            blocks.add(p.getLocation().add(-0.35, 0, -0.35).getBlock());
                            blocks.add(p.getLocation().add(0, 0, -0.35).getBlock());
                            blocks.add(p.getLocation().add(0.35, 0, -0.35).getBlock());

                            if (!p.hasMetadata("geckono")) {
                                if (p.isSneaking() && p.getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                                    if (p.getLocation().getBlock().getType() == Material.AIR || p.getLocation().getBlock().getType().isOccluding() == false) {
                                        for (Block block : blocks) {
                                            if (block.getType() != Material.AIR) {
                                                if (block.getType().isOccluding() == true) {
                                                    p.setVelocity(new Vector(0, 0.2, 0));
                                                    p.getWorld().spawnParticle(Particle.BLOCK_DUST, p.getLocation(), 5, 0.3, 0.3, 0.3, 0, block.getBlockData());
                                                    break;
                                                }
                                            }
                                        }
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
    public void playerDamage(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player p) {
            if (AbilityExtras.itemcheck(p, itemname) == true) {

                p.setMetadata("geckono", new FixedMetadataValue(plugin, "pat"));

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {

                        p.removeMetadata("geckono", plugin);

                    }
                }, 100);

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

                                Location loc = p.getLocation().add(0,-1.3,0);

                                spawnTail(loc, p);

                                p.getWorld().spawnParticle(Particle.CLOUD,p.getLocation().add(0,1,0),40,2,2,2,0);

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

    public void drawLine(Location point1, Location point2, double space) {
        World world = point1.getWorld();
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(222, 87, 87), Color.fromRGB(168, 64, 64), 1F);
            world.spawnParticle(Particle.DUST_COLOR_TRANSITION, p1.getX(), p1.getY(), p1.getZ(), 2, 0, 0, 0, dustTransition);
            length += space;
        }
    }

    public void drawLine1(Location point1, double space, Player p) {
        new BukkitRunnable() {
            World world = point1.getWorld();
            double distance = point1.distance(p.getEyeLocation());
            Vector p1 = point1.toVector();
            double length = 0;
            public void run() {
                Vector vector = p.getEyeLocation().toVector().clone().subtract(p1).normalize().multiply(space);
                p1.add(vector);
                world.spawnParticle(Particle.BLOCK_DUST, p1.getX(), p1.getY(), p1.getZ(), 10, 0.1, 0.1, 0.1, 0, Material.REDSTONE_BLOCK.createBlockData());
                Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(150 - ((int) length * 7), 0, 0), Color.fromRGB(168, 64, 64), 2F);
                world.spawnParticle(Particle.DUST_COLOR_TRANSITION, p1.getX(), p1.getY(), p1.getZ(), 2, 0, 0, 0, dustTransition);
                length += space;
                if (length > distance) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void spawnTail (Location loc, Player p) {

        loc.setYaw(90);
        loc = loc.add(0,-0.1,0);

        ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(loc.clone(), EntityType.ARMOR_STAND);
        armorStand.setMarker(true);
        armorStand.setInvisible(true);
        armorStand.setCollidable(false);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)));
        armorStand.setHelmet(new ItemStack(Material.TUFF));

        ArmorStand armorStand1 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-0.1,-0.09,-0.3), EntityType.ARMOR_STAND);
        armorStand1.setMarker(true);
        armorStand1.setInvisible(true);
        armorStand1.setCollidable(false);
        armorStand1.setInvulnerable(true);
        armorStand1.setGravity(false);
        armorStand1.setHeadPose(new EulerAngle(Math.toRadians(15), Math.toRadians(90), Math.toRadians(0)));
        armorStand1.setHelmet(new ItemStack(Material.TUFF));

        ArmorStand armorStand2 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-0.1,-0.09,0.3), EntityType.ARMOR_STAND);
        armorStand2.setMarker(true);
        armorStand2.setInvisible(true);
        armorStand2.setCollidable(false);
        armorStand2.setInvulnerable(true);
        armorStand2.setGravity(false);
        armorStand2.setHeadPose(new EulerAngle(Math.toRadians(15), Math.toRadians(270), Math.toRadians(0)));
        armorStand2.setHelmet(new ItemStack(Material.TUFF));

        ArmorStand armorStand3 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-0.5,-0.05,0), EntityType.ARMOR_STAND);
        armorStand3.setMarker(true);
        armorStand3.setInvisible(true);
        armorStand3.setCollidable(false);
        armorStand3.setInvulnerable(true);
        armorStand3.setGravity(false);
        armorStand3.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(20), Math.toRadians(0)));
        armorStand3.setHelmet(new ItemStack(Material.TUFF));

        ArmorStand armorStand4 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-0.35,-0.13,-0.2), EntityType.ARMOR_STAND);
        armorStand4.setMarker(true);
        armorStand4.setInvisible(true);
        armorStand4.setCollidable(false);
        armorStand4.setInvulnerable(true);
        armorStand4.setGravity(false);
        armorStand4.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(20), Math.toRadians(345)));
        armorStand4.setHelmet(new ItemStack(Material.DRIPSTONE_BLOCK));

        ArmorStand armorStand5 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-0.55,-0.13,0.15), EntityType.ARMOR_STAND);
        armorStand5.setMarker(true);
        armorStand5.setInvisible(true);
        armorStand5.setCollidable(false);
        armorStand5.setInvulnerable(true);
        armorStand5.setGravity(false);
        armorStand5.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(20), Math.toRadians(15)));
        armorStand5.setHelmet(new ItemStack(Material.DRIPSTONE_BLOCK));

        ArmorStand armorStand6 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-0.91,-0.16,-0), EntityType.ARMOR_STAND);
        armorStand6.setMarker(true);
        armorStand6.setInvisible(true);
        armorStand6.setCollidable(false);
        armorStand6.setInvulnerable(true);
        armorStand6.setGravity(false);
        armorStand6.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(30), Math.toRadians(15)));
        armorStand6.setHelmet(new ItemStack(Material.DRIPSTONE_BLOCK));

        ArmorStand armorStand7 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-0.75,-0.16,-0.25), EntityType.ARMOR_STAND);
        armorStand7.setMarker(true);
        armorStand7.setInvisible(true);
        armorStand7.setCollidable(false);
        armorStand7.setInvulnerable(true);
        armorStand7.setGravity(false);
        armorStand7.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(30), Math.toRadians(345)));
        armorStand7.setHelmet(new ItemStack(Material.DRIPSTONE_BLOCK));

        ArmorStand armorStand8 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-1.1,-0.18,-0.15), EntityType.ARMOR_STAND);
        armorStand8.setMarker(true);
        armorStand8.setInvisible(true);
        armorStand8.setCollidable(false);
        armorStand8.setInvulnerable(true);
        armorStand8.setGravity(false);
        armorStand8.setHeadPose(new EulerAngle(Math.toRadians(20), Math.toRadians(265), Math.toRadians(0)));
        armorStand8.setHelmet(new ItemStack(Material.DRIPSTONE_BLOCK));

        ArmorStand armorStand9 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-1.1,-0.18,-0.18), EntityType.ARMOR_STAND);
        armorStand9.setMarker(true);
        armorStand9.setInvisible(true);
        armorStand9.setCollidable(false);
        armorStand9.setInvulnerable(true);
        armorStand9.setGravity(false);
        armorStand9.setHeadPose(new EulerAngle(Math.toRadians(340), Math.toRadians(265), Math.toRadians(0)));
        armorStand9.setHelmet(new ItemStack(Material.DRIPSTONE_BLOCK));

        ArmorStand armorStand10 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-1.5,-0.2,0.15), EntityType.ARMOR_STAND);
        armorStand10.setMarker(true);
        armorStand10.setInvisible(true);
        armorStand10.setCollidable(false);
        armorStand10.setInvulnerable(true);
        armorStand10.setGravity(false);
        armorStand10.setHeadPose(new EulerAngle(Math.toRadians(315), Math.toRadians(245), Math.toRadians(0)));
        armorStand10.setHelmet(new ItemStack(Material.DRIPSTONE_BLOCK));

        ArmorStand armorStand11 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-1.8,-0.34,0.35), EntityType.ARMOR_STAND);
        armorStand11.setMarker(true);
        armorStand11.setInvisible(true);
        armorStand11.setCollidable(false);
        armorStand11.setInvulnerable(true);
        armorStand11.setGravity(false);
        armorStand11.setHeadPose(new EulerAngle(Math.toRadians(315), Math.toRadians(215), Math.toRadians(0)));
        armorStand11.setHelmet(new ItemStack(Material.DRIPSTONE_BLOCK));

        ArmorStand armorStand12 = (ArmorStand) p.getWorld().spawnEntity(loc.clone().add(-2.05,-0.27,0.55), EntityType.ARMOR_STAND);
        armorStand12.setMarker(true);
        armorStand12.setInvisible(true);
        armorStand12.setCollidable(false);
        armorStand12.setInvulnerable(true);
        armorStand12.setGravity(false);
        armorStand12.setHeadPose(new EulerAngle(Math.toRadians(315), Math.toRadians(175), Math.toRadians(0)));
        armorStand12.setHelmet(new ItemStack(Material.TNT));

        new BukkitRunnable() {
            public void run() {

                if (armorStand == null) {
                    cancel();
                }

                for (Entity entity : p.getWorld().getNearbyEntities(armorStand8.getLocation().add(0,1.5,0),2,2,2)) {
                    if (entity.getType() != EntityType.ARMOR_STAND && entity.getType() != EntityType.DROPPED_ITEM && entity != p) {

                        Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(255,0 , 0), Color.fromRGB(255, 255, 255), 2F);
                        p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, armorStand8.getLocation(), 90, 3, 3, 3, dustTransition);

                        p.getWorld().spawnParticle(Particle.CLOUD, armorStand8.getLocation(), 50, 2,2,2,0);
                        p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, armorStand8.getLocation(), 50, 2,2,2,0);

                        armorStand.remove();
                        armorStand1.remove();
                        armorStand2.remove();
                        armorStand3.remove();
                        armorStand4.remove();
                        armorStand5.remove();
                        armorStand6.remove();
                        armorStand7.remove();
                        armorStand8.remove();
                        armorStand9.remove();
                        armorStand10.remove();
                        armorStand11.remove();
                        armorStand12.remove();

                        p.getWorld().spawn(armorStand6.getLocation().add(0,2,0), Creeper.class, (explosive) -> {
                            explosive.setMaxFuseTicks(0);
                            explosive.ignite();
                            explosive.setInvisible(true);
                            explosive.setSilent(true);
                            explosive.setExplosionRadius(5);
                        });

                        cancel();

                    }
                }

            }
        }.runTaskTimer(plugin, 20L, 1L);
    }
}



