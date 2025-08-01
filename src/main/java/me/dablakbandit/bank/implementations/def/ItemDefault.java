package me.dablakbandit.bank.implementations.def;

import org.bukkit.inventory.ItemStack;

public class ItemDefault {

	private final ItemStack itemStack;


	public ItemDefault(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}
}
