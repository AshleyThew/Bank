package me.dablakbandit.bank.command.arguments.eco;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.dablakbandit.bank.api.Economy_Bank;
import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.utils.Format;
import me.dablakbandit.core.command.config.CommandConfiguration;
import net.milkbowl.vault.economy.EconomyResponse;

public class GiveArgument extends BankEndArgument{
	
	public GiveArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length == 0){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		if(args.length < 2){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		double amount = 0.0;
		try{
			amount = Double.parseDouble(args[1]);
		}catch(Exception e){
			base.sendFormattedMessage(s, ChatColor.RED + "Unable to parse " + args[1]);
			return;
		}
		Economy_Bank economy = Economy_Bank.getInstance();
		EconomyResponse er = economy.depositPlayer(args[0], amount);
		if(er.transactionSuccess()){
			base.sendFormattedMessage(s, ChatColor.GREEN + "$" + Format.formatMoney(amount) + " added to " + args[0] + "'s account. New balance: $" + Format.formatMoney(er.balance));
		}else{
			base.sendFormattedMessage(s, ChatColor.RED + "Failed: " + er.errorMessage);
		}
	}
	
}
