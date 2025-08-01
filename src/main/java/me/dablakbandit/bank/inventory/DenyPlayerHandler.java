package me.dablakbandit.bank.inventory;

import me.dablakbandit.core.inventory.PlayerInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class DenyPlayerHandler extends PlayerInventoryHandler {
	@Override
	public void parseClick(CorePlayers pl, Inventory clicked, Inventory top, InventoryClickEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void parseInventoryDrag(CorePlayers corePlayers, Inventory clicked, Inventory top, InventoryDragEvent event) {
		event.setCancelled(true);
	}
}
