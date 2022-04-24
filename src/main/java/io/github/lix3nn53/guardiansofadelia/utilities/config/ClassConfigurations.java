package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.AttributeType;
import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfo;
import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfoType;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.element.ElementType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillDataForTree;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillTier;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.SkillComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.config.SkillComponentLoader;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeDirection;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeOffset;
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

        YamlConfiguration config = ConfigurationUtils.createConfig(filePath, "config.yml");
        String startingClass = config.getString("startingClass");
        RPGClassManager.setStartingClass(startingClass.toUpperCase());

        List<String> directories = ConfigurationUtils.getAllDirectoriesInFile(filePath);

        for (String className : directories) {
            List<String> skillsFile = ConfigurationUtils.getAllDirectoriesInFile(filePath + File.separator + className + File.separator + "skills");
            YamlConfiguration classConfig = ConfigurationUtils.createConfig(filePath + File.separator + className, "class.yml");

            loadClass(className, classConfig, skillsFile);
        }
    }

    private static void loadClass(String className, YamlConfiguration classConfig, List<String> skillsFile) {
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
        for (String skillTierStr : skillsFile) {
            SkillTier skillTier = SkillTier.valueOf(skillTierStr.toUpperCase());

            HashMap<String, YamlConfiguration> skillConfigs = ConfigurationUtils.getAllConfigsInFile(filePath + File.separator + className + File.separator + "skills" + File.separator + skillTierStr);

            for (String skillName : skillConfigs.keySet()) {
                YamlConfiguration skillConfig = skillConfigs.get(skillName);
                Skill skill = loadSkill(skillName, skillConfig, skillTier);
                int id = skill.getId();
                if (skillSet.containsKey(id)) {
                    GuardiansOfAdelia.getInstance().getLogger().warning("Skill id " + id + " is already in use!");
                }
                skillSet.put(id, skill);
            }
        }

        YamlConfiguration skillTreeConfig = ConfigurationUtils.createConfig(filePath + File.separator + className, "skillTree.yml");
        List<Integer> rootSkills = loadSkillTree(skillSet, skillTreeConfig);

        SkillTree skillTree = new SkillTree(skillSet, rootSkills);

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

    private static Skill loadSkill(String name, ConfigurationSection skillSection, SkillTier skillTier) {
        int id = skillSection.getInt("id");
        List<String> description = skillSection.getStringList("description");
        int customModelData = skillSection.getInt("customModelData");
        List<Integer> reqPoints = skillTier.getDefaultReqPoints();
        List<Integer> manaCosts = skillSection.getIntegerList("manaCosts");
        List<Integer> cooldowns = skillSection.getIntegerList("cooldowns");

        Skill skill = new Skill(id, name, 4, Material.IRON_HOE, customModelData, description, reqPoints, manaCosts, cooldowns);
        SkillComponent triggerComponent = SkillComponentLoader.loadSection(skillSection.getConfigurationSection("trigger"), id);
        skill.addTrigger(triggerComponent);

        int triggerCount = ConfigurationUtils.getChildComponentCount(skillSection, "trigger");
        for (int i = 1; i <= triggerCount; i++) {
            SkillComponent triggerComponentExtra = SkillComponentLoader.loadSection(skillSection.getConfigurationSection("trigger" + i), id);
            skill.addTrigger(triggerComponentExtra);
        }

        return skill;
    }

    private static List<Integer> loadSkillTree(HashMap<Integer, Skill> skills, ConfigurationSection skillTreeConfig) {
        List<Integer> rootSkills = new ArrayList<>();

        int rootSkillCount = ConfigurationUtils.getChildComponentCount(skillTreeConfig, "rootSkill");

        for (int i = 1; i <= rootSkillCount; i++) {
            ConfigurationSection rootSkillSection = skillTreeConfig.getConfigurationSection("rootSkill" + i);
            int rootSkillId = rootSkillSection.getInt("id");
            rootSkills.add(rootSkillId);

            int rootX = rootSkillSection.getInt("rootX");
            int rootY = rootSkillSection.getInt("rootY");
            SkillTreeOffset rootOffset = new SkillTreeOffset(rootX, rootY);

            applySkillDataForTreeForSelfAndChild(skills, rootSkillSection, rootSkillId, -1, rootOffset);
        }

        return rootSkills;
    }

    private static void applySkillDataForTreeForSelfAndChild(HashMap<Integer, Skill> skills, ConfigurationSection skillSection, int id, int parentId, SkillTreeOffset rootOffset) {
        HashMap<Integer, SkillTreeDirection> childSkillMap = new HashMap<>();

        int childSkillCount = ConfigurationUtils.getChildComponentCount(skillSection, "child");
        for (int i = 1; i <= childSkillCount; i++) {
            ConfigurationSection childSection = skillSection.getConfigurationSection("child" + i);
            int childSkillId = childSection.getInt("id");
            String directionStr = childSection.getString("direction");
            SkillTreeDirection direction = SkillTreeDirection.valueOf(directionStr);

            childSkillMap.put(childSkillId, direction);

            applySkillDataForTreeForSelfAndChild(skills, childSection, childSkillId, id, null);
        }

        SkillDataForTree skillDataForTree = new SkillDataForTree(parentId, childSkillMap, rootOffset);

        Skill skill = skills.get(id);
        skill.setSkillDataForTree(skillDataForTree);
    }
}