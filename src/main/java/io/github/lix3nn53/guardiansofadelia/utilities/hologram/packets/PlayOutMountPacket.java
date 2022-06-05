package io.github.lix3nn53.guardiansofadelia.utilities.hologram.packets;

import com.comphenix.protocol.PacketType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayOutMountPacket extends AbstractPacket {

    private final List<Integer> entities = new ArrayList<>();
    private final LivingEntity vehicle;

    public PlayOutMountPacket(int entityID, LivingEntity vehicle) {
        super(entityID, PacketType.Play.Server.MOUNT);
        this.entities.add(entityID);
        this.vehicle = vehicle;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        if (entities.size() == 0) {
            packetContainer.getIntegers().write(0, vehicle.getEntityId());
            packetContainer.getIntegerArrays().write(0, new int[]{});
        } else {
            packetContainer.getIntegers().write(0, vehicle.getEntityId());
            packetContainer.getIntegerArrays().write(0, new int[]{entities.get(0)});
        }

        return this;
    }
}