package io.github.lix3nn53.guardiansofadelia.guardian.skill.component;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.TriggerListener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class TriggerComponent extends SkillComponent {

    public int skillId;

    // Save for stopping later
    public List<LivingEntity> saveTargets;
    public String triggerType;

    public TriggerComponent(boolean addLore, String triggerType) {
        super(addLore);

        this.triggerType = triggerType;
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        this.saveTargets = targets;

        return true;
    }

    public int getSkillId() {
        return skillId;
    }

    public void stopEffects() {
        if (saveTargets == null) {
            return;
        }

        for (LivingEntity target : this.saveTargets) {
            if (target instanceof Player) {
                TriggerListener.remove((Player) target, triggerType, skillId);
            }
        }
    }
}
