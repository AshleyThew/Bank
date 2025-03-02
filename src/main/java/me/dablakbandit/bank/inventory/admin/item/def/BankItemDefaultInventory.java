package me.dablakbandit.bank.inventory.admin.item.def;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.implementations.def.ItemDefault;
import me.dablakbandit.bank.implementations.def.ItemDefaultImplementation;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BankItemDefaultInventory extends BankInventoryHandler<CorePlayers>{
	
	private final ItemDefaultImplementation implementation	= ItemDefaultImplementation.getInstance();
	private int							scrolled;
	
	@Override
	public void init(){
		addInfo();
		addScrolls();
		int size = descriptor.getSize();
		addItems(size);
	}
	
	private void addInfo(){
		ItemStack is = clone(BankItemConfiguration.BANK_ADMIN_ITEM_BLANK.get(), "Click an item in your inventory to add");
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
			setItem(slot, (i) -> getItemStack(finalItem), (pl, t, inventory, event) -> onClick(pl, finalItem, event));
		}
	}
	
	@Override
	public CorePlayers getInvoker(CorePlayers pl){
		return pl;
	}
	
	public void onClick(CorePlayers pl, int slot, InventoryClickEvent event){
		if(slot >= implementation.getDefault().size()){ return; }
		if(event.isRightClick()){
			implementation.getDefault().remove(slot);
			pl.refreshInventory();
		}
	}
	
	private ItemStack getItemStack(int slot){
		int get = scrolled * 9 + slot;
		if(get >= implementation.getDefault().size()){ return null; }
		ItemDefault item = implementation.getDefault().get(get);
		ItemStack is = item.getItemStack();
		return add(is.clone(), "Right click to remove");
	}
}
