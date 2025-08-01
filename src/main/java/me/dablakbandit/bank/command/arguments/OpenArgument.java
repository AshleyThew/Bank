package me.dablakbandit.bank.command.arguments;

import me.dablakbandit.bank.command.base.BankAdvancedArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.OpenTypes;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class OpenArgument extends BankAdvancedArgument {

	public OpenArgument(CommandConfiguration.Command command) {
		super(command);
	}


	public void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (s instanceof ConsoleCommandSender) {
			if (args.length == 0) {
				this.base.sendArguments(s, cmd, original, this.arguments.entrySet());
			} else {
				Player player = Bukkit.getPlayerExact(args[0]);
				CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
				if (pl == null) {
					BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replace("<player>", args[0]));
					return;
				}
				OpenTypes[] values = OpenTypes.values();
				if (BankPluginConfiguration.BANK_OPENTYPE_SUBSET_COMMAND_ENABLED.get() && args.length > 1) {
					// copy array minus the first element
					String[] newArgs = new String[args.length - 1];
					System.arraycopy(args, 1, newArgs, 0, newArgs.length);
					// Convert newargs to values
					values = new OpenTypes[newArgs.length];
					for (int i = 0; i < newArgs.length; i++) {
						try {
							values[i] = OpenTypes.valueOf(newArgs[i].toUpperCase());
						} catch (IllegalArgumentException e) {
							BankLanguageConfiguration.sendFormattedMessage(s, "Invalid subset: " + newArgs[i]);
							return;
						}
					}
				}
				BankInfo bankInfo = pl.getInfo(BankInfo.class);
				OpenTypes[] finalValues = values;
				BankInventories inventories = BankInventoriesManager.getInstance().getBankInventories(finalValues);
				bankInfo.getPinInfo().checkPass(() -> BankInventoriesManager.getInstance().open(pl, inventories, finalValues));
				return;
			}
		}
		if (!checkPlayer(s)) {
			return;
		}
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player) s);
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		bankInfo.getPinInfo().checkPass(() -> BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU, OpenTypes.ALL));
	}

	@Override
	public void init() {
		addTabCompleter(0, new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender commandSender, String s, String[] strings) {
				if (!(commandSender instanceof ConsoleCommandSender)) {
					return Collections.emptyList();
				} else {
					return TabCompleter.PLAYERS.onTabComplete(commandSender, s, strings);
				}
			}
		});
	}
}
