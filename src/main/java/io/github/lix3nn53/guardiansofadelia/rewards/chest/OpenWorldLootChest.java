package io.github.lix3nn53.guardiansofadelia.rewards.chest;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.creatures.drops.MobDropGenerator;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class OpenWorldLootChest extends ALootChest {

    private static final long COOLDOWN_IN_MINUTES_MIN = 4;
    private static final long COOLDOWN_IN_MINUTES_MAX = 12;

    private boolean isOnCooldown = false;

    public OpenWorldLootChest(Location location, LootChestTier lootChestTier) {
        super(location, lootChestTier);
        // startPlayingParticles();
    }

    @Override
    public LivingEntity spawn() {
        if (isOnCooldown) return null;

        return super.spawn();
    }

    @Override
    public List<ItemStack> onDeath() {
        isOnCooldown = true;

        long cooldownInMinutes = (long) (COOLDOWN_IN_MINUTES_MIN + (Math.random() * (COOLDOWN_IN_MINUTES_MAX - COOLDOWN_IN_MINUTES_MIN)));

        new BukkitRunnable() {
            @Override
            public void run() {
                isOnCooldown = false;
                if (location.getChunk().isLoaded()) {
                    // startPlayingParticles();
                    spawn();
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), 20 * 60 * cooldownInMinutes);

        List<ItemStack> result = super.onDeath();

        GearLevel gearLevel = this.lootChestTier.getRandomGearLevel();

        List<ItemStack> drops = MobDropGenerator.getDrops(gearLevel, false, true);

        result.addAll(drops);

        return result;
    }
}
