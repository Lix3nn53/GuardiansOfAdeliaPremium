package io.github.lix3nn53.guardiansofadelia.chat;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterMisc;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.FakeHologram;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class SpeechBubble {

    private static final HashMap<Player, ArmorStand> chatHolograms = new HashMap<>();

    public static void player(Player player, String message) {
        float height = (float) player.getHeight();
        Location location = player.getLocation().clone().add(0, height + 0.4, 0);

        final int period = 2;
        final int ticksLimit = 100;

        new BukkitRunnable() {

            ArmorStand armorStand;
            int ticksPass = 0;

            @Override
            public void run() {
                if (ticksPass == ticksLimit) {
                    cancel();
                    armorStand.remove();
                    chatHolograms.remove(player);
                } else {
                    if (ticksPass == 0) {
                        ArmorStand old = chatHolograms.get(player);
                        if (old != null) {
                            old.remove();
                        }
                        armorStand = getHologram(location, message);

                        chatHolograms.put(player, this.armorStand);
                    } else if (armorStand.isDead()) {
                        cancel();
                        return;
                    }

                    Location location = player.getLocation().clone().add(0, height + 0.4, 0);
                    armorStand.teleport(location);
                    ticksPass += period;
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, period);
    }

    public static void entity(Entity entity, String message, int durationTicks, float offsetY) {
        float height = (float) entity.getHeight();
        Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);

        new BukkitRunnable() {

            ArmorStand armorStand;
            int ticksPass = 0;

            @Override
            public void run() {
                if (ticksPass == durationTicks) {
                    cancel();
                    armorStand.remove();
                } else {
                    if (ticksPass == 0) {
                        armorStand = getHologram(location, message);
                    }
                    Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);
                    armorStand.teleport(location);
                    ticksPass += 2;
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, 2L);
    }

    public static ArmorStand entityNoFollow(Entity entity, String message, int durationTicks, float offsetY) {
        float height = (float) entity.getHeight();

        Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);
        ArmorStand armorStand = getHologram(location, message);

        new BukkitRunnable() {

            @Override
            public void run() {
                cancel();
                armorStand.remove();
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), durationTicks);

        return armorStand;
    }

    public static void entityNoFollowPacket(Entity entity, String message, long durationTicks, float offsetY, Player player) {
        Location location = entity.getLocation().clone().add(0, 0.2 + offsetY, 0);

        int entityId = GuardiansOfAdelia.getEntityId();
        FakeHologram fakeHologram = new FakeHologram(entityId, location, message);
        fakeHologram.show(player);

        new BukkitRunnable() {

            @Override
            public void run() {
                fakeHologram.hide(player);
            }
        }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), durationTicks);
    }

    public static void countdown(Entity entity, String message, int durationTicks, float offsetY) {
        float height = (float) entity.getHeight();
        Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);

        new BukkitRunnable() {

            ArmorStand armorStand;
            int ticksPass = 0;

            @Override
            public void run() {
                if (ticksPass == durationTicks) {
                    cancel();
                    armorStand.remove();
                } else {
                    if (ticksPass == 0) {
                        armorStand = new Hologram(location, message + ChatPalette.YELLOW + " in " + durationTicks).getArmorStand();
                    } else {
                        armorStand.setCustomName(message + ChatPalette.YELLOW + " in " + (durationTicks - ticksPass));
                    }
                    Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);
                    armorStand.teleport(location);
                    ticksPass += 2;
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, 2L);
    }

    private static ArmorStand getHologram(Location location, String message) {
        return new Hologram(location, CustomCharacterMisc.SPEECH_BUBBLE.toString() + ChatPalette.GRAY + message).getArmorStand();
    }
}
