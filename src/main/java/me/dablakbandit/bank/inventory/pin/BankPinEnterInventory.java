package me.dablakbandit.bank.inventory.pin;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankPinInfo;
import me.dablakbandit.core.players.CorePlayers;

public class BankPinEnterInventory extends BankPinInventory{
	@Override
	public void onClick(CorePlayers pl, BankInfo bankInfo, Inventory inv, InventoryClickEvent event){
		BankSoundConfiguration.INVENTORY_PIN_CLICK.play(pl);
		BankPinInfo pinInfo = bankInfo.getPinInfo();
		int clicked = getClick(event);
		if(pinInfo.checkPinPass(String.valueOf(clicked))){
			if(pinInfo.hasPassed()){
				pinInfo.run();
				return;
			}
		}
		pl.refreshInventory();
	}
	
}
