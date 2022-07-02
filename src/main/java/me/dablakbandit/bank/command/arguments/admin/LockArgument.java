package me.dablakbandit.bank.command.arguments.admin;

import java.util.ArrayList;
import java.util.List;

import me.dablakbandit.bank.database.base.IInfoDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class LockArgument extends BankEndArgument{
	
	public LockArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	private final List<String> confirmationList = new ArrayList<>();
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length < 2){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		IInfoDatabase infoDatabase = BankDatabaseManager.getInstance().getInfoDatabase();
		if(player == null || pl == null){
			String uuid = infoDatabase.getUUIDDatabase().getUUID(args[0]);
			if(uuid == null){
				base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
				return;
			}
			pl = new CorePlayers(uuid);
		}
		
		boolean locked;
		try{
			locked = Boolean.parseBoolean(args[1]);
		}catch(Exception e){
			return;
		}

		infoDatabase.getPlayerLockDatabase().setLocked(pl, locked);
	}
	
	@Override
	public void init(){
		addTabCompleter(0, TabCompleter.PLAYERS);
		addTabCompleter(1, TabCompleter.ofValues("true", "false"));
	}
	
}
