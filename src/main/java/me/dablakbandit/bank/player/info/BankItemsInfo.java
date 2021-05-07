package me.dablakbandit.bank.player.info;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.Version;
import me.dablakbandit.core.utils.itemutils.IItemUtils;
import me.dablakbandit.core.utils.json.strategy.Exclude;
import me.dablakbandit.core.vault.Eco;

public class BankItemsInfo extends IBankInfo implements JSONInfo, PermissionsInfo{
	
	private static IItemUtils				itemUtils		= ItemUtils.getInstance();
	
	private Map<Integer, List<ItemStack>>	itemMap			= Collections.synchronizedMap(new HashMap<>());
	private Map<Integer, ItemStack>			tabItemMap		= Collections.synchronizedMap(new HashMap<>());
	private Map<Integer, String>			tabNameMap		= Collections.synchronizedMap(new HashMap<>());
	protected Map<Integer, Integer>			boughtSlotsMap	= Collections.synchronizedMap(new HashMap<>());
	private int								openTab			= 1;
	private int								scrolled		= 0;
	private int								boughtTabs;
	private int								commandSlots, permissionSlots;
	@Exclude
	private int								buySlots, buyTabs;
	
	public BankItemsInfo(CorePlayers pl){
		super(pl);
		for(int i = 1; i <= 9; i++){
			itemMap.computeIfAbsent(i, ArrayList::new);
		}
	}
	
	public Map<Integer, List<ItemStack>> getItemMap(){
		return itemMap;
	}
	
	public Map<Integer, ItemStack> getTabItemMap(){
		return tabItemMap;
	}
	
	public int getOpenTab(){
		return openTab;
	}
	
	public void setOpenTab(int openTab){
		this.openTab = openTab;
	}
	
	public int getScrolled(){
		return scrolled;
	}
	
	public void addScrolled(int add){
		this.scrolled = Math.max(0, this.scrolled + add);
	}
	
	public void setScrolled(int scrolled){
		this.scrolled = Math.max(0, scrolled);
	}
	
	public int getBoughtTabs(){
		return boughtTabs;
	}
	
	public void setBoughtTabs(int boughtTabs){
		this.boughtTabs = boughtTabs;
	}
	
	public int getCommandSlots(){
		return commandSlots;
	}
	
	public void addCommandSlots(int commandSlots){
		this.commandSlots += commandSlots;
	}
	
	public void setCommandSlots(int commandSlots){
		this.commandSlots = commandSlots;
	}
	
	public int getPermissionSlots(){
		return permissionSlots;
	}
	
	@Deprecated
	public void setPermissionSlots(int permissionSlots){
		this.permissionSlots = permissionSlots;
	}
	
	public int getBuySlots(){
		return buySlots;
	}
	
	public void incrementBuySlots(){
		if(getBoughtSlots(openTab) + buySlots < BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_MAX.get()){
			this.buySlots++;
		}
	}
	
	public void decrementBuySlots(){
		this.buySlots = Math.max(0, this.buySlots - 1);
	}
	
	public int getBuyTabs(){
		return buyTabs;
	}
	
	public void incrementBuyTabs(){
		if(getBankAllowedTabs(pl.getPlayer()) + buyTabs < 9){
			this.buyTabs++;
		}
	}
	
	public void decrementBuyTabs(){
		this.buyTabs = Math.max(0, this.buyTabs - 1);
	}
	
	public Map<Integer, Integer> getBoughtSlotsMap(){
		return boughtSlotsMap;
	}
	
	@Override
	public void jsonInit(){
		for(int i = 1; i <= 9; i++){
			itemMap.computeIfAbsent(i, ArrayList::new);
		}
		itemMap.values().forEach(l -> l.removeIf(is -> is == null || is.getType() == Material.AIR));
	}
	
	@Override
	public void jsonFinal(){
		
	}
	
	public void addInventoryToBank(Player player){
		addToBank(player, 9, 36);
	}
	
	public void addAllInventoryToBank(Player player){
		addToBank(player, 0, 36);
		addOffhandToBank(player);
	}
	
	public void addHotbarToBank(Player player){
		addToBank(player, 0, 9);
		addOffhandToBank(player);
	}
	
	public void addOffhandToBank(Player player){
		if(!Version.isAtleastNine()){ return; }
		ItemStack is = player.getInventory().getItemInOffHand();
		if(isEmpty(is)){ return; }
		player.getInventory().setItemInOffHand(addBankItem(player, is));
	}
	
