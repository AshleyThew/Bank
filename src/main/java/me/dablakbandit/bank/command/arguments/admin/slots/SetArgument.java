package me.dablakbandit.bank.command.arguments.admin.slots;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class SetArgument extends BankEndArgument{
	
	public SetArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length != 2){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		if(player == null || pl == null){
			base.sendFormattedMessage(s, ChatColor.RED + "Unknown player " + args[0]);
			return;
		}
		int amount = 0;
		try{
			amount = Integer.parseInt(args[1]);
		}catch(Exception e){
			e.printStackTrace();
		}
		pl.getInfo(BankItemsInfo.class).setCommandSlots(amount);
		base.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_SLOTS_SET.get().replaceAll("<amount>", args[1]).replaceAll("<player>", args[0]));
	}
	
}
