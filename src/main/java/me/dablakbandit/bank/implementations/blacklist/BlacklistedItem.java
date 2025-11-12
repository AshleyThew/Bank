package me.dablakbandit.bank.implementations.blacklist;

import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlacklistedItem {

	private static boolean hasCustomModelData = false;

	static {
		try {
			hasCustomModelData = NMSUtils.getMethod(ItemMeta.class, "getCustomModelData") != null;
		} catch (Exception e) {

		}
	}

	private boolean matchData, matchNBT, matchModelData;
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
		if (matchModelData && hasCustomModelData && !customModelDataEquals(is)) {
			return false;
		}
		if (matchNBT) {
			return itemStack.isSimilar(is);
		}
		return true;
	}

	private boolean customModelDataEquals(ItemStack is) {
		ItemMeta im1 = itemStack.getItemMeta();
		ItemMeta im2 = is.getItemMeta();
		if (im1 == null || im2 == null) {
			return false;
		}
		if (!im1.hasCustomModelData() || !im2.hasCustomModelData()) {
			return false;
		}
		return im1.getCustomModelData() == im2.getCustomModelData();
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

	public boolean isMatchNBT() {
		return matchNBT;
	}

	public void toggleMatchNBT() {
		this.matchNBT = !matchNBT;
	}

	public boolean isMatchModelData() {
		return matchModelData;
	}

	public void toggleMatchModelData() {
		this.matchModelData = !matchModelData;
	}

}