	protected void addToBank(Player player, int x, int z){
		Inventory inv = player.getInventory();
		for(int i = x; i < z; i++){
			ItemStack is = inv.getItem(i);
			if(isEmpty(is)){
				continue;
			}
			is = addBankItem(player, is);
			inv.setItem(i, is);
			if(is != null){ return; }
		}
	}
	
	public ItemStack addBankItem(Player player, ItemStack is){
		return addBankItem(player, is, openTab);
	}
	
	public ItemStack addBankItem(Player player, ItemStack is, int tab){
		if(isEmpty(is)){ return is; }
		if(ItemBlacklistImplementation.getInstance().isBlacklisted(is)){
			// pl.getPlayer().sendMessage(LanguageConfiguration.MESSAGE_ITEM_IS_BLACKLISTED.getMessage());
			return is;
		}
		int pass = getBankAllowedTabs(player);
		if(tab > pass){ return is; }
		
		int itemSize = is.getAmount();
		is = mergeBank(is, tab);
		if(isEmpty(is)){
			save(BankPluginConfiguration.BANK_SAVE_ITEM_DEPOSIT);
			return null;
		}
		int size = getBankSize(openTab);
		if(size < BankPluginConfiguration.BANK_ITEMS_TABS_SIZE_MAX.get()){
			if(getBankSize(tab) < getBankSlots(tab)){
				itemMap.get(tab).add(is);
				save(BankPluginConfiguration.BANK_SAVE_ITEM_DEPOSIT);
				return null;
			}
		}
		if(is.getAmount() != itemSize){
			save(BankPluginConfiguration.BANK_SAVE_ITEM_DEPOSIT);
		}
		// player.sendMessage(LanguageConfiguration.MESSAGE_BANK_IS_FULL.getMessage());
		return is;
	}
	
	protected ItemStack mergeBank(ItemStack is, int tab){
		int left = is.getAmount();
		int max = is.getMaxStackSize();
		for(ItemStack is1 : itemMap.get(tab)){
			if(!canMerge(is, is1)){
				continue;
			}
			int taken = max - is1.getAmount();
			if(taken > left){
				is1.setAmount(is1.getAmount() + left);
				return null;
			}else{
				left -= taken;
				is1.setAmount(max);
			}
		}
		is.setAmount(left);
		return is;
	}
	
	public boolean canMerge(ItemStack from, ItemStack to){
		if(from == null || to == null){ return false; }
		if(!itemUtils.canMerge(from, to)){ return false; }
		int max = to.getMaxStackSize();
		return to.getAmount() < max;
	}
	
	private boolean isEmpty(ItemStack is){
		return is == null || is.getType() == Material.AIR;
	}
	
	public int getBankAllowedTabs(Player player){
		int pass = BankPluginConfiguration.BANK_ITEMS_TABS_DEFAULT.get() + boughtTabs;
		pass = Math.min(pass, 9);
		if(BankPluginConfiguration.BANK_ITEMS_TABS_PERMISSION_ENABLED.get() && player != null){
			for(int i = 9; i > 1; i--){
				if(player.hasPermission("bank.tabs." + i)){
					if(i > pass){ return i; }
				}
			}
		}
		return pass;
	}
	
	public int getBankSize(int page){
		if(BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_PER_TAB.get()){ return itemMap.get(openTab).size(); }
		return itemMap.values().stream().map(List::size).mapToInt(Integer::intValue).sum();
	}
	
	public int getBoughtSlots(int page){
		return BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_PER_TAB.get() ? boughtSlotsMap.getOrDefault(page, 0) : boughtSlotsMap.getOrDefault(1, 0);
	}
	
	public int getBankSlots(int page){
		return BankPluginConfiguration.BANK_ITEMS_SLOTS_DEFAULT.get() + getBoughtSlots(page) + commandSlots + permissionSlots;
	}
	
	public int getBankSlots(){
		return getBankSlots(openTab);
	}
	
	public int getTotalSlots(){
		if(BankPluginConfiguration.BANK_ITEMS_TABS_ENABLED.get() && BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_PER_TAB.get()){
			int t = 0;
			for(int i = 1; i <= 9; i++){
				t += getBankSlots(i);
			}
			return t;
		}else{
			return getBankSlots(1);
		}
	}
	
