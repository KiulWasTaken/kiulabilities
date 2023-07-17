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

        int i = 10;

        for (AbilityItemNames abilitys : AbilityItemNames.values()) {

            inventory.setItem(i, createItemstack(abilitys.getMaterial(), abilitys.getDisplayName(), Arrays.asList(abilitys.getLore()), false));

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
