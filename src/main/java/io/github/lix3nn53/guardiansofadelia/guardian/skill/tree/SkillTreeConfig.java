package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

public class SkillTreeConfig {

    private final int parentSkillId;
    private final SkillTreeDirection skillTreeDirectionRelatedToParent;

    public SkillTreeConfig(int parentSkillId, SkillTreeDirection skillTreeDirectionRelatedToParent) {
        this.parentSkillId = parentSkillId;
        this.skillTreeDirectionRelatedToParent = skillTreeDirectionRelatedToParent;
    }

    public int getParentSkillId() {
        return parentSkillId;
    }

    public SkillTreeDirection getSkillTreeDirectionRelatedToParent() {
        return skillTreeDirectionRelatedToParent;
    }
}
