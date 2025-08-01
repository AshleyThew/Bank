package me.dablakbandit.bank.command.arguments;

import me.dablakbandit.bank.command.base.BankDefaultArgument;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.core.command.config.CommandConfiguration;
import org.bukkit.command.CommandSender;

public class EcoArgument extends BankDefaultArgument {

	public EcoArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	public void init() {

	}

	@Override
	public boolean hasPermission(CommandSender s) {
		if (!BankPluginConfiguration.BANK_IMPLEMENTATION_VAULT_OVERRIDE.get() || !BankPluginConfiguration.BANK_IMPLEMENTATION_VAULT_COMMANDS.get()) {
			return false;
		}
		return super.hasPermission(s);
	}
}
