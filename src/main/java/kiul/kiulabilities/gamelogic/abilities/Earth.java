package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Earth implements Listener {
    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();
    private final ArrayList<Player> voidthing = new ArrayList<>();
    public static ArrayList<Player> primaryTrigger = new ArrayList<>();


    @EventHandler
    public void onClick(PlayerInteractEvent e) {


        Player p = e.getPlayer();
        int primaryTimer = 20;
        int secondaryTimer = 25;

        if (p.getInventory().getItemInMainHand().getItemMeta().getLore() != null) {
            p.sendMessage("pre-test");
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + " » " + ChatColor.GRAY + "Launches the player into the air, creating a damaging crater when landing and negates fall damage")) {
                p.sendMessage("test 1");
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {
                        e.setCancelled(true);
                        primaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        p.sendMessage("test 2");
                        // ABILITY CODE START
                        if (p.isOnGround()) {
                            p.sendMessage("test 3");
                            primaryTrigger.add(p);
                            p.setVelocity(new Vector(0, 1, 0));
                            p.spawnParticle(Particle.BLOCK_DUST, p.getLocation(), 5);
                        }

                        //ABILITY CODE END
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
                        Location origin = p.getEyeLocation();
                        Vector direction = origin.getDirection();
                        Location centerLocation = origin.clone().add(direction);
                        Location rotated = origin.clone();
                        rotated.setPitch(0);
                        rotated.setYaw(origin.getYaw() - 90);
                        Vector rotation = rotated.getDirection();
                        Location blockLocation = centerLocation.clone().add(rotation).subtract(0,3/2,0);
                        rotation.multiply(-1);
                        int initialX = blockLocation.getBlockX();
                        int initialZ = blockLocation.getBlockZ();
                        for (int y = 0; y < 3; y++) {
                            for (int i = 0; i < 3; i++) {
                                Block block = blockLocation.getBlock();

                                block.setType(Material.DIRT);

                                blockLocation.add(rotation);
                            }

                            blockLocation.add(0, 1, 0); // Increase the height by 1
                            blockLocation.setX(initialX);
                            blockLocation.setZ(initialZ);
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

    @EventHandler
    public void ultCheckActivate (PlayerSwapHandItemsEvent e) {

        int ultimateTimer = 10;

        Player p = (Player) e.getPlayer();
        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Launches the player into the air, creating a damaging crater when landing and negates fall damage")) {
                e.setCancelled(true);
                if (ultimatePointsListeners.getUltPoints(p) >= ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId())) {
                    if (!ultimateCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (ultimateCooldown.get(p.getUniqueId())).longValue() > ultimateTimer * 1000)) {
                        ultimateCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        ultimatePointsListeners.useUltPoints(p, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                // ULTIMATE CODE HERE

                                // ULTIMATE CODE END HERE
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
    } @EventHandler
    public void noDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("rock") && primaryTrigger.contains(e.getEntity())) {
            e.setCancelled(true);
        } else {
            e.setDamage(6);
        }
    }
    @EventHandler
    public void primaryStart (EntityDamageEvent e) {
        Player p = (Player) e.getEntity();
        if (primaryTrigger.contains(p) && e.getCause() == EntityDamageEvent.DamageCause.FALL){
            e.setDamage(0);
            TNTPrimed rock = (TNTPrimed) p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
            rock.setMetadata("rock",new FixedMetadataValue(plugin,"beans"));
            rock.setYield(2);
            rock.setFuseTicks(0);


            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {

                    primaryTrigger.remove(p);
                }
            }, 20);

        }
    }
}

