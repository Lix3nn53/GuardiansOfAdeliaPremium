package io.github.lix3nn53.guardiansofadelia.menu.main.settings;

import io.github.lix3nn53.guardiansofadelia.chat.ChatChannel;
import io.github.lix3nn53.guardiansofadelia.chat.ChatChannelData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiSettings;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiChatChannels extends GuiGeneric {

    private final HashMap<Integer, ChatChannel> indexToChannel = new HashMap<>();
    private final ChatChannelData chatChannelData;

    public GuiChatChannels(GuardianData guardianData) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "menu.chatChannels.name"), 0);

        ItemStack backButton = OtherItems.getBackButton("Settings");
        setItem(0, backButton);

        int index = 9;

        this.chatChannelData = guardianData.getChatChannelData();

        for (ChatChannel chatChannel : ChatChannel.values()) {
            ChatChannel textingTo = chatChannelData.getTextingTo();
            ItemStack itemStack;
            boolean listening = chatChannelData.isListening(chatChannel);
            if (chatChannel.equals(textingTo)) {
                itemStack = new ItemStack(Material.YELLOW_WOOL);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatPalette.GOLD + chatChannel.toString());
                ArrayList<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatPalette.GOLD + "This channel is active.");
                lore.add(ChatPalette.GOLD + "Your messages will be sent to this channel.");
                lore.add("");
                lore.add(ChatPalette.GREEN + "You are listening to this channel.");
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            } else if (listening) {
                itemStack = new ItemStack(Material.LIME_WOOL);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatPalette.GREEN + chatChannel.toString());
                ArrayList<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatPalette.GOLD + "Right click to set this channel active.");
                lore.add("");
                lore.add(ChatPalette.GREEN + "You are listening to this channel.");
                lore.add(ChatPalette.RED + "Left click to stop listening to this channel.");
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            } else {
                itemStack = new ItemStack(Material.RED_WOOL);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatPalette.RED + chatChannel.toString());
                ArrayList<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatPalette.GOLD + "Right click to set this channel active.");
                lore.add("");
                lore.add(ChatPalette.RED + "You are not listening to this channel.");
                lore.add(ChatPalette.GREEN + "Left click to listen to this channel.");
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            }
            setItem(index, itemStack);
            indexToChannel.put(index, chatChannel);
            index += 2;
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuardianData guardianData;
        if (GuardianDataManager.hasGuardianData(player)) {
            guardianData = GuardianDataManager.getGuardianData(player);
        } else {
            return;
        }

        int slot = event.getSlot();
        if (slot == 0) {
            GuiSettings gui = new GuiSettings(guardianData);
            gui.openInventory(player);
        } else if (indexToChannel.containsKey(slot)) {
            ChatChannel chatChannel = indexToChannel.get(slot);
            if (event.getClick().isLeftClick()) {
                chatChannelData.toggleListening(chatChannel);
            } else if (event.getClick().isRightClick()) {
                chatChannelData.setTextingTo(chatChannel);
            }

            new GuiChatChannels(guardianData).openInventory(player);
        }
    }
}
