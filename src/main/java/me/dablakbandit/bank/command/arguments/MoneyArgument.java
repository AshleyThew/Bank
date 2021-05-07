package me.dablakbandit.bank.command.arguments;

import org.bukkit.command.CommandSender;

import me.dablakbandit.bank.command.base.BankDefaultArgument;
import me.dablakbandit.core.command.config.CommandConfiguration;

public class MoneyArgument extends BankDefaultArgument{
	
	public MoneyArgument(CommandConfiguration.Command command){
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
