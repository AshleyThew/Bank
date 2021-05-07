package me.dablakbandit.bank.command.arguments.admin;

import org.bukkit.command.CommandSender;

import me.dablakbandit.bank.command.base.BankDefaultArgument;
import me.dablakbandit.core.command.config.CommandConfiguration;

public class AddArgument extends BankDefaultArgument{
	
	public AddArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	public void init(){
		
	}
	
	@Override
	public boolean hasPermission(CommandSender s){
		return isPlayer(s) && super.hasPermission(s);
	}
	
}
