package kiul.kiulabilities.gamelogic.Methods;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.AbilityItemNames;
import kiul.kiulabilities.gamelogic.GlintEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class AbilityMenu {

    public static Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    public static void menuInventory (Player p) {

        Inventory inventory = Bukkit.createInventory(p, 45, ColoredText.translateHexCodes("&#8a0ac6&lS&#931ecd&le&#9c32d3&ll&#a547da&le&#ae5be1&lc&#b76fe8&lt &#c083ee&ly&#c998f5&lo&#d2acfc&lu&#d2acfc&lr &#c998f5&la&#c083ee&lb&#b76fe8&li&#ae5be1&ll&#a547da&li&#9c32d3&lt&#931ecd&ly&#8a0ac6&l!"));

        List<Material> pane = new ArrayList<>();
        pane.add(Material.BLUE_STAINED_GLASS_PANE);
        pane.add(Material.PURPLE_STAINED_GLASS_PANE);
        pane.add(Material.MAGENTA_STAINED_GLASS_PANE);
        pane.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        pane.add(Material.PINK_STAINED_GLASS_PANE);
        pane.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        pane.add(Material.MAGENTA_STAINED_GLASS_PANE);
        pane.add(Material.PURPLE_STAINED_GLASS_PANE);
        pane.add(Material.BLUE_STAINED_GLASS_PANE);
        pane.add(Material.PURPLE_STAINED_GLASS_PANE);
        pane.add(Material.PURPLE_STAINED_GLASS_PANE);
        pane.add(Material.MAGENTA_STAINED_GLASS_PANE);
        pane.add(Material.MAGENTA_STAINED_GLASS_PANE);
        pane.add(Material.PURPLE_STAINED_GLASS_PANE);
        pane.add(Material.PURPLE_STAINED_GLASS_PANE);
        pane.add(Material.BLUE_STAINED_GLASS_PANE);
        pane.add(Material.PURPLE_STAINED_GLASS_PANE);
        pane.add(Material.MAGENTA_STAINED_GLASS_PANE);
        pane.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        pane.add(Material.PINK_STAINED_GLASS_PANE);
        pane.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        pane.add(Material.MAGENTA_STAINED_GLASS_PANE);
        pane.add(Material.PURPLE_STAINED_GLASS_PANE);
        pane.add(Material.BLUE_STAINED_GLASS_PANE);

        int slot = 0;

        for (Material glassPane: pane) {
            inventory.setItem(slot, createItemstack(glassPane, " ", Arrays.asList(""), false));
            slot++;

            if (slot == 10 || slot == 19 || slot == 28) {
                slot = slot + 7;
            }
        }

        List<Material> wool = new ArrayList<>();
        wool.add(Material.LIGHT_BLUE_DYE);
        wool.add(Material.GREEN_DYE);
        wool.add(Material.ORANGE_DYE);
        wool.add(Material.PINK_DYE);
        wool.add(Material.WHITE_DYE);

        List<String> names = new ArrayList<>();
        names.add(AbilityItemNames.FROZONE.getDisplayName());
        names.add(AbilityItemNames.GECKO.getDisplayName());
        names.add(AbilityItemNames.IGNITION.getDisplayName());
        names.add(AbilityItemNames.SPECTRE.getDisplayName());
        names.add(AbilityItemNames.FEATHERWEIGHT.getDisplayName());

        List<String[]> lore = new ArrayList<>();

        lore.add(new String[]{
                "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Shoot a icey projectile that explodes on impact.",
        "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Construct a wall of ice, that also catches you.",
        "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Every player is incased within a ice ball.",
        " ",
        "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.FROZONE.Class")
        });

        lore.add(new String[]{
                "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Shoot out ur tongue, pulling in any entity u hit.",
        "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Become invisible until attacked/moving again.",
        "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Detach your tail, explodes when entity comes close.",
        " ",
        "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.GECKO.Class")
        });

        lore.add(new String[]{
                "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Spawn fire around the player.",
        "&#c4c4c4&lLeft-Click (Entity) &6» &#c4c4c4" + "Set the entity on fire, with a bang.",
        "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Summon a meteor rain that damages players and the landscape.",
        " ",
        "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.IGNITION.Class")
        });

        lore.add(new String[]{
                "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Zoom forward, repulsing players away if hit. (Not hitting anyone will debuff you!)",
        "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Double jump forward, doesn't work while falling. (attempting to fly will work to)",
        "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "EVERY player gets repulsed in a direction away from the player.",
        " ",
        "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.SPECTRE.Class")
        });

        lore.add(new String[]{
                "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Launch up into the air. (With elytra if active, 11s)",
        "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Shoots 8 s-bullets 360° around you.",
        "&#b1b1b1&lSwap-Item (&#b1b1b1DEACTIVATED&l) &6» &#b1b1b1" + "Activated elytra status permanently.",
        "&#919090&lSwap-Item (&#919090ACTIVATED&l) &6» &#919090" + "Spawn a shulker bullet above all players alive.",
        " ",
        "&#737373&lClass &6» " + plugin.getConfig().getString("Abilities.FEATHERWEIGHT.Class"),
        " ",
        "&#919090&lElytra-Status &6» " + "&c&lDEACTIVATED"
        });

        int i = 10;
        int getName = 0;
        int getLore = 0;

        for (Material mat : wool) {

            inventory.setItem(i, createItemstack(mat, names.get(getName), List.of(lore.get(getLore)), true));

            getLore++;
            getName++;
            i++;
            if (i == 17 || i == 26) {
                i++;
                i++;
            }
        }

        p.openInventory(inventory);

    }

    public static ItemStack createItemstack (Material material , String displayName , List<String> lore, Boolean glowing) {

        List<String> lore1 = new ArrayList<>();

        for (String str : lore) {
            lore1.add(ColoredText.translateHexCodes(str));
        }

        GlintEnchantment glow = new GlintEnchantment(new NamespacedKey(Kiulabilities.getPlugin(Kiulabilities.class), "glow"));

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ColoredText.translateHexCodes(displayName));
        itemMeta.setLore(lore1);
        if (glowing == true) {
            itemMeta.addEnchant(glow,1, true);
        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

}
