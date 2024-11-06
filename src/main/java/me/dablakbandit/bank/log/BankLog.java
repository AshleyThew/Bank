package me.dablakbandit.bank.log;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.core.config.path.EnumPath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

	private static List<String> alerts = new ArrayList<>();

	public static List<String> getAlerts() {
		return alerts;
	}

	public static void errorAlert(String message) {
		// if(BankPluginConfiguration.CONSOLE_ERROR.get()){
		alerts.add(prefix + message);
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.isOp()) {
				player.sendMessage(prefix + ChatColor.RED + message);
			}
		}
		errorAlways(message);
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
