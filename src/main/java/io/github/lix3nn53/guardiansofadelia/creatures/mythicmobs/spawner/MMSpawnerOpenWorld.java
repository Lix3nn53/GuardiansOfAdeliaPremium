package io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MMSpawnerOpenWorld extends MMSpawner {

    private static final long COOLDOWN_IN_MINUTES_MIN = 4;
    private static final long COOLDOWN_IN_MINUTES_MAX = 12;

    protected boolean isOnCooldown = false;
    private BukkitTask cooldownTask;

    public MMSpawnerOpenWorld(String mobKey, Location location) {
        super(mobKey, location);
    }

    @Override
    public LivingEntity spawn() {
        if (isOnCooldown) return null;

        return super.spawn();
    }

    @Override
    public void onDeath() {
        super.onDeath();
        GuardiansOfAdelia.getInstance().getLogger().info("Debug death MMSpawnerOpenWorld");

        isOnCooldown = true;

        long cooldownInMinutes = (long) (COOLDOWN_IN_MINUTES_MIN + (Math.random() * (COOLDOWN_IN_MINUTES_MAX - COOLDOWN_IN_MINUTES_MIN)));

        if (cooldownTask != null) {
            cooldownTask.cancel();
        }

        cooldownTask = new BukkitRunnable() {
            @Override
            public void run() {
                isOnCooldown = false;
                if (location.getChunk().isLoaded()) {
                    // startPlayingParticles();
                    spawn();
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), 20 * 60 * cooldownInMinutes);
    }
}
