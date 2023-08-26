package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import kiul.kiulabilities.gamelogic.Methods.StatusEffects;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Discharge implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();

    String configname = AbilityItemNames.DISCHARGE.name(); /** CHANGE 'ARTIFICER'*/

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    public ArrayList<Player> ultimateActive = new ArrayList<>();

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.DISCHARGE.getDisplayName())); /** CHANGE 'ARTIFICER'*/

    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);

                        /** PRIMARY - CODE START >> */
                        ItemStack dischargeTrident = new ItemStack(Material.TRIDENT);
                        ItemMeta dischargeTridentMeta = dischargeTrident.getItemMeta();
                        dischargeTridentMeta.addEnchant(Enchantment.RIPTIDE,2,false);
                        dischargeTridentMeta.setDisplayName(ChatColor.BLUE + "Discharge Trident");
                        dischargeTridentMeta.setUnbreakable(true);
                        dischargeTridentMeta.setLocalizedName("Discharge Trident");
                        dischargeTrident.setItemMeta(dischargeTridentMeta);
                        p.getInventory().setItemInOffHand(dischargeTrident);


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
                        Trident projectile = (Trident) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.TRIDENT);
                        projectile.setPierceLevel(1);
                        projectile.setShooter(p);
                        projectile.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                        projectile.setVelocity(p.getEyeLocation().getDirection().multiply(5));
                        projectile.setMetadata("channelling", new FixedMetadataValue(plugin, "pat"));
                        p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW,1,1);

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
                                ultimateActive.add(p);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (p.getGameMode() != GameMode.SPECTATOR) {
                                            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                                if (onlinePlayers.getGameMode() == GameMode.SURVIVAL && onlinePlayers.getLocation().getY() > 80) {
                                                    onlinePlayers.getWorld().spawnEntity(onlinePlayers.getLocation(),EntityType.LIGHTNING);
                                                }
                                            }
                                        } else {
                                            ultimateActive.remove(p);
                                            p.getWorld().setThundering(false);
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(plugin, 0L, 60L);

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
    public void removeTridentAfterUse (PlayerRiptideEvent e) {
        Player p = e.getPlayer();
        if (AbilityExtras.itemcheck(p,itemname)) {
            p.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
        }
    }
    @EventHandler
    public void preventTridentOrShieldMove (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (AbilityExtras.itemcheck(p,itemname) && e.getCurrentItem().getType() == Material.TRIDENT || e.getCurrentItem().getType() == Material.SHIELD) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void tridentChannelling (EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity victim = e.getEntity();
        if (damager instanceof LightningStrike && victim instanceof Player p && AbilityExtras.itemcheck(p,itemname)) {
            e.setCancelled(true);
            return;
        }
        if (damager.hasMetadata("channelling") && damager instanceof Trident trident) {
            victim.getWorld().spawnEntity(victim.getLocation(),EntityType.LIGHTNING);
            ((Player)trident.getShooter()).getWorld().playSound(((Player)trident.getShooter()),Sound.ITEM_TRIDENT_THUNDER,1,1);
            if (victim instanceof Player) {
                StatusEffects.root((Player) victim, 20);
            }
            if (victim.getLocation().getBlock().getType() == Material.WATER) {
                for (Entity nearbyEntities : victim.getNearbyEntities(7,7,7)) {
                    if (nearbyEntities instanceof Player nearbyPlayer) {
                        nearbyPlayer.damage(3,(Player)trident.getShooter());
                        StatusEffects.stun(nearbyPlayer,80);
                    }
                }
            }
        }
    }

    @EventHandler
    public void passiveAbility (PlayerMoveEvent e) {
        Player p = e.getPlayer();

        ArrayList<Player> preventInfiniteRepeatingTask = new ArrayList<>();

        if (!preventInfiniteRepeatingTask.contains(p) && AbilityExtras.itemcheck(p,itemname)) {
            preventInfiniteRepeatingTask.add(p);
            new BukkitRunnable() {
                boolean passiveActive = true;
                @Override
                public void run() {
                    if (passiveActive) {
                        if (p.getLocation().getBlock().getType() == Material.LAVA || p.getLocation().getBlock().getType() == Material.FIRE) {
                            for (Block b : getBlocks(p.getLocation().getBlock(), 3)) {
                                if (b.getType() == Material.FIRE) {
                                    b.setType(Material.AIR);
                                    p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, b.getLocation(), 5);
                                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 2, 1);
                                }
                                if (b.getType() == Material.LAVA) {
                                    b.setType(Material.OBSIDIAN);
                                    p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, b.getLocation(), 5);
                                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 2, 1);
                                }
                                passiveActive = !passiveActive;
                            }
                        } else {
                            p.getWorld().spawnParticle(Particle.WATER_DROP,p.getLocation().add(0,1,0),6,0.2,0.2,0.2);
                        }
                    } else {
                        preventInfiniteRepeatingTask.remove(p);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 10L);


        }
    }


    public ArrayList<Block> getBlocks(Block start, int radius){
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++){
            for(double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++){
                for(double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++){
                    Location loc = new Location(start.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }
            }
        }
        return blocks;
    }
}



