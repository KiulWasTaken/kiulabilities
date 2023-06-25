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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.HashMap;
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

    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);

                        /** PRIMARY - CODE START >> */


                        Location inFront = p.getLocation().add(p.getLocation().getDirection().normalize().multiply(2));
                        inFront.setY(p.getLocation().add(0,-1,0).getY());
                        Block spreadCenter = inFront.getBlock();
                        spread(spreadCenter,p);

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
            spread(e.getEntity().getLocation().add(0,-1,0).getBlock(),(Player) evokerFangs.getOwner());
            StatusEffects.root((Player) e.getEntity(),1);
        }
    }


    public void spread (Block spreadCenter,Player p) {
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



