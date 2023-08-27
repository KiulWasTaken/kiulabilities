package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class Stealth implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();
    private final ArrayList<Player> voidthing = new ArrayList<>();

    String configname = AbilityItemNames.STEALTH.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.STEALTH.getDisplayName()));

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

                            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                onlinePlayers.hidePlayer(plugin, p);
                                voidthing.add(p);
                                onlinePlayers.spawnParticle(Particle.BLOCK_CRACK, p.getLocation().clone().add(0, 1, 0), 15, 0.1, 0.5, 0.1,Material.NETHER_PORTAL.createBlockData());
                                onlinePlayers.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0.5f);
                            }
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60, 5, true, false));
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {

                                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                        onlinePlayers.showPlayer(plugin, p);
                                        voidthing.remove(p);
                                        onlinePlayers.spawnParticle(Particle.BLOCK_CRACK, p.getLocation().clone().add(0, 1, 0), 15, 0.1, 0.5, 0.1, Material.NETHER_PORTAL.createBlockData());
                                        onlinePlayers.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0.5f);
                                    }
                                }
                            }, 40);
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
                            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + " Primary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.DARK_AQUA + ChatColor.ITALIC + timer + "s!");
                        }
                    } else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {
                            e.setCancelled(true);

                            // ABILITY CODE START
                            Entity entity = p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                            ArmorStand stand = (ArmorStand) entity;
                            stand.setVisible(false);
                            stand.setMetadata("blind", new FixedMetadataValue(plugin, "pat"));
                            stand.setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
                            Vector playerDirection = p.getLocation().getDirection();
                            stand.getLocation().setDirection(playerDirection);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (!stand.isDead()) {
                                        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, stand.getLocation().add(0, 1.5, 0), 10, 0.1, 0.3, 0.1, Material.PURPLE_WOOL.createBlockData());
                                        stand.setVelocity(stand.getLocation().getDirection().normalize().multiply(0.5));
                                            for (Entity nearby : stand.getWorld().getNearbyEntities(stand.getLocation(), 4, 4, 4)) {
                                                if (nearby instanceof Player nearbyPlayer && nearby != p) {
                                                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, false, false));

                                                }
                                            }

                                    } else {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 1L);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (!stand.isDead()) {
                                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                            onlinePlayers.playSound(stand.getLocation(), Sound.BLOCK_WOOL_BREAK, 0.9f, 0.7f);
                                        }
                                    } else {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 5L);

                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    stand.remove();
                                }
                            }, 50);
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
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                ultTP(p);
                                p.playSound(p.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
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

    @EventHandler
    public void noHurtinTheVoid (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (voidthing.contains(p)) {
                e.setCancelled(true);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (p.isSneaking() == true && AbilityExtras.itemcheck(p,itemname)) {
                    e.setDamage(e.getDamage() / 2);
                }
            }
        }
    }

    @EventHandler
    public void noHurtOthersinTheVoid (EntityDamageByEntityEvent e) {
        Entity p =  e.getEntity();
        Entity pd =  e.getDamager();
        if (voidthing.contains(p) || voidthing.contains(pd)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void noPlaceinTheVoid (BlockPlaceEvent e) {
        if (voidthing.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void noBreakinTheVoid (BlockBreakEvent e) {
        if (voidthing.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void noEatinTheVoid (PlayerItemConsumeEvent e) {
        if (voidthing.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    public void ultTP (Player p) {
        Inventory inv = Bukkit.createInventory(p, 27, ChatColor.translateAlternateColorCodes('&', "&7&lUltimate (TP)"));

        ItemStack playerh = new ItemStack(Material.PLAYER_HEAD);

        List<Player> players = new ArrayList<>();

        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if (allPlayers.getAllowFlight() == false) {
                players.add(allPlayers);
            }
        }
        for (int i = 0; i < players.size(); i++) {
            int num = i;
            if (players.get(i) != p) {
                SkullMeta playerhmeta = (SkullMeta) playerh.getItemMeta();
                playerhmeta.setOwningPlayer(players.get(num));
                playerhmeta.setDisplayName(players.get(num).getName());
                playerh.setItemMeta(playerhmeta);
                inv.setItem(num, playerh);
            }
        }
        p.openInventory(inv);

        }

        @EventHandler
    public void menuClick (InventoryClickEvent e) {
            Player p = (Player) e.getWhoClicked();
            ArrayList<String> lore = new ArrayList<>();
            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&7&lUltimate (TP)"))) {
                if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.hidePlayer(plugin, p);
                        voidthing.add(p);
                        onlinePlayers.spawnParticle(Particle.BLOCK_CRACK, p.getLocation().clone().add(0, 1, 0), 15, 0.1, 0.5, 0.1, Material.NETHER_PORTAL.createBlockData());
                        onlinePlayers.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0.5f);
                    }

                    p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 100, 5, true, false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 1, true, false));
                    p.teleport(Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()));
                    p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0.5f);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {

                            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                onlinePlayers.showPlayer(plugin, p);
                                voidthing.remove(p);
                                onlinePlayers.spawnParticle(Particle.BLOCK_CRACK, p.getLocation().clone().add(0, 1, 0), 15, 0.1, 0.5, 0.1, Material.NETHER_PORTAL.createBlockData());
                                onlinePlayers.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0.5f);
                            }
                        }
                    }, 80);
                }
            }
        }
}
