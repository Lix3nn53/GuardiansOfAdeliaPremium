package io.github.lix3nn53.guardiansofadelia.menu.main.character;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiCharacter;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GuiCharacterClassManager extends GuiGeneric {

    private final List<RPGClass> values = new ArrayList<>();

    public GuiCharacterClassManager(GuardianData guardianData) {
        super(54, CustomCharacterGui.MENU_54.toString() + ChatPalette.BLACK + Translation.t(guardianData, "character.class.manager"), 0);

        RPGCharacter rpgCharacter = guardianData.getActiveCharacter();

        String rpgClassStr = rpgCharacter.getRpgClassStr();
        RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);

        ItemStack currentRPGClass = RPGClassManager.getPersonalIcon(rpgClass, rpgCharacter);

        List<ItemStack> items = new ArrayList<>();

        items.add(currentRPGClass);
        ItemStack arrowLeft = OtherItems.getArrowLeft();
        ItemMeta itemMeta = arrowLeft.getItemMeta();
        itemMeta.setDisplayName("This is your current class");
        arrowLeft.setItemMeta(itemMeta);
        items.add(arrowLeft);

        ItemStack arrowDown = OtherItems.getArrowDown();
        itemMeta = arrowDown.getItemMeta();
        itemMeta.setDisplayName("Change your class below");
        arrowDown.setItemMeta(itemMeta);
        items.add(arrowDown);
        items.add(null);

        Set<String> valuesStr = RPGClassManager.getClasses();
        for (String valueStr : valuesStr) {
            RPGClass value = RPGClassManager.getClass(valueStr);
            values.add(value);

            ItemStack itemStack = RPGClassManager.getPersonalIcon(value, rpgCharacter);

            items.add(itemStack);
        }

        ItemStack[] array = new ItemStack[items.size()];
        items.toArray(array); // fill the array

        GuiHelper.form54Big(this, array, "Character Menu");
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

        if (rpgCharacter != null) {
            int slot = event.getSlot();

            if (slot == 0) {
                GuiCharacter gui = new GuiCharacter(guardianData);
                gui.openInventory(player);
            } else {
                String rpgClassStr = null;

                if (GuiHelper.get54BigButtonIndexes(4).contains(slot)) {
                    rpgClassStr = values.get(0).getClassStringNoColor();
                } else if (GuiHelper.get54BigButtonIndexes(5).contains(slot)) {
                    rpgClassStr = values.get(1).getClassStringNoColor();
                } else if (GuiHelper.get54BigButtonIndexes(6).contains(slot)) {
                    rpgClassStr = values.get(2).getClassStringNoColor();
                } else if (GuiHelper.get54BigButtonIndexes(7).contains(slot)) {
                    rpgClassStr = values.get(3).getClassStringNoColor();
                }

                if (rpgClassStr == null) return;

                boolean b = rpgCharacter.changeClass(player, rpgClassStr, guardianData.getLanguage());
                if (b) {
                    player.closeInventory();

                    // progressChangeClassTasks
                    List<Quest> questList = rpgCharacter.getQuestList();
                    for (Quest quest : questList) {
                        quest.progressChangeClassTasks(player, rpgClassStr);
                    }
                }
            }
        }
    }
}
