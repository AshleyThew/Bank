package me.dablakbandit.bank.player.info;

import java.util.*;

import me.dablakbandit.bank.config.path.impl.BankPermissionStringListPath;
import me.dablakbandit.bank.implementations.def.ItemDefault;
import me.dablakbandit.bank.implementations.def.ItemDefaultImplementation;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.handler.BankItemsHandler;
import me.dablakbandit.bank.player.info.item.BankItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.json.strategy.Exclude;

public class BankItemsInfo extends IBankInfo implements JSONInfo, PermissionsInfo, BankDefaultInfo{


	@Deprecated
	private final Map<Integer, List<ItemStack>>	itemMap			= Collections.synchronizedMap(new HashMap<>());

	private final Map<Integer, List<BankItem>> bankItemMap = Collections.synchronizedMap(new HashMap<>());
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
		for(int i = 1; i <= 9; i++){
			bankItemMap.computeIfAbsent(i, ArrayList::new);
		}
		this.bankItemsHandler = new BankItemsHandler(this);
	}

	public Map<Integer, List<BankItem>> getBankItemMap() {
		return bankItemMap;
	}
	
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
	
	@Override
	public void jsonInit(){
		for(int i = 1; i <= 9; i++){
			bankItemMap.computeIfAbsent(i, ArrayList::new);
		}
		itemMap.values().forEach(l -> l.removeIf(is -> is == null || is.getType() == Material.AIR));
		itemMap.keySet().removeIf(i -> itemMap.get(i).isEmpty());
		for (Map.Entry<Integer, List<ItemStack>> entry : itemMap.entrySet()) {
			List<BankItem> bankItems = bankItemMap.get(entry.getKey());
			for (ItemStack item : entry.getValue()) {
				bankItems.add(new BankItem(item));
			}
		}
		itemMap.clear();
	}
	
	@Override
	public void jsonFinal(){
		
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
		totalTabCount = Math.max(0, tabCount);
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
