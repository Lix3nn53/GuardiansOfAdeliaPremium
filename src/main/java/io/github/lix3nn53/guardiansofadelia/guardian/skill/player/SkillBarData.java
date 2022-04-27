package io.github.lix3nn53.guardiansofadelia.guardian.skill.player;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillTier;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import org.bukkit.entity.Player;

public class SkillBarData {

    // Which skills to use in the skill bar
    private int one;
    private int two;
    private int three;
    private int ultimate;

    public SkillBarData(int one, int two, int three, int ultimate) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.ultimate = ultimate;
    }

    public SkillBarData() {
        this(-1, -1, -1, -1);
    }

    /**
     * @param i skill slot index
     * @return skillId assigned to slot
     */
    public int getSkillId(int i) {
        return switch (i) {
            case 0 -> one;
            case 1 -> two;
            case 2 -> three;
            case 3 -> ultimate;
            default -> -1;
        };
    }

    public void set(Player player, SkillTree skillTree, int slot, int skillId) {
        Skill skill = skillTree.getSkill(skillId);
        SkillTier skillTier = skill.getSkillTier();

        if (skillTier.equals(SkillTier.PASSIVE)) {
            player.sendMessage("You cannot assign passive skills to the skill bar.");
            return;
        } else if (skillTier.equals(SkillTier.ULTIMATE)) {
            for (int i = 0; i < 4; i++) {
                int skillIdOnSlot = getSkillId(i);
                if (skillIdOnSlot != -1) {
                    Skill skillOnSlot = skillTree.getSkill(skillIdOnSlot);
                    SkillTier skillTierOnSlot = skillOnSlot.getSkillTier();
                    if (skillTierOnSlot.equals(SkillTier.ULTIMATE)) {
                        player.sendMessage("You cannot assign more than one ultimate skill to the skill bar.");
                        return;
                    }
                }
            }
        }

        switch (slot) {
            case 0 -> one = skillId;
            case 1 -> two = skillId;
            case 2 -> three = skillId;
            case 3 -> ultimate = skillId;
        }
    }

    public int skillToSlot(int skillId) {
        if (skillId == one) {
            return 0;
        } else if (skillId == two) {
            return 1;
        } else if (skillId == three) {
            return 2;
        } else if (skillId == ultimate) {
            return 3;
        }

        return -1;
    }
}
