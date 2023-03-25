package me.dablakbandit.bank.config;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.StringListPath;

public class BankItemDefaultConfiguration extends CommentAdvancedConfiguration{

	private static final BankItemDefaultConfiguration configuration = new BankItemDefaultConfiguration(BankPlugin.getInstance());

	public static BankItemDefaultConfiguration getInstance(){
		return configuration;
	}

	private BankItemDefaultConfiguration(JavaPlugin plugin){
		super(plugin, "conf/default_items.yml");
	}
	
	@CommentArray({ "Default items in bank are saved here", "Use '/bank admin item default' in-game" })
	public static final StringListPath	ITEMS			= new StringListPath();
	
}
