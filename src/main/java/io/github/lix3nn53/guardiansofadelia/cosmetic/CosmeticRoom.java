package io.github.lix3nn53.guardiansofadelia.cosmetic;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.Cosmetic;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticColor;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticType;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.ArmorStandWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CosmeticRoom {

    private static final HashMap<Player, Location> players = new HashMap<>();
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
        CosmeticType cosmeticType = cosmetic.getType();
        EquipmentSlot equipmentSlot = cosmeticType.getEquipmentSlot();
        boolean onPlayer = cosmeticType.isOnPlayer();
        ItemStack showcase = cosmetic.getShowcase(color, tintIndex);

        if (onPlayer) {
            PlayerDisguise disguise;
            if (DisguiseAPI.isDisguised(player, armorStand)) {
                disguise = (PlayerDisguise) DisguiseAPI.getDisguise(player, armorStand);
            } else {
                disguise = new PlayerDisguise(player);
            }

            PlayerWatcher watcher = disguise.getWatcher();
            watcher.setNoGravity(true);
            watcher.setItemStack(equipmentSlot, showcase);

            DisguiseAPI.disguiseToPlayers(armorStand, disguise, player);
        } else {
            MobDisguise disguise;
            if (DisguiseAPI.isDisguised(player, armorStandTop)) {
                disguise = (MobDisguise) DisguiseAPI.getDisguise(player, armorStandTop);
            } else {
                disguise = new MobDisguise(DisguiseType.ARMOR_STAND);
            }

            LivingWatcher watcher = disguise.getWatcher();
            watcher.setNoGravity(true);
            watcher.setItemStack(equipmentSlot, showcase);

            DisguiseAPI.disguiseToPlayers(armorStandTop, disguise, player);
        }
    }

    public static void start(Player player, Location backLocation) {
        players.put(player, backLocation);

        if (armorStand == null || !armorStand.isValid()) {
            armorStand = location.getWorld().spawn(location, ArmorStand.class);
            armorStand.setVisible(true);
            armorStand.setGravity(false);
            armorStand.setInvulnerable(true);
        }

        if (armorStandTop == null || !armorStandTop.isValid()) {
            armorStandTop = location.getWorld().spawn(location, ArmorStand.class);
            armorStandTop.setVisible(false);
            armorStandTop.setGravity(false);
            armorStandTop.setInvulnerable(true);
        }

        armorStand.addPassenger(armorStandTop);
        rotate();

        PlayerDisguise disguise = new PlayerDisguise(player);

        PlayerWatcher watcher = disguise.getWatcher();
        watcher.setNoGravity(true);

        DisguiseAPI.disguiseToPlayers(armorStand, disguise, player);

        MobDisguise forPlayer = new MobDisguise(DisguiseType.ARMOR_STAND);
        ArmorStandWatcher watcherForPlayer = (ArmorStandWatcher) forPlayer.getWatcher();
        watcherForPlayer.setInvisible(false);
        watcherForPlayer.setMarker(true);
        DisguiseAPI.disguiseToAll(player, forPlayer);
    }

    public static void onQuit(Player player) {
        Location backLocation = players.remove(player);

        if (DisguiseAPI.isDisguised(player, armorStand)) {
            Disguise disguise = DisguiseAPI.getDisguise(player, armorStand);
            disguise.removeDisguise();
        }

        if (DisguiseAPI.isDisguised(player)) {
            DisguiseAPI.getDisguise(player).removeDisguise();
        }

        if (player.isOnline() && backLocation != null) {
            player.teleport(backLocation);
        }
    }

    public static boolean isPlayerInRoom(Player player) {
        return players.containsKey(player);
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

    public static Location getPlayerBackLocation(Player player) {
        return players.get(player);
    }
}
