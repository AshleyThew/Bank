package me.dablakbandit.bank.command.arguments.admin.force;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.OpenTypes;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenArgument extends BankEndArgument{
	
	public OpenArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length == 0){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		Player player = Bukkit.getPlayerExact(args[0]);
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		if(pl == null){
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
			return;
		}
		open(pl);
	}
	
	private void open(CorePlayers open){
		BankInventoriesManager.getInstance().open(open, BankInventories.BANK_MAIN_MENU, OpenTypes.ALL);
	}
	
	@Override
	public void init(){
		addTabCompleter(0, TabCompleter.PLAYERS);
	}
	
}
