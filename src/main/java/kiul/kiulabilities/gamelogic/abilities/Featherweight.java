package kiul.kiulabilities.gamelogic.abilities;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.*;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
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

public class Featherweight implements Listener {
    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);
    private final HashMap<UUID, Long> primaryCooldown = new HashMap<>();
    private final HashMap<UUID, Long> secondaryCooldown = new HashMap<>();

    private final HashMap<UUID, Long> ultimateCooldown = new HashMap<>();
    private final ArrayList<Player> voidthing = new ArrayList<>();

    String configname = AbilityItemNames.FEATHERWEIGHT.name();

    private int primaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Primary");
    private int secondaryTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Secondary");
    private int ultimateTimer = plugin.getConfig().getInt("Abilities." + configname + ".Cooldowns.Ultimate");

    String itemname = ChatColor.stripColor(ColoredText.translateHexCodes(AbilityItemNames.FEATHERWEIGHT.getDisplayName()));

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getLore() != null) {
                if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (!primaryCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (primaryCooldown.get(p.getUniqueId())).longValue() > primaryTimer * 1000)) {
                            e.setCancelled(true);

                            /** ABILITY CODE START**/

                            p.setVelocity(new Vector(0, 1, 0));

                            p.getWorld().spawnParticle(Particle.SPIT, p.getLocation().add(0,0.5,0), 10, 0, 0, 0, 0.1);
                            p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation().add(0,0.5,0), 10, 0, 0, 0, 0.2);

                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.3F, 1);

                            List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();

                            if (ChatColor.stripColor(lore.get(lore.size()-1)).equalsIgnoreCase("Elytra-Status » " + "ACTIVATED")) {

                                ItemStack chestplate = p.getInventory().getChestplate();

                                ItemStack item = new ItemStack(Material.ELYTRA);

                                ItemMeta itemMeta = item.getItemMeta();
                                AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                //AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
                                itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
                                //itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
                                itemMeta.setUnbreakable(true);
                                GlintEnchantment glow = new GlintEnchantment(new NamespacedKey(plugin, "glow"));
                                itemMeta.addEnchant(glow, 1, true);
                                itemMeta.setDisplayName(itemname);
                                item.setItemMeta(itemMeta);
                                p.getInventory().setChestplate(item);

                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {

                                        p.setGliding(true);

                                    }
                                }, 10);

                                double yaw = Math.toRadians(p.getLocation().getYaw());

                                double xOffset = -2 * Math.sin(yaw);
                                double zOffset = 2 * Math.cos(yaw);

                                Vector vec = p.getLocation().add(xOffset, 0, zOffset).toVector().subtract(p.getLocation().toVector()).normalize();

                                p.setVelocity(p.getVelocity().add(vec.multiply(0.5)).add(new Vector(0,0.3,0)));

                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {

                                        p.getInventory().remove(item);
                                        p.getInventory().setChestplate(chestplate);

                                    }
                                }, 120);

                            }

                            /**ABILITY CODE END**/

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

                            /** SECONDARY CODE START */

                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 0.3F, 1);

                            p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation().add(0,1,0), 10, 0, 0, 0, 0.1);

                            for (int i = 0; i <= 15; i++) {
                                double playeryaw = p.getLocation().getYaw();
                                if (playeryaw > 180) {
                                    playeryaw = -playeryaw;

                                }
                                double yaw = Math.toRadians(playeryaw + i * 22.5); // Convert yaw to radians

                                double xOffset = -2 * Math.sin(yaw);
                                double zOffset = 2 * Math.cos(yaw);

                                Vector vec = p.getLocation().add(xOffset, 0, zOffset).toVector().subtract(p.getLocation().toVector()).normalize();

                                ShulkerBullet shulkerBullet = (ShulkerBullet) p.getWorld().spawnEntity(p.getEyeLocation().add(0,-0.5,0).add(vec.multiply(2)), EntityType.SHULKER_BULLET);

                                shulkerBullet.setMetadata("shulkerbullet", new FixedMetadataValue(plugin, "pat"));
                                shulkerBullet.setVelocity(vec.multiply(0.5).add(new Vector(0,0.1,0)));

                                if (i == 15) {
                                    break;
                                }
                            }

                            /** SECONDARY CODE END */

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
                            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "»" + ChatColor.GRAY + "]" + ChatColor.LIGHT_PURPLE + " Secondary ability " + ChatColor.GRAY + "is on cooldown for another " + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + timer + "s!");                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void ultCheckActivate(PlayerSwapHandItemsEvent e) {

        Player p = (Player) e.getPlayer();
        if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(itemname)) {
                e.setCancelled(true);
                if (ultimatePointsListeners.getUltPoints(p) >= ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId())) {
                    if (!ultimateCooldown.containsKey(p.getUniqueId()) || (System.currentTimeMillis() - (ultimateCooldown.get(p.getUniqueId())).longValue() > ultimateTimer * 1000)) {
                        ultimateCooldown.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                        ultimatePointsListeners.useUltPoints(p, ultimatePointsListeners.requiredUltPoints.get(p.getUniqueId()));

                        List<String> lore1 = p.getInventory().getItemInMainHand().getItemMeta().getLore();
                        ItemMeta itemMeta = p.getInventory().getItemInMainHand().getItemMeta();
                        ItemStack item = p.getInventory().getItemInMainHand();

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {

                                /** ULTIMATE CODE HERE */

                                if (ChatColor.stripColor(lore1.get(lore1.size()-1)).equalsIgnoreCase("Elytra-Status » " + "ACTIVATED")) {

                                    ultimatePointsListeners.maximumUltPoints.put(p.getUniqueId(),6);
                                    ultimatePointsListeners.requiredUltPoints.put(p.getUniqueId(),2);

                                    for (Entity entity : p.getWorld().getEntities()) {
                                        if (entity != p) {
                                            if (entity.getType() != EntityType.ARMOR_STAND && entity.getType() != EntityType.DROPPED_ITEM && entity.getType() != EntityType.SHULKER_BULLET) {

                                                entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 0.5F, 1);

                                                ShulkerBullet shulkerBullet = (ShulkerBullet) p.getWorld().spawnEntity(entity.getLocation().add(0, 5, 0), EntityType.SHULKER_BULLET);
                                                shulkerBullet.setTarget(entity);
                                                shulkerBullet.setVelocity(new Vector(0,0.3,0));

                                                ShulkerBullet shulkerBullet1 = (ShulkerBullet) p.getWorld().spawnEntity(entity.getLocation().add(0, 5, 0), EntityType.SHULKER_BULLET);
                                                shulkerBullet1.setTarget(entity);
                                                shulkerBullet1.setVelocity(new Vector(0,0.3,0));

                                                ShulkerBullet shulkerBullet2 = (ShulkerBullet) p.getWorld().spawnEntity(entity.getLocation().add(0, 5, 0), EntityType.SHULKER_BULLET);
                                                shulkerBullet2.setTarget(entity);
                                                shulkerBullet2.setVelocity(new Vector(0,0.3,0));

                                            }
                                        }
                                    }

                                } else {

                                    ultimatePointsListeners.maximumUltPoints.put(p.getUniqueId(),6);
                                    ultimatePointsListeners.requiredUltPoints.put(p.getUniqueId(),2);

                                    List<String> lore = new ArrayList<>();

                                    for (String str : itemMeta.getLore()) {
                                        lore.add(str);
                                    }

                                    lore.remove(lore.size() - 1);

                                    lore.add(ColoredText.translateHexCodes("&#919090&lElytra-Status &6» " + "&a&lACTIVATED"));

                                    itemMeta.setLore(lore);

                                    item.setItemMeta(itemMeta);
                                }

                                /** ULTIMATE CODE END */

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
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        new BukkitRunnable() {
            public void run() {
                if (p.isSneaking() == true && AbilityExtras.itemcheck(p, itemname) == true) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10, 1, false, false));
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 15L);
        new BukkitRunnable() {
            public void run() {

                if (p.isSneaking() == true && p.getLocation().add(0, -1, 0).clone().getBlock().getType() == Material.AIR && AbilityExtras.itemcheck(p, itemname) == true) {

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
        }.runTaskTimer(plugin, 0L, 3L);
    }

    private double getDistanceToCenter(double x, double z, double CENTER_X, double CENTER_Z) {
        double deltaX = x - CENTER_X;
        double deltaZ = z - CENTER_Z;
        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }

    @EventHandler
    public void onshulkerhitplayer(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof ShulkerBullet shulkerBullet) {
            Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(122, 0, 122), Color.fromRGB(0, 0, 0), 3.0F);
            shulkerBullet.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, e.getEntity().getLocation(), 5, 0.5, 0.5, 0.5, dustTransition);
            if (shulkerBullet.hasMetadata("shulkerbullet")) {
                if (e.getEntity() instanceof Player p) {
                    e.setCancelled(true);
                    shulkerBullet.remove();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 1, true, false));
                }
            }
        }

    }
}
