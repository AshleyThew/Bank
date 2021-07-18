package me.dablakbandit.bank.config;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.StringListPath;

public class BankItemBlacklistConfiguration extends CommentAdvancedConfiguration{
	
	private static final BankItemBlacklistConfiguration configuration = new BankItemBlacklistConfiguration(BankPlugin.getInstance());
	
	public static BankItemBlacklistConfiguration getInstance(){
		return configuration;
	}
	
	private BankItemBlacklistConfiguration(JavaPlugin plugin){
		super(plugin, "itemblacklist.yml");
	}
	
	@CommentArray({ "Items that are blacklist are saved here", "Use '/bank admin item blacklist' in-game" })
	public static final StringListPath	BLACKLIST			= new StringListPath();
	
	@CommentArray({ "Blacklist items that contain a name.", "Accepts regex, eg: '(?i)hello' will match (Hello, hello)", "I suggest using a website such as https://regex101.com/ to create regex's" })
	public static final StringListPath	BLACKLISTED_NAME	= new StringListPath("Example name of item");
	
	@CommentArray({ "Blacklist items that contain lore string.", "Accepts regex, eg: '(?i)hello' will match (Hello, hello)", "I suggest using a website such as https://regex101.com/ to create regex's" })
	public static final StringListPath	BLACKLISTED_LORE	= new StringListPath("Example lore of item");
	
}
