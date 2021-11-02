package me.dablakbandit.bank.inventory.item;

import java.util.function.Consumer;

import me.dablakbandit.bank.api.BankAPI;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.inventory.AnvilInventory;
import me.dablakbandit.bank.inventory.exp.BankExpInventory;
import me.dablakbandit.bank.inventory.money.BankMoneyInventory;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.utils.EXPUtils;
import me.dablakbandit.core.vault.Eco;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.BankItemPath;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.core.players.CorePlayers;

public class BankItemsInventory extends BankInventoryHandler<BankInfo>{
	
	@Override
	public void init(){
		addBack();
		addSlots();
		addTabs();
		addTrashcan();
		addItemUtils();
		addScrolls();
		int size = descriptor.getSize();
		addItems(size);
		addTabs(size);
		setItem(BankItemConfiguration.BANK_ITEM_BLANK, this::doNothing);
	}
	
	private void addBack(){
		if(BankPluginConfiguration.BANK_ITEMS_ONLY.get()){
			setItem(0, BankItemConfiguration.BANK_ITEM_BLANK);
		}else{
			setItem(0, BankItemConfiguration.BANK_BACK, consumeSound(this::returnToMainMenu, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
		}
	}
	
	protected void returnToMainMenu(CorePlayers pl){
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU);
	}
	
	private void addSlots(){
		setItem(BankItemConfiguration.BANK_ITEM_SLOTS, this::getSlots,
				BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_ENABLED.get() ? consumeSound(getBuySlots(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_BUY_SLOTS) : this::doNothing);
	}
	
	private void doNothing(CorePlayers pl){
		
	}
	
	private ItemStack getSlots(BankItemPath path, BankInfo bankInfo){
		BankItemsInfo itemsInfo = bankInfo.getItemsInfo();
		int used = itemsInfo.getBankSize(itemsInfo.getOpenTab());
		int total = itemsInfo.getBankSlots(itemsInfo.getOpenTab());
		int available = total - used;
		return replaceNameLore(path, "<used>", "" + used, "<available>", "" + available, "<total>", "" + total);
	}
	
	private void addTabs(){
		setItem(BankItemConfiguration.BANK_ITEM_TABS, this::getTabs,
				BankPluginConfiguration.BANK_ITEMS_TABS_BUY_ENABLED.get() ? consumeSound(getBuyTabs(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_BUY_TABS) : this::doNothing);
	}
	
	private ItemStack getTabs(BankItemPath path, BankInfo bankInfo){
		return replaceNameLore(path, "<tabs>", "" + bankInfo.getItemsInfo().getTotalTabCount());
	}
	
	protected void addTrashcan(){
		if(!BankPluginConfiguration.BANK_ITEMS_TRASHCAN_ENABLED.get()){ return; }
		setItem(BankItemConfiguration.BANK_ITEM_TRASHCAN, consumeSound(getTrashcan(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_TRASHCAN));
	}
	
	protected Consumer<CorePlayers> getTrashcan(){
		return BankInventories.BANK_ITEMS_TRASHCAN;
	}
	
	protected void addItemUtils(){
		setItem(BankItemConfiguration.BANK_ITEM_ADD, consumeSound(getItemsAdd(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_ADD));
		setItem(BankItemConfiguration.BANK_ITEM_REMOVE, consumeSound(getItemsRemove(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_REMOVE));
		setItem(BankItemConfiguration.BANK_ITEM_SORT.getSlot(), BankItemConfiguration.BANK_ITEM_BLANK);
	}
	
	protected Consumer<CorePlayers> getBuySlots(){
		return BankInventories.BANK_ITEMS_BUY_SLOTS;
	}
	
	protected Consumer<CorePlayers> getBuyTabs(){
		return BankInventories.BANK_ITEMS_BUY_TABS;
	}
	
	protected Consumer<CorePlayers> getItemsAdd(){
		return BankInventories.BANK_ITEMS_ADD;
	}
	
	protected Consumer<CorePlayers> getItemsRemove(){
		return BankInventories.BANK_ITEMS_REMOVE;
	}
	
	private void open(CorePlayers pl, BankInventories inventories){
		BankInventoriesManager.getInstance().open(pl, inventories);
	}
	
	private void addScrolls(){
		setItem(BankItemConfiguration.BANK_ITEM_SCROLL_UP, (a, b) -> {
			addScroll(a, b, -1);
			BankSoundConfiguration.INVENTORY_ITEMS_SCROLL_UP.play(a);
		});
		setItem(BankItemConfiguration.BANK_ITEM_SCROLL_DOWN, (a, b) -> {
			addScroll(a, b, 1);
			BankSoundConfiguration.INVENTORY_ITEMS_SCROLL_DOWN.play(a);
		});
	}
	
	private void addScroll(CorePlayers pl, BankInfo bi, int add){
		bi.getItemsInfo().addScrolled(add);
		pl.refreshInventory();
	}
	
	private void addItems(int size){
		int show = BankItemConfiguration.BANK_ITEM_ITEMS.getSlot();
		if(!BankPluginConfiguration.BANK_ITEMS_TABS_ENABLED.get()){
			show += 9;
		}
		for(int item = 0; item < show; item++){
			int slot = item + 9;
			if(slot > size){
				break;
			}
			int finalItem = item;
			setItem(slot, (bi, i) -> getItemStack(bi, finalItem), this::onClick);
		}
		
	}
	
	private void addTabs(int size){
		if(!BankPluginConfiguration.BANK_ITEMS_TABS_ENABLED.get()){ return; }
		for(int tab = 1; tab <= 9; tab++){
			int slot = size - 10 + tab;
			if(slot < 0){
				break;
			}
			int finalTab = tab;
			setItem(slot, (bi, i) -> createTab(bi, finalTab), this::handleTab);
		}
	}
	
	@Override
	public BankInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankInfo.class);
	}
	
	public ItemStack createTab(BankInfo bankInfo, int tab){
		BankItemPath pathItem;
		BankItemPath pathDescription = null;
		BankItemsInfo itemsInfo = bankInfo.getItemsInfo();
		
		if(itemsInfo.getOpenTab() == tab){
			pathItem = BankItemConfiguration.BANK_ITEM_TAB_CURRENT;
		}else if(tab > itemsInfo.getTotalTabCount()){
			pathItem = BankItemConfiguration.BANK_ITEM_TAB_LOCKED;
			pathDescription = BankItemConfiguration.BANK_ITEM_TAB_LOCKED;
		}else{
			pathItem = BankItemConfiguration.BANK_ITEM_TAB_NUMBER;
		}
		if(pathDescription == null){
			pathDescription = pathItem;
		}
		ItemStack is = pathItem.get();
		is.setAmount(tab);
		String name = pathDescription.getName();
		if (BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_ENABLED.get()) {
			String set = bankInfo.getItemsInfo().getTabNameMap().get(tab);
			if(set!=null){
				name = set;
			}
		}
		return replaceCloneNameLore(is, name, pathDescription.getLore(), "<tab>", "" + tab, "<items>", "" + itemsInfo.getItemMap().get(tab).size());
	}
	
	public void onClick(CorePlayers pl, BankInfo bi, Inventory inv, InventoryClickEvent event){
		event.setCancelled(true);
		if(isHotbarSwap(event)){ return; }
		ItemStack is = event.getCursor();
		if(is != null && is.getType() != Material.AIR){
			handleItemInput(pl, bi.getItemsInfo(), is, event);
		}else{
			handleItemTake(pl, bi.getItemsInfo(), event);
		}
	}
	
	private boolean isHotbarSwap(InventoryClickEvent event){
		return event.getAction().equals(InventoryAction.HOTBAR_SWAP);
	}
	
	private void handleItemInput(CorePlayers pl, BankItemsInfo bi, ItemStack is, InventoryClickEvent event){
		ItemStack i = bi.addBankItem(pl.getPlayer(), is);
		event.setCursor(i);
		pl.refreshInventory();
		BankSoundConfiguration.INVENTORY_ITEMS_ITEM_ADD.play(pl);
	}
	
	private void handleItemTake(CorePlayers pl, BankItemsInfo bi, InventoryClickEvent event){
		ItemStack is = event.getCurrentItem();
		if(is == null || is.getType().equals(Material.AIR)){ return; }
		int itemSlot = event.getRawSlot() - 9;
		if(event.isShiftClick() || event.isLeftClick()){
			int clicked = bi.getScrolled() * 9 + itemSlot;
			if(bi.takeBankItemAt(pl.getPlayer(), bi.getOpenTab(), clicked, false)){
				BankSoundConfiguration.INVENTORY_ITEMS_ITEM_TAKE.play(pl);
				pl.refreshInventory();
			}
		}else if(event.isRightClick()){
			int clicked = bi.getScrolled() * 9 + itemSlot;
			if(bi.takeBankItemAt(pl.getPlayer(), bi.getOpenTab(), clicked, true)){
				BankSoundConfiguration.INVENTORY_ITEMS_ITEM_TAKE.play(pl);
				pl.refreshInventory();
			}
		}
	}
	
	protected void handleTab(CorePlayers pl, BankInfo bi, Inventory inv, InventoryClickEvent event){
		int tab = event.getRawSlot() - descriptor.getSize() + 10;
		if(BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_ENABLED.get() && event.isRightClick()){
			handleTabRename(pl, bi, tab);
			return;
		}
		if(bi.getItemsInfo().getOpenTab() == tab){ return; }
		bi.getItemsInfo().setOpenTab(tab);
		bi.getItemsInfo().addScrolled(-bi.getItemsInfo().getScrolled());
		pl.refreshInventory();
		BankSoundConfiguration.INVENTORY_ITEMS_CHANGE_TAB.play(pl);
	}

	protected void handleTabRename(CorePlayers pl, BankInfo bi, int tab){
		String current = bi.getItemsInfo().getTabNameMap().get(tab);
		if(current == null){
			current = " ";
		}
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_ITEM_TAB_RENAME.get().replace("<i>", "" + current), current){
			@Override
			public void cancel(CorePlayers pl){
				pl.setOpenInventory(BankItemsInventory.this);
			}

			@Override
			public void close(CorePlayers pl){
				pl.setOpenInventory(BankItemsInventory.this);
			}

			@Override
			public void onClick(CorePlayers pl, String value){
				if(value.startsWith(" ")){
					value = value.substring(1, value.length());
				}
				if(!value.isEmpty()){
					renameTab(pl, bi, tab, ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', value));
				}
				pl.setOpenInventory(BankItemsInventory.this);
			}
		});
	}

	protected void renameTab(CorePlayers pl, BankInfo bankInfo, int tab, String name){
		if(BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_MONEY.get() > 0){
			if(!Eco.getInstance().getEconomy().has(pl.getPlayer(), BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_MONEY.get())){
				return;
			}
		}
		if(BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_EXP.get() > 0){
			if(EXPUtils.getExp(pl.getPlayer()) < BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_EXP.get()){
				return;
			}
		}
		if(BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_MONEY.get() > 0){
			Eco.getInstance().getEconomy().withdrawPlayer(pl.getPlayer(), BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_MONEY.get());
		}
		if(BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_EXP.get() > 0){
			int exp = EXPUtils.getExp(pl.getPlayer());
			exp -= BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_EXP.get();
			EXPUtils.setExp(pl.getPlayer(), exp);
		}
		bankInfo.getItemsInfo().getTabNameMap().put(tab, name);
	}
	
	protected ItemStack getItemStack(BankInfo bi, Integer slot){
		BankItemsInfo itemsInfo = bi.getItemsInfo();
		return itemsInfo.getBankItemAtInt(slot + itemsInfo.getScrolled() * 9, itemsInfo.getOpenTab());
	}
	
	public void set(CorePlayers pl, Player player, Inventory inventory){
		ensureScrolled(getInvoker(pl).getItemsInfo());
		super.set(pl, player, inventory);
	}
	
	protected void ensureScrolled(BankItemsInfo info){
		int max = Math.max(0, (int)Math.ceil(info.getItems(info.getOpenTab()).size() / 9.0) - BankPluginConfiguration.BANK_ITEMS_TABS_ROWS.get());
		int current = info.getScrolled();
		int fixed = Math.min(max, current);
		if(current != fixed){
			info.setScrolled(fixed);
		}
	}
}
