package io.github.lix3nn53.guardiansofadelia.utilities.hologram.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class EntityEquipmentPacket extends AbstractPacket {

    private final ItemStack helmet;

    public EntityEquipmentPacket(int entityID, ItemStack helmet) {
        super(entityID, PacketType.Play.Server.ENTITY_EQUIPMENT);
        this.helmet = helmet;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        packetContainer.getIntegers().write(0, entityID);
        packetContainer.getSlotStackPairLists().write(0, Collections.singletonList(new Pair<>(EnumWrappers.ItemSlot.HEAD,
                helmet)));

        return this;
    }
}