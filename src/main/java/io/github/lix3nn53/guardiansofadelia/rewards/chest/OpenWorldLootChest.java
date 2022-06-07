package io.github.lix3nn53.guardiansofadelia.rewards.chest;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.creatures.drops.MobDropGenerator;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpenWorldLootChest extends MMSpawnerOpenWorld {

    private final LootChestTier lootChestTier;

    public OpenWorldLootChest(Location location, LootChestTier lootChestTier) {
        super(lootChestTier.getMobKey(), location);
        this.lootChestTier = lootChestTier;
    }

    public LootChestTier getLootChestTier() {
        return lootChestTier;
    }

    @Override
    public void onDeath() {
        super.onDeath();
        GuardiansOfAdelia.getInstance().getLogger().info("Debug death OpenWorldLootChest");

        List<ItemStack> result = lootChestTier.getLoot();

        GearLevel gearLevel = this.lootChestTier.getRandomGearLevel();

        List<ItemStack> drops = MobDropGenerator.getDrops(gearLevel, false, true);

        result.addAll(drops);

        World world = location.getWorld();
        for (ItemStack itemStack : result) {
            world.dropItemNaturally(location, itemStack);
        }
    }
}
