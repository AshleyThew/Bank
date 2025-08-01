package me.dablakbandit.bank.command.arguments.admin;

import me.dablakbandit.bank.BankCoreHandler;
import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.player.info.PermissionsInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadArgument extends BankEndArgument {

	public ReloadArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		BankCoreHandler.getInstance().reload();
		for (CorePlayers pl : CorePlayerManager.getInstance().getPlayers().values()) {
			for (PermissionsInfo permissionsInfo : pl.getAllInfoType(PermissionsInfo.class)) {
				permissionsInfo.checkPermissions(pl.getPlayer(), false);
			}
		}
		base.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_RELOAD.get().replaceAll("<version>", BankPlugin.getInstance().getDescription().getVersion()));
	}
}
