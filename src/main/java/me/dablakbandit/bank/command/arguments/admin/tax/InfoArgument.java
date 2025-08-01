package me.dablakbandit.bank.command.arguments.admin.tax;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.core.command.config.CommandConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class InfoArgument extends BankEndArgument {

	public InfoArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {

	}

}
