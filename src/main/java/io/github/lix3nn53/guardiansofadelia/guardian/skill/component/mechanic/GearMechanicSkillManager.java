package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic;

import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearType;

import java.util.HashMap;

public class GearMechanicSkillManager {

    private static final HashMap<WeaponGearType, Integer> weaponToSkillId = new HashMap<>();

    public static void register(WeaponGearType weaponGearType, int skillId) {
        weaponToSkillId.put(weaponGearType, skillId);
    }

    public static int getRequiredSkillId(WeaponGearType weaponGearType) {
        return weaponToSkillId.get(weaponGearType);
    }
}
