package me.dablakbandit.bank.implementations.cheque;

import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.OpenTypes;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChequeItemListener implements Listener {

	private static final ChequeItemListener instance = new ChequeItemListener();

	public static ChequeItemListener getInstance() {
		return instance;
	}

	private ChequeItemListener() {
		// Private constructor to enforce singleton pattern
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		ItemStack item = event.getItem();
		if (item == null) {
			return;
		}

		ChequeImplementation chequeImplementation = ChequeImplementation.getInstance();

		// Handle cheque redemption
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
		if (chequeImplementation.isChequeItem(item)) {
			event.setCancelled(true);
			if (pl != null) {
				if (pl.getInfo(BankInfo.class).isLocked(false)) {
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_BANK_LOCKED.get());
					return;
				}
				chequeImplementation.redeemChequeItem(pl, item);
			}
			return;
		}

		// Handle cheque book usage (open cheque creation GUI)
		if (chequeImplementation.isChequeBook(item)) {
			event.setCancelled(true);
			if (pl != null) {
				TempCheque tempCheque = new TempCheque(BankPluginConfiguration.BANK_CHEQUES_MINIMUM_AMOUNT.get(), true);
				BankInfo info = pl.getInfo(BankInfo.class);
				info.setTempCheque(tempCheque);
				info.getPinInfo().checkPass(() -> {
					BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_CHEQUE_CREATE, new OpenTypes[0]);
				});
			}
		}
	}

}
