package me.dablakbandit.bank.command.arguments.exp.withdraw;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class AllArgument extends BankEndArgument{
	
	public AllArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(!checkPlayer(s)){ return; }
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player)s);
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		bankInfo.getPinInfo().checkPass(() -> withdrawAll(bankInfo));
	}
	
	private void withdrawAll(BankInfo bankInfo){
		double withdraw = bankInfo.getExpInfo().getExp();
		bankInfo.getExpInfo().withdrawExp(bankInfo.getPlayers(), withdraw);
	}
	
}
