package me.dablakbandit.bank.inventory.item;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.players.CorePlayers;

public class BankAddItemsInventory extends BankInventoryHandler<BankInfo>{
	
	@Override
	public void init(){
		int size = descriptor.getSize();
		setAll(size, BankItemConfiguration.BANK_ITEM_BLANK);
		setItem(0, BankItemConfiguration.BANK_BACK, consumeSound(this::returnToItems, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
		setItem(BankItemConfiguration.BANK_ADD_ALL, consumeSound(this::addAll, BankSoundConfiguration.INVENTORY_ITEMS_ADD_ALL));
		setItem(BankItemConfiguration.BANK_ADD_INVENTORY, consumeSound(this::addInventory, BankSoundConfiguration.INVENTORY_ITEMS_ADD_INVENTORY));
		setItem(BankItemConfiguration.BANK_ADD_HOTBAR, consumeSound(this::addHotbar, BankSoundConfiguration.INVENTORY_ITEMS_ADD_HOTBAR));
	}
	
	private void addAll(CorePlayers pl, BankInfo bankInfo){
		bankInfo.getItemsInfo().addAllInventoryToBank(pl.getPlayer());
		returnToItems(pl);
	}
	
	private void addInventory(CorePlayers pl, BankInfo bankInfo){
		bankInfo.getItemsInfo().addInventoryToBank(pl.getPlayer());
		returnToItems(pl);
	}
	
	private void addHotbar(CorePlayers pl, BankInfo bankInfo){
		bankInfo.getItemsInfo().addHotbarToBank(pl.getPlayer());
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
