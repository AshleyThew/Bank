package me.dablakbandit.bank.inventory.admin.blacklist;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.implementations.blacklist.BlacklistedItem;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.core.inventory.PlayerInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;

public class BankBlacklistPlayerHandler extends PlayerInventoryHandler{
	
	private ItemBlacklistImplementation implementation = ItemBlacklistImplementation.getInstance();
	
	@Override
	public void parseClick(CorePlayers pl, Inventory clicked, Inventory top, InventoryClickEvent event){
		ItemStack is = event.getCurrentItem();
		if(is == null || is.getType() == Material.AIR){ return; }
		implementation.getBlacklisted().add(new BlacklistedItem(is.clone()));
		implementation.save();
		event.setCancelled(true);
		pl.refreshInventory();
	}
	
	@Override
	public void parseInventoryDrag(CorePlayers corePlayers, Inventory clicked, Inventory top, InventoryDragEvent event){
		event.setCancelled(true);
	}
}
