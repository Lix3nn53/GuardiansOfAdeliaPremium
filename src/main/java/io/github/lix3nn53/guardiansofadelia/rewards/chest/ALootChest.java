package io.github.lix3nn53.guardiansofadelia.rewards.chest;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.utilities.PersistentDataContainerUtil;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ALootChest {

    protected final LootChestTier lootChestTier;
    protected Location location;
    protected LivingEntity spawned;

    public ALootChest(Location location, LootChestTier lootChestTier) {
        this.location = location;
        this.lootChestTier = lootChestTier;
        // startPlayingParticles();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LootChestTier getLootChestTier() {
        return lootChestTier;
    }

    public LivingEntity spawn() {
        String key = this.lootChestTier.getMobKey();

        BukkitAPIHelper apiHelper = MythicBukkit.inst().getAPIHelper();
        Entity entity = null;
        try {
            entity = apiHelper.spawnMythicMob(key, this.location, 1);
        } catch (InvalidMobTypeException e) {
            GuardiansOfAdelia.getInstance().getLogger().info("LootChest mythicmob code error: " + key);
            e.printStackTrace();
        }
        if (entity == null) return null;
        if (!(entity instanceof LivingEntity chest)) {
            GuardiansOfAdelia.getInstance().getLogger().info("LootChest is not LivingEntity, code: " + key);
            return null;
        }

        PersistentDataContainerUtil.putString(LootChestManager.LOOT_CHEST_KEY, this.lootChestTier.name(), entity);

        if (spawned != null) {
            spawned.remove();
        }

        spawned = chest;

        LootChestManager.onSpawn(this);

        return spawned;
    }

    public LivingEntity getSpawned() {
        return spawned;
    }

    public void despawn() {
        onDespawn();
        if (spawned != null) {
            spawned.remove();
            spawned = null;
        }
    }

    private void onDespawn() {
        if (spawned != null) {
            LootChestManager.onDespawn(spawned);
        }
    }

    public List<ItemStack> onDeath() {
        onDespawn();

        return lootChestTier.getLoot();
    }

    public void rotate() {
        float yaw = location.getYaw();
        yaw += 90;
        location.setYaw(yaw);

        if (spawned != null && spawned.isValid()) {
            spawned.teleport(location);
        }
    }
}
