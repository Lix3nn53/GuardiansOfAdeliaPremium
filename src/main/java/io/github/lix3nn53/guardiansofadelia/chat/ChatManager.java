package io.github.lix3nn53.guardiansofadelia.chat;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.party.Party;
import io.github.lix3nn53.guardiansofadelia.party.PartyManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacter;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGuild;
import io.github.lix3nn53.guardiansofadelia.text.font.NegativeSpace;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ChatManager {

    private static final HashMap<Player, ArmorStand> chatHolograms = new HashMap<>();

    public static void chatHologram(Player player, String message) {
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
                        this.armorStand = new Hologram(location, ChatPalette.YELLOW + "< " + ChatPalette.GRAY + message + ChatPalette.YELLOW + " >").getArmorStand();
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

    public static void chatHologramEntity(Entity entity, String message, int durationTicks, float offsetY) {
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
                        armorStand = new Hologram(location, ChatPalette.YELLOW + "< " + ChatPalette.GRAY + message + ChatPalette.YELLOW + " >").getArmorStand();
                    }
                    Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);
                    armorStand.teleport(location);
                    ticksPass += 2;
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, 2L);
    }

    public static void chatHologramEntityWithCountDown(Entity entity, String message, int durationTicks, float offsetY) {
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

    /**
     * Create chat hologram and return boolean to allow on normal chat
     *
     * @param player
     * @param message
     * @return allow message to normal chat
     */
    public static boolean onChat(Player player, String message) {
        ChatChannel chatChannel = null;
        ChatChannelData chatChannelData = null;

        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            chatChannelData = guardianData.getChatChannelData();
            chatChannel = chatChannelData.getTextingTo();
        }

        if (chatChannel == null) { //send message to normal chat
            chatHologram(player, message);
            return true;
        }

        String prefix = chatChannel.getPrefix();
        ChatPalette chatPalette = chatChannel.getPalette();
        String displayName = player.getDisplayName();
        String messageWithPrefix = chatPalette + "[" + prefix + "]" + ChatColor.RESET + displayName + getChatSuffix() + message;

        if (chatChannel.equals(ChatChannel.GUILD)) {
            Guild guild = GuildManager.getGuild(player);
            if (guild != null) {
                guild.getMembers().forEach(memberUUID -> {
                    Player member = Bukkit.getPlayer(memberUUID);
                    if (member != null) {
                        member.sendMessage(messageWithPrefix);
                    }
                });
            } else {
                player.sendMessage(ChatColor.RED + "You are not in a guild.");
            }
        } else if (chatChannel.equals(ChatChannel.PARTY)) {
            Party party = PartyManager.getParty(player);
            if (party != null) {
                party.getMembers().forEach(member -> {
                    member.sendMessage(messageWithPrefix);
                });
            } else {
                player.sendMessage(ChatPalette.RED + "You are not in a party.");
            }
        } else if (chatChannel.equals(ChatChannel.PRIVATE)) {
            Player privateChatTo = chatChannelData.getPrivateChatTo();
            if (privateChatTo != null) {
                if (GuardianDataManager.hasGuardianData(privateChatTo)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(privateChatTo);
                    ChatChannelData chatChannelDataTo = guardianData.getChatChannelData();
                    boolean listening = chatChannelDataTo.isListening(ChatChannel.PRIVATE);
                    if (listening) {
                        privateChatTo.sendMessage(messageWithPrefix);
                        player.sendMessage(messageWithPrefix);
                    } else {
                        player.sendMessage(ChatColor.RED + "That player is not listening to private chat.");
                    }
                }
            } else {
                player.sendMessage(ChatPalette.RED + "You are not chatting to anyone.");
            }
        } else { // GLOBAL CHANNELS
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (GuardianDataManager.hasGuardianData(onlinePlayer)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(onlinePlayer);
                    ChatChannelData chatChannelData1 = guardianData.getChatChannelData();
                    if (chatChannelData1.isListening(chatChannel)) {
                        onlinePlayer.sendMessage(messageWithPrefix);
                    }
                }
            }
        }

        return false;
    }

    public static String getFormat(Player player) {
        // String format = "<group-prefix>%s<group-suffix>%s"; //first %s is player.getDisplayName(), second %s is the message
        String format = "%s<group-suffix>%s"; //first %s is player.getDisplayName(), second %s is the message
        // replacing your values
        // format = format.replace("<group-prefix>", getChatPrefix(player));
        format = format.replace("<group-suffix>", getChatSuffix());

        return format;
    }

    private static String getChatPrefix(Player player) {
        String prefix = "";
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            StaffRank staffRank = guardianData.getStaffRank();
            if (!staffRank.equals(StaffRank.NONE)) {
                CustomCharacter customCharacter = staffRank.getCustomCharacter();
                prefix += customCharacter;
            }
            PremiumRank premiumRank = guardianData.getPremiumRank();
            if (!premiumRank.equals(PremiumRank.NONE)) {
                CustomCharacter customCharacter = premiumRank.getCustomCharacter();
                prefix += customCharacter;
            }
            if (GuildManager.inGuild(player)) {
                Guild guild = GuildManager.getGuild(player);
                String tag = guild.getTag();
                int length = tag.length();
                CustomCharacter customCharacter;
                if (length == 2) {
                    customCharacter = CustomCharacterGuild.GUILD_2;
                } else if (length == 3) {
                    customCharacter = CustomCharacterGuild.GUILD_3;
                } else if (length == 4) {
                    customCharacter = CustomCharacterGuild.GUILD_4;
                } else { // if (length == 5) {
                    customCharacter = CustomCharacterGuild.GUILD_5;
                }

                prefix += customCharacter + tag + NegativeSpace.POSITIVE_2;
            }
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                ChatTag chatTag = activeCharacter.getChatTag();
                CustomCharacter customCharacter = chatTag.getCustomCharacter();
                prefix += customCharacter;
            }
        }
        return prefix + ChatPalette.WHITE + NegativeSpace.POSITIVE_2;
    }

    public static String getChatSuffix() {
        return ChatPalette.YELLOW + " > " + ChatPalette.GRAY;
    }

    public static void updatePlayerName(Player player) {
        String chatPrefix = getChatPrefix(player);

        String name = player.getName();
        player.setDisplayName(chatPrefix + name);
        player.setPlayerListName(chatPrefix + name);
    }

    public static void onPlayerJoin(Player player) {
        updatePlayerName(player);
    }
}
