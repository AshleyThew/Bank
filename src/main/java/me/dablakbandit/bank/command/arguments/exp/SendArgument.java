package me.dablakbandit.bank.command.arguments.exp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class SendArgument extends BankEndArgument{
	
	public SendArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(checkArguments(s, cmd, label, args, original)){ return; }
		if(args.length < 2){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		if(!checkPlayer(s)){ return; }
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player)s);
		if(!checkPassedPin(pl)){ return; }
		Player p = Bukkit.getPlayerExact(args[0]);
		final CorePlayers payTo = CorePlayerManager.getInstance().getPlayer(p);
		if(p == null || payTo == null || payTo.getInfo(BankInfo.class).isLocked(false)){
			BankSoundConfiguration.GLOBAL_ERROR.play((Player)s);
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
			return;
		}
		int amount;
		try{
			amount = Integer.parseInt(args[1]);
		}catch(Exception e){
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNABLE_PARSE.get().replaceAll("<value>", args[1]));
			return;
		}
		try{
			BankInfo bankInfo = pl.getInfo(BankInfo.class);
			bankInfo.getExpInfo().send(payTo, amount);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean checkPassedPin(CorePlayers pl){
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		if(!bankInfo.getPinInfo().hasPassed()){
			base.sendFormattedMessage(pl.getPlayer(), BankLanguageConfiguration.MESSAGE_PIN_ENTER_BEFORE.get());
			bankInfo.getPinInfo().checkPass(() -> {
			});
			return false;
		}
		return true;
	}
	
}
