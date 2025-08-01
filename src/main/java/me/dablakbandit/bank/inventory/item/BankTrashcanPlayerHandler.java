package me.dablakbandit.bank.inventory.item;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.core.inventory.PlayerInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class BankTrashcanPlayerHandler extends PlayerInventoryHandler {
	@Override
	public void parseClick(CorePlayers pl, Inventory clicked, Inventory top, InventoryClickEvent event) {
		if (BankPluginConfiguration.BANK_ITEMS_TRASHCAN_BLACKLIST_ENABLED.get()) {
			if (event.getCurrentItem() != null && ItemBlacklistImplementation.getInstance().isTrashBlacklisted(event.getCurrentItem())) {
				event.setCancelled(true);
				return;
			}
		}
		Bukkit.getScheduler().runTaskLater(BankPlugin.getInstance(), () -> pl.refreshInventory(BankTrashcanInventory.class), 1);
	}

	@Override
	public void parseInventoryDrag(CorePlayers pl, Inventory clicked, Inventory top, InventoryDragEvent event) {
		Bukkit.getScheduler().runTaskLater(BankPlugin.getInstance(), () -> pl.refreshInventory(BankTrashcanInventory.class), 1);
	}
}
