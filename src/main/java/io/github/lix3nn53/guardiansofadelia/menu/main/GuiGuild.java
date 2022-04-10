package io.github.lix3nn53.guardiansofadelia.menu.main;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.menu.main.minigames.GuiMinigamesJoinLOS;
import io.github.lix3nn53.guardiansofadelia.menu.main.minigames.GuiMinigamesJoinWBMK;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuiGuild extends GuiGeneric {

    public GuiGuild(GuardianData guardianData) {
        super(27, CustomCharacterGui.MENU_27_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "menu.guild.name"), 0);

        ItemStack info = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = info.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.RED + "Info");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to join this minigame");
        itemMeta.setLore(lore);
        info.setItemMeta(itemMeta);

        ItemStack members = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.RED + "Members");
        members.setItemMeta(itemMeta);

        ItemStack rankings = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.RED + "Rankings");
        rankings.setItemMeta(itemMeta);

        ItemStack upgrades = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.RED + "Upgrades");
        upgrades.setItemMeta(itemMeta);

        ItemStack war = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.RED + "War");
        war.setItemMeta(itemMeta);

        ItemStack destroy = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.RED + "destroy");
        destroy.setItemMeta(itemMeta);

        GuiHelper.form27Small(this, new ItemStack[]{members, rankings}, "Main Menu");
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

        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory.getType().equals(InventoryType.CHEST)) {
            int slot = event.getSlot();
            if (slot == 0) {
                GuiMain gui = new GuiMain(guardianData);
                gui.openInventory(player);
            } else if (GuiHelper.get27SmallButtonIndex(0) == slot) {
                GuiMinigamesJoinLOS gui = new GuiMinigamesJoinLOS();
                gui.openInventory(player);
            } else if (GuiHelper.get27SmallButtonIndex(1) == slot) {
                GuiMinigamesJoinWBMK gui = new GuiMinigamesJoinWBMK();
                gui.openInventory(player);
            }
        }
    }
}
