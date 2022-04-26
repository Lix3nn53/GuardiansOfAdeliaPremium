package io.github.lix3nn53.guardiansofadelia.guardian.skill.managers;

import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfo;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionBarInfoManager {

    private static final HashMap<Player, List<ActionBarInfo>> playerToActionBarInfo = new HashMap<>();

    public static void setActionBarInfo(Player player, List<ActionBarInfo> actionBarInfo) {
        playerToActionBarInfo.put(player, actionBarInfo);
    }

    public static List<ActionBarInfo> getActionBarInfo(Player player) {
        return playerToActionBarInfo.get(player);
    }

    public static void clearActionBarInfo(Player player) {
        playerToActionBarInfo.remove(player);
    }

    public static void addActionBarInfo(Player player, ActionBarInfo actionBarInfo) {
        List<ActionBarInfo> actionBarInfos = playerToActionBarInfo.get(player);
        if (actionBarInfos == null) {
            actionBarInfos = new ArrayList<>();
            playerToActionBarInfo.put(player, actionBarInfos);
        }
        actionBarInfos.add(actionBarInfo);
    }

    public static void removeActionBarInfo(Player player, ActionBarInfo actionBarInfo) {
        List<ActionBarInfo> actionBarInfos = playerToActionBarInfo.get(player);
        if (actionBarInfos != null) {
            actionBarInfos.remove(actionBarInfo);
        }
    }
}
