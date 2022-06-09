package io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MMSpawnerOpenWorld extends MMSpawner {

    private final long cooldownMin;
    private final long cooldownMax;

    private BukkitTask cooldownTask;

    public MMSpawnerOpenWorld(String mobKey, Location location, long cooldownMin, long cooldownMax) {
        super(mobKey, location);
        this.cooldownMin = cooldownMin;
        this.cooldownMax = cooldownMax;
    }

    @Override
    public void spawn() {
        GuardiansOfAdelia.getInstance().getLogger().info("Debug spawn MMSpawnerOpenWorld: " + this.getClass().getSimpleName());
        if (cooldownTask != null) {
            GuardiansOfAdelia.getInstance().getLogger().info("Cooldown isCancelled: " + cooldownTask.isCancelled());
        } else {
            GuardiansOfAdelia.getInstance().getLogger().info("Cooldown is null");
        }
        if (cooldownTask != null && !cooldownTask.isCancelled()) return;
        GuardiansOfAdelia.getInstance().getLogger().info("Debug spawn MMSpawnerOpenWorld pass cooldown check");

        super.spawn();
    }

    @Override
    public void onDeath() {
        super.onDeath();

        final long finalCooldown = (long) (cooldownMin + (Math.random() * (cooldownMax - cooldownMin)));
        GuardiansOfAdelia.getInstance().getLogger().info("Debug death MMSpawnerOpenWorld cooldown: " + finalCooldown);

        if (cooldownTask != null) {
            cooldownTask.cancel();
        }

        cooldownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (getLocation().getChunk().isLoaded()) {
                    GuardiansOfAdelia.getInstance().getLogger().info("Debug cooldown finished MMSpawnerOpenWorld");
                    cancel();
                    spawn();
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), finalCooldown);
    }
}
