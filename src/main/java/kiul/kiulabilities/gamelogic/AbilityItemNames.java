package kiul.kiulabilities.gamelogic;

import kiul.kiulabilities.Kiulabilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

    public enum AbilityItemNames {
        UNNAMED(AbilityExtras.displayname("UNNAMED.DisplayName")),
        ARTIFICER(AbilityExtras.displayname("ARTIFICER.DisplayName")),
        EARTH(AbilityExtras.displayname("EARTH.DisplayName")),
        FEATHERWEIGHT(AbilityExtras.displayname("FEATHERWEIGHT.DisplayName")),
        TRACKER(AbilityExtras.displayname("TRACKER.DisplayName")),
        STEALTH(AbilityExtras.displayname("STEALTH.DisplayName")),
        CATALYST(AbilityExtras.displayname("CATALYST.DisplayName")),
        DISCHARGE(AbilityExtras.displayname("DISCHARGE.DisplayName")),
        IGNITION(AbilityExtras.displayname("IGNITION.DisplayName"));

        private String label;

        AbilityItemNames(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

    }
