package io.github.lix3nn53.guardiansofadelia.guardian.character;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.Attribute;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.AttributeType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.sounds.CustomSound;
import io.github.lix3nn53.guardiansofadelia.sounds.GoaSound;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.centermessage.MessageUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RPGClassStats {
    private final SkillRPGClassData skillRPGClassData;
    private final HashMap<AttributeType, Integer> attributeInvested;
    private int totalExp;

    public RPGClassStats(SkillRPGClassData skillRPGClassData, HashMap<AttributeType, Integer> attributeInvested, int totalExp) {
        this.skillRPGClassData = skillRPGClassData;
        this.attributeInvested = attributeInvested;
        this.totalExp = totalExp;
    }

    public RPGClassStats() {
        this.skillRPGClassData = new SkillRPGClassData();
        this.attributeInvested = new HashMap<>();
        this.totalExp = 0;
    }

    public SkillRPGClassData getSkillRPGClassData() {
        return skillRPGClassData;
    }

    public void giveExpNoMessage(int expToGive) {
        int currentLevel = RPGClassExperienceManager.getLevel(this.totalExp);

        if (currentLevel >= 20) { //last level is 20
            return;
        }

        this.totalExp += expToGive;
    }

    public void giveExp(Player player, int expToGive) {
        int currentLevel = RPGClassExperienceManager.getLevel(this.totalExp);

        if (currentLevel >= 20) { //last level is 20
            return;
        }

        this.totalExp += expToGive;

        int newLevel = RPGClassExperienceManager.getLevel(this.totalExp);

        if (currentLevel < newLevel) { //level up
            if (GuardianDataManager.hasGuardianData(player)) {
                GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                if (guardianData.hasActiveCharacter()) {
                    RPGCharacter activeCharacter = guardianData.getActiveCharacter();

                    String rpgClassStr = activeCharacter.getRpgClassStr();

                    RPGClass aClass = RPGClassManager.getClass(rpgClassStr);

                    String classString = aClass.getClassString();

                    MessageUtils.sendCenteredMessage(player, classString + ChatPalette.GOLD + " Level up!");
                    MessageUtils.sendCenteredMessage(player, ChatPalette.YELLOW + "Congratulations, your new " + classString +
                            ChatPalette.YELLOW + " level is " + ChatPalette.GOLD + newLevel);
                    CustomSound customSound = GoaSound.LEVEL_UP.getCustomSound();
                    customSound.play(player.getLocation());
                }
            }
        }
    }

    public int getTotalExperience() {
        return totalExp;
    }

    public int getInvested(AttributeType attributeType) {
        return attributeInvested.get(attributeType);
    }

    public void setInvested(Attribute attribute, int invested, RPGCharacterStats rpgCharacterStats, boolean fixDisplay) {
        AttributeType attributeType = attribute.getAttributeType();
        attributeInvested.put(attributeType, invested);
        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            attribute.onValueChange(rpgCharacterStats, invested);
        }
    }

    public void investPoint(Attribute attribute, int amount, RPGCharacterStats rpgCharacterStats, boolean fixDisplay) {
        int invested = 0;
        AttributeType attributeType = attribute.getAttributeType();
        if (attributeInvested.containsKey(attributeType)) {
            invested = attributeInvested.get(attributeType);
        }
        invested += amount;
        attributeInvested.put(attributeType, invested);
        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            attribute.onValueChange(rpgCharacterStats, invested);
        }
    }

    public void downgradePoint(Attribute attribute, int amount, RPGCharacterStats rpgCharacterStats, boolean fixDisplay) {
        int invested = 0;
        AttributeType attributeType = attribute.getAttributeType();
        if (attributeInvested.containsKey(attributeType)) {
            invested = attributeInvested.get(attributeType);
        }
        invested -= amount;
        attributeInvested.put(attributeType, invested);
        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            attribute.onValueChange(rpgCharacterStats, invested);
        }
    }

    public void resetAttributes(RPGCharacterStats rpgCharacterStats) {
        attributeInvested.clear();

        int invested = 0;
        if (attributeInvested.containsKey(AttributeType.BONUS_MAX_HEALTH)) {
            invested = attributeInvested.get(AttributeType.BONUS_MAX_HEALTH);
        }
        rpgCharacterStats.onMaxHealthChange(invested);

        invested = 0;
        if (attributeInvested.containsKey(AttributeType.BONUS_MAX_MANA)) {
            invested = attributeInvested.get(AttributeType.BONUS_MAX_MANA);
        }
        rpgCharacterStats.onCurrentManaChange(invested);
    }

    public int getInvestedAttributePoints() {
        int total = 0;
        for (int invested : attributeInvested.values()) {
            total += invested;
        }

        return total;
    }

    public int getAttributePointsLeftToSpend(int totalExp) {
        int level = RPGCharacterExperienceManager.getLevel(totalExp);

        int inventedPointsOnAttributes = getInvestedAttributePoints();

        int pointsPerLevel = 1;

        return (level * pointsPerLevel) - inventedPointsOnAttributes;
    }

    public HashMap<AttributeType, Integer> getAttributeInvestedMap() {
        return attributeInvested;
    }
}
