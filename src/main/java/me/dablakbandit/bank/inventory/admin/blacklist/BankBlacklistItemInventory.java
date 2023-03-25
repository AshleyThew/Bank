package me.dablakbandit.bank.inventory.admin.blacklist;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.admin.BankAdminInfo;
import me.dablakbandit.core.players.CorePlayers;

public class BankBlacklistItemInventory extends BankInventoryHandler<BankAdminInfo>{
	
	@Override
	public void init(){
		int size = descriptor.getSize();
		setAll(size, BankItemConfiguration.BANK_ITEM_BLANK);
		setItem(BankItemConfiguration.BANK_ITEM_BLACKLIST_BACK, BankInventories.BANK_ADMIN_BLACKLIST);
		setItem(3, this::getData, this::toggleData);
		setItem(5, this::getNBT, this::toggleNBT);
	}
	
	private ItemStack getNBT(BankAdminInfo info){
		BankItemPath item = BankItemConfiguration.BANK_ITEM_BLACKLIST_RED;
		if(info.getItem().isMatchNBT()){
			item = BankItemConfiguration.BANK_ITEM_BLACKLIST_GREEN;
		}
		return clone(item.get(), item.getName().replaceAll("<name>", "Match NBT"));
	}
	
	private ItemStack getData(BankAdminInfo info){
		BankItemPath item = BankItemConfiguration.BANK_ITEM_BLACKLIST_RED;
		if(info.getItem().isMatchData()){
			item = BankItemConfiguration.BANK_ITEM_BLACKLIST_GREEN;
		}
		return clone(item.get(), item.getName().replaceAll("<name>", "Match Data"));
	}
	
	private void toggleNBT(CorePlayers pl, BankAdminInfo info){
		info.getItem().toggleMatchNBT();
		pl.refreshInventory();
	}
	
	private void toggleData(CorePlayers pl, BankAdminInfo info){
		info.getItem().toggleMatchData();
		pl.refreshInventory();
	}
	
	@Override
	public BankAdminInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankAdminInfo.class);
	}
	
	@Override
	public void onClose(CorePlayers pl, Player player){
		super.onClose(pl, player);
		ItemBlacklistImplementation.getInstance().save();
	}
}
