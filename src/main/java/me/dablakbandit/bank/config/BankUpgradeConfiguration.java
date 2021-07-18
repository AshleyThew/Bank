package me.dablakbandit.bank.config;

import org.bukkit.plugin.Plugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.BooleanPath;
import me.dablakbandit.core.config.path.IntegerPath;

public class BankUpgradeConfiguration extends CommentAdvancedConfiguration{
	
	private static final BankUpgradeConfiguration config = new BankUpgradeConfiguration(BankPlugin.getInstance());
	
	public static BankUpgradeConfiguration getInstance(){
		return config;
	}
	
	@Comment("No upgrade needed at this time")
	public static final BooleanPath	UPGRADE_CONFIRM				= new BooleanPath(false);
	
	@Comment("DO NOT TOUCH, THIS WILL TRIGGER AN UPGRADE")
	public static final IntegerPath	UPGRADE_VERSION				= new IntegerPath(0);
	
	@CommentArray({ "SET THIS ONLY TO TRUE IF UPGRADING FROM BEFORE 4.1.0", "THIS WILL TRIGGER A CONVERSION, WHICH MAY TAKE SOME TIME", "MAKE SURE TO BACKUP OLD BANK DATA" })
	public static final BooleanPath	UPGRADE_CONVERSION_OLD		= new BooleanPath(false);
	
	@CommentArray({ "IF USING MYSQL ON THE OLD PLUGIN AND WISH TO KEEP USING MYSQL SET THIS TO TRUE" })
	public static final BooleanPath	UPGRADE_CONVERSION_MYSQL	= new BooleanPath(false);
	
	private BankUpgradeConfiguration(Plugin plugin){
		super(plugin, "upgrade.yml");
	}
	
	//@formatter:off
	public void notifyUpgrade(){
		getConfig().setComment(UPGRADE_CONFIRM.getPath(), 
				"!!! READ THE MESSAGE BELOW IT IS IMPORTANT !!!",
						"The plugin is in need of upgrading please confirm this upgrade by setting to true",
						"If this is your first time installing the plugin you may set to true",
						"Note this will backup current configs and overwrite them",
						"Please ensure you are up to date on previous upgrades",
						"Meaning you will have to edit every config file again",
						"The plugin will need reconfiguring after upgrade, config paths may change",
						"",
						"If you are also upgrading from a pre 4.1.0 release you will also need to enable",
						"conversion from the old plugin");
		saveConfig();
	}
	//@formatter:on
}
