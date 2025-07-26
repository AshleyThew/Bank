package me.dablakbandit.bank.inventory.admin.blacklist;

import me.dablakbandit.bank.implementations.blacklist.BlacklistedItem;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.bank.inventory.admin.BankAdminInventory;
import me.dablakbandit.bank.player.info.admin.BankAdminInfo;
import me.dablakbandit.core.inventory.PlayerInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BankBlacklistPlayerHandler extends PlayerInventoryHandler implements BankAdminInventory {

	private final ItemBlacklistImplementation implementation = ItemBlacklistImplementation.getInstance();

	@Override
	public void parseClick(CorePlayers pl, Inventory clicked, Inventory top, InventoryClickEvent event) {
		BankAdminInfo adminInfo = pl.getInfo(BankAdminInfo.class);
		ItemStack is = event.getCurrentItem();
		if (is == null || is.getType() == Material.AIR) {
			return;
		}
		implementation.getBlacklisted(adminInfo.getBlacklistType()).add(new BlacklistedItem(is.clone()));
		implementation.save();
		event.setCancelled(true);
		pl.refreshInventory();
	}

	@Override
	public void parseInventoryDrag(CorePlayers corePlayers, Inventory clicked, Inventory top, InventoryDragEvent event) {
		event.setCancelled(true);
	}
}
