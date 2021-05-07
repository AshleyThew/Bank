package me.dablakbandit.bank.config;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.StringListPath;

public class BankItemBlacklistConfiguration extends CommentAdvancedConfiguration{
	
	private static BankItemBlacklistConfiguration configuration = new BankItemBlacklistConfiguration(BankPlugin.getInstance());
	
	public static BankItemBlacklistConfiguration getInstance(){
		return configuration;
	}
	
	private BankItemBlacklistConfiguration(JavaPlugin plugin){
		super(plugin, "itemblacklist.yml");
	}
	
	@CommentArray({ "Items that are blacklist are saved here", "Use '/bank admin item blacklist' in-game" })
	public static StringListPath BLACKLIST = new StringListPath();
	
}
