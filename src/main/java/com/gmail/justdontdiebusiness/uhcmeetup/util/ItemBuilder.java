package com.gmail.justdontdiebusiness.uhcmeetup.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material);
        this.itemStack.setAmount(amount);
    }

    public ItemBuilder setName(String name) {
        this.itemStack.getItemMeta().setDisplayName(name);
        return this;
    }

    public ItemBuilder setEnchantments(Enchantment enchantment, int level) {
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemStack build() {
        return this.itemStack;
    }
}
