package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.SkillComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Must be in first skill component list
 */
public class InitializeTrigger extends SkillComponent {

    private int skillId;
    private int lastCastCounter;

    public InitializeTrigger(ConfigurationSection configurationSection) {
        super(!configurationSection.contains("addLore") || configurationSection.getBoolean("addLore"));
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        this.skillId = skillId;

        return false;
    }

    @Override
    public List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel) {
        return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);
    }

    public void startEffects(Player caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        this.skillId = skillId;

        executeChildren(caster, skillLevel, targets, castCounter, skillId);
        lastCastCounter = castCounter;
    }

    public void stopPreviousEffects(Player caster) {
        SkillDataManager.stopRepeatTasksOfCast(caster, lastCastCounter);
        SkillDataManager.removeSavedEntities(caster, lastCastCounter);
    }
}
