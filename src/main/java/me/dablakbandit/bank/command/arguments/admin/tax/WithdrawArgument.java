package me.dablakbandit.bank.command.arguments.admin.tax;

import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.utils.calculation.PaymentCalculator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class WithdrawArgument extends BankEndArgument{

	public WithdrawArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length != 2){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		Player player = Bukkit.getPlayerExact(args[0]);
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		if(player == null || pl == null){
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
			return;
		}
		BankInfo bankInfo = pl.getType(BankInfo.class);
		if(bankInfo.isLocked(false)){
			base.sendFormattedMessage(s, ChatColor.RED + "Player locked " + args[0]);
			return;
		}
		double amount = 0;
		try{
			amount = Integer.parseInt(args[1]);
		}catch(Exception e){
			e.printStackTrace();
		}
		PaymentCalculator calculator = bankInfo.getMoneyInfo().getPaymentCalculator(amount, false);
		amount = calculator.getResult();
		//base.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_SLOTS_SET.get().replaceAll("<amount>", args[1]).replaceAll("<player>", args[0]));
	}
	
}
