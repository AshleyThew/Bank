package me.dablakbandit.bank.implementations.blacklist;

import org.bukkit.inventory.ItemStack;

public class BlacklistedItem {

	private boolean matchData, matchNBT;
	private final ItemStack itemStack;

	public BlacklistedItem(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.itemStack.setAmount(1);
	}

	public boolean equals(ItemStack is) {
		if (is.getType() != itemStack.getType()) {
			return false;
		}
		if (matchData && itemStack.getDurability() != is.getDurability()) {
			return false;
		}
		return itemStack.isSimilar(is);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public boolean isMatchData() {
		return matchData;
	}

	public void toggleMatchData() {
		this.matchData = !matchData;
	}

	public void setMatchData(boolean matchData) {
		this.matchData = matchData;
	}

	public boolean isMatchNBT() {
		return matchNBT;
	}

	public void toggleMatchNBT() {
		this.matchNBT = !matchNBT;
	}

	public void setMatchNBT(boolean matchNBT) {
		this.matchNBT = matchNBT;
	}

}
