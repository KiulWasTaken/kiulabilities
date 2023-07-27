package kiul.kiulabilities.gamelogic.Commands;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.AbilityLores;
import kiul.kiulabilities.gamelogic.Methods.ColoredText;
import kiul.kiulabilities.gamelogic.GlintEnchantment;
import kiul.kiulabilities.gamelogic.Methods.ultimatePointsListeners;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class OperatorCommands implements TabExecutor, Listener {

    public Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (p.isOp()) {
            if (label.equalsIgnoreCase("test")) {

                for (AbilityItemNames abilitys : AbilityItemNames.values()) {
                    if (abilitys.name().toString().toLowerCase().equalsIgnoreCase(args[0])) {

                        p.sendMessage(abilitys.getDisplayName() + " " + abilitys.getMaxPoints() + " " + abilitys.getRequiredPoints() + " " +
                                abilitys.getMaterial() + " " + abilitys.getLore());

                        spawnAbilityItem(p, abilitys.getMaxPoints(), abilitys.getRequiredPoints(), abilitys.getMaterial(), List.of(abilitys.getLore()), abilitys.getDisplayName(), null);
                        break;

                    }
                }

            } else if (label.equalsIgnoreCase("giveultpoint")) { /** /giveultpoint */
                ultimatePointsListeners.addUltPoint(p);
            }
        } else {
            p.sendMessage("fail, not operator!");
        }
        return false;
    }


    public void spawnAbilityItem(Player p, Integer maxUltPoints, Integer requiredUltPoints, Material material, List<String> lore, String displayName, String metaData) {
        ultimatePointsListeners.maximumUltPoints.put(p.getUniqueId(), maxUltPoints);
        ultimatePointsListeners.requiredUltPoints.put(p.getUniqueId(), requiredUltPoints);
        ItemStack abilityTrigger = new ItemStack(material);
        ItemMeta abilityTriggerMeta = abilityTrigger.getItemMeta();
        List<String> lores = new ArrayList<>();
        for (String str : lore) {
            lores.add(ColoredText.translateHexCodes(str));
        }
        abilityTriggerMeta.setLore(lores);
        abilityTriggerMeta.setDisplayName(ColoredText.translateHexCodes(displayName));
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
