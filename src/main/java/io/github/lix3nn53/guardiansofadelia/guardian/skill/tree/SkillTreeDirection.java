package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

import java.util.HashMap;

public enum SkillTreeDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    LEFT_UP,
    LEFT_DOWN,
    RIGHT_UP,
    RIGHT_DOWN;

    public int getIndex(int parentIndex) {
        switch (this) {
            case UP -> {
                return parentIndex - (9 * 2);
            }
            case DOWN -> {
                return parentIndex + (9 * 2);
            }
            case LEFT -> {
                return parentIndex - 2;
            }
            case RIGHT -> {
                return parentIndex + 2;
            }
            case LEFT_UP -> {
                return parentIndex - 11;
            }
            case LEFT_DOWN -> {
                return parentIndex + 7;
            }
            case RIGHT_UP -> {
                return parentIndex - 7;
            }
            case RIGHT_DOWN -> {
                return parentIndex + 11;
            }
        }

        return -1;
    }

    public HashMap<Integer, SkillTreeArrow> getArrows(int parentIndex) {
        HashMap<Integer, SkillTreeArrow> result = new HashMap<>();

        switch (this) {
            case UP -> {
                result.put(parentIndex - 9, SkillTreeArrow.UP);
            }
            case DOWN -> {
                result.put(parentIndex + 9, SkillTreeArrow.UP);
            }
            case LEFT -> {
                result.put(parentIndex - 1, SkillTreeArrow.UP);
            }
            case RIGHT -> {
                result.put(parentIndex + 1, SkillTreeArrow.UP);
            }
            case LEFT_UP -> {
                result.put(parentIndex - 10, SkillTreeArrow.LEFT);
                result.put(parentIndex - 9, SkillTreeArrow.UP);
            }
            case LEFT_DOWN -> {
                result.put(parentIndex + 8, SkillTreeArrow.LEFT);
                result.put(parentIndex + 9, SkillTreeArrow.DOWN);
            }
            case RIGHT_UP -> {
                result.put(parentIndex - 8, SkillTreeArrow.RIGHT);
                result.put(parentIndex - 9, SkillTreeArrow.UP);
            }
            case RIGHT_DOWN -> {
                result.put(parentIndex + 10, SkillTreeArrow.RIGHT);
                result.put(parentIndex + 9, SkillTreeArrow.DOWN);
            }
        }

        return result;
    }
}
