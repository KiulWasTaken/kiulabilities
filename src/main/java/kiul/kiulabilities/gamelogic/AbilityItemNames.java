package kiul.kiulabilities.gamelogic;

import kiul.kiulabilities.Kiulabilities;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras;
import kiul.kiulabilities.gamelogic.Methods.AbilityExtras.*;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public enum AbilityItemNames {
        FEATHERWEIGHT(AbilityExtras.displayname("FEATHERWEIGHT.DisplayName"), 3, 3, Material.WHITE_DYE, AbilityLores.featherweightLore),
        STEALTH(AbilityExtras.displayname("STEALTH.DisplayName"), 6, 2, Material.LIGHT_GRAY_DYE, AbilityLores.stealthLore),
        EARTH(AbilityExtras.displayname("EARTH.DisplayName"), 6, 2, Material.BROWN_DYE, AbilityLores.earthLore),
        ARTIFICER(AbilityExtras.displayname("ARTIFICER.DisplayName"), 6, 2, Material.RED_DYE, AbilityLores.atificerLore),
        IGNITION(AbilityExtras.displayname("IGNITION.DisplayName"), 6, 2, Material.ORANGE_DYE, AbilityLores.ignitionLore),
        TRACKER(AbilityExtras.displayname("TRACKER.DisplayName"), 3, 3, Material.LIME_DYE, AbilityLores.trackerLore),
        GECKO(AbilityExtras.displayname("GECKO.DisplayName"), 2, 1, Material.GREEN_DYE, AbilityLores.geckoLore),
        CATALYST(AbilityExtras.displayname("CATALYST.DisplayName"), 2, 2, Material.CYAN_DYE, AbilityLores.catalystLore),
        FROZONE(AbilityExtras.displayname("FROZONE.DisplayName"), 2, 1, Material.LIGHT_BLUE_DYE, AbilityLores.frozoneLore),
        DISCHARGE(AbilityExtras.displayname("DISCHARGE.DisplayName"), 6, 4, Material.BLUE_DYE, AbilityLores.dischargeLore),
        SPECTRE(AbilityExtras.displayname("SPECTRE.DisplayName"), 4, 2, Material.PINK_DYE, AbilityLores.spectreLore),
        CELLO(AbilityExtras.displayname("CELLO.DisplayName"), 4, 2, Material.PURPLE_DYE, AbilityLores.celloLore);

        private String displayName;
        private int maxPoints;
        private int requiredPoints;
        private Material material;
        private String[] lore;

        AbilityItemNames(String label, int maxPoints, int requiredPoints, Material material, String[] lore) {
                this.displayName = label;
                this.maxPoints = maxPoints;
                this.requiredPoints = requiredPoints;
                this.material = material;
                this.lore = lore;
        }

        public String getDisplayName() {
                return displayName;
        }

        public int getMaxPoints() {
                return maxPoints;
        }

        public int getRequiredPoints() {
                return requiredPoints;
        }

        public Material getMaterial() {
                return material;
        }

        public String[] getLore() {
                return lore;
        }

    }
