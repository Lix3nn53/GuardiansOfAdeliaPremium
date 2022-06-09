package io.github.lix3nn53.guardiansofadelia.interactables;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import org.bukkit.Location;

public class GenericInteractable extends MMSpawnerOpenWorld {

    public GenericInteractable(String mobKey, Location location, long cooldownMin, long cooldownMax) {
        super(mobKey, location, cooldownMin, cooldownMax);
    }

}
