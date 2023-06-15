package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;

public class Artificer implements Listener {
    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();
    private final ArrayList<Player> voidthing = new ArrayList<>();

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand().getItemMeta() != null && p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0) == (ChatColor.WHITE + "Right-Click" + ChatColor.GOLD + " » " + ChatColor.GRAY + "Creates a small, non-damaging explosion that boosts the player several blocks"));
            e.setCancelled(true);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        int primaryTimer = 1;
        int secondaryTimer = 1;

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore() != null) {
                if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click" + ChatColor.GOLD + " » " + ChatColor.GRAY + "Creates a small, non-damaging explosion that boosts the player several blocks")) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {
                            e.setCancelled(true);
                            primaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                            // ABILITY CODE START

                            p.sendMessage("check");

                            for (Player ap : Bukkit.getOnlinePlayers()) {
                                ap.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                                ap.spawnParticle(Particle.EXPLOSION_LARGE, p.getLocation(), 1);
                                ap.spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 1);
                                ap.spawnParticle(Particle.SQUID_INK, p.getLocation(), 5, 0.1, 0.1, 0.1, 0.001);
                                ap.spawnParticle(Particle.LAVA, p.getLocation(), 10, 0.1, 0.1, 0.1, 0.001);
                                ap.spawnParticle(Particle.ASH, p.getLocation(), 25, 0.1, 0.1, 0.1, 0.5);
                            }
                            p.setVelocity(p.getLocation().getDirection().multiply(1));
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

                            List<Entity> nearbyEntities = p.getNearbyEntities(5, 5, 5);
                            for (Entity q : nearbyEntities) {
                                q.setVelocity(q.getVelocity().multiply(-1));
                                if (q instanceof Player) {
                                    setVelocity(q, p);
                                }
                            }
                            for (Player ap : Bukkit.getOnlinePlayers()) {
                                ap.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                                ap.spawnParticle(Particle.EXPLOSION_LARGE, p.getLocation(), 1, 0.1, 1, 0.1);
                                ap.spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 1);
                                ap.spawnParticle(Particle.SQUID_INK, p.getLocation(), 5, 0.1, 0.1, 0.1, 0.001);
                                ap.spawnParticle(Particle.LAVA, p.getLocation(), 10, 0.1, 0.1, 0.1, 0.001);
                                ap.spawnParticle(Particle.ASH, p.getLocation(), 25, 0.1, 0.1, 0.1, 0.5);
                            }

                            TNTPrimed boom = p.getWorld().spawn(p.getLocation(), TNTPrimed.class);
                            boom.setMetadata("boom", new FixedMetadataValue(plugin, "uruguay"));
                            boom.setYield(2);
                            boom.setFuseTicks(0);

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
    public void ultCheckActivate (PlayerSwapHandItemsEvent e) {
        // wee woo woo woo
        int ultimateTimer = 10;

        Player p = (Player) e.getPlayer();

        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.WHITE + "Right-Click" + ChatColor.GOLD + " » " + ChatColor.GRAY + "Creates a small, non-damaging explosion that boosts the player several blocks")) {
                e.setCancelled(true);
                if (ultimatePointsListeners.getUltPoints(p) >= ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId())) {
                    if (!ultimateCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (ultimateCooldown.get(p.getUniqueId())).longValue() > ultimateTimer * 1000)) {
                        ultimateCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        ultimatePointsListeners.useUltPoints(p, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                // ULTIMATE CODE HERE
                                for (Player ap : Bukkit.getOnlinePlayers()) {
                                    if (ap != p);
                                    {
                                        ap.getWorld().spawnEntity(ap.getLocation().add(2, 7, 2), EntityType.PRIMED_TNT);
                                        ap.getWorld().spawnEntity(ap.getLocation().add(-2, 7, -2), EntityType.PRIMED_TNT);
                                        ap.getWorld().spawnEntity(ap.getLocation().add(2, 7, -2), EntityType.PRIMED_TNT);
                                    }
                                }
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
    public void passiveAbility (PlayerItemConsumeEvent e){

        Player p = e.getPlayer();

        if (e.getItem().getType() == Material.GOLDEN_APPLE){
            p.getInventory().addItem(new ItemStack(Material.TNT));
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_CHIME,1,1);
        }


    }
    private void setVelocity(Entity toPush, Player aura) {
        Location pushTo = aura.getLocation().subtract(toPush.getLocation());
        org.bukkit.util.Vector pushVector = new Vector(pushTo.toVector().normalize().multiply(-0.8).getX(), 0.5, pushTo.toVector().normalize().multiply(-0.8).getZ());
        toPush.setVelocity(pushVector);
    }

    @EventHandler
    public void noDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("boom")) {
            e.setCancelled(true);
        }
    }
}
