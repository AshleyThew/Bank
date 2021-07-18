package me.dablakbandit.bank.log;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.core.config.path.EnumPath;

public class BankLog{
	
	private static final String prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "Bank" + ChatColor.GRAY + "] ";
	
	public static String getPrefix(){
		return prefix;
	}
	
	public static void info(String message){
		info(BankLogLevel.HIGHEST, message);
	}
	
	public static void info(EnumPath<BankLogLevel> level, String message){
		info(level.get(), message);
	}
	
	public static void info(BankLogLevel level, String message){
		if(BankPluginConfiguration.BANK_LOG_LEVEL.get() != null){
			if(!BankPluginConfiguration.BANK_LOG_LEVEL.get().isAtleast(level)){ return; }
		}
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.DARK_AQUA + message);
	}
	
	public static void debug(Object object){
		debug(object == null ? "null" : object.toString());
	}
	
	public static void debug(Object... object){
		debug(Arrays.stream(object).map(o -> o == null ? "null" : o.toString()).collect(Collectors.joining(", ")));
	}
	
	public static void debug(String message){
		// if(BankPluginConfiguration.CONSOLE_DEBUG.get()){
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + message);
		// }
	}
	
	public static void error(String message){
		// if(BankPluginConfiguration.CONSOLE_ERROR.get()){
		errorAlways(message);
		// }
	}
	
	public static void errorAlways(String message){
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + message);
	}
	
}
