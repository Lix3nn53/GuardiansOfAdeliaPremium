package io.github.lix3nn53.guardiansofadelia.guardian.character;

import io.github.lix3nn53.guardiansofadelia.chat.ChatManager;
import io.github.lix3nn53.guardiansofadelia.chat.ChatTag;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillTree;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.jobs.RPGCharacterCraftingStats;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.rpginventory.RPGInventory;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public final class RPGCharacter {

    private final RPGInventory rpgInventory;

    private final HashMap<String, RPGClassStats> classToClassStats;
    private String rpgClassStr;
    private SkillBar skillBar;

    private final RPGCharacterStats rpgCharacterStats;

    private List<Quest> questList = new ArrayList<>();
    private List<Integer> turnedInQuests = new ArrayList<>();

    private final RPGCharacterCraftingStats craftingStats = new RPGCharacterCraftingStats();

    private ChatTag chatTag = ChatTag.NOVICE;

    public RPGCharacter(String rpgClassStr, Player player, HashMap<String, RPGClassStats> classToClassStats) {
        this.rpgInventory = new RPGInventory(player);
        rpgCharacterStats = new RPGCharacterStats(player, rpgClassStr);
        this.rpgClassStr = rpgClassStr.toUpperCase();
        this.classToClassStats = classToClassStats;

        RPGClassStats rpgClassStats = this.classToClassStats.get(this.rpgClassStr);
        SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();
        RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);
        this.skillBar = new SkillBar(player, rpgClass.getSkillTree(), skillRPGClassData, false);

    }

    public RPGCharacter(String rpgClassStr, Player player) {
        this.rpgInventory = new RPGInventory(player);
        rpgCharacterStats = new RPGCharacterStats(player, rpgClassStr);
        this.rpgClassStr = rpgClassStr.toUpperCase();
        RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);

        this.classToClassStats = new HashMap<>();
        RPGClassStats rpgClassStats = new RPGClassStats();
        this.classToClassStats.put(rpgClassStr, rpgClassStats);
        SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();
        this.skillBar = new SkillBar(player, rpgClass.getSkillTree(), skillRPGClassData, false);
    }

    public String getRpgClassStr() {
        return rpgClassStr;
    }

    public HashMap<String, RPGClassStats> getClassToClassStats() {
        return classToClassStats;
    }

    public RPGClassStats getCurrentRPGClassStats() {
        return classToClassStats.get(this.rpgClassStr.toUpperCase());
    }

    public RPGClassStats getRPGClassStats() {
        String rpgClassStr = getRpgClassStr();
        if (classToClassStats.containsKey(rpgClassStr)) {
            return classToClassStats.get(rpgClassStr);
        }

        return new RPGClassStats();
    }

    public RPGClassStats getRPGClassStats(String rpgClassStr) {
        if (classToClassStats.containsKey(rpgClassStr.toUpperCase())) {
            return classToClassStats.get(rpgClassStr.toUpperCase());
        }

        return new RPGClassStats();
    }

    public void clearRPGClassStats() {
        classToClassStats.clear();
    }

    public RPGClassStats addClassStats(String newClassStr) {
        RPGClassStats rpgClassStats = new RPGClassStats();
        classToClassStats.put(newClassStr.toUpperCase(), rpgClassStats);

        return rpgClassStats;
    }

    public boolean changeClass(Player player, String newClassStr, String lang) {
        String s = newClassStr.toUpperCase();
        RPGClass rpgClass = RPGClassManager.getClass(s);

        SkillDataManager.onPlayerQuit(player);

        RPGClassStats rpgClassStats;
        if (!classToClassStats.containsKey(s)) { // Add class stats if it does not exist
            rpgClassStats = addClassStats(s);
        } else {
            rpgClassStats = classToClassStats.get(s);
        }

        this.rpgClassStr = s;

        this.skillBar.onQuit();

        SkillTree skillTree = rpgClass.getSkillTree();
        SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();

        this.skillBar = new SkillBar(player, skillTree, skillRPGClassData, true);
        this.skillBar.remakeSkillBar(skillTree, skillRPGClassData, lang);

        this.rpgCharacterStats.setRpgClassStr(s);
        this.rpgCharacterStats.recalculateEquipment(rpgClassStr);
        player.sendMessage(ChatPalette.YELLOW + "Changed class to " + rpgClass.getClassString() + ChatPalette.YELLOW + "!");

        ActionBarInfo actionBarInfo = rpgClass.getActionBarInfo();


        return true;
    }

    public boolean acceptQuest(Quest quest, Player player) {
        if (!hasQuest(quest.getQuestID())) {
            if (this.questList.size() < 5) {
                this.questList.add(quest);
                quest.onAccept(player);
                return true;
            }
        }
        return false;
    }

    public boolean turnInQuest(int questID, Player player, boolean ignoreCompilation) {
        Optional<Quest> questOptional = this.questList.stream()
                .filter(characterQuest -> characterQuest.getQuestID() == questID)
                .findAny();
        if (questOptional.isPresent()) {
            Quest quest = questOptional.get();
            if (ignoreCompilation) {
                this.questList.remove(quest);
                this.turnedInQuests.add(questID);
                quest.onTurnIn(player);
                return true;
            } else if (quest.isCompleted()) {
                this.questList.remove(quest);
                this.turnedInQuests.add(questID);
                quest.onTurnIn(player);
                return true;
            }
        }
        return false;
    }

    public List<Quest> getQuestList() {
        return this.questList;
    }

    public void setQuestList(List<Quest> questList) {
        this.questList = questList;
    }

    public List<Integer> getTurnedInQuests() {
        return this.turnedInQuests;
    }

    public void setTurnedInQuests(List<Integer> turnedInQuests) {
        this.turnedInQuests = turnedInQuests;
    }

    public RPGInventory getRpgInventory() {
        return rpgInventory;
    }

    public ChatTag getChatTag() {
        return chatTag;
    }

    public void setChatTag(Player player, ChatTag chatTag) {
        this.chatTag = chatTag;
        ChatManager.updatePlayerName(player);
    }

    public boolean hasQuest(int questId) {
        return this.questList.stream()
                .anyMatch(playerQuest -> playerQuest.getQuestID() == questId);
    }

    public boolean progressTaskOfQuestWithIndex(Player player, int questId, int taskIndex) {
        Quest quest = null;
        for (Quest i : this.questList) {
            if (i.getQuestID() == questId) {
                quest = i;
                break;
            }
        }

        if (quest == null) return false;

        return quest.progressTaskWithIndex(player, taskIndex);
    }

    public RPGCharacterStats getRpgCharacterStats() {
        return rpgCharacterStats;
    }

    public SkillBar getSkillBar() {
        return skillBar;
    }

    public RPGCharacterCraftingStats getCraftingStats() {
        return craftingStats;
    }
}
