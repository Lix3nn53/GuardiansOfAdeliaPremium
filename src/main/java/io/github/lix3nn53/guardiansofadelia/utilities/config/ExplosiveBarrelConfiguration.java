package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.MMSpawnerManager;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import io.github.lix3nn53.guardiansofadelia.interactables.ExplosiveBarrel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExplosiveBarrelConfiguration {

    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "world" + File.separator + "interactables";
    public static String MOB_KEY = null;
    private static FileConfiguration configFile;

    static void createConfigs() {
        configFile = ConfigurationUtils.createConfig(filePath, "explosiveBarrels.yml");
    }

    static void loadConfigs() {
        load();
    }

    static void writeConfigs() {
        write("explosiveBarrels.yml");
    }

    private static void load() {
        MOB_KEY = configFile.getString("mobKey");

        int themeCount = ConfigurationUtils.getChildComponentCount(configFile, "barrel");

        for (int i = 1; i <= themeCount; i++) {
            ConfigurationSection section = configFile.getConfigurationSection("barrel" + i);
            String worldString = section.getString("world");
            World world = Bukkit.getWorld(worldString);
            float x = (float) section.getDouble("x");
            float y = (float) section.getDouble("y");
            float z = (float) section.getDouble("z");

            ExplosiveBarrel explosiveBarrel = new ExplosiveBarrel(MOB_KEY, new Location(world, x, y, z));

            MMSpawnerManager.addGlobalSpawner(explosiveBarrel);
        }
    }

    private static void write(String fileName) {
        HashMap<String, List<MMSpawnerOpenWorld>> chunkKeyToLootChests = MMSpawnerManager.getChunkKeyToSpawners();

        int i = 1;
        for (String chunkKey : chunkKeyToLootChests.keySet()) {
            List<MMSpawnerOpenWorld> openWorldSpawners = chunkKeyToLootChests.get(chunkKey);

            for (MMSpawnerOpenWorld spawner : openWorldSpawners) {
                if (spawner instanceof ExplosiveBarrel explosiveBarrel) {
                    Location location = explosiveBarrel.getLocation();

                    configFile.set("barrel" + i + ".world", location.getWorld().getName());
                    configFile.set("barrel" + i + ".x", location.getX());
                    configFile.set("barrel" + i + ".y", location.getY());
                    configFile.set("barrel" + i + ".z", location.getZ());

                    i++;
                }
            }
        }

        try {
            configFile.save(filePath + File.separator + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
