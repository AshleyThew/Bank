package me.dablakbandit.bank.command.arguments.cheque;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.implementations.cheque.ChequeImplementation;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreateArgument extends BankEndArgument {

	public CreateArgument(CommandConfiguration.Command command) {
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

		if (!BankPluginConfiguration.BANK_CHEQUES_ENABLED.get()) {
			base.sendFormattedMessage(s, BankLanguageConfiguration.MESSAGE_CHEQUE_DISABLED.get());
			return;
		}

		try {
			Player player = (Player) s;
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
			BankInfo bankInfo = pl.getInfo(BankInfo.class);

			bankInfo.getPinInfo().checkPass(() -> {
				try {
					double amount = Double.parseDouble(args[0]);
					UUID recipientUuid = null;

					// Check if recipient player is specified
					if (args.length > 1) {
						String recipientName = args[1];
						Player recipientPlayer = Bukkit.getPlayer(recipientName);

						if (recipientPlayer == null) {
							// Only allow online players
							BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_RECIPIENT_NOT_FOUND.get());
							return;
						}

						recipientUuid = recipientPlayer.getUniqueId();
					}

					createCheque(pl, amount, recipientUuid);
				} catch (NumberFormatException e) {
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_INVALID_AMOUNT.get());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createCheque(CorePlayers pl, double amount, UUID recipientUuid) {
		ChequeImplementation.getInstance().createCheque(pl, amount, recipientUuid);
	}

	@Override
	public void init() {
		addBlank("<amount>");
		addBlank("[player]");
	}
}
