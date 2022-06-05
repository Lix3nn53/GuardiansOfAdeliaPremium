package io.github.lix3nn53.guardiansofadelia.utilities.hologram;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CosmeticHologram extends FakeHologram {

    private BukkitTask rotationTask;

    public CosmeticHologram(int entityID, Location location, String text) {
        super(entityID, location, text);
    }

    public void start(Player source, ItemStack helmet) {
        show(source);
        mount(source, source);
        setHelmet(source, helmet);

        source.getNearbyEntities(RANGE, RANGE, RANGE).forEach(entity -> {
            if (entity instanceof Player player) {
                show(player);
                mount(player, source);
                setHelmet(player, helmet);
            }
        });

        startRotationRunnable(source);
    }

    private void startRotationRunnable(Player source) {
        if (rotationTask != null) {
            rotationTask.cancel();
        }

        this.rotationTask = new BukkitRunnable() {

            @Override
            public void run() {
                if (!source.isOnline()) {
                    cancel();
                    return;
                }

                Location location = source.getLocation();

                look(source, location);
                rotateHead(source, location);

                source.getNearbyEntities(RANGE, RANGE, RANGE).forEach(entity -> {
                    if (entity instanceof Player player) {
                        look(player, location);
                        rotateHead(player, location);
                    }
                });
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 2L, 10L);
    }

    @Override
    public void destroy() {
        super.destroy();

        if (rotationTask != null) {
            rotationTask.cancel();
            rotationTask = null;
        }
    }
}
