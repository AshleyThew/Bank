package me.dablakbandit.bank.command.arguments.eco;

import me.dablakbandit.bank.api.BankAPI;
import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.command.config.CommandConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetArgument extends BankEndArgument {

	public SetArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (args.length < 2) {
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		double amount;
		try {
			amount = Double.parseDouble(args[1]);
		} catch (Exception e) {
			base.sendFormattedMessage(s, ChatColor.RED + "Unable to parse " + args[1]);
			return;
		}
		String uuid = BankAPI.getInstance().getUUID(args[0]);
		if (uuid == null) {
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
			return;
		}
		BankAPI.getInstance().setMoney(uuid, amount);
		base.sendFormattedMessage(s, ChatColor.GREEN + "Set " + args[0] + "'s balance to: $" + Format.formatMoney(amount));
	}

}
