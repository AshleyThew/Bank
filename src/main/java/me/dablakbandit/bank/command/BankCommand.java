package me.dablakbandit.bank.command;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankCommandConfiguration;
import me.dablakbandit.core.command.config.CommandConfiguration;
import org.bukkit.plugin.Plugin;

import static me.dablakbandit.bank.config.BankCommandConfiguration.BANK;

public class BankCommand extends ABankCommand {

	private static BankCommand command;

	public static void setup(BankPlugin bankPlugin) {
		command = new BankCommand(bankPlugin, BankCommandConfiguration.getInstance(), BANK);
	}

	public static BankCommand getInstance() {
		return command;
	}

	protected BankCommand(Plugin plugin, CommandConfiguration config, CommandConfiguration.Command base) {
		super(plugin, config, base);
	}

	public void load() {
		arguments.clear();
		loadArguments();
		register();
		init();
	}


}
