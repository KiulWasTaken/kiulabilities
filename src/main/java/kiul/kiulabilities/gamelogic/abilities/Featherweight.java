package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Featherweight implements Listener {
    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();
    private final ArrayList<Player> voidthing = new ArrayList<>();


    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        int primaryTimer = 5;
        int secondaryTimer = 5;

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore() != null) {
                if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Fly up into the sky")) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {
                            e.setCancelled(true);
                            primaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                            /** ABILITY CODE START**/
                            p.setVelocity(new Vector(0, 1, 0));
                            for (Player ap : Bukkit.getOnlinePlayers()) {
                                ap.spawnParticle(Particle.SPIT, p.getLocation(), 10);
                                ap.spawnParticle(Particle.CLOUD, p.getLocation(), 10);
                            }
                            /**ABILITY CODE END**/
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {

                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.9f);
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_AQUA + "Primary Ability Charged!"));
                                }
                            }, primaryTimer * 20);

                        } else {
                            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.DARK_AQUA + " Primary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.DARK_AQUA + ChatColor.ITALIC + (primaryTimer * 1000 - (System.currentTimeMillis() - ((Long) primaryCooldown.get(p.getUniqueId())).longValue())) + "ms!");
                        }
                    } else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {
                            e.setCancelled(true);
                            secondaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                            // ABILITY CODE START
                            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                            stand.setVisible(false);

                            for (Entity ne : stand.getNearbyEntities(7, 10, 7)) {
                                double distance = getDistanceToCenter(ne.getLocation().getX(), ne.getLocation().getY(), stand.getLocation().getX(), stand.getLocation().getY());
                                if (ne != p) {
                                    ne.setVelocity(ne.getLocation().toVector().subtract(stand.getLocation().toVector()).normalize().multiply(-distance).add(new Vector(0, 0.15, 0)));

                                }
                            }

                            //ABILITY CODE END
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {

                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.8f);
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.LIGHT_PURPLE + "Secondary Ability Charged!"));
                                }
                            }, secondaryTimer * 20);


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

        int ultimateTimer = 10;

        Player p = (Player) e.getPlayer();
        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Fly up into the sky")) {
                e.setCancelled(true);
                if (ultimatePointsListeners.getUltPoints(p) >= ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId())) {
                    if (!ultimateCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (ultimateCooldown.get(p.getUniqueId())).longValue() > ultimateTimer * 1000)) {
                        ultimateCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        ultimatePointsListeners.useUltPoints(p, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()));

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                // ULTIMATE CODE HERE
                                ItemStack item = new ItemStack(Material.ELYTRA); // Replace with your desired item type

                                ItemMeta itemMeta = item.getItemMeta();
                                AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
                                itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
                                itemMeta.setUnbreakable(true);
                                item.setItemMeta(itemMeta);
                                p.getInventory().setChestplate(item);
                                //ULTIMATE CODE END
                            }
                        }, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()) * 20);

                    } else {
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.RED + " Ultimate ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.RED + ChatColor.ITALIC + (ultimateTimer * 1000 - (System.currentTimeMillis() - ((Long) ultimateCooldown.get(p.getUniqueId())).longValue())) + "ms!");
                    }
                } else {
                    ultimatePointsListeners.CheckUltPoints(p);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        new BukkitRunnable() {
            public void run() {
                if (p.isSneaking() == true) {
                    if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Fly up into the sky")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10, 1, false, false));
                    } else {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin,0L,10L);
        new BukkitRunnable() {
            public void run() {

                if (p.isSneaking() == true && p.getLocation().add(0, -1, 0).clone().getBlock().getType() == Material.AIR) {

                        Location center = p.getLocation().add(0, -2, 0); // replace world, x, y, z with your desired values
                        double radius = 0.5;
                        double height = 0.1; // controls the height of the spiral
                        int totalParticles = 100; // adjust the total number of particles

                        for (int i = 0; i < totalParticles; i++) {
                            double angle = 0.1 * i;
                            double x = (radius + height * angle) * Math.cos(angle);
                            double y = height * angle;
                            double z = (radius + height * angle) * Math.sin(angle);
                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1); // Example particle type and color
                            Location loc = center.clone().add(x, y, z);
                            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0, dustOptions);
                        }
                    } else {
                        cancel();
                    }
                }
        }.runTaskTimer(plugin,0L,3L);
    }

    private double getDistanceToCenter(double x, double z, double CENTER_X, double CENTER_Z) {
        double deltaX = x - CENTER_X;
        double deltaZ = z - CENTER_Z;
        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }
    }
