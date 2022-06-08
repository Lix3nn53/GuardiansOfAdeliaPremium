package io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MMSpawnerOpenWorld extends MMSpawner {

    private final long cooldownMin;
    private final long cooldownMax;

    protected boolean isOnCooldown = false;
    private BukkitTask cooldownTask;

    public MMSpawnerOpenWorld(String mobKey, Location location, long cooldownMin, long cooldownMax) {
        super(mobKey, location);
        this.cooldownMin = cooldownMin;
        this.cooldownMax = cooldownMax;
    }

    @Override
    public void spawn() {
        if (isOnCooldown) return;

        super.spawn();
    }

    @Override
    public void onDeath() {
        super.onDeath();
        GuardiansOfAdelia.getInstance().getLogger().info("Debug death MMSpawnerOpenWorld");

        isOnCooldown = true;

        final long finalCooldown = (long) (cooldownMin + (Math.random() * (cooldownMax - cooldownMin)));

        if (cooldownTask != null) {
            cooldownTask.cancel();
        }

        cooldownTask = new BukkitRunnable() {
            @Override
            public void run() {
                isOnCooldown = false;
                if (getLocation().getChunk().isLoaded()) {
                    // startPlayingParticles();
                    spawn();
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), 20 * finalCooldown);
    }
}
