package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Tracker implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();


    @EventHandler
    public void onClick(PlayerInteractEvent e) {


        Player p = e.getPlayer();
        int primaryTimer = 5;
        int secondaryTimer = 5;

        if (p.getInventory().getItemInMainHand().getItemMeta().getLore() != null) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Summon a wolf that hunts down and stuns the nearest player")) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {
                        e.setCancelled(true);
                        primaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        // ABILITY CODE START

                        Wolf roger = (Wolf) p.getWorld().spawnEntity(p.getLocation().add(0,1,0),EntityType.WOLF);
                        roger.setAngry(true);
                        roger.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,99999,2,false,false));
                        roger.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6);
                        roger.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(100);
                        Location user = p.getLocation();
                        ArrayList<Double> nearbyPlayers = new ArrayList<>();
                        HashMap<Double,Player> players = new HashMap<>();


                        for (Player nearby : Bukkit.getOnlinePlayers()){
                            if (nearby != p) {
                                nearbyPlayers.add(user.distance(nearby.getLocation()));
                                players.put(user.distance(nearby.getLocation()), nearby);
                            }
                        }

                        Arrays.sort(nearbyPlayers.toArray());
                        roger.setTarget(players.get(nearbyPlayers.get(0)));
                        roger.setVelocity((players.get(nearbyPlayers.get(0)).getLocation().add(0,1,0).toVector().subtract(roger.getLocation().toVector())).normalize().multiply(0.8));
                        roger.setCustomName(p.getDisplayName() + "'s Bloodhound Roger");

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                 if (!roger.isDead()) {
                                     roger.remove();
                                 }
                            }
                        }, 200);

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

                        for (Block nearbyBlocks : getBlocks(p.getLocation().getBlock(),2)) {
                            if (nearbyBlocks.getType() == Material.GRASS) {
                                nearbyBlocks.setType(Material.SWEET_BERRY_BUSH);
                                BlockState state = nearbyBlocks.getState();
                                Ageable ageable = (Ageable) state.getBlockData();
                                ageable.setAge(2);
                                state.setBlockData(ageable);
                                state.update();



                            }
                        }
                        Location center = p.getLocation().add(0,1,0); // replace world, x, y, z with your desired values
                        double radius = 2;

                        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 16) {
                            double x = radius * Math.cos(theta);
                            double z = radius * Math.sin(theta);
                            Location loc = center.clone().add(x, 0, z);
                            loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 1);
                        }
                        p.getLocation().getWorld().playSound(p.getLocation(),Sound.ITEM_BONE_MEAL_USE,1,1);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                for (Block nearbyBlocks : getBlocks(p.getLocation().getBlock(),3)) {
                                    if (nearbyBlocks.getType() == Material.GRASS) {
                                        nearbyBlocks.setType(Material.SWEET_BERRY_BUSH);
                                        BlockState state = nearbyBlocks.getState();
                                        Ageable ageable = (Ageable) state.getBlockData();
                                        ageable.setAge(2);
                                        state.setBlockData(ageable);
                                        state.update();



                                    }
                                }
                                Location center = p.getLocation().add(0,0.5,0); // replace world, x, y, z with your desired values
                                double radius = 3;

                                for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 16) {
                                    double x = radius * Math.cos(theta);
                                    double z = radius * Math.sin(theta);
                                    Location loc = center.clone().add(x, 0, z);
                                    loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 1);
                                }
                                p.getLocation().getWorld().playSound(p.getLocation(),Sound.ITEM_BONE_MEAL_USE,1,1);
                            }
                        },  10);



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
    public void ultCheckActivate(PlayerSwapHandItemsEvent e) {

        int ultimateTimer = 10;

        Player p = (Player) e.getPlayer();
        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Summon a wolf that hunts down and stuns the nearest player")) {
                e.setCancelled(true);
                if (ultimatePointsListeners.getUltPoints(p) >= ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId())) {
                    if (!ultimateCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (ultimateCooldown.get(p.getUniqueId())).longValue() > ultimateTimer * 1000)) {
                        ultimateCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        ultimatePointsListeners.useUltPoints(p, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                // ULT CODE
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
    public void noDropItem(PlayerDropItemEvent e) {

        Player p = (Player) e.getPlayer();
        if (e.getItemDrop().getItemStack().getType() == Material.SWEET_BERRIES) {
            e.setCancelled(true);
        }


    }


    @EventHandler
    public void dontEatIt(PlayerItemConsumeEvent e) {

        Player p = (Player) e.getPlayer();
        if (e.getItem().getType() == Material.SWEET_BERRIES) {
            e.setCancelled(true);
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
            if (roger.hasMetadata("ROGER")) {
                p.getWorld().createExplosion(roger.getLocation(),1,false);
                roger.remove();
            }
        }

    }
}
