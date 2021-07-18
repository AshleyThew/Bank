package me.dablakbandit.bank;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import me.dablakbandit.bank.command.BankCommand;
import me.dablakbandit.bank.config.*;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.implementations.BankImplementationManager;
import me.dablakbandit.bank.implementations.placeholder.BankPlaceholderManager;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.BankPlayerManager;
import me.dablakbandit.bank.player.converter.Converters;
import me.dablakbandit.bank.save.auto.AutoSaveManager;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.bank.upgrade.UpgradeManager;
import me.dablakbandit.core.commands.AbstractCommand;
import me.dablakbandit.core.metrics.Metrics;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.plugin.CoreHandler;
import me.dablakbandit.core.updater.PluginUpdater;

/**
 * The Bank core handler.
 * Replacement of Bank plugin main package, used to load, enable and disable plugin elements.
 */
public class BankCoreHandler extends CoreHandler implements Listener{
	
	private static final BankCoreHandler main = new BankCoreHandler();
	
	/**
	 * Get bank core handler instance.
	 *
	 * @return the bank core handler
	 */
	public static BankCoreHandler getInstance(){
		return main;
	}
	
	private BankPlugin		plugin;
	private final UpgradeManager	upgradeManager	= UpgradeManager.getInstance();
	private boolean			upgraded		= false;
	private boolean			firstInstall	= false;
	
	/**
	 * Handler load.
	 */
	public void onLoad(){
		// Assign plugin instance for reference
		plugin = BankPlugin.getInstance();
		
		// Check for plugin updates from spigot
		PluginUpdater.getInstance().checkUpdate(plugin, "18968");
		// Check if an upgrade exists
		upgraded = checkUpgrade();
		// Check for plugin first install
		checkFirstInstall();
		
		// Load elements of the plugin
		load();
		
		// Register BankPlayerManager with core plugin
		CorePlayerManager.getInstance().addListener(BankPlayerManager.getInstance());
	}
	
	/**
	 * Check if this is the plugins first run time.
	 */
	private void checkFirstInstall(){
		firstInstall = !new File(plugin.getDataFolder(), "config.yml").exists();
	}
	
	/**
	 * Handler load elements of the plugin.
	 */
	private void load(){
		// Initialize loader thread
		LoaderManager.getInstance();
		
		// Load elements of plugin
		loadConfigurations();
		loadDatabase();
		loadCommands();
		loadOther();
		
		// Notify console bank has loaded
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Bank loaded");
	}
	
	/**
	 * Reload elements of the plugin.
	 * Calls methods in order, disable, load, enable.
	 * @see #disable() 
	 * @see #load() 
	 * @see #enable() 
	 */
	public void reload(){
		disable();
		load();
		enable();
	}
	
	/**
	 * Checks the upgrade from bank versions below 4.1.0 for backwards compatibility.
	 * @see UpgradeManager
	 */
	private boolean checkUpgrade(){
		// Check for upgrade
		if(upgradeManager.hasUpgrade()){
			// Ensure owner has confirmed the upgrade process
			if(!upgradeManager.confirmUpgrade()){
				// Ask owner to confirm upgrade
				BankUpgradeConfiguration.getInstance().notifyUpgrade();
				upgradeManager.printUpgradeAndShutdown();
				return false;
			}
			// Perform upgrade
			upgradeManager.upgrade();
			BankUpgradeConfiguration.getInstance().saveConfig();
		}
		return true;
	}
	
	/**
	 * Loads database with SaveType from config.
	 * @see me.dablakbandit.bank.save.type.SaveType
	 * @see BankDatabaseManager
	 * @see BankPluginConfiguration
	 */
	private void loadDatabase(){
		BankDatabaseManager.getInstance().load(BankPluginConfiguration.BANK_SAVE_TYPE.get());
	}
	
