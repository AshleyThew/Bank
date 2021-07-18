package me.dablakbandit.bank.command.arguments.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.player.info.admin.BankAdminInfo;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.bank.save.loader.runner.SaveRunner;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class SaveArgument extends BankEndArgument{
	
	public SaveArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(!checkPlayer(s)){ return; }
		CorePlayers admin = CorePlayerManager.getInstance().getPlayer((Player)s);
		BankAdminInfo adminInfo = admin.getInfo(BankAdminInfo.class);
		CorePlayers opened = adminInfo.getOpened();
		if(opened != null){
			LoaderManager.getInstance().runAsync(() -> {
				new SaveRunner(opened, false).run();
				Bukkit.getScheduler().runTask(BankPlugin.getInstance(), () -> base.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_SAVED.get()));
			});
			adminInfo.setOpened(null);
		}
	}
	
	@Override
	public boolean hasPermission(CommandSender s){
		return isPlayer(s) && super.hasPermission(s);
	}
	
}
