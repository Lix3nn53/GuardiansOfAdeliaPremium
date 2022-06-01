package io.github.lix3nn53.guardiansofadelia.cosmetic.gui;

import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticManager;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticRoom;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.Cosmetic;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticColor;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticType;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class CosmeticRoomGui extends GuiGeneric {

    private static final int[] ITEM_SLOTS = {20, 21, 22, 24, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
    private final HashMap<Integer, Cosmetic> slotToCosmetic = new HashMap<>();
    private CosmeticType selectedType;
    private int pageIndex;

    public CosmeticRoomGui(GuardianData guardianData, CosmeticType selectedType, int pageIndex) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + "Cosmetic Room", 0);

        this.selectedType = selectedType;
        this.pageIndex = pageIndex;

        ItemStack weapons = new ItemStack(Material.YELLOW_WOOL);
        ItemMeta itemMeta = weapons.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setCustomModelData(27);
        itemMeta.setDisplayName(ChatPalette.BLUE + Translation.t(guardianData, "cosmetic.weapon"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.weapon.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.weapon.l2"));
        itemMeta.setLore(lore);
        weapons.setItemMeta(itemMeta);
        this.setItem(1, weapons);

        ItemStack helmet = new ItemStack(Material.YELLOW_WOOL);
        itemMeta.setCustomModelData(2);
        itemMeta.setDisplayName(ChatPalette.BLUE + Translation.t(guardianData, "cosmetic.helmet"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.helmet.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.helmet.l2"));
        itemMeta.setLore(lore);
        helmet.setItemMeta(itemMeta);
        this.setItem(3, helmet);

        ItemStack back = new ItemStack(Material.YELLOW_WOOL);
        itemMeta.setCustomModelData(2);
        itemMeta.setDisplayName(ChatPalette.BLUE + Translation.t(guardianData, "cosmetic.back"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.back.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.back.l2"));
        itemMeta.setLore(lore);
        back.setItemMeta(itemMeta);
        this.setItem(5, back);

        setCosmetics(selectedType, pageIndex);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuardianData guardianData;
        RPGCharacter rpgCharacter;
        if (GuardianDataManager.hasGuardianData(player)) {
            guardianData = GuardianDataManager.getGuardianData(player);

            if (guardianData.hasActiveCharacter()) {
                rpgCharacter = guardianData.getActiveCharacter();
            } else {
                return;
            }
        } else {
            return;
        }

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;

        int slot = event.getSlot();
        if (slot == 1) {
            new CosmeticRoomGuiWeapon(guardianData, selectedType, pageIndex).openInventory(player);
            return;
        } else if (slot == 3) {
            selectedType = CosmeticType.HELMET_SKIN;
        } else if (slot == 5) {
            selectedType = CosmeticType.COSMETIC_BACK;
        } else if (slot == 46) {
            if (pageIndex > 0) {
                pageIndex--;
            }
        } else if (slot == 52) {
            pageIndex++;
        } else if (slotToCosmetic.containsKey(slot)) {
            // click on cosmetic
            Cosmetic cosmetic = slotToCosmetic.get(slot);
            if (selectedType.canChangeColor()) {
                new CosmeticRoomGuiColor(guardianData, cosmetic).openInventory(player);
            } else {
                CosmeticRoom.setCosmeticToShowcase(player, cosmetic, null, 0);
                player.closeInventory();
            }
            return;
        }

        setCosmetics(selectedType, pageIndex);
    }

    private void setCosmetics(CosmeticType cosmeticType, int page) {
        int start = ITEM_SLOTS.length * page;

        start = start + cosmeticType.getIdOffset();

        slotToCosmetic.clear();
        for (int i = 0; i < ITEM_SLOTS.length; i++) {
            int slot = ITEM_SLOTS[i];

            Cosmetic cosmetic = CosmeticManager.get(start + i);

            if (cosmetic != null) {
                this.setItem(slot, cosmetic.getShowcase(CosmeticColor.RED, 2));
                slotToCosmetic.put(slot, cosmetic);
            } else {
                this.setItem(slot, new ItemStack(Material.AIR));
            }
        }
    }
}
