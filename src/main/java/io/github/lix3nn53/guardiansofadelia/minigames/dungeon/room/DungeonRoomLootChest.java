package io.github.lix3nn53.guardiansofadelia.minigames.dungeon.room;

import io.github.lix3nn53.guardiansofadelia.creatures.drops.MobDropGenerator;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawner;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.rewards.chest.LootChestTier;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class DungeonRoomLootChest extends MMSpawner {

    private final LootChestTier lootChestTier;
    private final Vector offset;
    private float yaw;
    private final float pitch;

    public DungeonRoomLootChest(LootChestTier lootChestTier, Vector offset, float yaw, float pitch) {
        super(lootChestTier.getMobKey(), null);
        this.lootChestTier = lootChestTier;
        this.offset = offset;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public LivingEntity spawn() {
        throw new IllegalArgumentException("This method is not implemented for DungeonRoomLootChest");
    }

    public boolean spawn(Location dungeonStart, int roomNo, int roomCount) {
        float random = (float) Math.random();

        float chance = ((float) roomNo) / roomCount;

        if (chance < random) {
            return false;
        }

        Location add = dungeonStart.clone().add(offset);

        add.setYaw(yaw);
        add.setPitch(pitch);

        super.setLocation(add);

        super.spawn();

        return true;
    }

    public Vector getOffset() {
        return offset;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    @Override
    public void rotate() {
        super.rotate();

        this.yaw += 90;
    }

    @Override
    public void onDeath() {
        super.onDeath();

        List<ItemStack> result = lootChestTier.getLoot();

        GearLevel gearLevel = this.lootChestTier.getRandomGearLevel();

        List<ItemStack> drops = MobDropGenerator.getDrops(gearLevel, true, true);

        result.addAll(drops);

        World world = location.getWorld();
        for (ItemStack itemStack : result) {
            world.dropItemNaturally(location, itemStack);
        }
    }
}
