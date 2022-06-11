package io.github.lix3nn53.guardiansofadelia.utilities.hologram;

import io.github.lix3nn53.guardiansofadelia.utilities.packets.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class FakeHologram {

    protected static final int RANGE = 24;

    protected final int entityID;
    private Set<Player> viewing = new HashSet<>();
    protected String text;
    private final EntityDestroyPacket entityDestroyPacket;
    private Location location;

    public FakeHologram(int entityID, Location location, String text) {
        this.entityID = entityID;
        this.location = location;
        this.text = text;
        entityDestroyPacket = new EntityDestroyPacket(entityID);
        entityDestroyPacket.load();
    }

    public void hide(@NotNull Player player) {
        entityDestroyPacket.send(player);
        viewing.remove(player);
    }

    public void show(@NotNull Player player) {
        new SpawnEntityLivingPacket(entityID, location)
                .load()
                .send(player);
        new EntityMetadataPacket(entityID, text, true, true)
                .load()
                .send(player);
        viewing.add(player);
    }

    public void showNearby() {
        AbstractPacket spawn = new SpawnEntityLivingPacket(entityID, location)
                .load();
        AbstractPacket metadata = new EntityMetadataPacket(entityID, text, true, true)
                .load();

        location.getWorld().getNearbyEntities(location, RANGE, RANGE, RANGE).forEach(entity -> {
            if (entity instanceof Player player) {
                spawn.send(player);
                metadata.send(player);
                viewing.add(player);
            }
        });
    }

    public void updateText(String text) {
        AbstractPacket load = new EntityMetadataPacket(entityID, text, true, true).load();

        for (Player player : viewing) {
            load.send(player);
        }

        this.text = text;
    }

    public void mount(@NotNull Player player, Player vehicle) {
        new PlayOutMountPacket(entityID, vehicle)
                .load()
                .send(player);
    }

    public void unmount(@NotNull Player player, Player vehicle) {
        new PlayOutMountPacket(-1, vehicle)
                .load()
                .send(player);
    }

    public void setHelmet(Player receiver, ItemStack itemStack) {
        new EntityEquipmentPacket(entityID, itemStack)
                .load()
                .send(receiver);
    }

    public void teleport(Location location) {
        AbstractPacket load = new EntityTeleportPacket(entityID, location).load();

        for (Player player : viewing) {
            load.send(player);
        }

        this.location = location;
    }

    public void look(Player receiver, Location location) {
        new EntityLookPacket(entityID, location)
                .load()
                .send(receiver);
    }

    public void rotate(Player receiver, Location location) {
        new EntityRotationPacket(entityID, location)
                .load()
                .send(receiver);
    }

    public void rotateHead(Player receiver, Location location) {
        new EntityHeadRotationPacket(entityID, location)
                .load()
                .send(receiver);
    }

    public void destroy() {
        if (viewing.isEmpty()) {
            return;
        }

        for (Player player : viewing) {
            entityDestroyPacket.send(player);
        }

        viewing.clear();
    }

    public boolean isViewing(Player player) {
        return viewing.contains(player);
    }

    public void setViewing(Set<Player> viewing) {
        this.viewing.removeAll(viewing);

        for (Player player : viewing) {
            entityDestroyPacket.send(player);
        }

        this.viewing = viewing;
    }
}
