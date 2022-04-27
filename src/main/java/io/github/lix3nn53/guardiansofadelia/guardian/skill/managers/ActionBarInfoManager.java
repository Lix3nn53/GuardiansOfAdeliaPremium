package io.github.lix3nn53.guardiansofadelia.guardian.skill.managers;

import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfo;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionBarInfoManager {

    private static final HashMap<Player, HashMap<Integer, ActionBarInfo>> playerToCastCounterToActionBar = new HashMap<>();

    public static List<ActionBarInfo> getActionBarInfo(Player player) {
        HashMap<Integer, ActionBarInfo> castCounterToActionBars = playerToCastCounterToActionBar.get(player);

        if (castCounterToActionBars == null) {
            return null;
        }

        return new ArrayList<>(castCounterToActionBars.values());
    }

    public static void clearActionBarInfo(Player player) {
        playerToCastCounterToActionBar.remove(player);
    }

    public static void setActionBarInfo(Player player, ActionBarInfo actionBarInfo, int castCounter) {
        HashMap<Integer, ActionBarInfo> castCounterToActionBars = playerToCastCounterToActionBar.get(player);

        if (castCounterToActionBars == null) {
            castCounterToActionBars = new HashMap<>();
            playerToCastCounterToActionBar.put(player, castCounterToActionBars);
        }

        castCounterToActionBars.put(castCounter, actionBarInfo);
    }

    public static void removeActionBarInfo(Player player, int castCounter) {
        player.sendMessage("removeActionBarInfo: castCounter: " + castCounter);

        HashMap<Integer, ActionBarInfo> actionBarInfos = playerToCastCounterToActionBar.get(player);
        if (actionBarInfos != null) {
            actionBarInfos.remove(castCounter);
            if (actionBarInfos.isEmpty()) {
                playerToCastCounterToActionBar.remove(player);
            }
        }
    }
}
