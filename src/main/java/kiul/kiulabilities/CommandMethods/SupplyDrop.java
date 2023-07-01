package kiul.kiulabilities.CommandMethods;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.ColoredText;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SupplyDrop implements Listener {

    public static Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    public static void SpawnLootCrate (Player p) {

        Location loc = getrandomlocation(p);

        Material mat = getrandomCOLOR(p);

        Chicken chicken = (Chicken) p.getWorld().spawnEntity(loc, EntityType.CHICKEN);
        chicken.setCollidable(false);
        chicken.setInvulnerable(true);
        chicken.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,10000000,100, false, false));
        chicken.setInvisible(true);
        chicken.setSilent(true);

        Rabbit chicken1 = (Rabbit) p.getWorld().spawnEntity(loc, EntityType.RABBIT);
        chicken1.setCollidable(false);
        chicken1.setInvulnerable(true);
        chicken1.setInvisible(true);
        chicken1.setSilent(true);
        chicken1.setGravity(false);
        chicken1.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, false, false));

        Rabbit chicken2 = (Rabbit) p.getWorld().spawnEntity(loc, EntityType.RABBIT);
        chicken2.setCollidable(false);
        chicken2.setInvulnerable(true);
        chicken2.setInvisible(true);
        chicken2.setSilent(true);
        chicken2.setGravity(false);
        chicken2.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, false, false));

        Rabbit chicken3 = (Rabbit) p.getWorld().spawnEntity(loc, EntityType.RABBIT);
        chicken3.setCollidable(false);
        chicken3.setInvulnerable(true);
        chicken3.setInvisible(true);
        chicken3.setSilent(true);
        chicken3.setGravity(false);
        chicken3.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, false, false));

        Rabbit chicken4 = (Rabbit) p.getWorld().spawnEntity(loc, EntityType.RABBIT);
        chicken4.setCollidable(false);
        chicken4.setInvulnerable(true);
        chicken4.setInvisible(true);
        chicken4.setSilent(true);
        chicken4.setGravity(false);
        chicken4.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, false, false));



        ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setMarker(true);
        armorStand.setInvisible(true);
        armorStand.setCollidable(false);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setHelmet(new ItemStack(Material.BARREL));
        armorStand.setMetadata("lootdrop", new FixedMetadataValue(plugin, "pat"));

        ArmorStand armorStand1 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand1.setMarker(true);
        armorStand1.setInvisible(true);
        armorStand1.setCollidable(false);
        armorStand1.setInvulnerable(true);
        armorStand1.setGravity(false);
        armorStand1.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)));
        armorStand1.setHelmet(new ItemStack(mat));

        ArmorStand armorStand2 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand2.setMarker(true);
        armorStand2.setInvisible(true);
        armorStand2.setCollidable(false);
        armorStand2.setInvulnerable(true);
        armorStand2.setGravity(false);
        armorStand2.setHeadPose(new EulerAngle(Math.toRadians(12), Math.toRadians(0), Math.toRadians(0)));
        armorStand2.setHelmet(new ItemStack(mat));

        ArmorStand armorStand3 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand3.setMarker(true);
        armorStand3.setInvisible(true);
        armorStand3.setCollidable(false);
        armorStand3.setInvulnerable(true);
        armorStand3.setGravity(false);
        armorStand3.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(12)));
        armorStand3.setHelmet(new ItemStack(mat));

        ArmorStand armorStand4 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand4.setMarker(true);
        armorStand4.setInvisible(true);
        armorStand4.setCollidable(false);
        armorStand4.setInvulnerable(true);
        armorStand4.setGravity(false);
        armorStand4.setHeadPose(new EulerAngle(Math.toRadians(348), Math.toRadians(0), Math.toRadians(0)));
        armorStand4.setHelmet(new ItemStack(mat));

        ArmorStand armorStand5 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand5.setMarker(true);
        armorStand5.setInvisible(true);
        armorStand5.setCollidable(false);
        armorStand5.setInvulnerable(true);
        armorStand5.setGravity(false);
        armorStand5.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(348)));
        armorStand5.setHelmet(new ItemStack(mat));

        ArmorStand armorStand6 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand6.setMarker(true);
        armorStand6.setInvisible(true);
        armorStand6.setCollidable(false);
        armorStand6.setInvulnerable(true);
        armorStand6.setGravity(false);
        armorStand6.setHeadPose(new EulerAngle(Math.toRadians(12), Math.toRadians(0), Math.toRadians(12)));
        armorStand6.setHelmet(new ItemStack(mat));

        ArmorStand armorStand7 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand7.setMarker(true);
        armorStand7.setInvisible(true);
        armorStand7.setCollidable(false);
        armorStand7.setInvulnerable(true);
        armorStand7.setGravity(false);
        armorStand7.setHeadPose(new EulerAngle(Math.toRadians(348), Math.toRadians(0), Math.toRadians(12)));
        armorStand7.setHelmet(new ItemStack(mat));

        ArmorStand armorStand8 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand8.setMarker(true);
        armorStand8.setInvisible(true);
        armorStand8.setCollidable(false);
        armorStand8.setInvulnerable(true);
        armorStand8.setGravity(false);
        armorStand8.setHeadPose(new EulerAngle(Math.toRadians(348), Math.toRadians(0), Math.toRadians(348)));
        armorStand8.setHelmet(new ItemStack(mat));

        ArmorStand armorStand9 = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand9.setMarker(true);
        armorStand9.setInvisible(true);
        armorStand9.setCollidable(false);
        armorStand9.setInvulnerable(true);
        armorStand9.setGravity(false);
        armorStand9.setHeadPose(new EulerAngle(Math.toRadians(12), Math.toRadians(0), Math.toRadians(348)));
        armorStand9.setHelmet(new ItemStack(mat));

        chicken1.setLeashHolder(chicken);
        chicken2.setLeashHolder(chicken);
        chicken3.setLeashHolder(chicken);
        chicken4.setLeashHolder(chicken);

        for (Player player : Bukkit.getOnlinePlayers()) {
            double yaw = Math.atan2(armorStand.getLocation().getX() - player.getLocation().getX(), armorStand.getLocation().getZ() - player.getLocation().getZ()) * 180 / Math.PI;

            if (yaw > 0) {
                yaw = -yaw;
            } else {
                yaw = Math.abs(yaw);
            }

            player.sendMessage(ColoredText.translateHexCodes(
                    "&4&l<&#fbf451&lC&#fbbe3f&lr&#fc882d&la&#fc511b&lt&#fd1b09&le&#fd1b09&l-&#fc511b&lD&#fc882d&lr&#fbbe3f&lo&#fbf451&lp&4&l> " +
             "&#6da87dA suppy-crate has spawned! &8&l[&#5aa852" + getYawCardinalPoint((float) yaw) + "&8&l]"));

        }

        new BukkitRunnable() {
            public void run() {

                Location loc1 = chicken.getLocation();
                Location loc2 = chicken.getLocation();
                loc1.setYaw(-90);
                loc2.setYaw(90);

                armorStand1.teleport(loc1.clone().add(0,0.861,0));

                armorStand2.teleport(loc1.clone().add(0.6,0.8,0));
                armorStand3.teleport(loc1.clone().add(0,0.8,-0.6));
                armorStand4.teleport(loc1.clone().add(-0.6,0.8,0));
                armorStand5.teleport(loc1.clone().add(0,0.8,0.6));

                armorStand6.teleport(loc1.clone().add(0.6,0.738,-0.6));
                armorStand7.teleport(loc1.clone().add(-0.6,0.738,-0.6));
                armorStand8.teleport(loc1.clone().add(-0.6,0.738,0.6));
                armorStand9.teleport(loc1.clone().add(0.6,0.738,0.6));

                Location as6 = armorStand6.getLocation();
                as6.setYaw(-135);
                chicken1.teleport(as6.clone().add(0,1.2,0));
                Location as7 = armorStand7.getLocation();
                as7.setYaw(135);
                chicken2.teleport(as7.clone().add(0,1.2,0));
                Location as8 = armorStand8.getLocation();
                as8.setYaw(35);
                chicken3.teleport(as8.clone().add(0,1.2,0));
                Location as9 = armorStand9.getLocation();
                as9.setYaw(-35);
                chicken4.teleport(as9.clone().add(0,1.2,0));

                armorStand.teleport(loc1.clone().add(0,-1.35,0));

                armorStand.getWorld().spawnParticle(Particle.CLOUD,armorStand.getLocation(),1,2,0.5,2, 0);

                if (chicken.getVelocity().getY() < -0.0784 && chicken.getVelocity().getY() > -0.0785 || chicken.getLocation().getBlock().getType() == Material.WATER || chicken.getLocation().getBlock().getType() == Material.LAVA || chicken.getLocation().getBlock().getType() == Material.COBWEB) {
                    cancel();

                    armorStand1.remove();
                    armorStand2.remove();
                    armorStand3.remove();
                    armorStand4.remove();
                    armorStand5.remove();
                    armorStand6.remove();
                    armorStand7.remove();
                    armorStand8.remove();
                    armorStand9.remove();

                    chicken.remove();
                    chicken1.remove();
                    chicken2.remove();
                    chicken3.remove();
                    chicken4.remove();

                    p.getWorld().spawn(armorStand.getLocation().clone().add(0,1,0), PolarBear.class, (polarBear) -> {
                    polarBear.setInvisible(true);
                    polarBear.setCollidable(false);
                    polarBear.setInvulnerable(true);
                    polarBear.setGravity(false);
                    polarBear.setAI(false);
                    polarBear.setSilent(true);
                    polarBear.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,10000000,100, false, false));
                    polarBear.setMetadata("lootdrop", new FixedMetadataValue(plugin, "pat"));
                    });

                    armorStand.getWorld().spawnParticle(Particle.CLOUD,armorStand.getLocation().clone().add(0,2,0),40,2,2,2,0);
                    armorStand.getWorld().spawnParticle(Particle.TOTEM,armorStand.getLocation().clone().add(0,2,0),40,2,4,2,0);

                    armorStand.getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.5F, 0.8F);

                    new BukkitRunnable() {
                        public void run() {
                            if (armorStand.isDead()) {
                                cancel();
                            } else {
                                Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(235, 198, 52), Color.fromRGB(50, 50, 50), 2F);
                                p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, armorStand.getLocation().clone().add(0, 5, 0), 8, 0, 5, 0, dustTransition);

                                p.getWorld().spawnParticle(Particle.TOTEM, armorStand.getLocation().clone().add(0,2.5,0),3,2,3,2,0);
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 10L);
                }

            }
        }.runTaskTimer(plugin, 0L, 3L);

    }

    @EventHandler
    public void onDeath(PlayerInteractEntityEvent e) {

        if (e.getRightClicked() instanceof PolarBear entity) {
            if (entity.hasMetadata("lootdrop")) {
                entity.remove();
                entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 30, 1, 1, 1, 0);
                entity.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, entity.getLocation().add(0,-0.5,0), 5, 0.5, 0.5, 0.5, 0);
                entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_CHEST_OPEN,0.4F,0.8F);

                Random random = new Random();

                ultimatePointsListeners.addUltPoint(e.getPlayer());
                entity.getWorld().dropItem(entity.getLocation().add(0,1,0), new ItemStack(Material.GOLDEN_APPLE, random.nextInt(3)));
                entity.getWorld().dropItem(entity.getLocation().add(0,1,0), new ItemStack(Material.TNT, random.nextInt(6)));
                entity.getWorld().dropItem(entity.getLocation().add(0,1,0), new ItemStack(Material.ENDER_PEARL, random.nextInt(2)));

                ItemStack fireb = new ItemStack(Material.FIRE_CHARGE);
                fireb.setAmount(random.nextInt(4));
                ItemMeta fbMeta = fireb.getItemMeta();
                fbMeta.setDisplayName(ChatColor.BOLD + "Fire Ball");
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.DARK_AQUA + "Right click to launch a fireball!");
                fbMeta.setLore(lore);
                fireb.setItemMeta(fbMeta);

                entity.getWorld().dropItem(entity.getLocation().add(0,1,0), fireb);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ColoredText.translateHexCodes(
                            "&4&l<&#fbf451&lC&#fbbe3f&lr&#fc882d&la&#fc511b&lt&#fd1b09&le&#fd1b09&l-&#fc511b&lD&#fc882d&lr&#fbbe3f&lo&#fbf451&lp&4&l> " +
                                    "&#6da87d&l" + e.getPlayer().getDisplayName() + "&#6da87d has collected a supply-crate!"));
                }

                for (Entity entity1 : entity.getWorld().getNearbyEntities(entity.getLocation(),0.5,3,0.5)) {
                    if (entity1.hasMetadata("lootdrop")) {
                        entity1.remove();
                        break;
                    }
                }
            }
        }

    }

    public static Location getrandomlocation (Player p) {

        Random random = new Random();

        World world = p.getWorld();

        double x = (int) world.getWorldBorder().getCenter().getX() - world.getWorldBorder().getSize() / 2 + random.nextInt((int) world.getWorldBorder().getSize());
        double z = (int) world.getWorldBorder().getCenter().getZ() - world.getWorldBorder().getSize() / 2 + random.nextInt((int) world.getWorldBorder().getSize());

        Location randomLocation = new Location(world, x, 0, z);

        randomLocation.setY(p.getWorld().getHighestBlockYAt(randomLocation) + 100);

        return randomLocation;

    }

    public static String getYawCardinalPoint(float yaw) {
        // Adjust yaw to be within the range of -180 to 180
        yaw = (yaw % 360 + 360) % 360;
        if (yaw > 180) {
            yaw -= 360;
        }

        // Determine the cardinal point based on the adjusted yaw
        if (yaw >= -22.5 && yaw < 22.5) {
            return "South";
        } else if (yaw >= 22.5 && yaw < 67.5) {
            return "South-West";
        } else if (yaw >= 67.5 && yaw < 112.5) {
            return "West";
        } else if (yaw >= 112.5 && yaw < 157.5) {
            return "North-West";
        } else if (yaw >= 157.5 || yaw < -157.5) {
            return "North";
        } else if (yaw >= -157.5 && yaw < -112.5) {
            return "North-East";
        } else if (yaw >= -112.5 && yaw < -67.5) {
            return "East";
        } else {
            return "South-East";
        }
    }

    public static Material getrandomCOLOR (Player p) {

        Random random = new Random();

        int number = random.nextInt(11);

        switch (number) {
            case 0:
                return Material.RED_CARPET;
            case 1:
                return Material.PINK_CARPET;
            case 2:
                return Material.MAGENTA_CARPET;
            case 3:
                return Material.YELLOW_CARPET;
            case 4:
                return Material.LIME_CARPET;
            case 5:
                return Material.ORANGE_CARPET;
            case 6:
                return Material.LIGHT_BLUE_CARPET;
            case 7:
                return Material.CYAN_CARPET;
            case 8:
                return Material.ORANGE_CARPET;
            case 9:
                return Material.GREEN_CARPET;
            case 10:
                return Material.MOSS_CARPET;

        }
        return null;
    }

}
