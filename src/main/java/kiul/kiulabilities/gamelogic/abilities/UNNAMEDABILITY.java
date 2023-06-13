package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class UNNAMEDABILITY implements Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private int primaryTimer = 5;
    private int secondaryTimer = 5;

    @EventHandler
    public void onClick(PlayerInteractEvent e) {


        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(AbilityItemNames.UNNAMEDABILITY)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {

                        e.setCancelled(true);
                        if (secondaryCooldown.isEmpty()) {
                            secondaryCooldown.put(p.getUniqueId(), (long) 0);
                        }
                        primaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

                        if (!Kiulabilities.ABILITYUSED.contains(p.getUniqueId())) {

                            Kiulabilities.ABILITYUSED.add(p.getUniqueId());

                            TimerTask(p);

                        }

                        //ABILITY CODE: {

                        Vector direction = p.getEyeLocation().getDirection().multiply(2);

                        AtomicInteger DashAmount = new AtomicInteger(5);
                        int Delay = 100;

                        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                        Runnable task = () -> {
                            p.setVelocity(direction);
                            DashAmount.getAndDecrement();
                            if (DashAmount.get() <= 0) {
                                executor.shutdown();
                            }
                        };

                        executor.scheduleAtFixedRate(task, 0, Delay, TimeUnit.MILLISECONDS);

                        // }

                    } else {
                        p.sendMessage("primary no");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();

        if (itemcheck(p) == true) {

            if (p.getGameMode() == GameMode.SURVIVAL) {
                e.setCancelled(true);

                if (!secondaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (secondaryCooldown.get(p.getUniqueId())).longValue() > secondaryTimer * 1000)) {

                    if (primaryCooldown.isEmpty()) {
                        primaryCooldown.put(p.getUniqueId(), (long) 0);
                    }
                    secondaryCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

                    if (!Kiulabilities.ABILITYUSED.contains(p.getUniqueId())) {

                        Kiulabilities.ABILITYUSED.add(p.getUniqueId());

                        TimerTask(p);

                    }

                    p.setVelocity(new Vector(0, 0.6, 0).add(p.getEyeLocation().getDirection().multiply(1.2)));

                    for (Player player : Bukkit.getOnlinePlayers()) {

                        player.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.1F, 1);
                        player.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.1F, 0.5F);
                        player.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.1F, 1.5F);

                        player.spawnParticle(Particle.CLOUD, p.getLocation().add(0, -1, 0), 20, 1, 0, 1, -0.005);

                    }

                    p.setAllowFlight(false);
                    p.setFlying(false);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.setAllowFlight(true);
                        }
                    }.runTaskLater(plugin, secondaryTimer * 20);

                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerDropItemEvent e) {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(AbilityItemNames.UNNAMEDABILITY)) {

                e.setCancelled(true);

            }
        }

    }

    @EventHandler
    public void onEntityDamage(PlayerMoveEvent event) {



    }

    public void TimerTask (Player p) {

        new BukkitRunnable() {
            public void run() {

                double nnum = ((double) (primaryTimer * 1000 - (System.currentTimeMillis() - ((Long) primaryCooldown.get(p.getUniqueId())).longValue())));
                double nnum1 = 20 * (nnum / (primaryTimer * 1000));
                int nnum2 = 20 - (int) nnum1;

                String str1 = primary(nnum2);

                double num = ((double) (secondaryTimer * 1000 - (System.currentTimeMillis() - ((Long) secondaryCooldown.get(p.getUniqueId())).longValue())));
                double num1 = 20 * (num / (secondaryTimer * 1000));
                int num2 = 20 - (int) num1;

                String str2 = secondary(num2);

                TimerActionBar(p, str1, str2);

            }
        }.runTaskTimer(plugin, 0L, 3L);

    }

    public String primary (Integer nnum2) {

        if (nnum2 >= 0 && nnum2 <= 20) {
            String secondary = ChatColor.translateAlternateColorCodes('&', "&a" + String.valueOf("|").repeat(nnum2) + "&c" + String.valueOf("|").repeat(20 - nnum2));

            return secondary;
        } else {
            String secondary = ChatColor.translateAlternateColorCodes('&', "&6" + String.valueOf("|").repeat(20));

            return secondary;
        }
    }

    public String secondary (Integer num2) {

        if (num2 >= 0 && num2 <= 20) {
            String secondary = ChatColor.translateAlternateColorCodes('&', "&a" + String.valueOf("|").repeat(num2) + "&c" + String.valueOf("|").repeat(20 - num2));

            return secondary;
        } else {
            String secondary = ChatColor.translateAlternateColorCodes('&', "&6" + String.valueOf("|").repeat(20));

            return secondary;
        }
    }

    public boolean itemcheck (Player p) {

        p.sendMessage("check");

        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null && is.hasItemMeta()) {
                if (ChatColor.stripColor(is.getItemMeta().getDisplayName()).equalsIgnoreCase(AbilityItemNames.UNNAMEDABILITY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void TimerActionBar (Player p, String primary, String secondary) {

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                "|" + primary + "&f| &6« &8[&2&lP&8] &6- &8[&3&lS&8] &6» &f|" + secondary + "&f|")));

    }
}