	public int getTotalUsedSlots(){
		if(BankPluginConfiguration.BANK_ITEMS_TABS_ENABLED.get()){
			int t = 0;
			for(int i = 1; i <= 9; i++){
				t += getItems().get(i).size();
			}
			return t;
		}else{
			return getItems().get(1).size();
		}
	}
	
	public ItemStack getBankItemAtInt(int slot, int page){
		List<ItemStack> listItems = getItems(page);
		if(slot >= listItems.size()){ return null; }
		return listItems.get(slot);
	}
	
	public List<ItemStack> getItems(int page){
		return itemMap.get(page);
	}
	
	@Deprecated
	private void setBankItemAtInt(int slot, int page, ItemStack is){
		List<ItemStack> listItems = itemMap.get(page);
		listItems.set(slot, is);
	}
	
	private void removeBankItemAtInt(int slot, int page){
		List<ItemStack> listItems = itemMap.get(page);
		listItems.remove(slot);
	}
	
	public void removeBankToInventory(Player player){
		boolean taken = removeBankTo(player, 9, 36);
		if(taken){
			save(BankPluginConfiguration.BANK_SAVE_ITEM_WITHDRAW);
		}
	}
	
	public void removeAllBankToInventory(Player player){
		boolean taken = false;
		taken |= removeBankTo(player, 0, 36);
		taken |= removeBankToOffhand(player);
		if(taken){
			save(BankPluginConfiguration.BANK_SAVE_ITEM_WITHDRAW);
		}
	}
	
	public void removeBankToHotbar(Player player){
		boolean taken = false;
		taken |= removeBankTo(player, 0, 9);
		taken |= removeBankToOffhand(player);
		if(taken){
			save(BankPluginConfiguration.BANK_SAVE_ITEM_WITHDRAW);
		}
	}
	
	protected boolean removeBankTo(Player player, int x, int z){
		Inventory inv = player.getInventory();
		List<ItemStack> items = this.itemMap.get(this.openTab);
		Iterator<ItemStack> it = items.iterator();
		boolean taken = false;
		while(it.hasNext()){
			ItemStack is = it.next();
			for(int i = x; i < z; i++){
				ItemStack is1 = inv.getItem(i);
				if(isEmpty(is1)){
					inv.setItem(i, is);
					it.remove();
					taken = true;
					break;
				}else if(canMerge(is, is1)){
					int size = is.getAmount();
					int max = is1.getMaxStackSize();
					int b = max - is1.getAmount();
					if(b > size){
						is1.setAmount(is1.getAmount() + size);
						it.remove();
						taken = true;
						break;
					}else{
						size -= b;
						is1.setAmount(max);
						is.setAmount(size);
						taken = true;
					}
				}
			}
		}
		return taken;
	}
	
	private boolean removeBankToOffhand(Player player){
		if(!Version.isAtleastNine()){ return false; }
		List<ItemStack> items = this.itemMap.get(this.openTab);
		Iterator<ItemStack> it = items.iterator();
		boolean taken = false;
		while(it.hasNext()){
			ItemStack is = it.next();
			ItemStack is1 = player.getInventory().getItemInOffHand();
			if(isEmpty(is1)){
				player.getInventory().setItemInOffHand(is);
				it.remove();
				taken = true;
				break;
			}else if(canMerge(is, is1)){
				int size = is.getAmount();
				int max = is1.getMaxStackSize();
				int b = max - is1.getAmount();
				if(b > size){
					is1.setAmount(is1.getAmount() + size);
					it.remove();
					taken = true;
					break;
				}else{
					size -= b;
					is1.setAmount(max);
					is.setAmount(size);
					taken = true;
				}
			}
		}
		return taken;
	}
	
	private ItemStack mergeIntoInventory(Player player, ItemStack merge, int amount){
		if(merge == null){ return null; }
		Inventory inv = player.getInventory();
		for(int pSlot = 0; pSlot < 36; pSlot++){
			ItemStack is1 = inv.getItem(pSlot);
			if(!canMerge(merge, is1)){
				continue;
			}
			int size = Math.min(merge.getAmount(), amount);
			int max = is1.getMaxStackSize();
			int possible = max - is1.getAmount();
			if(possible >= size){
				is1.setAmount(is1.getAmount() + size);
				return null;
			}else{
				is1.setAmount(max);
				merge.setAmount(size - possible);
			}
		}
		return merge;
	}
	
