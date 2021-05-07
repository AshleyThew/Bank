package me.dablakbandit.bank.command.arguments.exp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.utils.Format;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class BalanceArgument extends BankEndArgument{
	
	public BalanceArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(!checkPlayer(s)){ return; }
		try{
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player)s);
			BankInfo bankInfo = pl.getInfo(BankInfo.class);
			bankInfo.getPinInfo().checkPass(() -> balance(bankInfo));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void balance(BankInfo bankInfo){
		BankLanguageConfiguration.sendMessage(bankInfo.getPlayers(), BankLanguageConfiguration.MESSAGE_EXP_BALANCE.get().replaceAll("<exp>", Format.formatExp(bankInfo.getExpInfo().getExp())));
	}
	
}
