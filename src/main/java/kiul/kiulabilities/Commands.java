package kiul.kiulabilities;

import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.ColoredText;
import kiul.kiulabilities.gamelogic.GlintEnchantment;
import kiul.kiulabilities.gamelogic.ultimatePointsListeners;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Commands implements TabExecutor, Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("test")) {

            ArrayList<String> lore = new ArrayList<>();
            switch (args[0]) {
                case "stealth": {
                    int maxUltimatePoints = 6;
                    int requiredUltimatePoints = 2;
                    Material material = Material.LIGHT_GRAY_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.STEALTH.getLabel());
                    String metaData = "stealth";

                    //
                    lore.add(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Become intangible and invisible for a short time");
                    lore.add(ChatColor.WHITE + "Left-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Shortsight players in a radius directly in front of you for a short time");
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
                case "tracker": {
                    int maxUltimatePoints = 3;
                    int requiredUltimatePoints = 3;
                    Material material = Material.LIME_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.TRACKER.getLabel());
                    String metaData = "tracker";

                    //
                    lore.add(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Summon a wolf that hunts down and stuns the nearest player");
                    lore.add(ChatColor.WHITE + "Left-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Grow a defensive cluster of sweet berry bushes in nearby grass");
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
                case "featherweight": {
                    int maxUltimatePoints = 3;
                    int requiredUltimatePoints = 3;
                    Material material = Material.WHITE_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.FEATHERWEIGHT.getLabel());
                    String metaData = "featherweight";

                    //
                    lore.add(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Fly up into the sky");
                    lore.add(ChatColor.WHITE + "Left-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "----");
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
                case "artificer": {
                    int maxUltimatePoints = 6;
                    int requiredUltimatePoints = 2;
                    Material material = Material.RED_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.ARTIFICER.getLabel());
                    String metaData = "artificer";

                    //
                    lore.add(ChatColor.WHITE + "Right-Click" + ChatColor.GOLD + " » " + ChatColor.GRAY + "Creates a small, non-damaging explosion that boosts the player several blocks");
                    lore.add(ChatColor.WHITE + "Left-Click " + ChatColor.GOLD + "» " + ChatColor.GRAY + "Create a medium explosion at your feet, throwing nearby players back and dealing damage");
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
                case "earth": {
                    int maxUltimatePoints = 6;
                    int requiredUltimatePoints = 2;
                    Material material = Material.BROWN_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.EARTH.getLabel());
                    String metaData = "earth";

                    //
                    lore.add(ChatColor.WHITE + "Right-Click " + ChatColor.GOLD + " » " + ChatColor.GRAY + "Launches the player into the air, creating a damaging crater when landing and negates fall damage");
                    lore.add(ChatColor.WHITE + "Left-Click " + ChatColor.GOLD + " » " + ChatColor.GRAY + "-");
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
                case "spectre": {
                    int maxUltimatePoints = 6;
                    int requiredUltimatePoints = 4;
                    Material material = Material.PINK_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.SPECTRE.getLabel());
                    String metaData = null;

                    //
                    lore.add(ColoredText.translateHexCodes("&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Zoom forward, repulsing players away if hit. (Not hitting anyone will debuff you!)"));
                    lore.add(ColoredText.translateHexCodes("&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Double jump forward, doesn't work while falling. (attempting to fly will work to)"));
                    lore.add(ColoredText.translateHexCodes("&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "EVERY player gets repulsed in a direction away from the player."));
                    lore.add(ColoredText.translateHexCodes(" "));
                    lore.add(ColoredText.translateHexCodes("&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.UNNAMED.Class")));
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
                case "catalyst": {
                    int maxUltimatePoints = 6;
                    int requiredUltimatePoints = 4;
                    Material material = Material.CYAN_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.CATALYST.getLabel());
                    String metaData = null;

                    //
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
                case "discharge": {
                    int maxUltimatePoints = 6;
                    int requiredUltimatePoints = 4;
                    Material material = Material.LIGHT_BLUE_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.DISCHARGE.getLabel());
                    String metaData = null;

                    //
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
                case "ignition": {
                    int maxUltimatePoints = 6;
                    int requiredUltimatePoints = 2;
                    Material material = Material.ORANGE_DYE;
                    String displayName = ColoredText.translateHexCodes(AbilityItemNames.IGNITION.getLabel());
                    String metaData = null;

                    //
                    //

                    spawnAbilityItem(p, maxUltimatePoints, requiredUltimatePoints, material, lore, displayName, metaData);
                }
                break;
            }
        } else if (label.equalsIgnoreCase("giveultpoint")) { /** /giveultpoint */
            ultimatePointsListeners.addUltPoint(p);
        }
        return false;
    }

    public void spawnAbilityItem (Player p, Integer maxUltPoints, Integer requiredUltPoints, Material material, List<String> lore, String displayName, String metaData) {
        ultimatePointsListeners.maximumUltPoints.put(p.getUniqueId(),maxUltPoints);
        ultimatePointsListeners.requiredUltPoints.put(p.getUniqueId(),requiredUltPoints);
        ItemStack abilityTrigger = new ItemStack(material);
        ItemMeta abilityTriggerMeta = abilityTrigger.getItemMeta();
        abilityTriggerMeta.setLore(lore);
        abilityTriggerMeta.setDisplayName(displayName);
        GlintEnchantment glow = new GlintEnchantment(new NamespacedKey(plugin, "glow"));
        abilityTriggerMeta.addEnchant(glow, 1, true);
        if (metaData != null) {
            p.setMetadata(metaData, new FixedMetadataValue(plugin, "pat"));
        }
        abilityTrigger.setItemMeta(abilityTriggerMeta);
        p.getInventory().addItem(abilityTrigger);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (command.getName().equals("test")) {
            if (args.length < 2) {
                List<String> arguments = new ArrayList<>();

                for (AbilityItemNames names : AbilityItemNames.values()) {
                    arguments.add(names.name().toLowerCase());
                }

                return arguments;
            }
        }
        return null;
    }
}
