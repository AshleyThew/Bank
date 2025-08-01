package me.dablakbandit.bank.inventory.item;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.inventory.ItemStack;

public class BankBuyTabsInventory extends BankInventoryHandler<BankItemsInfo> {

	@Override
	public void init() {
		setItem(BankItemConfiguration.BANK_ITEM_BUY_TAB_BACK, consumeSound(this::returnToItems, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
		setItem(BankItemConfiguration.BANK_ITEM_BUY_TAB_BLANK);
		setItem(BankItemConfiguration.BANK_ITEM_BUY_TAB_MINUS, consumeSound(this::decrease, BankSoundConfiguration.INVENTORY_ITEMS_BUY_TABS_MINUS));
		setItem(BankItemConfiguration.BANK_ITEM_BUY_TAB_ADD, consumeSound(this::increase, BankSoundConfiguration.INVENTORY_ITEMS_BUY_TABS_ADD));
		setItem(BankItemConfiguration.BANK_ITEM_BUY_TAB_BUY, this::getBuy, consumeSound(this::buy, BankSoundConfiguration.INVENTORY_ITEMS_BUY_TABS_BUY));
	}

	private void increase(CorePlayers pl, BankItemsInfo info) {
		info.getBankItemsHandler().incrementBuyTabs();
		pl.refreshInventory();
	}

	private void decrease(CorePlayers pl, BankItemsInfo info) {
		info.getBankItemsHandler().decrementBuyTabs();
		pl.refreshInventory();
	}

	private ItemStack getBuy(BankItemPath path, BankItemsInfo bankInfo) {
		int cost = bankInfo.getBankItemsHandler().getBuyTabs() * BankPluginConfiguration.BANK_ITEMS_TABS_BUY_COST.get();
		// ChatColor.GREEN + "Used Slots: <used>", ChatColor.GREEN + "Available Slots: <available>", ChatColor.GREEN + "Total Slots: <total>",
		return replaceNameLore(path, "<tabs>", "" + bankInfo.getBankItemsHandler().getBuyTabs(), "<cost>", "" + cost);
	}

	private void buy(CorePlayers pl, BankItemsInfo info) {
		if (info.getBankItemsHandler().buyTabs(pl)) {
			returnToItems(pl);
		}
	}

	protected void returnToItems(CorePlayers pl) {
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_ITEMS);
	}

	@Override
	public BankItemsInfo getInvoker(CorePlayers pl) {
		return pl.getInfo(BankInfo.class).getItemsInfo();
	}

}
