package kiul.kiulabilities.gamelogic;

import kiul.kiulabilities.Kiulabilities;
import org.bukkit.plugin.Plugin;

public class AbilityLores {

    public static Plugin plugin = Kiulabilities.getPlugin(Kiulabilities.class);

    public static String[] spectreLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Zoom forward, repulsing players away if hit. (Not hitting anyone will debuff you!)",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Double jump forward, doesn't work while falling. (attempting to fly will work to)",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "EVERY player gets repulsed in a direction away from the player.",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.SPECTRE.Class")
    };

    public static String[] atificerLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.ARTIFICER.Class")
    };

    public static String[] earthLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.EARTH.Class")
    };

    public static String[] featherweightLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Launch up into the air. (With elytra if active, 11s)",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Shoots 8 s-bullets 360° around you.",
            "&#b1b1b1&lSwap-Item (&#b1b1b1DEACTIVATED&l) &6» &#b1b1b1" + "Activated elytra status permanently.",
            "&#919090&lSwap-Item (&#919090ACTIVATED&l) &6» &#919090" + "Spawn a shulker bullet above all players alive.",
            " ",
            "&#737373&lClass &6» " + plugin.getConfig().getString("Abilities.FEATHERWEIGHT.Class"),
            " ",
            "&#919090&lElytra-Status &6» " + "&c&lDEACTIVATED"
    };

    public static String[] trackerLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.TRACKER.Class")
    };

    public static String[] stealthLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.STEALTH.Class")
    };

    public static String[] catalystLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.CATALYST.Class")
    };

    public static String[] dischargeLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.DISCHARGE.Class")
    };

    public static String[] ignitionLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Spawn fire around the player.",
            "&#c4c4c4&lLeft-Click (Entity) &6» &#c4c4c4" + "Set the entity on fire, with a bang.",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Summon a meteor rain that damages players and the landscape.",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.IGNITION.Class")
    };

    public static String[] frozoneLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Shoot a icey projectile that explodes on impact.",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Construct a wall of ice, that also catches you.",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Every player is incased within a ice ball.",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.FROZONE.Class")
    };

    public static String[] geckoLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Shoot out ur tongue, pulling in any entity u hit.",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Become invisible until attacked/moving again.",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Detach your tail, explodes when entity comes close.",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.GECKO.Class")
    };

}
