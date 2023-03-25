package me.dablakbandit.bank.inventory.admin.item.def;

import me.dablakbandit.bank.implementations.def.ItemDefault;
import me.dablakbandit.bank.implementations.def.ItemDefaultImplementation;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.implementations.blacklist.BlacklistedItem;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.core.inventory.PlayerInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;

public class BankItemDefaultPlayerHandler extends PlayerInventoryHandler{
	
	private final ItemDefaultImplementation implementation = ItemDefaultImplementation.getInstance();
	
	@Override
	public void parseClick(CorePlayers pl, Inventory clicked, Inventory top, InventoryClickEvent event){
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if(is == null || is.getType() == Material.AIR){ return; }
		implementation.getDefault().add(new ItemDefault(is));
		implementation.save();
		pl.refreshInventory();
	}
	
	@Override
	public void parseInventoryDrag(CorePlayers corePlayers, Inventory clicked, Inventory top, InventoryDragEvent event){
		event.setCancelled(true);
	}
}
