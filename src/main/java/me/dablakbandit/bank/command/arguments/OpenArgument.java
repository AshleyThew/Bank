package me.dablakbandit.bank.command.arguments;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
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

public class OpenArgument extends BankEndArgument{
	
	public OpenArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(s instanceof ConsoleCommandSender){
			if(args.length == 0){
				this.base.sendArguments(s, cmd, original, this.arguments.entrySet());
			}else{
				Player player = Bukkit.getPlayerExact(args[0]);
				CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
				if(pl == null){
					BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replace("<player>", args[0]));
					return;
				}
				BankInfo bankInfo = pl.getInfo(BankInfo.class);
				bankInfo.getPinInfo().checkPass(() -> BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU));
			}
		}else{
			if(!checkPlayer(s)){ return; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player)s);
			BankInfo bankInfo = pl.getInfo(BankInfo.class);
			bankInfo.getPinInfo().checkPass(() -> BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU));
		}
	}
	
	@Override
	public void init(){
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
