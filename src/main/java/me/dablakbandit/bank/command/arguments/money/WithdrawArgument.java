package me.dablakbandit.bank.command.arguments.money;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.vault.Eco;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.bank.config.BankLanguageConfiguration;

public class WithdrawArgument extends BankEndArgument {

	public WithdrawArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (checkArguments(s, cmd, label, args, original)) {
			return;
		}
		if (args.length == 0) {
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		if (!checkPlayer(s)) {
			return;
		}
		final double amount;
		try {
			amount = Format.parseAmount(args[0]);
		} catch (NumberFormatException e) {
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNABLE_PARSE.get().replace("<value>", args[0]));
			return;
		}
		try {
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player) s);
			BankInfo bankInfo = pl.getInfo(BankInfo.class);
			bankInfo.getPinInfo().checkPass(() -> {
				try {
					withdraw(bankInfo, amount);
				} catch (Exception ignored) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void withdraw(BankInfo bankInfo, double amount) {
		if (Eco.getInstance().getEconomy() == null) {
			return;
		}
		amount = Math.max(0, amount);
		bankInfo.getMoneyInfo().withdrawMoney(bankInfo.getPlayers(), amount);
	}

	@Override
	public void init() {
		addBlank("<amount>");
	}
}
