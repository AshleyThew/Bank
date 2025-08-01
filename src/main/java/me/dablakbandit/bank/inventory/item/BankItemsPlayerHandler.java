package me.dablakbandit.bank.inventory.item;

import me.dablakbandit.bank.config.BankItemBlacklistConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.core.inventory.PlayerInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BankItemsPlayerHandler extends PlayerInventoryHandler {
	@Override
	public void parseClick(CorePlayers pl, Inventory clicked, Inventory top, InventoryClickEvent event) {
		if (BankItemBlacklistConfiguration.BLACKLISTED_PLAYER_SLOTS.get().contains(event.getSlot())) {
			event.setCancelled(true);
			return;
		}
		ItemStack is = event.getCurrentItem();
		if (is != null && !is.getType().equals(Material.AIR)) {
			if (event.isShiftClick()) {
				BankItemsInfo info = pl.getInfo(BankItemsInfo.class);
				event.setCancelled(true);
				ItemStack remaining = info.getBankItemsHandler().addBankItem(pl.getPlayer(), is, false);
				event.setCurrentItem(remaining);
				// bi.save(BankPluginConfiguration.SAVE_ITEM_DEPOSIT);
				if (remaining == null) {
					BankSoundConfiguration.INVENTORY_ITEMS_ITEM_ADD.play(pl);
				} else {
					BankSoundConfiguration.INVENTORY_ITEMS_ITEM_FULL.play(pl);
				}
				pl.refreshInventory();
			}
		} else {
			if (event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void parseInventoryDrag(CorePlayers corePlayers, Inventory clicked, Inventory top, InventoryDragEvent event) {
		event.setCancelled(true);
	}
}
