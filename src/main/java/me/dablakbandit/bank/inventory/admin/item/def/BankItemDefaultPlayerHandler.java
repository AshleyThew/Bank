package me.dablakbandit.bank.inventory.admin.item.def;

import me.dablakbandit.bank.implementations.def.ItemDefault;
import me.dablakbandit.bank.implementations.def.ItemDefaultImplementation;
import me.dablakbandit.bank.inventory.admin.BankAdminInventory;
import me.dablakbandit.core.inventory.PlayerInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BankItemDefaultPlayerHandler extends PlayerInventoryHandler implements BankAdminInventory {

	private final ItemDefaultImplementation implementation = ItemDefaultImplementation.getInstance();

	@Override
	public void parseClick(CorePlayers pl, Inventory clicked, Inventory top, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is == null || is.getType() == Material.AIR) {
			return;
		}
		implementation.getDefault().add(new ItemDefault(is));
		implementation.save();
		pl.refreshInventory();
	}

	@Override
	public void parseInventoryDrag(CorePlayers corePlayers, Inventory clicked, Inventory top, InventoryDragEvent event) {
		event.setCancelled(true);
	}
}
