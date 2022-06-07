package io.github.lix3nn53.guardiansofadelia.commands.admin;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.MMSpawnerManager;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawner;
import io.github.lix3nn53.guardiansofadelia.menu.admin.GuiAdminDailyRewards;
import io.github.lix3nn53.guardiansofadelia.rewards.chest.LootChestTier;
import io.github.lix3nn53.guardiansofadelia.rewards.chest.OpenWorldLootChest;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandAdminReward implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("adminreward")) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatPalette.YELLOW + "/adminreward setdaily");
                player.sendMessage(ChatPalette.YELLOW + "/adminreward addlootchest [0-3 = tier]");
                player.sendMessage(ChatPalette.YELLOW + "/adminreward rotate - rotate nearby loot-chest");
            } else if (args[0].equals("setdaily")) {
                GuiAdminDailyRewards gui = new GuiAdminDailyRewards();
                gui.openInventory(player);
            } else if (args[0].equals("addlootchest")) {
                Block targetBlock = player.getTargetBlock(null, 12);

                Material type = targetBlock.getType();

                if (!type.equals(Material.CHEST)) {
                    player.sendMessage(ChatPalette.RED + "You must be looking to a chest");
                    return false;
                }

                int tierIndex = Integer.parseInt(args[1]);

                LootChestTier value = LootChestTier.values()[tierIndex];

                Location location = targetBlock.getLocation();

                location.add(0.5, 0, 0.5);

                OpenWorldLootChest lootChest = new OpenWorldLootChest(location, value);
                lootChest.spawn();

                MMSpawnerManager.addGlobalSpawner(lootChest);

                player.sendMessage(ChatPalette.GREEN_DARK + "Added loot chest, tier: " + value);
                // lootChest.startPlayingParticles();
                targetBlock.setType(Material.AIR);
                lootChest.spawn();
            } else if (args[0].equals("rotate")) {
                List<Entity> nearbyEntities = player.getNearbyEntities(4, 4, 4);

                for (Entity entity : nearbyEntities) {
                    if (!(entity instanceof LivingEntity)) continue;

                    LivingEntity livingEntity = (LivingEntity) entity;

                    MMSpawner lootChest = MMSpawnerManager.getSpawnerOfEntity(livingEntity);

                    if (lootChest == null) continue;

                    lootChest.rotate();
                }
            }

            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        return false;
    }
}
