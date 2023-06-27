package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.StatusEffects;
import kiul.kiulabilities.gamelogic.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ColoredText;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Discharge implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    static boolean isUltActive = false;

    String configname = AbilityItemNames.DISCHARGE.name(); /** CHANGE 'ARTIFICER'*/

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.DISCHARGE.getLabel())); /** CHANGE 'ARTIFICER'*/

    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {

        Player p = e.getPlayer();


        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);

                        /** PRIMARY - CODE START >> */
                        if (isUltActive == false) {
                            ItemStack t = new ItemStack(Material.TRIDENT);
                            ItemMeta tm = t.getItemMeta();
                            tm.setDisplayName("Discharge Trident");
                            t.setItemMeta(tm);
                            p.getInventory().setItemInOffHand(t);
                        } else {
                            for (Entity ap : p.getNearbyEntities(13, 13, 13)) {
                                    ap.getWorld().spawnEntity(ap.getLocation(),EntityType.LIGHTNING);
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
                        if (p.getLocation().getBlock().getType() == Material.WATER && isUltActive == false) {
                            for (Entity ap : p.getNearbyEntities(13, 13, 13)) {
                                if (ap instanceof Player) {
                                    if (ap.getLocation().getBlock().getType() == Material.WATER) {
                                        ((Player) ap).damage(4, p); //make static methods for debuffs
                                        spawnParticleTrail(p.getLocation(), ap.getLocation(), Particle.BLOCK_CRACK, p, 50, 0);



                                    }
                                }
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
                                p.getWorld().setThundering(true);
                                p.getWorld().setThunderDuration(999999999);

                                ItemStack ut = new ItemStack(Material.TRIDENT);
                                ItemMeta utm = ut.getItemMeta();
                                utm.setDisplayName("Discharge Ultimate Trident");
                                utm.setUnbreakable(true);
                                utm.addEnchant(Enchantment.RIPTIDE,1,false);
                                ut.setItemMeta(utm);
                                p.getInventory().setItemInOffHand(ut);


                                isUltActive = true;
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
    private void spawnParticleTrail(Location from, Location to, Particle particleType, Player player, int PARTICLE_COUNT, int VARIANCE) {
        Random random = new Random();
        double distance = from.distance(to);
        double step = distance / PARTICLE_COUNT;

        for (double i = 0; i < distance; i += step) {
            double offsetX = random.nextDouble() * VARIANCE * 1 - VARIANCE;
            double offsetY = random.nextDouble() * VARIANCE * 1 - VARIANCE;
            double offsetZ = random.nextDouble() * VARIANCE * 1 - VARIANCE;

            Location particleLocation = from.clone().add(to.clone().subtract(from).multiply(i));
            particleLocation.add(offsetX, offsetY, offsetZ);

            player.getWorld().spawnParticle(particleType, particleLocation, 1, Material.YELLOW_WOOL.createBlockData());
        }
    }
    @EventHandler
    public void tridentThrow (ProjectileLaunchEvent e) {
        if (e.getEntity().getType() == EntityType.TRIDENT && e.getEntity().getShooter() instanceof Player && ((Player) e.getEntity().getShooter()).hasMetadata("discharge")) {
            e.getEntity().setMetadata("discharge", new FixedMetadataValue(plugin, "pat"));
            ItemStack s = new ItemStack(Material.SHIELD);
            ItemMeta sm = s.getItemMeta();
            sm.setUnbreakable(true);
            s.setItemMeta(sm);
            ((Player) e.getEntity().getShooter()).getInventory().setItemInOffHand(s);
        }
    }
    @EventHandler
    public void tridentHit (ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata("discharge") && e.getHitEntity() != null) {
            e.getHitEntity().getWorld().spawnEntity(e.getHitEntity().getLocation(),EntityType.LIGHTNING);
            StatusEffects.root((Player) e.getHitEntity(),1);
            e.getEntity().remove();
            if (e.getHitEntity().getLocation().getBlock().getType() == Material.WATER) {
                for (Entity ap : e.getHitEntity().getNearbyEntities(13,13,13)) {
                    if (ap instanceof Player && e.getEntity().getShooter() instanceof Player) {
                        if (ap.getLocation().getBlock().getType() == Material.WATER) {
                            ((Player) ap).damage(4, (Player) e.getEntity().getShooter());
                            StatusEffects.stun(((Player) ap).getPlayer(), 3);

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void tridentClick (InventoryClickEvent e) {

        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("discharge trident")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void lightningDamage (EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING && e.getEntity() instanceof Player && e.getEntity().hasMetadata("discharge")) {
            e.setCancelled(true);
        }
    }
}






