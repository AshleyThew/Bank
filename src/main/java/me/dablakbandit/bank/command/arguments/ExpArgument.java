package me.dablakbandit.bank.command.arguments;

import me.dablakbandit.bank.command.base.BankDefaultArgument;
import me.dablakbandit.core.command.config.CommandConfiguration;
import org.bukkit.command.CommandSender;

public class ExpArgument extends BankDefaultArgument {

	public ExpArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	public void init() {

	}

	@Override
	public boolean hasPermission(CommandSender s) {
		return isPlayer(s) && super.hasPermission(s);
	}

}
