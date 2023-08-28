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
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Launch yourself a short distance in the direction you are facing",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Create an explosion at your location. Damages enemies and destroys terrain",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Spawn 3 TNT in the air above all players",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.ARTIFICER.Class")
    };

    public static String[] earthLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "broken",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "dont",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "use",
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
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Summon a wolf that hunts down the nearest player. On hit, the wolf damages, stuns and inflicts bleed on the hunted player.",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Grows sweet berries in a small radius around the player",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Spawn particles around you that point towards all living players. Brighter = closer.",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.TRACKER.Class")
    };

    public static String[] stealthLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Enter 'stealth mode' for a brief time, where you are unable to be damaged by any source and are completely invisible",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Create a blinding orb that moves forwards and afflicts players close to it with blindness",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Enter 'stealth-mode' for a longer duration than normal, and teleport to a player of your choice anywhere on the map",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.STEALTH.Class")
    };

    public static String[] catalystLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Create a wall of sculk directly in front of you and spread sculk around your feet",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Spawn an evoker fang that snaps at all nearby players' locations, ROOTing them on hit.",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Once active, kills will give you temporary speed, absorption and strength.",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.CATALYST.Class")
    };

    public static String[] dischargeLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Replace your shield with a riptide trident; can only be used once before disappearing",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Fire a trident in the direction you are facing. Strikes hit players with lightning. Hitting a player in water will chain the attack with nearby players also in water",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Create a map-wide thunderstorm that randomly strikes any player above y80",
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
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Every player is encased within a ice ball.",
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
    public static String[] celloLore = new String[]{
            "&#d8d8d8&lRight-Click &6» &#d8d8d8" + "Look at a player to mark them as your 'best-friend'. if you have a best friend and they are in range (10) , give them a shield. If they are outside range, afflict nearby players with weakness.",
            "&#c4c4c4&lLeft-Click &6» &#c4c4c4" + "Fire a bolt that STUNs, FLINGs or HEALs players near its impact point. Toggle mode with sneak.",
            "&#b1b1b1&lSwap-Item &6» &#b1b1b1" + "Revive your best friend at the cost of yours and their max hp (reduced to 8 hearts)",
            " ",
            "&#919090&lClass &6» " + plugin.getConfig().getString("Abilities.CELLO.Class")
    };

}
