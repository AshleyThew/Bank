package me.dablakbandit.bank.command.arguments.admin.permission;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.player.info.PermissionsInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class UpdateArgument extends BankEndArgument{
	
	public UpdateArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length != 1){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		CorePlayers other = CorePlayerManager.getInstance().getPlayer(player);
		if(other == null){
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
			return;
		}
		other.getAllInfo().stream().filter(info -> info instanceof PermissionsInfo).forEach(info -> ((PermissionsInfo)info).checkPermissions(player));
		BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_PERMISSION_UPDATED.get().replace("<player>", args[0]));
	}
	
	@Override
	public void init(){
		addTabCompleter(0, TabCompleter.PLAYERS);
	}
}
