package io.github.lix3nn53.guardiansofadelia.interactables;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import org.bukkit.Location;

public class ExplosiveBarrel extends MMSpawnerOpenWorld {

    public ExplosiveBarrel(String mobKey, Location location) {
        super(mobKey, location, 20 * 60, 20 * 300);
    }

}
