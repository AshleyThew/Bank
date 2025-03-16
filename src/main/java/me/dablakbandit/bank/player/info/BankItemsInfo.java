package me.dablakbandit.bank.player.info;

import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.path.impl.BankPermissionStringListPath;
import me.dablakbandit.bank.implementations.def.ItemDefault;
import me.dablakbandit.bank.implementations.def.ItemDefaultImplementation;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.handler.BankItemsHandler;
import me.dablakbandit.bank.player.info.item.BankItem;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.json.strategy.Exclude;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;
import java.util.stream.Collectors;

public class BankItemsInfo extends IBankInfo implements JSONInfo, PermissionsInfo, BankDefaultInfo{

	@Deprecated
	private final Map<Integer, List<ItemStack>>	itemMap			= Collections.synchronizedMap(new HashMap<>());

	@Deprecated
	private final Map<Integer, List<BankItem>> bankItemMap = Collections.synchronizedMap(new HashMap<>());

	private final Map<Integer, Map<Integer, BankItem>> bankItemSlotMap = Collections.synchronizedMap(new HashMap<>());

	private final Map<Integer, ItemStack>		tabItemMap		= Collections.synchronizedMap(new HashMap<>());
	private final Map<Integer, String>			tabNameMap		= Collections.synchronizedMap(new HashMap<>());
	protected final Map<Integer, Integer>		boughtSlotsMap	= Collections.synchronizedMap(new HashMap<>());
	private int									openTab			= 1;
	private int									scrolled		= 0;
	private int									boughtTabs;
	private int									commandSlots, permissionSlots;

	private int permissionMergeMax;
	@Exclude
	private int totalTabCount;
	@Exclude
	private final BankItemsHandler bankItemsHandler;
	
	public BankItemsInfo(CorePlayers pl){
		super(pl);
		this.bankItemsHandler = new BankItemsHandler(this);
	}

	public Collection<List<BankItem>> getBankItems() {
		return bankItemSlotMap.values().stream().map(Map::values).map(ArrayList::new).collect(Collectors.toList());
	}

	public Map<Integer, BankItem> getTabBankItemsMap(int tab) {
		return bankItemSlotMap.computeIfAbsent(tab, k -> Collections.synchronizedMap(new HashMap<>()));
	}


//	public List<BankItem> getTabBankItems(int tab) {
//		Map<Integer, BankItem> slotMap = getTabBankItemsMap(tab);
//		List<BankItem> bankItems = new ArrayList<>(slotMap.values());
//		bankItems.removeIf(bi -> bi.getItemStack() == null || bi.getItemStack().getType() == Material.AIR);
//		return bankItems;
//	}
	
	public Map<Integer, ItemStack> getTabItemMap(){
		return tabItemMap;
	}

