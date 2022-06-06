package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.rewards.chest.LootChestManager;
import io.github.lix3nn53.guardiansofadelia.rewards.chest.LootChestTier;
import io.github.lix3nn53.guardiansofadelia.rewards.chest.OpenWorldLootChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LootChestConfiguration {

    private static FileConfiguration lootChestsConfig;
    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "rewards";

    static void createConfigs() {
        lootChestsConfig = ConfigurationUtils.createConfig(filePath, "lootChests.yml");
    }

    static void loadConfigs() {
        loadLootChests();
    }

    static void writeConfigs() {
        writeLootChests("lootChests.yml");
    }

    private static void loadLootChests() {
        int themeCount = ConfigurationUtils.getChildComponentCount(lootChestsConfig, "chest");

        for (int i = 1; i <= themeCount; i++) {
            ConfigurationSection section = lootChestsConfig.getConfigurationSection("chest" + i);
            String worldString = section.getString("world");
            World world = Bukkit.getWorld(worldString);
            float x = (float) section.getDouble("x");
            float y = (float) section.getDouble("y");
            float z = (float) section.getDouble("z");
            float yaw = (float) section.getDouble("yaw");
            float pitch = (float) section.getDouble("pitch");
            Location location = new Location(world, x, y, z, yaw, pitch);
            String tierStr = section.getString("tier");
            LootChestTier lootChestTier = LootChestTier.valueOf(tierStr);
            OpenWorldLootChest lootChest = new OpenWorldLootChest(location, lootChestTier);

            LootChestManager.addLootChest(lootChest);
        }
    }

    private static void writeLootChests(String fileName) {
        HashMap<String, List<OpenWorldLootChest>> chunkKeyToLootChests = LootChestManager.getChunkKeyToLootChests();

        int i = 1;
        for (String chunkKey : chunkKeyToLootChests.keySet()) {
            List<OpenWorldLootChest> lootChests = chunkKeyToLootChests.get(chunkKey);

            for (OpenWorldLootChest lootChest : lootChests) {
                Location location = lootChest.getLocation();
                LootChestTier lootChestTier = lootChest.getLootChestTier();

                lootChestsConfig.set("chest" + i + ".world", location.getWorld().getName());
                lootChestsConfig.set("chest" + i + ".x", location.getX());
                lootChestsConfig.set("chest" + i + ".y", location.getY());
                lootChestsConfig.set("chest" + i + ".z", location.getZ());
                lootChestsConfig.set("chest" + i + ".yaw", location.getYaw());
                lootChestsConfig.set("chest" + i + ".pitch", location.getPitch());
                lootChestsConfig.set("chest" + i + ".tier", lootChestTier.name());

                i++;
            }
        }

        try {
            String filePath = ConfigManager.DATA_FOLDER + File.separator + "rewards";

            lootChestsConfig.save(filePath + File.separator + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
