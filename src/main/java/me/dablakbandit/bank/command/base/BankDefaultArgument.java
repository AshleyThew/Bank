package me.dablakbandit.bank.command.base;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.player.PlayerChecks;
import me.dablakbandit.core.command.DefaultArgument;
import me.dablakbandit.core.configuration.CommandConfiguration;

public abstract class BankDefaultArgument extends DefaultArgument{
	
	private static final PlayerChecks playerChecks = PlayerChecks.getInstance();
	
	public BankDefaultArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	public boolean isPlayer(CommandSender s){
		return s instanceof Player;
	}
	
	public boolean checkPlayer(CommandSender s){
		if(!(s instanceof Player)){
			base.sendFormattedMessage(s, ChatColor.RED + "Command can only be run by players");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean hasPermission(CommandSender s){
		return this.permission == null || checkPermissionInfo(s, this.permission);
	}
	
	protected boolean checkPermissionInfo(CommandSender s, String permission){
		if(!(s instanceof Player)){ return super.hasPermission(s); }
		return playerChecks.checkPermissionInfo((Player)s, permission, false);
	}
	
}
