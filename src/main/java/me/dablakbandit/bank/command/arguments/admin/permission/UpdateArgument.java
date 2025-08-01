package me.dablakbandit.bank.command.arguments.admin.permission;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.player.info.PermissionsInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateArgument extends BankEndArgument {

	public UpdateArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (args.length == 0) {
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		Player player = Bukkit.getPlayerExact(args[0]);
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		if (pl == null) {
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
			return;
		}
		boolean debug = false;
		if (args.length > 1) {
			try {
				debug = Boolean.parseBoolean(args[1]);
			} catch (Exception e) {
			}
		}
		for (PermissionsInfo permissionsInfo : pl.getAllInfoType(PermissionsInfo.class)) {
			permissionsInfo.checkPermissions(player, debug);
		}
		BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_PERMISSION_UPDATED.get().replace("<player>", args[0]));
	}

	@Override
	public void init() {
		addTabCompleter(0, TabCompleter.PLAYERS);
	}
}
