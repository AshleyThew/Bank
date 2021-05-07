package me.dablakbandit.bank.command.arguments.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.dablakbandit.bank.BankCoreHandler;
import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.core.command.config.CommandConfiguration;

public class ReloadArgument extends BankEndArgument{
	
	public ReloadArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		BankCoreHandler.getInstance().reload();
		base.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_RELOAD.get().replaceAll("<version>", BankPlugin.getInstance().getDescription().getVersion()));
	}
}
