package me.dablakbandit.bank.command.arguments.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;
import me.dablakbandit.core.utils.jsonformatter.click.SuggestCommandEvent;
import me.dablakbandit.core.utils.jsonformatter.hover.ShowTextEvent;

public class LookupArgument extends BankEndArgument{
	
	public LookupArgument(CommandConfiguration.Command command){
		super(command);
	}
	
	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original){
		if(args.length == 0){
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		String name = args[0];
		String uuid = BankDatabaseManager.getInstance().getInfoDatabase().getUUIDDatabase().getUUID(name);
		if(uuid == null){
			base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", name));
			return;
		}
		if(s instanceof Player){
			new JSONFormatter().append(BankLanguageConfiguration.COMMAND_MESSAGE_FORMAT.get().replaceAll("<message>", ChatColor.WHITE + "UUID for: " + name + " is ")).appendHoverClick(uuid, new ShowTextEvent(ChatColor.GREEN + "Click to copy"), new SuggestCommandEvent("" + uuid)).send((Player)s);
		}else{
			base.sendFormattedMessage(s, ChatColor.WHITE + "UUID for: " + name + " is " + uuid);
		}
	}
}
