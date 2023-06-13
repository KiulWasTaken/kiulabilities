package kiul.kiulabilities.gamelogic;

import kiul.kiulabilities.Kiulabilities;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ultimatePointsListeners implements Listener {


    public static HashMap<UUID,Integer> requiredUltPoints = new HashMap<>();
    public static HashMap<UUID,Integer> maximumUltPoints = new HashMap<>();
    public static void addUltPoint(Player p){


        if (ultimatePointsConfig.get().get(p.getUniqueId().toString()) == null) {
            ultimatePointsConfig.get().set(p.getUniqueId().toString(),1);
            p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL,1,0.5f);
            CheckUltPoints(p);
        }
        int ultPoints = (Integer) ultimatePointsConfig.get().get(p.getUniqueId().toString());
        if (ultPoints < 6) {
            ultimatePointsConfig.get().set(p.getUniqueId().toString(), ultimatePointsConfig.get().get(p.getUniqueId().toString() + 1));
            ultPoints ++;
            p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL,1,0.5f);
            switch (ultPoints) {
                case 0:
                    switch (requiredUltPoints.get(p.getUniqueId())) {
                        case 1:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 2:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 3:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 4:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 5:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 6:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                    }
                    break;
                case 1:
                    switch (requiredUltPoints.get(p.getUniqueId())) {
                        case 1:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 2:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 3:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 4:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 5:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 6:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                    }
                    break;
                case 2:
                    switch (requiredUltPoints.get(p.getUniqueId())) {
                        case 1:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 2:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 3:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 4:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 5:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 6:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                    }
                    break;
                case 3:
                    switch (requiredUltPoints.get(p.getUniqueId())) {
                        case 1:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 2:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 3:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 4:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 5:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 6:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                    }
                    break;
                case 4:
                    switch (requiredUltPoints.get(p.getUniqueId())) {
                        case 1:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 2:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 3:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 4:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 5:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 6:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                    }
                    break;
                case 5:
                    switch (requiredUltPoints.get(p.getUniqueId())) {
                        case 1:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 2:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 3:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 4:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 5:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 6:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                    }
                    break;
                case 6:
                    switch (requiredUltPoints.get(p.getUniqueId())) {
                        case 1:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 2:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 3:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 4:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 5:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + ChatColor.GOLD + "❑" + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                        case 6:
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                            break;
                    }
                    break;
            }
            ultimatePointsConfig.save();
        }
    }
    public static void useUltPoints (Player p, final int amount) {
        ultimatePointsConfig.save();
        new BukkitRunnable() {
            int repeat = amount;
            int ultPoints = (Integer) ultimatePointsConfig.get().get(p.getUniqueId().toString());
            @Override
            public void run() {
                if (repeat >= 1) {
                    ultimatePointsConfig.get().set(p.getUniqueId().toString(), ultPoints - 1);
                    ultPoints --;
                    ultimatePointsConfig.save();
                    CheckUltPoints(p);
                    p.playSound(p.getLocation(), Sound.ITEM_HONEY_BOTTLE_DRINK,1,2f);
                    repeat--;
                } else {
                    ultimatePointsConfig.save();
                    CheckUltPoints(p);
                    cancel();
                }
            }
        }.runTaskTimer(Kiulabilities.getPlugin(Kiulabilities.class), 0L, 20L);

    }
    public static void CheckUltPoints (Player p) {
        if (ultimatePointsConfig.get().get(p.getUniqueId().toString()) == null) {
            ultimatePointsConfig.get().set(p.getUniqueId().toString(), 0);
        }
        ultimatePointsConfig.save();
        int ultPoints = (Integer) ultimatePointsConfig.get().get(p.getUniqueId().toString());
        switch (ultPoints) {
            case 0:
                switch (requiredUltPoints.get(p.getUniqueId())) {
                    case 1:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 2:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 3:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 4:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 5:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 6:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                }
                break;
            case 1:
                switch (requiredUltPoints.get(p.getUniqueId())) {
                    case 1:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 2:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 3:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 4:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 5:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 6:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                }
                break;
            case 2:
                switch (requiredUltPoints.get(p.getUniqueId())) {
                    case 1:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 2:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 3:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 4:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 5:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 6:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                }
                break;
            case 3:
                switch (requiredUltPoints.get(p.getUniqueId())) {
                    case 1:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 2:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 3:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 4:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 5:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 6:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                }
                break;
            case 4:
                switch (requiredUltPoints.get(p.getUniqueId())) {
                    case 1:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 2:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 3:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 4:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 5:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 6:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                }
                break;
            case 5:
                switch (requiredUltPoints.get(p.getUniqueId())) {
                    case 1:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 2:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 3:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 4:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 5:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 6:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.WHITE + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                }
                break;
            case 6:
                switch (requiredUltPoints.get(p.getUniqueId())) {
                    case 1:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 2:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 3:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 4:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GOLD + "❑" + ChatColor.GOLD + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 5:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + ChatColor.GOLD + "❑" + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                    case 6:
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GRAY  + "[" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.GREEN + "❑" + ChatColor.DARK_GRAY  + "]"));
                        break;
                }
                break;
        }
        ultimatePointsConfig.save();
    }
    public static int getUltPoints (Player p) {
        ultimatePointsConfig.save();
        if (ultimatePointsConfig.get().get(p.getUniqueId().toString()) == null) {
            ultimatePointsConfig.get().set(p.getUniqueId().toString(), 0);
        }
        int ultPoints = (Integer) ultimatePointsConfig.get().get(p.getUniqueId().toString());
        return ultPoints; }

    @EventHandler
    public void onKill (PlayerDeathEvent e) {
        Player killed = e.getEntity();
        Player p = e.getEntity().getKiller();
            if (getUltPoints(p) < maximumUltPoints.get(p.getUniqueId())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Kiulabilities.getPlugin(Kiulabilities.class), new Runnable() {
                    @Override
                    public void run() {
                        addUltPoint(p);
                    }
                }, 10);

        }
    }
}
