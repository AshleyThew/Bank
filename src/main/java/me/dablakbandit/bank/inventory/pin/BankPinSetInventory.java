package me.dablakbandit.bank.inventory.pin;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankPinInfo;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public class BankPinSetInventory extends BankPinInventory {

	@Override
	public void init() {
		super.init();
		setItem(BankItemConfiguration.BANK_PIN_NUMBER_BACK, getComplete());
	}

	@Override
	public void onClick(CorePlayers pl, BankInfo bankInfo, Inventory inv, InventoryClickEvent event) {
		BankSoundConfiguration.INVENTORY_PIN_CLICK.play(pl);
		BankPinInfo pinInfo = bankInfo.getPinInfo();
		int clicked = getClick(event);
		if (pinInfo.checkPinSet(String.valueOf(clicked))) {
			getComplete().accept(pl);
		} else {
			pl.refreshInventory();
		}
	}

	protected Consumer<CorePlayers> getComplete() {
		return BankInventories.BANK_PIN_MENU;
	}

}