	private ItemStack addIntoInventory(Player player, ItemStack add){
		if(add == null){ return null; }
		Inventory inv = player.getInventory();
		for(int i = 0; i < 36; i++){
			ItemStack inventoryItem = inv.getItem(i);
			if(isEmpty(inventoryItem)){
				inv.setItem(i, add);
				return null;
			}
		}
		return add;
	}
	
	private boolean takeBankItemAt(Player player, int tab, int slot, int take){
		Inventory inv = player.getInventory();
		ItemStack is = getBankItemAtInt(slot, tab);
		if(is == null){ return false; }
		int original = is.getAmount();
		int toTake = Math.min(is.getAmount(), take);
		int left = is.getAmount() - toTake;
		ItemStack copy = is.clone();
		copy.setAmount(toTake);
		copy = mergeIntoInventory(player, copy, toTake);
		copy = addIntoInventory(player, copy);
		left += copy != null ? copy.getAmount() : 0;
		if(left <= 0){
			removeBankItemAtInt(slot, tab);
			return true;
		}else{
			is.setAmount(left);
			return left != original;
		}
	}
	
	public boolean takeBankItemAt(Player player, int tab, int slot, boolean half){
		ItemStack ia = getBankItemAtInt(slot, tab);
		if(ia == null){ return false; }
		int total = ia.getAmount();
		boolean taken = takeBankItemAt(player, tab, slot, half ? (int)Math.ceil(ia.getAmount() / 2.0) : total);
		if(taken){
			save(BankPluginConfiguration.BANK_SAVE_ITEM_WITHDRAW);
		}
		return taken;
	}
	
	public void sort(Comparator<ItemStack> comparator){
		sort(openTab, comparator);
	}
	
	public void sort(int page, Comparator<ItemStack> comparator){
		List<ItemStack> list = itemMap.get(page);
		list.sort(comparator);
		itemMap.put(page, list);
	}
	
	public boolean buySlots(CorePlayers pl){
		if(buySlots == 0){ return false; }
		double d = buySlots * BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_COST.get();
		if(Eco.getInstance().getEconomy().has(pl.getName(), d) && Eco.getInstance().getEconomy().withdrawPlayer(pl.getName(), d).transactionSuccess()){
			// player.sendMessage(LanguageConfiguration.MESSAGE_SLOTS_BOUGHT.getMessage().replace("<i>", "" + buy_slot_amount).replace("<p>", Format.formatMoney(d)));
			int slot = BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_PER_TAB.get() ? openTab : 1;
			boughtSlotsMap.put(slot, getBoughtSlots(slot) + buySlots);
			buySlots = 0;
			return true;
		}else{
			// player.sendMessage(LanguageConfiguration.MESSAGE_SLOTS_FAILED.getMessage());
		}
		return false;
	}
	
	public boolean buyTabs(CorePlayers pl){
		if(buyTabs == 0){ return false; }
		double d = buyTabs * BankPluginConfiguration.BANK_ITEMS_TABS_BUY_COST.get();
		if(Eco.getInstance().getEconomy().has(pl.getName(), d) && Eco.getInstance().getEconomy().withdrawPlayer(pl.getName(), d).transactionSuccess()){
			// player.sendMessage(LanguageConfiguration.MESSAGE_SLOTS_BOUGHT.getMessage().replace("<i>", "" + buy_slot_amount).replace("<p>", Format.formatMoney(d)));
			boughtTabs += buyTabs;
			buyTabs = 0;
			return true;
		}else{
			// player.sendMessage(LanguageConfiguration.MESSAGE_SLOTS_FAILED.getMessage());
		}
		return false;
	}
	
	public Map<Integer, List<ItemStack>> getItems(){
		return this.itemMap;
	}
	
	@Override
	public void checkPermissions(){
		Collection<PermissionAttachmentInfo> permissions = pl.getPlayer().getEffectivePermissions();
		
		List<Integer> maxList = BankPermissionConfiguration.PERMISSION_SLOTS.getValue(pl, permissions);
		if(maxList.size() > 0){
			if(BankPluginConfiguration.BANK_ITEMS_SLOTS_PERMISSION_COMBINE.get()){
				permissionSlots = maxList.stream().mapToInt(Integer::intValue).sum();
			}else{
				permissionSlots = Collections.max(maxList);
			}
		}else{
			permissionSlots = 0;
		}
	}
}