	/**
	 * Loads all configuration elements for the plugin.
	 * @see BankPluginConfiguration
	 * @see BankLanguageConfiguration
	 * @see BankCommandConfiguration
	 * @see BankItemConfiguration
	 * @see BankInventoryConfiguration
	 * @see BankPermissionConfiguration
	 * @see BankItemBlacklistConfiguration
	 * @see BankSoundConfiguration
	 */
	private void loadConfigurations(){
		BankPluginConfiguration.getInstance().load();
		BankLanguageConfiguration.getInstance().load();
		BankCommandConfiguration.getInstance().load();
		BankItemConfiguration.getInstance().load();
		BankInventoryConfiguration.getInstance().load();
		BankPermissionConfiguration.getInstance().load();
		BankItemBlacklistConfiguration.getInstance().load();
		BankSoundConfiguration.getInstance().load();
	}
	
	/**
	 * Load bank command for the plugin.
	 */
	private void loadCommands(){
		BankCommand.getInstance().load();
	}
	
	/**
	 * Loads other elements of the plugin, to be called last.
	 * Inventories, Implementations, Expire & Converters.
	 * @see BankInventoriesManager
	 * @see BankImplementationManager
	 * @see Converters
	 */
	private void loadOther(){
		BankInventoriesManager.getInstance().load();
		BankImplementationManager.getInstance().load();
		Converters.load();
	}
	
	/**
	 * Handler enable.
	 */
	public void onEnable(){
		// If the upgrade process failed don't continue to enable
		if(!upgraded){ return; }
		
		// Handle first install
		if(firstInstall){
			handleFirstInstall();
		}
		
		// Enable elements of plugin
		enable();
		
		// Start loader thread
		LoaderManager.getInstance().start();
		// Report metrics
		addMetrics();
		// Enable Bank Player Manager
		BankPlayerManager.getInstance().enable();
		// Initialize placeholders
		BankPlaceholderManager.getInstance();
	}
	
	/**
	 * Enable elements of the plugin.
	 */
	private void enable(){
		// Enable implementations
		BankImplementationManager.getInstance().enable();
		// Enable autosave
		AutoSaveManager.getInstance().enable();
		
		// Notify console bank has enabled
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Bank Enabled");
	}
	
	/**
	 * Disable elements of the plugin.
	 */
	private void disable(){
		// Remove plugin commands
		AbstractCommand.removePluginCommands(plugin);
		
		// Disable all implementations
		BankImplementationManager.getInstance().disable();
		// Disable autosave
		AutoSaveManager.getInstance().disable();
		// Disable converters
		Converters.disable();
		
		// Notify console bank has disabled
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Bank Disabled");
	}
	
	/**
	 * Handler disable.
	 */
	public void onDisable(){
		// If plugin failed to upgrade
		if(!upgraded){ return; }
		
		// Disable elements of the plugin
		disable();
		
		//
		BankPlayerManager.getInstance().disable();
		LoaderManager.getInstance().stop();
	}
	
	/**
	 * Enable bStats metrics.
	 */
	private void addMetrics(){
		try{
			// Start metrics with plugin instance
			Metrics m = new Metrics(plugin);
			// Add custom chart for savetype
			m.addCustomChart(new Metrics.SimplePie("save_type", () -> BankPluginConfiguration.BANK_SAVE_TYPE.get().name()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Check for elements when first installed.
	 * @see #checkCitizens() 
	 */
	private void handleFirstInstall(){
		firstInstall = false;
		// Notify console of first install
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "First install, detecting other plugins.");
		
		// Checks
		checkCitizens();
	}
	
	/**
	 * On first install check for citizens plugin to enable
	 */
	private void checkCitizens(){
		// If the Citizens plugin exists
		if(Bukkit.getPluginManager().getPlugin("Citizens") != null){
			// Notify console of citizens detection
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Detected citizens enabling.");
			// Enable citizens support
			BankPluginConfiguration.BANK_TYPE_CITIZENS_ENABLED.set(true);
		}
	}
}
