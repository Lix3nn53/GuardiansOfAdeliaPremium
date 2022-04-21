package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.AttributeType;
import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfo;
import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfoType;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.element.ElementType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.SkillComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.config.SkillComponentLoader;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ArmorGearType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ShieldGearType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearType;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassConfigurations {

    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "classes";

    public static void loadConfigs() {
        loadClassConfigs();
    }

    private static void loadClassConfigs() {
        RPGClassManager.clearClasses();

        List<String> directories = ConfigurationUtils.getAllDirectoriesInFile(filePath);

        for (String className : directories) {
            HashMap<String, YamlConfiguration> skillConfigs = ConfigurationUtils.getAllConfigsInFile(filePath + File.separator + className + File.separator + "skills");
            YamlConfiguration classConfig = ConfigurationUtils.createConfig(filePath + File.separator + className, "class.yml");

            loadClass(className, classConfig, skillConfigs);
        }
    }

    private static void loadClass(String className, YamlConfiguration classConfig, HashMap<String, YamlConfiguration> skillConfigs) {
        GuardiansOfAdelia.getInstance().getLogger().info("className: " + className);

        String colorStr = classConfig.getString("color");
        GuardiansOfAdelia.getInstance().getLogger().info("colorStr: " + colorStr);
        ChatPalette color = ChatPalette.valueOf(colorStr);

        String mainElementStr = classConfig.getString("mainElement");
        ElementType mainElement = ElementType.valueOf(mainElementStr);

        List<String> description = classConfig.getStringList("description");

        int classIconCustomModelData = classConfig.getInt("classIconCustomModelData");

        int attributeElementDamage = classConfig.getInt("attributeElementDamage");
        int attributeElementDefense = classConfig.getInt("attributeElementDefense");
        int attributeMaxHealth = classConfig.getInt("attributeMaxHealth");
        int attributeMaxMana = classConfig.getInt("attributeMaxMana");
        int attributeCriticalChance = classConfig.getInt("attributeCriticalChance");
        HashMap<AttributeType, Integer> attributeTiers = new HashMap<>();
        attributeTiers.put(AttributeType.BONUS_ELEMENT_DAMAGE, attributeElementDamage);
        attributeTiers.put(AttributeType.BONUS_ELEMENT_DEFENSE, attributeElementDefense);
        attributeTiers.put(AttributeType.BONUS_MAX_HEALTH, attributeMaxHealth);
        attributeTiers.put(AttributeType.BONUS_MAX_MANA, attributeMaxMana);
        attributeTiers.put(AttributeType.BONUS_CRITICAL_CHANCE, attributeCriticalChance);

        HashMap<Integer, Skill> skillSet = new HashMap<>();
        for (String skillName : skillConfigs.keySet()) {
            YamlConfiguration skillConfig = skillConfigs.get(skillName);
            Skill skill = loadSkill(skillConfig, 0);
            int id = skill.getId();
            if (skillSet.containsKey(id)) {
                GuardiansOfAdelia.getInstance().getLogger().warning("Skill id " + id + " is already in use!");
            }
            skillSet.put(id, skill);
        }
        SkillTree skillTree = new SkillTree(skillSet);

        // TODO make gear types unlock with skills not classes
        List<ShieldGearType> shieldGearTypes = new ArrayList<>();
        if (classConfig.contains("shieldGearTypes")) {
            List<String> gearTypes = classConfig.getStringList("shieldGearTypes");
            for (String gearType : gearTypes) {
                ShieldGearType shieldGearType = ShieldGearType.valueOf(gearType);
                shieldGearTypes.add(shieldGearType);
            }
        }
        List<WeaponGearType> weaponGearTypes = new ArrayList<>();
        if (classConfig.contains("weaponGearTypes")) {
            List<String> gearTypes = classConfig.getStringList("weaponGearTypes");
            for (String gearType : gearTypes) {
                WeaponGearType weaponGearType = WeaponGearType.valueOf(gearType);
                weaponGearTypes.add(weaponGearType);
            }
        }
        List<ArmorGearType> armorGearTypes = new ArrayList<>();
        if (classConfig.contains("armorGearTypes")) {
            List<String> gearTypes = classConfig.getStringList("armorGearTypes");
            for (String gearType : gearTypes) {
                ArmorGearType armorGearType = ArmorGearType.valueOf(gearType);
                armorGearTypes.add(armorGearType);
            }
        }

        // TODO make actionBarInfo unlock with skills not classes
        ActionBarInfoType actionBarInfoType = classConfig.contains("actionBarInfoType") ? ActionBarInfoType.valueOf(classConfig.getString("actionBarInfoType")) : null;
        String actionBarIcon = classConfig.getString("actionBarIcon");
        String actionBarKey = classConfig.getString("actionBarKey");

        ActionBarInfo actionBarInfo = new ActionBarInfo(actionBarInfoType, actionBarIcon, actionBarKey);

        RPGClass rpgClass = new RPGClass(color, mainElement, className, classIconCustomModelData, attributeTiers, skillTree,
                actionBarInfo, shieldGearTypes, weaponGearTypes, armorGearTypes, description);

        RPGClassManager.addClass(className, rpgClass);
    }

    private static Skill loadSkill(ConfigurationSection skillSection, int skillIndex) {
        int id = skillSection.getInt("id");
        int parentSkillId = skillSection.getInt("parentSkillId");
        String name = skillSection.getString("name");
        List<String> description = skillSection.getStringList("description");
        int customModelData = skillSection.getInt("customModelData");
        List<Integer> reqPoints = getDefaultReqPoints(skillIndex);
        List<Integer> manaCosts = skillSection.getIntegerList("manaCosts");
        List<Integer> cooldowns = skillSection.getIntegerList("cooldowns");

        Skill skill = new Skill(id, name, 4, Material.IRON_HOE, customModelData, description, reqPoints, manaCosts, cooldowns, parentSkillId);
        SkillComponent triggerComponent = SkillComponentLoader.loadSection(skillSection.getConfigurationSection("trigger"));
        skill.addTrigger(triggerComponent);

        int triggerCount = ConfigurationUtils.getChildComponentCount(skillSection, "trigger");
        for (int i = 1; i <= triggerCount; i++) {
            SkillComponent triggerComponentExtra = SkillComponentLoader.loadSection(skillSection.getConfigurationSection("trigger" + i));
            skill.addTrigger(triggerComponentExtra);
        }

        return skill;
    }

    private static List<Integer> getDefaultReqPoints(int skillIndex) {
        List<Integer> reqPoints = new ArrayList<>();
        if (skillIndex == 0) {
            reqPoints.add(1);
            reqPoints.add(1);
            reqPoints.add(1);
            reqPoints.add(1);
        } else if (skillIndex == 1) {
            reqPoints.add(1);
            reqPoints.add(1);
            reqPoints.add(1);
            reqPoints.add(1);
        } else if (skillIndex == 2) {
            reqPoints.add(1);
            reqPoints.add(1);
            reqPoints.add(1);
            reqPoints.add(1);
        } else if (skillIndex == 3) {
            reqPoints.add(2);
            reqPoints.add(2);
            reqPoints.add(2);
            reqPoints.add(2);
        } else if (skillIndex == 4) {
            reqPoints.add(3);
            reqPoints.add(3);
            reqPoints.add(3);
            reqPoints.add(3);
        }

        return reqPoints;
    }
}