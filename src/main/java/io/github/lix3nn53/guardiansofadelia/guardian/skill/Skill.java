package io.github.lix3nn53.guardiansofadelia.guardian.skill;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.SkillComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.InitializeTrigger;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeArrowWithOffset;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeDirection;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeOffset;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Skill {

    private final int id;
    private final String name;
    private final int maxSkillLevel;
    private final Material material;
    private final int customModelData;
    private final List<String> description;

    private final List<Integer> reqSkillPoints;

    // skill attributes
    private final List<Integer> manaCosts;
    private final List<Integer> cooldowns;

    private final List<SkillComponent> triggers = new ArrayList<>();

    // skill tree
    private final int parentId;
    private final HashMap<Integer, SkillTreeDirection> childSkills;
    private final SkillTreeDirection skillTreeDirection;
    private SkillTreeOffset rootStartOffset; // Required if the skill is root

    public Skill(int id, String name, int maxSkillLevel, Material material, int customModelData,
                 List<String> description, List<Integer> reqSkillPoints, List<Integer> manaCosts,
                 List<Integer> cooldowns, int parentId, HashMap<Integer, SkillTreeDirection> childSkills, SkillTreeDirection skillTreeDirection) {
        this.id = id;
        this.name = name;
        this.maxSkillLevel = maxSkillLevel;
        this.material = material;
        this.customModelData = customModelData;
        this.description = description;
        this.reqSkillPoints = reqSkillPoints;
        this.manaCosts = manaCosts;
        this.cooldowns = cooldowns;
        this.parentId = parentId;
        this.childSkills = childSkills;
        this.skillTreeDirection = skillTreeDirection;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMaxSkillLevel() {
        return reqSkillPoints.size();
    }

    public int getReqSkillPoints(int skillLevel) {
        if (skillLevel == maxSkillLevel) return 9000;
        return reqSkillPoints.get(skillLevel);
    }

    public int getManaCost(int skillLevel) {
        if (skillLevel == 0) return manaCosts.get(0);
        return manaCosts.get(skillLevel - 1);
    }

    public int getCooldown(int skillLevel) {
        if (skillLevel == 0) return cooldowns.get(0);
        return cooldowns.get(skillLevel - 1);
    }

    public int getCurrentSkillLevel(int pointsInvested) {
        int skillLevel = 0;

        int totalReqPoint = 0;
        for (int reqPoint : reqSkillPoints) {
            totalReqPoint += reqPoint;
            if (pointsInvested >= totalReqPoint) {
                skillLevel++;
            } else {
                break;
            }
        }

        return skillLevel;
    }

    public List<String> getDescription() {
        return description;
    }

    public ItemStack getIcon(String lang, int playerPoints, int investedPoints) {
        int skillLevel = getCurrentSkillLevel(investedPoints);

        ItemStack icon = new ItemStack(getMaterial());

        ItemMeta itemMeta = icon.getItemMeta();
        itemMeta.setCustomModelData(customModelData);

        itemMeta.setDisplayName(getName() + " (" + skillLevel + "/" + getMaxSkillLevel() + ")");
        List<String> lore = new ArrayList<>();

        int reqSkillPoints = getReqSkillPoints(skillLevel);
        ChatPalette reqSkillPointsColor = ChatPalette.RED;
        if (playerPoints >= reqSkillPoints) {
            reqSkillPointsColor = ChatPalette.GREEN_DARK;
        }

        lore.add(reqSkillPointsColor + "Required Skill Points: " + reqSkillPoints);

        lore.add("");
        if (skillLevel == 0) {
            lore.add(ChatPalette.BLUE_LIGHT + "Mana cost: " + getManaCost(skillLevel));
            lore.add(ChatPalette.BLUE + "Cooldown: " + getCooldown(skillLevel));
        } else if (skillLevel == maxSkillLevel) {
            lore.add(ChatPalette.BLUE_LIGHT + "Mana cost: " + getManaCost(skillLevel - 1));
            lore.add(ChatPalette.BLUE + "Cooldown: " + getCooldown(skillLevel - 1));
        } else {
            lore.add(ChatPalette.BLUE_LIGHT + "Mana cost: " + getManaCost(skillLevel - 1) + " -> " + getManaCost(skillLevel));
            lore.add(ChatPalette.BLUE + "Cooldown: " + getCooldown(skillLevel - 1) + " -> " + getCooldown(skillLevel));
        }

        lore.add("");
        //skill attributes
        for (SkillComponent trigger : triggers) {
            lore.addAll(trigger.getSkillLoreAdditions(lang, new ArrayList<>(), skillLevel));
        }

        lore.add("");
        lore.addAll(getDescription());

        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

        icon.setItemMeta(itemMeta);
        return icon;
    }

    public boolean cast(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        boolean didCast = false;
        for (SkillComponent trigger : triggers) {
            didCast = trigger.execute(caster, skillLevel, targets, castCounter, skillId) || didCast;
        }
        return didCast;
    }

    public void addTrigger(SkillComponent skillComponent) {
        triggers.add(skillComponent);
    }

    public List<InitializeTrigger> getInitializeTriggers() {
        List<InitializeTrigger> initializeTriggers = new ArrayList<>();

        for (SkillComponent skillComponent : triggers) {
            if (skillComponent instanceof InitializeTrigger) {
                initializeTriggers.add((InitializeTrigger) skillComponent);
            }
        }

        return initializeTriggers;
    }

    public int getParentId() {
        return parentId;
    }

    public HashMap<Integer, SkillTreeDirection> getChildSkills() {
        return childSkills;
    }

    public SkillTreeDirection getSkillTreeDirection() {
        return skillTreeDirection;
    }

    public SkillTreeOffset getRootStartOffset() {
        return rootStartOffset;
    }

    public void setRootStartOffset(SkillTreeOffset rootStartOffset) {
        this.rootStartOffset = rootStartOffset;
    }

    public void applyChildSkillTreeOffsetAndArrows(HashMap<Integer, Skill> skillSet, HashMap<Integer, SkillTreeOffset> skillIdToOffset, List<SkillTreeArrowWithOffset> skillTreeArrows) {
        for (int childSkillId : childSkills.keySet()) {
            SkillTreeDirection skillTreeDirection = childSkills.get(childSkillId);
            SkillTreeOffset offset = skillTreeDirection.getOffset(rootStartOffset);
            skillIdToOffset.put(childSkillId, offset);

            List<SkillTreeArrowWithOffset> arrows = skillTreeDirection.getArrows(rootStartOffset);
            skillTreeArrows.addAll(arrows);

            // Do the same for the child skills
            Skill child = skillSet.get(childSkillId);
            child.applyChildSkillTreeOffsetAndArrows(skillSet, skillIdToOffset, skillTreeArrows);
        }
    }
}