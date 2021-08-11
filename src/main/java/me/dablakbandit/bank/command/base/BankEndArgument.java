package me.dablakbandit.bank.command.base;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.player.PlayerChecks;
import me.dablakbandit.core.commands.AdvancedArgument;
import me.dablakbandit.core.commands.EndArgument;
import me.dablakbandit.core.configuration.CommandConfiguration;

public abstract class BankEndArgument extends EndArgument{
	
	private static final PlayerChecks playerChecks = PlayerChecks.getInstance();
	
	public BankEndArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	private BankEndArgument(String argument){
		super(argument);
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
	
	public boolean checkArguments(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length == 0){ return false; }
		AdvancedArgument argument = arguments.get(args[0]);
		if(argument == null){ return false; }
		argument.onCommand(s, cmd, label, Arrays.copyOfRange(args, 1, args.length), original);
		return true;
	}
	
	protected void addBlank(String blank){
		addArgument(new BankEndArgument(blank){
			@Override
			protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
				upper.onCommand(s, cmd, label, args, original);
			}
		});
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
