package me.dablakbandit.bank.command;

import static me.dablakbandit.bank.config.BankCommandConfiguration.BANK;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankCommandConfiguration;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.player.info.BankPermissionInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.command.config.ConfigurationCommand;
import me.dablakbandit.core.players.CorePlayerManager;

public class BankCommand extends ConfigurationCommand{
	
	private static BankCommand command = new BankCommand(BankPlugin.getInstance(), BankCommandConfiguration.getInstance(), BANK);
	
	public static BankCommand getInstance(){
		return command;
	}
	
	protected BankCommand(Plugin plugin, CommandConfiguration config, CommandConfiguration.Command base){
		super(plugin, config, base);
	}
	
	public void load(){
		arguments.clear();
		loadArguments();
		register();
		init();
	}
	
	protected void sendTitle(CommandSender s, String base, String args){
		s.sendMessage(BankLanguageConfiguration.COMMAND_TITLE_FORMAT.get().replace("<base>", WordUtils.capitalize(base)).replace("<args>", WordUtils.capitalize(args)));
	}
	
	protected void sendArgument(CommandSender s, String base, String args, String additional){
		s.sendMessage(BankLanguageConfiguration.COMMAND_COMMANDS_FORMAT.get().replace("<base>", base).replaceAll("<args>", args + " " + additional));
	}
	
	public void sendFormattedMessage(CommandSender s, String message){
		s.sendMessage(BankLanguageConfiguration.COMMAND_MESSAGE_FORMAT.get().replace("<message>", message));
	}
	
	public void sendUnknownCommand(CommandSender s, Command cmd, String label, String[] args){
		String a = String.join(" ", args);
		s.sendMessage(BankLanguageConfiguration.COMMAND_UNKNOWN_FORMAT.get().replace("<base>", cmd.getLabel()).replace("<args>", a));
	}
	
	public void sendPermission(CommandSender s, Command cmd, String label, String[] args){
		s.sendMessage(BankLanguageConfiguration.COMMAND_NO_PERMISSION.get());
	}
	
	@Override
	public boolean hasPermission(CommandSender s){
		return this.permission == null || checkPermissionInfo(s, this.permission);
	}
	
	protected boolean checkPermissionInfo(CommandSender s, String permission){
		if(!(s instanceof Player)){ return super.hasPermission(s); }
		return CorePlayerManager.getInstance().getPlayer((Player)s).getInfo(BankPermissionInfo.class).checkPermission(permission, false);
	}
}
