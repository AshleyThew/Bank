package me.dablakbandit.bank.implementations.blacklist;

import java.util.List;
import java.util.stream.Collectors;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.bank.config.BankItemBlacklistConfiguration;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.core.utils.json.JSONParser;

public class ItemBlacklistImplementation extends BankImplementation{
	
	private static final ItemBlacklistImplementation implementation = new ItemBlacklistImplementation();
	
	public static ItemBlacklistImplementation getInstance(){
		return implementation;
	}
	
	private boolean					enabled	= false;
	private final List<BlacklistedItem>	blacklisted;
	
	private ItemBlacklistImplementation(){
		this.blacklisted = BankItemBlacklistConfiguration.BLACKLIST.get().stream().map(string -> JSONParser.fromJSON(string, BlacklistedItem.class)).collect(Collectors.toList());
	}
	
	public boolean isBlacklisted(ItemStack itemStack){
		if(!enabled){ return false; }
		boolean blacklisted = isItemBlacklisted(itemStack) || isNameBlacklisted(itemStack) || isLoreBlacklisted(itemStack);
		return BankPluginConfiguration.BANK_ITEMS_BLACKLIST_MODE.get() == BlacklistMode.WHITELIST ? !blacklisted : blacklisted;
	}
	
	private boolean isItemBlacklisted(ItemStack itemStack){
		return blacklisted.stream().anyMatch(bi -> bi.equals(itemStack));
	}
	
	private boolean isNameBlacklisted(ItemStack itemStack){
		ItemMeta itemMeta = itemStack.getItemMeta();
		if(itemMeta == null || !itemMeta.hasDisplayName()){ return false; }
		String displayName = itemMeta.getDisplayName();
		String strippedName = ChatColor.stripColor(displayName);
		return BankItemBlacklistConfiguration.BLACKLISTED_NAME.get().stream().anyMatch(blacklistedName -> strippedName.contains(blacklistedName) || strippedName.matches(blacklistedName));
	}
	
	private boolean isLoreBlacklisted(ItemStack itemStack){
		ItemMeta itemMeta = itemStack.getItemMeta();
		if(itemMeta == null || !itemMeta.hasLore()){ return false; }
		List<String> itemMetaLore = itemMeta.getLore();
		return itemMetaLore.stream().anyMatch(lore -> {
			String strippedLore = ChatColor.stripColor(lore);
			return BankItemBlacklistConfiguration.BLACKLISTED_LORE.get().stream().anyMatch(blacklistedLore -> strippedLore.contains(blacklistedLore) || strippedLore.matches(blacklistedLore));
		});
	}
	
	@Override
	public void load(){
		
	}
	
	public void save(){
		BankItemBlacklistConfiguration.BLACKLIST.set(blacklisted.stream().map(bi -> JSONParser.toJson(bi).toString()).collect(Collectors.toList()));
	}
	
	public List<BlacklistedItem> getBlacklisted(){
		return blacklisted;
	}
	
	@Override
	public void enable(){
		enabled = true;
	}
	
	@Override
	public void disable(){
		enabled = false;
	}
}