	public Map<Integer, String> getTabNameMap() {
		return tabNameMap;
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

	public int getPermissionMergeMax() {
		return permissionMergeMax;
	}

	public Map<Integer, Integer> getBoughtSlotsMap(){
		return boughtSlotsMap;
	}
	
	public int getTotalTabCount(){
		return totalTabCount;
	}

	public int getMaxTabNotEmpty() {
		return bankItemSlotMap.entrySet().stream().filter(e -> !e.getValue().isEmpty()).mapToInt(Map.Entry::getKey).max().orElse(1);
	}
	
	@Override
	public void jsonInit(){
		// Convert old list-based items to new slot-based items
		for (Map.Entry<Integer, List<BankItem>> entry : bankItemMap.entrySet()) {
			int tab = entry.getKey();
			List<BankItem> items = entry.getValue();
			Map<Integer, BankItem> slotMap = getTabBankItemsMap(tab);

			// Assign consecutive slots for now, but these can be changed later
			for (int i = 0; i < items.size(); i++) {
				BankItem item = items.get(i);
				if (item.getItemStack() != null && item.getItemStack().getType() != Material.AIR) {
					slotMap.put(i, item);
				}
			}
		}

		// Handle legacy conversion
		itemMap.values().forEach(l -> l.removeIf(is -> is == null || is.getType() == Material.AIR));
		itemMap.keySet().removeIf(i -> itemMap.get(i).isEmpty());
		for (Map.Entry<Integer, List<ItemStack>> entry : itemMap.entrySet()) {
			int tab = entry.getKey();
			List<ItemStack> items = entry.getValue();
			Map<Integer, BankItem> slotMap = getTabBankItemsMap(tab);

			for (int i = 0; i < items.size(); i++) {
				ItemStack item = items.get(i);
				if (item != null && item.getType() != Material.AIR) {
					slotMap.put(i, new BankItem(item));
				}
			}
		}
		itemMap.clear();
		bankItemMap.clear(); // Clear the old lists since we're using maps now
	}
	
	@Override
	public void jsonFinal(){
		bankItemSlotMap.values().removeIf(Map::isEmpty);
	}

	@Override
	public void checkPermissions(Permissible permissible, boolean debug){
		Collection<PermissionAttachmentInfo> permissions = permissible.getEffectivePermissions();
		if(!(permissible instanceof BankPermissionStringListPath.PathPermissible)) {
			List<Integer> maxList = BankPermissionConfiguration.PERMISSION_SLOTS.getValue(permissions);
			if (!maxList.isEmpty()) {
				if (BankPluginConfiguration.BANK_ITEMS_SLOTS_PERMISSION_COMBINE.get()) {
					permissionSlots = maxList.stream().mapToInt(Integer::intValue).sum();
				} else {
					permissionSlots = Collections.max(maxList);
				}
			} else {
				permissionSlots = 0;
			}
			if(debug){
				BankLog.debug("Permissions for " + pl.getName() + " slots is: " + permissionSlots);
			}
		}
		int tabCount = BankPluginConfiguration.BANK_ITEMS_TABS_DEFAULT.get() + boughtTabs;
		if(BankPluginConfiguration.BANK_ITEMS_TABS_PERMISSION_ENABLED.get()){
			int permissionsCount = 0;
			List<Integer> tabsList = BankPermissionConfiguration.PERMISSION_TABS.getValue(permissions);
			if(BankPluginConfiguration.BANK_ITEMS_TABS_PERMISSION_COMBINE.get()){
				permissionsCount = tabsList.stream().reduce(0, Integer::sum);
			}else{
				for(int tab : tabsList){
					permissionsCount = Math.max(permissionsCount, tab);
				}
			}
			tabCount = Math.max(tabCount, permissionsCount + boughtTabs);
			if(debug){
				BankLog.debug("Permissions for " + pl.getName() + " tabs is: " + permissionsCount);
			}
		}
		totalTabCount = Math.min(Math.max(0, tabCount), BankPluginConfiguration.BANK_ITEMS_TABS_MAX.get());
		List<Integer> permissionSlotMerge = BankPermissionConfiguration.PERMISSION_SLOT_MERGE.getValue(permissions);
		if (!permissionSlotMerge.isEmpty()) {
			permissionMergeMax = Math.max(Collections.max(permissionSlotMerge), BankPluginConfiguration.BANK_ITEMS_SLOTS_MERGE_MAX.get());
		} else {
			permissionMergeMax = BankPluginConfiguration.BANK_ITEMS_SLOTS_MERGE_MAX.get();
		}
	}

	@Override
	public void initDefault() {
		if(pl.getPlayer() != null && BankPluginConfiguration.BANK_ITEMS_DEFAULT_ENABLED.get()){
			for (ItemDefault itemDefault : ItemDefaultImplementation.getInstance().getDefault()) {
				bankItemsHandler.addBankItem(pl.getPlayer(), itemDefault.getItemStack(), true);
			}
		}
	}

	public BankItemsHandler getBankItemsHandler() {
		return bankItemsHandler;
	}

}
