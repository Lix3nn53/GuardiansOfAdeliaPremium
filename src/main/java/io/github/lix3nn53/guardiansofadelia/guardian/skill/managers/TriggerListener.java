package io.github.lix3nn53.guardiansofadelia.guardian.skill.managers;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.commands.admin.CommandAdmin;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.TriggerComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TriggerListener {
    // Every skill can have only one trigger of a type

    // Player to skillid to triggerType to trigger
    private static final HashMap<Player, HashMap<Integer, HashMap<String, TriggerComponent>>> playerToSkillToTriggerClassToTrigger = new HashMap<>();

    public static void onPlayerQuit(Player player) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> skillToTriggers = playerToSkillToTriggerClassToTrigger.get(player);

            for (HashMap<String, TriggerComponent> triggers : skillToTriggers.values()) {
                for (TriggerComponent trigger : triggers.values()) {
                    trigger.stopEffects();
                }
            }

            playerToSkillToTriggerClassToTrigger.remove(player);
        }
    }

    public static void add(Player player, TriggerComponent triggerComponent, int skillId) {
        HashMap<Integer, HashMap<String, TriggerComponent>> map = new HashMap<>();
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            map = playerToSkillToTriggerClassToTrigger.get(player);
        }
        HashMap<String, TriggerComponent> triggerComponents = new HashMap<>();
        if (map.containsKey(skillId)) {
            triggerComponents = map.get(skillId);
        }
        triggerComponents.put(triggerComponent.getClass().getName(), triggerComponent);
        playerToSkillToTriggerClassToTrigger.put(player, map);
        if (CommandAdmin.DEBUG_MODE) player.sendMessage("ADD, new size: " + map.size());
    }

    public static void remove(Player player, String triggerType, int skillId) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            HashMap<String, TriggerComponent> triggers = map.get(skillId);
            triggers.remove(triggerType);
            if (map.isEmpty()) {
                playerToSkillToTriggerClassToTrigger.remove(player);
            } else {
                playerToSkillToTriggerClassToTrigger.put(player, map);
            }
            if (CommandAdmin.DEBUG_MODE) player.sendMessage("REMOVE trigger, new size: " + map.size());
        }
    }

    public static void onPlayerLandGround(Player player) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            List<Integer> toRemove = new ArrayList<>();

            for (int skillId : map.keySet()) {
                HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
                for (TriggerComponent triggerComponent : triggerComponents.values()) {
                    if (!(triggerComponent instanceof LandTrigger)) continue;
                    LandTrigger trigger = (LandTrigger) triggerComponent;
                    trigger.callback(player);

                    toRemove.add(skillId); // always remove this trigger even if child fails somehow
                }
            }

            for (int skillId : toRemove) {
                TriggerListener.remove(player, LandTrigger.class.getName(), skillId);
            }
        }
    }

    public static void onPlayerTookDamage(Player player, LivingEntity attacker) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            HashMap<Integer, TookDamageTrigger> toRemove = new HashMap<>();

            for (int skillId : map.keySet()) {
                HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
                for (TriggerComponent triggerComponent : triggerComponents.values()) {
                    if (!(triggerComponent instanceof TookDamageTrigger)) continue;
                    TookDamageTrigger trigger = (TookDamageTrigger) triggerComponent;
                    boolean callback = trigger.callback(player, attacker);

                    if (callback) {
                        toRemove.put(skillId, trigger);
                    }
                }
            }

            for (int skillId : toRemove.keySet()) {
                TookDamageTrigger trigger = toRemove.get(skillId);
                int skillLevel = trigger.getSkillLevel();
                List<Integer> cooldowns = trigger.getCooldowns();

                if (!cooldowns.isEmpty()) {
                    TriggerListener.remove(player, TookDamageTrigger.class.getName(), skillId);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            TriggerListener.add(player, trigger, skillId);
                        }
                    }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), cooldowns.get(skillLevel - 1));
                }
            }
        }
    }

    public static void onPlayerNormalAttack(Player player, LivingEntity target, boolean isProjectile) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            HashMap<Integer, NormalAttackTrigger> toRemove = new HashMap<>();

            for (int skillId : map.keySet()) {
                HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
                for (TriggerComponent triggerComponent : triggerComponents.values()) {
                    if (!(triggerComponent instanceof NormalAttackTrigger)) continue;
                    NormalAttackTrigger trigger = (NormalAttackTrigger) triggerComponent;
                    boolean callback = trigger.callback(player, target, isProjectile);

                    if (callback) {
                        toRemove.put(skillId, trigger);
                    }
                }
            }

            for (int skillId : toRemove.keySet()) {
                NormalAttackTrigger trigger = toRemove.get(skillId);
                int skillLevel = trigger.getSkillLevel();
                List<Integer> cooldowns = trigger.getCooldowns();

                if (!cooldowns.isEmpty()) {
                    TriggerListener.remove(player, NormalAttackTrigger.class.getName(), skillId);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            TriggerListener.add(player, trigger, skillId);
                        }
                    }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), cooldowns.get(skillLevel - 1));
                }
            }
        }
    }

    public static void onPlayerSkillAttack(Player player, LivingEntity target) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            HashMap<Integer, SkillAttackTrigger> toRemove = new HashMap<>();

            for (int skillId : map.keySet()) {
                HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
                for (TriggerComponent triggerComponent : triggerComponents.values()) {
                    if (!(triggerComponent instanceof SkillAttackTrigger)) continue;
                    SkillAttackTrigger trigger = (SkillAttackTrigger) triggerComponent;
                    boolean callback = trigger.callback(player, target);

                    if (callback) {
                        toRemove.put(skillId, trigger);
                    }
                }
            }

            for (int skillId : toRemove.keySet()) {
                SkillAttackTrigger trigger = toRemove.get(skillId);
                int skillLevel = trigger.getSkillLevel();
                List<Integer> cooldowns = trigger.getCooldowns();

                if (!cooldowns.isEmpty()) {
                    TriggerListener.remove(player, SkillAttackTrigger.class.getName(), skillId);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            TriggerListener.add(player, trigger, skillId);
                        }
                    }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), cooldowns.get(skillLevel - 1));
                }
            }
        }
    }

    public static void onPlayerSkillCast(Player player) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            HashMap<Integer, SkillCastTrigger> toRemove = new HashMap<>();

            for (int skillId : map.keySet()) {
                HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
                for (TriggerComponent triggerComponent : triggerComponents.values()) {
                    if (!(triggerComponent instanceof SkillCastTrigger)) continue;
                    SkillCastTrigger trigger = (SkillCastTrigger) triggerComponent;
                    boolean callback = trigger.callback(player);

                    if (callback) {
                        toRemove.put(skillId, trigger);
                    }
                }
            }

            for (int skillId : toRemove.keySet()) {
                SkillCastTrigger trigger = toRemove.get(skillId);
                int skillLevel = trigger.getSkillLevel();
                List<Integer> cooldowns = trigger.getCooldowns();

                if (!cooldowns.isEmpty()) {
                    TriggerListener.remove(player, SkillCastTrigger.class.getName(), skillId);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            TriggerListener.add(player, trigger, skillId);
                        }
                    }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), cooldowns.get(skillLevel - 1));
                }
            }
        }
    }

    public static void onPlayerShootCrossbow(Player player, Arrow arrow) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            for (HashMap<String, TriggerComponent> skillTriggers : map.values()) {
                for (TriggerComponent triggerComponent : skillTriggers.values()) {
                    if (!(triggerComponent instanceof AddPiercingToArrowShootFromCrossbowTrigger)) continue;
                    AddPiercingToArrowShootFromCrossbowTrigger trigger = (AddPiercingToArrowShootFromCrossbowTrigger) triggerComponent;
                    trigger.callback(arrow);
                }
            }
        }
    }

    public static void onPlayerSavedEntitySpawn(Player player, LivingEntity created) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            HashMap<Integer, SavedEntitySpawnTrigger> toRemove = new HashMap<>();

            for (int skillId : map.keySet()) {
                HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
                for (TriggerComponent triggerComponent : triggerComponents.values()) {
                    if (!(triggerComponent instanceof SavedEntitySpawnTrigger)) continue;
                    SavedEntitySpawnTrigger trigger = (SavedEntitySpawnTrigger) triggerComponent;
                    boolean callback = trigger.callback(player, created);

                    if (callback) {
                        toRemove.put(skillId, trigger);
                    }
                }
            }

            for (int skillId : toRemove.keySet()) {
                SavedEntitySpawnTrigger trigger = toRemove.get(skillId);
                int skillLevel = trigger.getSkillLevel();
                List<Integer> cooldowns = trigger.getCooldowns();

                if (!cooldowns.isEmpty()) {
                    TriggerListener.remove(player, SavedEntitySpawnTrigger.class.getName(), skillId);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            TriggerListener.add(player, trigger, skillId);
                        }
                    }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), cooldowns.get(skillLevel - 1));
                }
            }
        }
    }

    public static void onPlayerCompanionSpawn(Player player, LivingEntity spawned) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> map = playerToSkillToTriggerClassToTrigger.get(player);

            HashMap<Integer, CompanionSpawnTrigger> toRemove = new HashMap<>();

            for (int skillId : map.keySet()) {
                HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
                for (TriggerComponent triggerComponent : triggerComponents.values()) {
                    if (!(triggerComponent instanceof CompanionSpawnTrigger)) continue;
                    CompanionSpawnTrigger trigger = (CompanionSpawnTrigger) triggerComponent;
                    boolean callback = trigger.callback(player, spawned);

                    if (callback) {
                        toRemove.put(skillId, trigger);
                    }
                }
            }

            for (int skillId : toRemove.keySet()) {
                CompanionSpawnTrigger trigger = toRemove.get(skillId);
                int skillLevel = trigger.getSkillLevel();
                List<Integer> cooldowns = trigger.getCooldowns();

                if (!cooldowns.isEmpty()) {
                    TriggerListener.remove(player, CompanionSpawnTrigger.class.getName(), skillId);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            TriggerListener.add(player, trigger, skillId);
                        }
                    }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), cooldowns.get(skillLevel - 1));
                }
            }
        }
    }

    public static void onSkillUpgrade(Player player, InitializeTrigger initializeTrigger, int skillId, int nextSkillLevel, int castCounter) {
        stopInit(player, skillId); //stop old init
        initializeTrigger.stopPreviousEffects(player);

        List<LivingEntity> targets = new ArrayList<>();
        targets.add(player);

        initializeTrigger.startEffects(player, nextSkillLevel, targets, castCounter, skillId);
    }

    public static void onSkillDowngrade(Player player, InitializeTrigger initializeTrigger, int skillId, int nextSkillLevel, int castCounter) {
        stopInit(player, skillId);
        initializeTrigger.stopPreviousEffects(player);
        if (nextSkillLevel == 0) return;

        List<LivingEntity> targets = new ArrayList<>();
        targets.add(player);

        initializeTrigger.startEffects(player, nextSkillLevel, targets, castCounter, skillId);
    }

    private static void stopInit(Player player, int skillId) {
        if (playerToSkillToTriggerClassToTrigger.containsKey(player)) {
            HashMap<Integer, HashMap<String, TriggerComponent>> skillToTriggers = playerToSkillToTriggerClassToTrigger.get(player);

            HashMap<String, TriggerComponent> removed = skillToTriggers.remove(skillId);

            for (TriggerComponent trigger : removed.values()) {
                trigger.stopEffects();
            }

            if (skillToTriggers.isEmpty()) {
                playerToSkillToTriggerClassToTrigger.remove(player);
            } else {
                playerToSkillToTriggerClassToTrigger.put(player, skillToTriggers);
            }
        }
    }
}
