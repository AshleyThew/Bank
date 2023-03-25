package me.dablakbandit.bank.inventory.item;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.players.CorePlayers;

public class BankTrashcanInventory extends BankInventoryHandler<BankInfo>{
	
	@Override
	public void init(){
		setItem(BankItemConfiguration.BANK_TRASHCAN_BACK, consumeSound(this::returnToItems, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
	}
	
	protected void returnToItems(CorePlayers pl){
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_ITEMS);
	}
	
	@Override
	public BankInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankInfo.class);
	}
	
}
