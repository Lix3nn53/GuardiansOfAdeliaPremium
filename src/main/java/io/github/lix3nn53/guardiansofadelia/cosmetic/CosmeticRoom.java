package io.github.lix3nn53.guardiansofadelia.cosmetic;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.Cosmetic;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticColor;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticType;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CosmeticRoom {

    private static final List<Player> players = new ArrayList<>();
    private static ArmorStand armorStand;
    private static ArmorStand armorStandTop;
    private static Location location;
    private static Location tpLocation;

    public static Location getTpLocation() {
        return tpLocation;
    }

    public static void setTpLocation(Location location) {
        CosmeticRoom.tpLocation = location;
    }

    public static void setLocation(Location location) {
        CosmeticRoom.location = location;
    }

    public static void setCosmeticToShowcase(Player player, Cosmetic cosmetic, CosmeticColor color, int tintIndex) {
        PlayerDisguise disguise;
        if (DisguiseAPI.isDisguised(player, armorStand)) {
            disguise = (PlayerDisguise) DisguiseAPI.getDisguise(player, armorStand);
        } else {
            disguise = new PlayerDisguise(player);
        }

        PlayerWatcher watcher = disguise.getWatcher();
        watcher.setNoGravity(true);
        watcher.setCustomNameVisible(false);

        CosmeticType cosmeticType = cosmetic.getType();
        EquipmentSlot equimentSlot = cosmeticType.getEquimentSlot();
        boolean onPlayer = cosmeticType.isOnPlayer();

        ItemStack showcase = cosmetic.getShowcase(color, tintIndex);
        if (onPlayer) {
            watcher.setItemStack(equimentSlot, showcase);
        } else {
            armorStandTop.getEquipment().setItem(equimentSlot, showcase);
        }

        DisguiseAPI.disguiseToPlayers(armorStand, disguise, player);
    }

    public static void start(Player player) {
        players.add(player);

        if (armorStand == null || !armorStand.isValid()) {
            armorStand = location.getWorld().spawn(location, ArmorStand.class);
            armorStand.setVisible(true);
            armorStand.setGravity(false);
            armorStand.setInvulnerable(true);

            if (armorStandTop == null || !armorStandTop.isValid()) {
                armorStandTop = location.getWorld().spawn(location, ArmorStand.class);
                armorStandTop.setVisible(true);
                armorStandTop.setGravity(false);
                armorStandTop.setInvulnerable(true);
            }

            armorStand.addPassenger(armorStandTop);
            rotate();
        }

        PlayerDisguise disguise = new PlayerDisguise(player);

        PlayerWatcher watcher = disguise.getWatcher();
        watcher.setNoGravity(true);
        watcher.setCustomNameVisible(false);

        DisguiseAPI.disguiseToPlayers(armorStand, disguise, player);
    }

    public static void end(Player player) {
        players.remove(player);

        if (DisguiseAPI.isDisguised(player, armorStand)) {
            Disguise disguise = DisguiseAPI.getDisguise(player, armorStand);
            disguise.removeDisguise();
        }
    }

    public static boolean isPlayerInRoom(Player player) {
        return players.contains(player);
    }

    private static void rotate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (armorStand == null || !armorStand.isValid()) {
                    cancel();
                    return;
                }

                Location location = armorStand.getLocation();
                float yaw = location.getYaw();

                if (yaw >= 180) {
                    yaw = -180;
                }

                location.setYaw(yaw + 4);

                armorStand.eject();
                armorStand.teleport(location);
                armorStandTop.teleport(location);
                armorStand.addPassenger(armorStandTop);
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 40L, 2L);
    }
}
