package me.dablakbandit.bank.inventory.admin.blacklist;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.implementations.blacklist.BlacklistedItem;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.admin.BankAdminInfo;
import me.dablakbandit.core.players.CorePlayers;

import java.util.List;

public class BankBlacklistInventory extends BankInventoryHandler<CorePlayers>{
	
	private final ItemBlacklistImplementation	implementation	= ItemBlacklistImplementation.getInstance();
	private int							scrolled;
	
	@Override
	public void init(){
		addInfo();
		addScrolls();
		int size = descriptor.getSize();
		addItems(size);
	}
	
	private void addInfo(){
		ItemStack is = clone(BankItemConfiguration.BANK_ITEM_BLANK.get(), "Click an item in your inventory to add");
		for(int i = 0; i < 9; i++){
			setItem(i, () -> is);
		}
	}
	
	private void addScrolls(){
		setItem(BankItemConfiguration.BANK_ITEM_BLACKLIST_SCROLL_UP, (pl) -> addScroll(pl, -1));
		setItem(BankItemConfiguration.BANK_ITEM_BLACKLIST_SCROLL_DOWN, (pl) -> addScroll(pl, 1));
	}
	
	private void addScroll(CorePlayers pl, int add){
		this.scrolled = Math.max(0, this.scrolled + add);
		pl.refreshInventory();
	}
	
	private void addItems(int size){
		int show = BankItemConfiguration.BANK_ITEM_BLACKLIST_ITEMS.getExtendValue("Slots", Integer.class);
		int start = BankItemConfiguration.BANK_ITEM_BLACKLIST_ITEMS.getExtendValue("Start", Integer.class);
		for(int item = 0; item < show; item++){
			int slot = item + start;
			if(slot > size){
				break;
			}
			int finalItem = item;
			setItem(slot, (pl, i) -> getItemStack(pl, finalItem), (pl, t, inventory, event) -> onClick(pl, finalItem, event));
		}
	}
	
	@Override
	public CorePlayers getInvoker(CorePlayers pl){
		return pl;
	}
	
	public void onClick(CorePlayers pl, int slot, InventoryClickEvent event){
		BankAdminInfo adminInfo = pl.getInfo(BankAdminInfo.class);
		List<BlacklistedItem> blacklisted = implementation.getBlacklisted(adminInfo.getBlacklistType());
		if(slot >= blacklisted.size()){ return; }
		if(event.isRightClick()){
			blacklisted.remove(slot);
			pl.refreshInventory();
		}else{
			BlacklistedItem item = blacklisted.get(slot);
			adminInfo.setItem(item);
			BankInventoriesManager.getInstance().openBypass(pl, BankInventories.BANK_ADMIN_BLACKLIST_ITEM);
		}
	}
	
	private ItemStack getItemStack(CorePlayers pl, int slot){
		BankAdminInfo adminInfo = pl.getInfo(BankAdminInfo.class);
		int get = scrolled * 9 + slot;
		List<BlacklistedItem> blacklisted = implementation.getBlacklisted(adminInfo.getBlacklistType());
		if(get >= blacklisted.size()){ return null; }
		BlacklistedItem item = blacklisted.get(get);
		ItemStack is = item.getItemStack();
		return add(is.clone(), "Match Data: " + item.isMatchData(), "Match NBT: " + item.isMatchNBT(), "Left click to edit", "Right click to remove");
	}
}
