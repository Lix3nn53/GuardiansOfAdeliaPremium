package io.github.lix3nn53.guardiansofadelia.items.list;

import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OtherItems {

    public static ItemStack getBoat() {
        ItemStack item = new ItemStack(Material.OAK_BOAT);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatPalette.YELLOW + "Boat");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "TODO explanation");
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public static ItemStack getArrow(int amount) {
        ItemStack itemStack = new ItemStack(Material.ARROW, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Arrow");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Ammunition for bows & crossbows");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getSaddle() {
        ItemStack itemStack = new ItemStack(Material.SADDLE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Saddle");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Required to control mounts.");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getEmpty(String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(40);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getBackButton(String backTo) {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(41);
        itemMeta.setDisplayName(ChatPalette.RED + "Go back to " + backTo);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getUnassignedSkill() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Unassigned Skill");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "You haven't unlocked a skill for this slot yet");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getArrowUp() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Up");
        itemMeta.setCustomModelData(44);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getArrowDown() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Down");
        itemMeta.setCustomModelData(42);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getArrowLeft() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Left");
        itemMeta.setCustomModelData(43);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getArrowRight() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Right");
        itemMeta.setCustomModelData(45);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getArrowUpLeft() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Up Left");
        itemMeta.setCustomModelData(47);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getArrowUpRight() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Up Right");
        itemMeta.setCustomModelData(48);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getArrowDownLeft() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Down Left");
        itemMeta.setCustomModelData(46);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getArrowDownRight() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Down Right");
        itemMeta.setCustomModelData(49);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
