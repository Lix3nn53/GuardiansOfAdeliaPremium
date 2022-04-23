package io.github.lix3nn53.guardiansofadelia.menu.main.character;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillTreeData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeArrowWithOffset;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeOffset;
import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiCharacter;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiCharacterSkills extends GuiGeneric {

    private final GuardianData guardianData;

    // Calculations
    private final SkillTree skillTree;

    // This is where the skill tree displays and moves
    /*private final Integer[][] virtualSlots = new Integer[][]{{9, 10, 11, 12, 13, 14, 15, 16}, {18, 19, 20, 21, 22, 23, 24, 25},
            {27, 28, 29, 30, 31, 32, 33, 34}, {36, 37, 38, 39, 40, 41, 42, 43}, {45, 46, 47, 48, 49, 50, 51, 52}};*/
    private final Integer[][] virtualSlots = new Integer[][]{{45, 46, 47, 48, 49, 50, 51, 52}, {36, 37, 38, 39, 40, 41, 42, 43}, {27, 28, 29, 30, 31, 32, 33, 34},
            {18, 19, 20, 21, 22, 23, 24, 25}, {9, 10, 11, 12, 13, 14, 15, 16}};

    // State, use this to calculate virtual slots
    private final SkillTreeOffset viewOffset;

    // Easier clicks
    private final HashMap<Integer, Integer> slotToSkillIdForClick = new HashMap<>();

    public GuiCharacterSkills(Player player, GuardianData guardianData, RPGCharacter rpgCharacter,
                              int pointsLeft, SkillTreeOffset viewOffset) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "skill.name") +
                " (" + Translation.t(guardianData, "skill.points") + ": " + pointsLeft + ")", 0);

        this.guardianData = guardianData;
        this.viewOffset = viewOffset;

        String rpgClassStr = rpgCharacter.getRpgClassStr();
        RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);
        this.skillTree = rpgClass.getSkillTree();

        String language = guardianData.getLanguage();

        ItemStack backButton = OtherItems.getBackButton("Character Menu");
        this.setItem(0, backButton);

        ItemStack rightButton = new ItemStack(Material.GLASS_PANE);
        ItemMeta itemMeta = rightButton.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "View Right");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to move view to right.");
        itemMeta.setLore(lore);
        rightButton.setItemMeta(itemMeta);
        this.setItem(26, rightButton);

        ItemStack leftButton = new ItemStack(Material.GLASS_PANE);
        itemMeta = leftButton.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "View Left");
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to move view to left.");
        itemMeta.setLore(lore);
        leftButton.setItemMeta(itemMeta);
        this.setItem(35, leftButton);

        ItemStack upButton = new ItemStack(Material.GLASS_PANE);
        itemMeta = upButton.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "View Up");
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to move view up.");
        itemMeta.setLore(lore);
        upButton.setItemMeta(itemMeta);
        this.setItem(44, upButton);

        ItemStack downButton = new ItemStack(Material.GLASS_PANE);
        itemMeta = downButton.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "View Down");
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to move view down.");
        itemMeta.setLore(lore);
        downButton.setItemMeta(itemMeta);
        this.setItem(53, downButton);

        ItemStack fillItem = new ItemStack(Material.GLASS_PANE);
        itemMeta = fillItem.getItemMeta();
        itemMeta.setDisplayName("");
        fillItem.setItemMeta(itemMeta);
        for (int i = 1; i < 9; i++) {
            this.setItem(i, fillItem);
        }
        this.setItem(17, fillItem);

        RPGClassStats rpgClassStats = rpgCharacter.getRPGClassStats();
        SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();
        formVirtualView(player, skillRPGClassData);
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

        int slot = event.getSlot();

        if (slot == 0) {
            GuiCharacter gui = new GuiCharacter(guardianData);
            gui.openInventory(player);
        } else if (slot == 26) {
            moveVirtualDisplay(VirtualMove.RIGHT);
        } else if (slot == 35) {
            moveVirtualDisplay(VirtualMove.LEFT);
        } else if (slot == 44) {
            moveVirtualDisplay(VirtualMove.UP);
        } else if (slot == 53) {
            moveVirtualDisplay(VirtualMove.DOWN);
        } else if (this.slotToSkillIdForClick.containsKey(slot)) {
            int skillId = this.slotToSkillIdForClick.get(slot);

            RPGClassStats rpgClassStats = rpgCharacter.getRPGClassStats();
            SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();

            SkillBar skillBar = rpgCharacter.getSkillBar();

            if (event.isLeftClick()) {
                boolean upgradeSkill = skillRPGClassData.upgradeSkill(player, skillId, this.skillTree, rpgClassStats, skillBar, guardianData.getLanguage());
                if (upgradeSkill) {
                    int pointsLeft = rpgClassStats.getSkillRPGClassData().getSkillPointsLeftToSpend(player);

                    GuiCharacterSkills guiCharacterSkills = new GuiCharacterSkills(player, guardianData, rpgCharacter, pointsLeft, this.viewOffset);
                    guiCharacterSkills.openInventory(player);

                    List<Quest> questList = rpgCharacter.getQuestList();
                    for (Quest quest : questList) {
                        quest.progressUpgradeSkillTasks(player, skillId);
                    }
                }
            } else if (event.isRightClick()) {
                boolean downgradeSkill = skillRPGClassData.downgradeSkill(player, skillId, this.skillTree, rpgClassStats, skillBar, guardianData.getLanguage());
                if (downgradeSkill) {
                    int pointsLeft = rpgClassStats.getSkillRPGClassData().getSkillPointsLeftToSpend(player);

                    GuiCharacterSkills guiCharacterSkills = new GuiCharacterSkills(player, guardianData, rpgCharacter, pointsLeft, this.viewOffset);
                    guiCharacterSkills.openInventory(player);
                }
            }
        }
    }

    private void moveVirtualDisplay(VirtualMove direction) {
        switch (direction) {
            case LEFT -> {
                this.viewOffset.setX(this.viewOffset.getX() - 1);
            }
            case RIGHT -> {
                this.viewOffset.setX(this.viewOffset.getX() + 1);
            }
            case UP -> {
                this.viewOffset.setY(this.viewOffset.getY() + 1);
            }
            case DOWN -> {
                this.viewOffset.setY(this.viewOffset.getY() - 1);
            }
        }
    }

    private int getMaxX() {
        return this.viewOffset.getX() + 2;
    }

    private int getMinX() {
        return this.viewOffset.getX() - 2;
    }

    private int getMaxY() {
        return this.viewOffset.getY() + 8;
    }

    private int getMinY() {
        return this.viewOffset.getY();
    }

    private void formVirtualView(Player player, SkillRPGClassData rpgClassData) {
        String lang = this.guardianData.getLanguage();

        HashMap<Integer, SkillTreeOffset> skillIdToOffset = this.skillTree.getSkillIdToOffset();
        List<SkillTreeArrowWithOffset> skillTreeArrows = this.skillTree.getSkillTreeArrows();

        int maxX = getMaxX();
        int minX = getMinX();
        int maxY = getMaxY();
        int minY = getMinY();

        for (int i = 0; i < skillTreeArrows.size(); i++) {
            SkillTreeArrowWithOffset skillTreeArrow = skillTreeArrows.get(i);

            SkillTreeOffset skillTreeOffset = skillTreeArrow.getSkillTreeOffset();

            boolean b = skillTreeOffset.inBounds(minX, maxX, minY, maxY);
            if (b) {
                int slot = virtualOffsetToSlot(skillTreeOffset);

                ItemStack itemStack = skillTreeArrow.getSkillTreeArrow().getItemStack();
                this.setItem(slot, itemStack);
            }
        }

        slotToSkillIdForClick.clear();
        SkillTreeData skillTreeData = rpgClassData.getSkillTreeData();
        for (int skillId : skillIdToOffset.keySet()) {
            SkillTreeOffset skillTreeOffset = skillIdToOffset.get(skillId);
            boolean b = skillTreeOffset.inBounds(minX, maxX, minY, maxY);
            if (b) {
                int slot = virtualOffsetToSlot(skillTreeOffset);

                Skill skill = this.skillTree.getSkill(skillId);
                int skillPointsLeftToSpend = rpgClassData.getSkillPointsLeftToSpend(player);
                int investedSkillPoints = skillTreeData.getInvestedSkillPoints(skillId);
                ItemStack icon = skill.getIcon(lang, skillPointsLeftToSpend, investedSkillPoints);

                this.setItem(slot, icon);

                slotToSkillIdForClick.put(slot, skillId);
            }
        }
    }

    public int virtualOffsetToSlot(SkillTreeOffset skillTreeOffset) {
        int x = skillTreeOffset.getX() - this.viewOffset.getX();
        int y = skillTreeOffset.getY() - this.viewOffset.getY();

        return this.virtualSlots[y][x];
    }

    enum VirtualMove {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
