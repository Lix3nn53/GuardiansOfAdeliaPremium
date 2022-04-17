package io.github.lix3nn53.guardiansofadelia.guardian.skill;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillTreeData;

import java.util.HashMap;

public class SkillTree {

    private final HashMap<Integer, Skill> skillSet;

    public SkillTree(HashMap<Integer, Skill> skillSet) {
        this.skillSet = skillSet;
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
        int parentSkillId = skill.getParentSkillId();

        if (parentSkillId == -1) {
            return true;
        }

        int investedSkillPoints = skillTreeData.getInvestedSkillPoints(parentSkillId);

        return investedSkillPoints > 0;
    }
}
