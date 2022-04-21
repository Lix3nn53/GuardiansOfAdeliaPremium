package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum SkillTreeArrow {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        switch (this) {
            case UP -> {
                itemMeta.setDisplayName(ChatPalette.GRAY_DARK + "UP");
            }
            case DOWN -> {
                itemMeta.setDisplayName(ChatPalette.GRAY_DARK + "DOWN");
            }
            case LEFT -> {
                itemMeta.setDisplayName(ChatPalette.GRAY_DARK + "LEFT");
            }
            case RIGHT -> {
                itemMeta.setDisplayName(ChatPalette.GRAY_DARK + "RIGHT");
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
