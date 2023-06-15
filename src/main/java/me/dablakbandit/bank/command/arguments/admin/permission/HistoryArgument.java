package me.dablakbandit.bank.command.arguments.admin.permission;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.player.info.BankPermissionInfo;
import me.dablakbandit.bank.player.info.admin.BankAdminInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class HistoryArgument extends BankEndArgument{
	
	public HistoryArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length != 1){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		Player player = Bukkit.getPlayerExact(args[0]);
		CorePlayers open = CorePlayerManager.getInstance().getPlayer(player);
		if(open == null){
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
			return;
		}
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player)s);
		pl.getInfo(BankAdminInfo.class).setPermissionsCheck(new ArrayList<>(open.getInfo(BankPermissionInfo.class).getHistory()));
		BankInventoriesManager.getInstance().openBypass(pl, BankInventories.BANK_ADMIN_PERMISSION_HISTORY);
	}
	
	@Override
	public void init(){
		addTabCompleter(0, TabCompleter.PLAYERS);
	}
	
	@Override
	public boolean hasPermission(CommandSender s){
		return isPlayer(s) && super.hasPermission(s);
	}
}
