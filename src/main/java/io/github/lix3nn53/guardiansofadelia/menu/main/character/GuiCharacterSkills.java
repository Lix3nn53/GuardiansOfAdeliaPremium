package io.github.lix3nn53.guardiansofadelia.menu.main.character;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiCharacter;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class GuiCharacterSkills extends GuiGeneric {

    private final HashMap<Integer, Integer> skillIdToSlotVirtual = new HashMap<>();

    private final int slotStart = 27; // first slot of middle line

    // This is where the skill tree displays and moves
    private final Integer[][] virtualSlots = new Integer[][]{{9, 10, 11, 12, 13, 14, 15, 16}, {18, 19, 20, 21, 22, 23, 24, 25},
            {27, 28, 29, 30, 31, 32, 33, 34}, {36, 37, 38, 39, 40, 41, 42, 43}, {45, 46, 47, 48, 49, 50, 51, 52}};

    // State, use this to calculate virtual slots
    private int offsetX = 0;
    private int offsetY = 0;

    public GuiCharacterSkills(Player player, GuardianData guardianData, RPGCharacter rpgCharacter, SkillBar skillBar, int pointsLeft) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "skill.name") +
                " (" + Translation.t(guardianData, "skill.points") + ": " + pointsLeft + ")", 0);

        RPGClassStats rpgClassStats = rpgCharacter.getRPGClassStats();
        String rpgClassStr = rpgCharacter.getRpgClassStr();
        RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);
        SkillTree skillTree = rpgClass.getSkillTree();


        String language = guardianData.getLanguage();
        if (skillSet.containsKey(0)) {
            Skill skillOne = skillSet.get(0);
            int investedSkillPoints = skillBar.getInvestedSkillPoints(0);
            ItemStack icon = skillOne.getIcon(language, pointsLeft, investedSkillPoints);
            ItemMeta itemMeta = icon.getItemMeta();
            String displayName = itemMeta.getDisplayName();
            itemMeta.setDisplayName(displayName + " (" + Translation.t(guardianData, "skill.invested") + ": " + investedSkillPoints + ")");
            icon.setItemMeta(itemMeta);
            this.setItem(10, icon);
        }

        if (skillSet.containsKey(1)) {
            Skill skillTwo = skillSet.get(1);
            int investedSkillPoints = skillBar.getInvestedSkillPoints(1);
            ItemStack icon = skillTwo.getIcon(language, pointsLeft, investedSkillPoints);
            ItemMeta itemMeta = icon.getItemMeta();
            String displayName = itemMeta.getDisplayName();
            itemMeta.setDisplayName(displayName + " (" + Translation.t(guardianData, "skill.invested") + ": " + investedSkillPoints + ")");
            icon.setItemMeta(itemMeta);
            this.setItem(13, icon);
        }

        if (skillSet.containsKey(2)) {
            Skill skillThree = skillSet.get(2);
            int investedSkillPoints = skillBar.getInvestedSkillPoints(2);
            ItemStack icon = skillThree.getIcon(language, pointsLeft, investedSkillPoints);
            ItemMeta itemMeta = icon.getItemMeta();
            String displayName = itemMeta.getDisplayName();
            itemMeta.setDisplayName(displayName + " (" + Translation.t(guardianData, "skill.invested") + ": " + investedSkillPoints + ")");
            icon.setItemMeta(itemMeta);
            this.setItem(16, icon);
        }

        if (skillSet.containsKey(3)) {
            Skill skillFour = skillSet.get(3);
            int investedSkillPoints = skillBar.getInvestedSkillPoints(3);
            ItemStack icon = skillFour.getIcon(language, pointsLeft, investedSkillPoints);
            ItemMeta itemMeta = icon.getItemMeta();
            String displayName = itemMeta.getDisplayName();
            itemMeta.setDisplayName(displayName + " (" + Translation.t(guardianData, "skill.invested") + ": " + investedSkillPoints + ")");
            icon.setItemMeta(itemMeta);
            this.setItem(20, icon);
        }

        if (skillSet.containsKey(4)) {
            Skill skillFive = skillSet.get(4);
            int investedSkillPoints = skillBar.getInvestedSkillPoints(4);
            ItemStack icon = skillFive.getIcon(language, pointsLeft, investedSkillPoints);
            ItemMeta itemMeta = icon.getItemMeta();
            String displayName = itemMeta.getDisplayName();
            itemMeta.setDisplayName(displayName + " (" + Translation.t(guardianData, "skill.invested") + ": " + investedSkillPoints + ")");
            icon.setItemMeta(itemMeta);
            this.setItem(24, icon);
        }

        ItemStack backButton = OtherItems.getBackButton("Character Menu");
        this.setItem(0, backButton);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuardianData guardianData;
        RPGCharacter rpgCharacter;
        if (GuardianDataManager.hasGuardianData(player)) {
            guardianData = GuardianDataManager.getGuardianData(player);

            if (guardianData.hasActiveCharacter()) {
                rpgCharacter = guardianData.getActiveCharacter();
            } else {
                return;
            }
        } else {
            return;
        }

        SkillBar skillBar = rpgCharacter.getSkillBar();

        int skillIndex = -1;

        int slot = event.getSlot();

        if (slot == 0) {
            GuiCharacter gui = new GuiCharacter(guardianData);
            gui.openInventory(player);
            return;
        } else if (slot == 10) {
            skillIndex = 0;
        } else if (slot == 13) {
            skillIndex = 1;
        } else if (slot == 16) {
            skillIndex = 2;
        } else if (slot == 20) {
            skillIndex = 3;
        } else if (slot == 24) {
            skillIndex = 4;
        }

        if (skillIndex != -1) {
            if (event.isLeftClick()) {
                boolean upgradeSkill = skillBar.upgradeSkill(skillIndex, rpgCharacter.getRPGClassStats(), guardianData.getLanguage());
                if (upgradeSkill) {
                    int pointsLeft = skillBar.getSkillPointsLeftToSpend();
                    GuiCharacterSkills guiCharacterSkills = new GuiCharacterSkills(player, guardianData, rpgCharacter, skillBar, pointsLeft);
                    guiCharacterSkills.openInventory(player);

                    List<Quest> questList = rpgCharacter.getQuestList();
                    for (Quest quest : questList) {
                        quest.progressUpgradeSkillTasks(player, skillIndex);
                    }
                }
            } else if (event.isRightClick()) {
                boolean downgradeSkill = skillBar.downgradeSkill(skillIndex, rpgCharacter.getCurrentRPGClassStats(), guardianData.getLanguage());
                if (downgradeSkill) {
                    int pointsLeft = skillBar.getSkillPointsLeftToSpend();
                    GuiCharacterSkills guiCharacterSkills = new GuiCharacterSkills(player, guardianData, rpgCharacter, skillBar, pointsLeft);
                    guiCharacterSkills.openInventory(player);
                }
            }
        }
    }

    private void moveVirtualDisplay(VirtualMove direction) {
        switch (direction) {
            case LEFT -> {
                this.offsetX -= 1;
            }
            case RIGHT -> {
                this.offsetX += 1;
            }
            case UP -> {
                this.offsetY += 1;
            }
            case DOWN -> {
                this.offsetY -= 1;
            }
        }
    }

    private Integer[][] getDisplayedVirtualSlots() {

    }

    private enum VirtualMove {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
