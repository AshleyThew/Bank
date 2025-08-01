package me.dablakbandit.bank.command.arguments.cheque;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.implementations.cheque.ChequeImplementation;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookArgument extends BankEndArgument {

	public BookArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (checkArguments(s, cmd, label, args, original)) {
			return;
		}

		if (!checkPlayer(s)) {
			return;
		}

		try {
			Player player = (Player) s;
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
			BankInfo bankInfo = pl.getInfo(BankInfo.class);

			bankInfo.getPinInfo().checkPass(() -> {
				purchaseChequeBook(pl, bankInfo);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void purchaseChequeBook(CorePlayers pl, BankInfo bankInfo) {
		// Check if cheques are enabled
		if (!BankPluginConfiguration.BANK_CHEQUES_ENABLED.get()) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_DISABLED.get());
			return;
		}

		// Get cheque book cost from config
		double cost = BankPluginConfiguration.BANK_CHEQUES_BOOK_COST.get();

		BankMoneyInfo moneyInfo = bankInfo.getMoneyInfo();

		// Check if player has enough money
		if (moneyInfo.getMoney() < cost) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_BOOK_INSUFFICIENT_FUNDS.get());
			return;
		}

		// Deduct money
		if (!moneyInfo.subtractMoney(cost)) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_BOOK_INSUFFICIENT_FUNDS.get());
			return;
		}

		// Give cheque book item
		ItemStack chequeBook = ChequeImplementation.getInstance().createChequeBook();
		pl.getPlayer().getInventory().addItem(chequeBook);

		BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_BOOK_PURCHASED.get());
	}

	@Override
	public void init() {
		// No arguments needed for book command
	}
}
