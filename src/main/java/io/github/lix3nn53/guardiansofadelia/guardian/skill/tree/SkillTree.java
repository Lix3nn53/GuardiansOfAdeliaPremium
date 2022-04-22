package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillTreeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SkillTree {

    private final HashMap<Integer, Skill> skillSet;

    // Skill GUI
    private final HashMap<Integer, SkillTreeOffset> skillIdToOffset = new HashMap<>();
    private final List<SkillTreeArrowWithOffset> skillTreeArrows = new ArrayList<>();

    public SkillTree(HashMap<Integer, Skill> skillSet) {
        this.skillSet = skillSet;

        // START calculate skill tree offset
        List<Integer> rootSkillIds = new ArrayList<>();

        for (int skillId : skillSet.keySet()) {
            Skill skill = skillSet.get(skillId);
            int rootSkillId = skill.getParentId();

            if (rootSkillId < 0) {
                rootSkillIds.add(skillId);
            }
        }

        for (int rootSkillId : rootSkillIds) {
            Skill rootSkill = skillSet.get(rootSkillId);
            SkillTreeOffset rootStartOffset = rootSkill.getRootStartOffset();
            skillIdToOffset.put(rootSkillId, rootStartOffset);

            rootSkill.applyChildSkillTreeOffsetAndArrows(skillSet, skillIdToOffset, skillTreeArrows);
        }
        // END calculate skill tree offset
    }

    public Skill getSkill(int id) {
        return skillSet.get(id);
    }

    public boolean containsSkill(int id) {
        return skillSet.containsKey(id);
    }

    // Parent skill must be unlocked
    public boolean canLearnSkill(int id, SkillTreeData skillTreeData) {
        Skill skill = skillSet.get(id);
        int rootSkillId = skill.getParentId();

        if (rootSkillId == -1) {
            return true;
        }

        int investedSkillPoints = skillTreeData.getInvestedSkillPoints(rootSkillId);

        return investedSkillPoints > 0;
    }

    public Set<Integer> getSkillIds() {
        return skillSet.keySet();
    }
}
