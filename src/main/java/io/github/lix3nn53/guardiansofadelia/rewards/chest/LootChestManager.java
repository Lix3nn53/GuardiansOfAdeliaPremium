package io.github.lix3nn53.guardiansofadelia.rewards.chest;

import io.github.lix3nn53.guardiansofadelia.utilities.LocationUtils;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LootChestManager {

    public static final String LOOT_CHEST_KEY = "lootChest";
    private static final HashMap<String, List<OpenWorldLootChest>> chunkKeyToLootChests = new HashMap<>();

    private static final HashMap<LivingEntity, ALootChest> spawnedLootChests = new HashMap<>();

    public static void onChunkLoad(String chunkKey) {
        if (chunkKeyToLootChests.containsKey(chunkKey)) {
            List<OpenWorldLootChest> lootChests = chunkKeyToLootChests.get(chunkKey);

            for (OpenWorldLootChest lootChest : lootChests) {
                // lootChest.startPlayingParticles();
                LivingEntity spawn = lootChest.spawn();
            }
        }
    }

    public static void onChunkUnload(String chunkKey) {
        if (chunkKeyToLootChests.containsKey(chunkKey)) {
            List<OpenWorldLootChest> lootChests = chunkKeyToLootChests.get(chunkKey);

            for (OpenWorldLootChest lootChest : lootChests) {
                // lootChest.stopPlayingParticles();
                lootChest.despawn();
            }
        }
    }

    /*public static LootChest isLootChest(Location location) {
        String chunkKey = LocationUtils.getChunkKey(location);
        if (chunkKeyToLootChests.containsKey(chunkKey)) {
            List<LootChest> lootChests = chunkKeyToLootChests.get(chunkKey);

            for (LootChest lootChest : lootChests) {
                float v = (float) lootChest.getLocation().distanceSquared(location);
                if (v < 4) {
                    return lootChest;
                }
            }
        }

        return null;
    }*/

    public static ALootChest getLootChest(LivingEntity livingEntity) {
        return spawnedLootChests.get(livingEntity);
    }

    public static void addLootChest(OpenWorldLootChest lootChest) {
        String chunkKey = LocationUtils.getChunkKey(lootChest.getLocation());
        if (chunkKeyToLootChests.containsKey(chunkKey)) {
            List<OpenWorldLootChest> list = chunkKeyToLootChests.get(chunkKey);
            list.add(lootChest);
            chunkKeyToLootChests.put(chunkKey, list);
        } else {
            List<OpenWorldLootChest> holograms = new ArrayList<>();
            holograms.add(lootChest);
            chunkKeyToLootChests.put(chunkKey, holograms);
        }
    }

    public static HashMap<String, List<OpenWorldLootChest>> getChunkKeyToLootChests() {
        return chunkKeyToLootChests;
    }

    public static void onSpawn(ALootChest lootChest) {
        spawnedLootChests.put(lootChest.getSpawned(), lootChest);
    }

    public static void onDespawn(LivingEntity livingEntity) {
        spawnedLootChests.remove(livingEntity);
    }
}
