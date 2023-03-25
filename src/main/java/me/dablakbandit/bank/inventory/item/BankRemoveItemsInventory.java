package me.dablakbandit.bank.inventory.item;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.players.CorePlayers;

public class BankRemoveItemsInventory extends BankInventoryHandler<BankInfo>{
	
	@Override
	public void init(){
		int size = descriptor.getSize();
		setAll(size, BankItemConfiguration.BANK_ITEM_BLANK);
		setItem(BankItemConfiguration.BANK_REMOVE_BACK, consumeSound(this::returnToItems, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
		setItem(BankItemConfiguration.BANK_REMOVE_ALL, consumeSound(this::removeAll, BankSoundConfiguration.INVENTORY_ITEMS_REMOVE_ALL));
		setItem(BankItemConfiguration.BANK_REMOVE_INVENTORY, consumeSound(this::removeInventory, BankSoundConfiguration.INVENTORY_ITEMS_REMOVE_INVENTORY));
		setItem(BankItemConfiguration.BANK_REMOVE_HOTBAR, consumeSound(this::removeHotbar, BankSoundConfiguration.INVENTORY_ITEMS_REMOVE_HOTBAR));
	}
	
	private void removeAll(CorePlayers pl, BankInfo bankInfo){
		bankInfo.getItemsInfo().removeAllBankToInventory(pl.getPlayer());
		returnToItems(pl);
	}
	
	private void removeInventory(CorePlayers pl, BankInfo bankInfo){
		bankInfo.getItemsInfo().removeBankToInventory(pl.getPlayer());
		returnToItems(pl);
	}
	
	private void removeHotbar(CorePlayers pl, BankInfo bankInfo){
		bankInfo.getItemsInfo().removeBankToHotbar(pl.getPlayer());
		returnToItems(pl);
	}
	
	protected void returnToItems(CorePlayers pl){
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_ITEMS);
	}
	
	@Override
	public BankInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankInfo.class);
	}
	
}
