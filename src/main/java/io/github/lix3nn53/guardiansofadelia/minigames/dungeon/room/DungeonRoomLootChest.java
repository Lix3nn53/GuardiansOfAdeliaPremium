package io.github.lix3nn53.guardiansofadelia.minigames.dungeon.room;

import io.github.lix3nn53.guardiansofadelia.creatures.drops.MobDropGenerator;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.rewards.chest.ALootChest;
import io.github.lix3nn53.guardiansofadelia.rewards.chest.LootChestTier;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class DungeonRoomLootChest extends ALootChest {

    private final Vector offset;
    private final float yaw;
    private final float pitch;

    public DungeonRoomLootChest(LootChestTier lootChestTier, Vector offset, float yaw, float pitch) {
        super(null, lootChestTier);
        this.offset = offset;
        this.yaw = yaw;
        this.pitch = pitch;
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
    public List<ItemStack> onDeath() {
        List<ItemStack> result = super.onDeath();

        GearLevel gearLevel = this.lootChestTier.getRandomGearLevel();

        List<ItemStack> drops = MobDropGenerator.getDrops(gearLevel, true, true);

        result.addAll(drops);

        return result;
    }
}
