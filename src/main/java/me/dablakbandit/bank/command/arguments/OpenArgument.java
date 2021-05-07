package me.dablakbandit.bank.command.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class OpenArgument extends BankEndArgument{
	
	public OpenArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(!checkPlayer(s)){ return; }
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player)s);
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		bankInfo.getPinInfo().checkPass(() -> BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU));
	}
	
	@Override
	public void init(){
		
	}
	
	@Override
	public boolean hasPermission(CommandSender s){
		return isPlayer(s) && super.hasPermission(s);
	}
}
