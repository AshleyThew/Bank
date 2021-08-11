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
import me.dablakbandit.bank.player.PlayerChecks;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.command.config.ConfigurationCommand;

public class BankCommand extends ConfigurationCommand{
	
	private static final BankCommand	command			= new BankCommand(BankPlugin.getInstance(), BankCommandConfiguration.getInstance(), BANK);
	private static final PlayerChecks	playerChecks	= PlayerChecks.getInstance();
	
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
		BankLanguageConfiguration.sendMessage(s, BankLanguageConfiguration.COMMAND_TITLE_FORMAT.get().replace("<base>", WordUtils.capitalize(base)).replace("<args>", WordUtils.capitalize(args)));
	}
	
	protected void sendArgument(CommandSender s, String base, String args, String additional){
		BankLanguageConfiguration.sendMessage(s, BankLanguageConfiguration.COMMAND_COMMANDS_FORMAT.get().replace("<base>", base).replaceAll("<args>", args + " " + additional));
	}
	
	public void sendFormattedMessage(CommandSender s, String message){
		BankLanguageConfiguration.sendMessage(s, BankLanguageConfiguration.COMMAND_MESSAGE_FORMAT.get().replace("<message>", message));
	}
	
	public void sendUnknownCommand(CommandSender s, Command cmd, String label, String[] args){
		String a = String.join(" ", args);
		BankLanguageConfiguration.sendMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_FORMAT.get().replace("<base>", cmd.getLabel()).replace("<args>", a));
	}
	
	public void sendPermission(CommandSender s, Command cmd, String label, String[] args){
		BankLanguageConfiguration.sendMessage(s, BankLanguageConfiguration.COMMAND_NO_PERMISSION.get());
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(s instanceof Player){
			Player player = (Player)s;
			if(playerChecks.checkWorldDisabled(player)){
				sendFormattedMessage(s, BankLanguageConfiguration.MESSAGE_WORLD_DISABLED.get());
				return true;
			}
			if(playerChecks.checkGamemodeDisabled(player)){
				sendFormattedMessage(s, BankLanguageConfiguration.MESSAGE_GAMEMODE_DISABLED.get().replace("<gamemode>", player.getGameMode().name().toLowerCase()));
				return true;
			}
		}
		return super.onCommand(s, cmd, label, args);
	}
	
	@Override
	public boolean hasPermission(CommandSender s){
		return this.permission == null || checkPermissionInfo(s, this.permission);
	}
	
	protected boolean checkPermissionInfo(CommandSender s, String permission){
		if(!(s instanceof Player)){ return super.hasPermission(s); }
		return playerChecks.checkPermissionInfo((Player)s, permission, false);
	}
}
