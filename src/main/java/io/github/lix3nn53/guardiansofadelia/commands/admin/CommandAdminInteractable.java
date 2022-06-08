package io.github.lix3nn53.guardiansofadelia.commands.admin;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.MMSpawnerManager;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawner;
import io.github.lix3nn53.guardiansofadelia.interactables.ExplosiveBarrel;
import io.github.lix3nn53.guardiansofadelia.interactables.chest.LootChest;
import io.github.lix3nn53.guardiansofadelia.interactables.chest.LootChestTier;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.config.ExplosiveBarrelConfiguration;
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

public class CommandAdminInteractable implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("admininteractable")) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatPalette.YELLOW + "/admininteractable rotate - rotate nearby interactables");
                player.sendMessage(ChatPalette.YELLOW + "/admininteractable add chest [0-3 = tier]");
                player.sendMessage(ChatPalette.YELLOW + "/admininteractable add explosive");
            } else if (args[0].equals("add")) {
                if (args[1].equals("chest")) {
                    Block targetBlock = player.getTargetBlock(null, 12);

                    Material type = targetBlock.getType();

                    if (!type.equals(Material.CHEST)) {
                        player.sendMessage(ChatPalette.RED + "You must be looking at a chest");
                        return false;
                    }

                    int tierIndex = Integer.parseInt(args[2]);

                    LootChestTier value = LootChestTier.values()[tierIndex];

                    Location location = targetBlock.getLocation();

                    location.add(0.5, 0, 0.5);

                    LootChest lootChest = new LootChest(location, value);
                    lootChest.spawn();

                    MMSpawnerManager.addGlobalSpawner(lootChest);

                    player.sendMessage(ChatPalette.GREEN_DARK + "Added loot chest! Tier: " + value);
                    // lootChest.startPlayingParticles();
                    targetBlock.setType(Material.AIR);
                    lootChest.spawn();
                } else if (args[1].equals("explosive")) {
                    Block targetBlock = player.getTargetBlock(null, 12);

                    Material type = targetBlock.getType();

                    if (!type.equals(Material.TNT)) {
                        player.sendMessage(ChatPalette.RED + "You must be looking at a tnt");
                        return false;
                    }

                    Location location = targetBlock.getLocation();

                    location.add(0.5, 0, 0.5);

                    ExplosiveBarrel explosiveBarrel = new ExplosiveBarrel(ExplosiveBarrelConfiguration.MOB_KEY, location);
                    explosiveBarrel.spawn();

                    MMSpawnerManager.addGlobalSpawner(explosiveBarrel);

                    player.sendMessage(ChatPalette.GREEN_DARK + "Added explosive barrel!");
                    targetBlock.setType(Material.AIR);
                    explosiveBarrel.spawn();
                }
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
