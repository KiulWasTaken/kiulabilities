package kiul.kiulabilities.gamelogic;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

    public enum AbilityItemNames {
        UNNAMED("unnamed"),
        ARTIFICER("artificer ability item"),
        EARTH("earth ability item"),
        FEATHERWEIGHT("featherweight ability item"),
        TRACKER("tracker ability item"),
        STEALTH("stealth ability item"),
        CATALYST("catalyst ability item");

        private String label;

        AbilityItemNames(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
