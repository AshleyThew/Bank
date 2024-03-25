package me.dablakbandit.bank.config;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.StringListPath;
import org.bukkit.plugin.java.JavaPlugin;

public class BankItemDefaultConfiguration extends CommentAdvancedConfiguration{

	private static BankItemDefaultConfiguration configuration;

	public static void load(BankPlugin plugin) {
		configuration = new BankItemDefaultConfiguration(plugin);
		configuration.load();
	}

	public static BankItemDefaultConfiguration getInstance(){
		return configuration;
	}

	private BankItemDefaultConfiguration(JavaPlugin plugin){
		super(plugin, "conf/default_items.yml");
	}
	
	@CommentArray({ "Default items in bank are saved here", "Use '/bank admin item default' in-game" })
	public static final StringListPath	ITEMS			= new StringListPath();
	
}
