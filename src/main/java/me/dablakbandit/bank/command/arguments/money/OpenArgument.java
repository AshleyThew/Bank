package me.dablakbandit.bank.command.arguments.money;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.OpenTypes;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenArgument extends BankEndArgument {

	public OpenArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (!checkPlayer(s)) {
			return;
		}
		if (!BankPluginConfiguration.BANK_OPENTYPE_SUBSET_COMMAND_ENABLED.get()) {
			BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_NOT_AVAILABLE.get());
			return;
		}
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player) s);
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		bankInfo.getPinInfo().checkPass(() -> BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MONEY, OpenTypes.MONEY));
	}

	@Override
	public boolean hasPermission(CommandSender s) {
		if (!BankPluginConfiguration.BANK_OPENTYPE_SUBSET_COMMAND_ENABLED.get()) {
			return false;
		}
		return super.hasPermission(s);
	}
}
