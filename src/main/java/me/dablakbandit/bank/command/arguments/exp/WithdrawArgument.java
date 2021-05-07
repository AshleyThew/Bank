package me.dablakbandit.bank.command.arguments.exp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class WithdrawArgument extends BankEndArgument{
	
	public WithdrawArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(checkArguments(s, cmd, label, args, original)){ return; }
		if(args.length == 0){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		if(!checkPlayer(s)){ return; }
		try{
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player)s);
			BankInfo bankInfo = pl.getInfo(BankInfo.class);
			bankInfo.getPinInfo().checkPass(() -> {
				try{
					withdraw(bankInfo, Double.parseDouble(args[0]));
				}catch(Exception e){
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void withdraw(BankInfo bankInfo, double amount){
		bankInfo.getExpInfo().withdrawExp(bankInfo.getPlayers(), amount);
	}
	
	@Override
	public void init(){
		addBlank("<amount>");
	}
}
