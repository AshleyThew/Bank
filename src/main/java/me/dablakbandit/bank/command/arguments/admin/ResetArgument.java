package me.dablakbandit.bank.command.arguments.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.player.BankPlayerManager;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class ResetArgument extends BankEndArgument{
	
	public ResetArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	private List<String> confirmationList = new ArrayList();
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length == 0){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		if(!confirmationList.remove(args[0])){
			confirmationList.add(args[0]);
			s.sendMessage(ChatColor.RED + "This will reset all of " + args[0] + "'s bank data.");
			s.sendMessage(ChatColor.GREEN + "Please run the command again to confirm.");
			return;
		}
		
		Player player = Bukkit.getPlayer(args[0]);
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		if(player == null || pl == null){
			String uuid = BankDatabaseManager.getInstance().getInfoDatabase().getUUIDDatabase().getUUID(args[0]);
			if(uuid == null){
				base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
				return;
			}
			pl = new CorePlayers(uuid);
		}
		BankPlayerManager.getInstance().addCorePlayers(pl);
		LoaderManager.getInstance().save(pl, false);
		s.sendMessage(ChatColor.GREEN + args[0] + "'s bank data reset.");
	}
	
	@Override
	public void init(){
		addTabCompleter(0, TabCompleter.PLAYERS);
	}
	
}
