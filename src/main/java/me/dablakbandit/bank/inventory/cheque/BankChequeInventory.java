package me.dablakbandit.bank.inventory.cheque;

import me.dablakbandit.bank.config.*;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.implementations.cheque.ChequeImplementation;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.inventory.path.BankItemPathReplacer;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.inventory.ItemStack;

public class BankChequeInventory extends BankInventoryHandler<BankInfo> {

	private static final BankItemPathReplacer<BankInfo> replacer = new BankItemPathReplacer<BankInfo>().addMoney("<cost>", (t) -> BankPluginConfiguration.BANK_CHEQUES_CREATE_COST.get());

	@Override
	public void init() {
		addBack();
		setItem(BankItemConfiguration.BANK_CHEQUE_MENU_BLANK);
		setItem(BankItemConfiguration.BANK_CHEQUE_MENU_CREATE, consumePermissions(BankPermissionConfiguration.PERMISSION_CHEQUE_CREATE, consumeSound(this::createCheque, BankSoundConfiguration.INVENTORY_ITEMS_CHEQUE_OPEN_CREATE)));
		setItem(BankItemConfiguration.BANK_CHEQUE_MENU_BOOK, this::getChequeBook, consumePermissions(BankPermissionConfiguration.PERMISSION_CHEQUE_BOOK, consumeSound(this::getChequeBook, BankSoundConfiguration.INVENTORY_ITEMS_CHEQUE_BUY_BOOK)));
	}

	private void createCheque(CorePlayers pl, BankInfo info) {
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_CHEQUE_CREATE);
	}

	private ItemStack getChequeBook(BankItemPath path, BankInfo info) {
		if (BankPluginConfiguration.BANK_CHEQUES_BOOK_ENABLED.get()) {
			return replacer.apply(path, info);
		}
		return null;
	}

	private void getChequeBook(CorePlayers pl, BankInfo info) {
		double cost = BankPluginConfiguration.BANK_CHEQUES_BOOK_COST.get();
		BankMoneyInfo moneyInfo = pl.getInfo(BankMoneyInfo.class);

		if (moneyInfo.getMoney() < cost) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_BOOK_INSUFFICIENT_FUNDS.get());
			return;
		}
		moneyInfo.setMoney(moneyInfo.getMoney() - cost);
		ItemStack chequeBook = ChequeImplementation.getInstance().createChequeBook();

		pl.getPlayer().getInventory().addItem(chequeBook);
		BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_BOOK_PURCHASED.get());
	}

	private void addBack() {
		setItem(BankItemConfiguration.BANK_CHEQUE_MENU_BACK, consumeSound(this::returnToMainMenu, BankSoundConfiguration.INVENTORY_MENU_OPEN_MAIN));
	}

	protected void returnToMainMenu(CorePlayers pl) {
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU);
	}

	@Override
	public BankInfo getInvoker(CorePlayers pl) {
		return pl.getInfo(BankInfo.class);
	}
}
