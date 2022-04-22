package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

import java.util.ArrayList;
import java.util.List;

public enum SkillTreeDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    LEFT_UP,
    LEFT_DOWN,
    RIGHT_UP,
    RIGHT_DOWN;

    public SkillTreeOffset getOffset(SkillTreeOffset parentOffset) {
        return new SkillTreeOffset(getOffsetX(parentOffset.getX()), getOffsetY(parentOffset.getY()));
    }

    private int getOffsetX(int parentX) {
        switch (this) {
            case UP, DOWN -> {
                return parentX;
            }
            case LEFT, LEFT_DOWN, LEFT_UP -> {
                return parentX - 2;
            }
            case RIGHT, RIGHT_UP, RIGHT_DOWN -> {
                return parentX + 2;
            }
        }

        return parentX;
    }

    private int getOffsetY(int parentY) {
        switch (this) {
            case UP, RIGHT_UP, LEFT_UP -> {
                return parentY + 1;
            }
            case DOWN, RIGHT_DOWN, LEFT_DOWN -> {
                return parentY - 1;
            }
            case LEFT, RIGHT -> {
                return parentY;
            }
        }

        return parentY;
    }

    public List<SkillTreeArrowWithOffset> getArrows(SkillTreeOffset parentOffset) {
        List<SkillTreeArrowWithOffset> result = new ArrayList<>();

        int parentX = parentOffset.getX();
        int parentY = parentOffset.getY();

        switch (this) {
            case UP -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.UP, new SkillTreeOffset(parentX, parentY + 1)));
            }
            case DOWN -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.DOWN, new SkillTreeOffset(parentX, parentY - 1)));
            }
            case LEFT -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.LEFT, new SkillTreeOffset(parentX - 1, parentY)));
            }
            case RIGHT -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.RIGHT, new SkillTreeOffset(parentX + 1, parentY)));
            }
            case LEFT_UP -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.UP, new SkillTreeOffset(parentX, parentY + 1)));
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.LEFT, new SkillTreeOffset(parentX - 1, parentY + 1)));
            }
            case LEFT_DOWN -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.DOWN, new SkillTreeOffset(parentX, parentY - 1)));
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.LEFT, new SkillTreeOffset(parentX - 1, parentY - 1)));
            }
            case RIGHT_UP -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.UP, new SkillTreeOffset(parentX, parentY + 1)));
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.RIGHT, new SkillTreeOffset(parentX + 1, parentY + 1)));
            }
            case RIGHT_DOWN -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.DOWN, new SkillTreeOffset(parentX, parentY - 1)));
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.RIGHT, new SkillTreeOffset(parentX + 1, parentY - 1)));
            }
        }

        return result;
    }
}
