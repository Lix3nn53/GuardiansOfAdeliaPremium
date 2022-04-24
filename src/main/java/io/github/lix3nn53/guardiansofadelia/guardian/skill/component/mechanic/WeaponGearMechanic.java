package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.MechanicComponent;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearType;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class WeaponGearMechanic extends MechanicComponent {

    private final WeaponGearType weaponGearType;

    public WeaponGearMechanic(WeaponGearType weaponGearType, int skillId) {
        super(true);

        this.weaponGearType = weaponGearType;

        GearMechanicSkillManager.register(weaponGearType, skillId);
    }

    public WeaponGearMechanic(ConfigurationSection configurationSection, int skillId) {
        super(!configurationSection.contains("addLore") || configurationSection.getBoolean("addLore"));

        if (!configurationSection.contains("weaponGearType")) {
            configLoadError("weaponGearType");
        }

        this.weaponGearType = WeaponGearType.valueOf(configurationSection.getString("weaponGearType"));

        GearMechanicSkillManager.register(weaponGearType, skillId);
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        if (targets.isEmpty()) return false;

        for (LivingEntity target : targets) {
            if (target instanceof Player) {
                Player player = (Player) target;
                boolean b = GuardianDataManager.hasGuardianData(player);
                if (!b) continue;

                GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();
            }
        }

        return true;
    }

    @Override
    public List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel) {
        if (!this.addLore) return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);

        if (skillLevel == 0) {
            additions.add(ChatPalette.BLUE_LIGHT + "Learn using weapon: " + ChatPalette.YELLOW + weaponGearType);
        }

        return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);
    }

    public enum PushType {
        FIXED,
        INVERSE,
        SCALED
    }
}
