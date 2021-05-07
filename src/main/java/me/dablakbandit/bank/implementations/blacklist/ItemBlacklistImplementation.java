package me.dablakbandit.bank.implementations.blacklist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.config.BankItemBlacklistConfiguration;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.core.utils.json.JSONParser;

public class ItemBlacklistImplementation extends BankImplementation{
	
	private static ItemBlacklistImplementation implementation = new ItemBlacklistImplementation();
	
	public static ItemBlacklistImplementation getInstance(){
		return implementation;
	}
	
	private boolean					enabled		= false;
	private List<BlacklistedItem>	blacklisted	= new ArrayList();
	
	private ItemBlacklistImplementation(){
		blacklisted = BankItemBlacklistConfiguration.BLACKLIST.get().stream().map(string -> JSONParser.fromJSON(string, BlacklistedItem.class)).collect(Collectors.toList());
	}
	
	public boolean isBlacklisted(ItemStack is){
		if(!enabled){ return false; }
		return blacklisted.stream().anyMatch(bi -> bi.equals(is));
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
