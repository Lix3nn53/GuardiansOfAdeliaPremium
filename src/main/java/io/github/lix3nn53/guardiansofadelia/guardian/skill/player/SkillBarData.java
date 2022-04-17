package io.github.lix3nn53.guardiansofadelia.guardian.skill.player;

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

    public int get(int i) {
        return switch (i) {
            case 0 -> one;
            case 1 -> two;
            case 2 -> three;
            case 3 -> ultimate;
            default -> -1;
        };
    }

    public void set(int i, int skill) {
        switch (i) {
            case 0 -> one = skill;
            case 1 -> two = skill;
            case 2 -> three = skill;
            case 3 -> ultimate = skill;
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
