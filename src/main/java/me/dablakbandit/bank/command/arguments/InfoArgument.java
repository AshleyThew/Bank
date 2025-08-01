package me.dablakbandit.bank.command.arguments;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.implementations.placeholder.BankPlaceholderManager;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoArgument extends BankEndArgument {

	public InfoArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (!checkPlayer(s)) {
			return;
		}

		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player) s);
		BankLanguageConfiguration.sendMessage(s, BankLanguageConfiguration.MESSAGE_INFO_BORDER.get());
		for (String info : BankLanguageConfiguration.MESSAGE_INFO_VIEW.get()) {
			BankLanguageConfiguration.sendMessage(s, BankPlaceholderManager.getInstance().replace(pl, info));
		}
		BankLanguageConfiguration.sendMessage(s, BankLanguageConfiguration.MESSAGE_INFO_BORDER.get());
	}

	@Override
	public void init() {

	}

	@Override
	public boolean hasPermission(CommandSender s) {
		return isPlayer(s) && super.hasPermission(s);
	}
}
