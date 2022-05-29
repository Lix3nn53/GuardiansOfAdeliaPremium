package io.github.lix3nn53.guardiansofadelia.npc.speech;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.chat.SpeechBubble;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.menu.quest.GuiQuestList;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterMisc;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class QuestSpeech {

    private static void lookAtNPC(Player player, Entity npc) {
        Location start = player.getLocation();
        float height = (float) (npc.getHeight()) / 2f;
        Location targetLocation = npc.getLocation().add(0, height, 0);
        Vector vectorBetweenPoints = targetLocation.toVector().subtract(start.toVector());
        start.setDirection(vectorBetweenPoints);
        player.teleport(start);
    }

    public static void startDialogue(Player player, Entity npc, List<String> messages, int index) {
        if (index >= messages.size()) {
            NPCSpeechManager.onQuestDialogueEnd(player);

            // Open quest gui again
            if (GuardianDataManager.hasGuardianData(player)) {
                GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
                NPC byId = npcRegistry.getNPC(npc);
                GuiQuestList questGui = new GuiQuestList(byId, player, guardianData);
                questGui.openInventory(player);
            }

            return;
        }

        String message = CustomCharacterMisc.SPEECH_BUBBLE.toString() + ChatPalette.GRAY + messages.get(index);

        long duration = getDuration(message);
        SpeechBubble.entityNoFollowPacket(npc, message, duration, 0, player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (duration + 10), 0));

        new BukkitRunnable() {
            @Override
            public void run() {
                startDialogue(player, npc, messages, index + 1);
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), duration);
    }

    private static long getDuration(String message) {
        return message.length() * 2L;
    }
}
