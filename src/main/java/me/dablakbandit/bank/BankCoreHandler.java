package me.dablakbandit.bank;

import me.dablakbandit.bank.command.BankCommand;
import me.dablakbandit.bank.config.*;
import me.dablakbandit.bank.convert.BankVersion1_20_6Converter;
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
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * The Bank core handler.
 * Replacement of Bank plugin main package, used to load, enable and disable plugin elements.
 */
public class BankCoreHandler extends CoreHandler implements Listener {

	private static final BankCoreHandler main = new BankCoreHandler();

	/**
	 * Get bank core handler instance.
	 *
	 * @return the bank core handler
	 */
	public static BankCoreHandler getInstance() {
		return main;
	}

	private BankPlugin plugin;
	private boolean upgraded = false;
	private boolean converting = false;
	private boolean firstInstall = false;

	/**
	 * Handler load.
	 */
	public void onLoad() {
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
	private void checkFirstInstall() {
		firstInstall = !new File(plugin.getDataFolder(), "config.yml").exists();
	}

	/**
	 * Handler load elements of the plugin.
	 */
	private void load() {
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
	 *
	 * @see #disable()
	 * @see #load()
	 * @see #enable()
	 */
	public void reload() {
		disable();
		load();
		enable();
	}

	/**
	 * Checks the upgrade from bank versions below 4.1.0 for backwards compatibility.
	 *
	 * @see UpgradeManager
	 */
	private boolean checkUpgrade() {
		UpgradeManager upgradeManager = UpgradeManager.getInstance();
		// Check for upgrade
		if (upgradeManager.hasUpgrade()) {
			// Ensure owner has confirmed the upgrade process
			if (!upgradeManager.confirmUpgrade()) {
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
	 *
	 * @see me.dablakbandit.bank.save.type.SaveType
	 * @see BankDatabaseManager
	 * @see BankPluginConfiguration
	 */
	private void loadDatabase() {
		BankDatabaseManager.load(plugin, BankPluginConfiguration.BANK_SAVE_TYPE.get());
	}

	/**
	 * Loads all configuration elements for the plugin.
	 *
	 * @see BankPluginConfiguration
	 * @see BankLanguageConfiguration
	 * @see BankCommandConfiguration
	 * @see BankItemConfiguration
	 * @see BankInventoryConfiguration
	 * @see BankPermissionConfiguration
	 * @see BankItemBlacklistConfiguration
	 * @see BankSoundConfiguration
	 */
	private void loadConfigurations() {
		File pluginFolder = BankPlugin.getInstance().getDataFolder();
		if (!pluginFolder.exists()) {
			pluginFolder.mkdirs();
		}
		File confFolder = new File(pluginFolder, "conf/");
		if (!confFolder.exists()) {
			confFolder.mkdirs();
		}
		moveConfigFile("commands.yml");
		moveConfigFile("hiscores.yml");
		moveConfigFile("itemblacklist.yml");
		moveConfigFile("default_items.yml");
		moveConfigFile("language.yml");
		moveConfigFile("permissions.yml");
		moveConfigFile("sounds.yml");
		moveConfigFile("inventories.yml");
		moveConfigFile("items.yml");
		BankPluginConfiguration.load(plugin);
		BankLanguageConfiguration.load(plugin);
		BankCommandConfiguration.load(plugin);
		BalTopCommandConfiguration.load(plugin);
		BankItemConfiguration.load(plugin);
		BankInventoryConfiguration.load(plugin);
		BankPermissionConfiguration.load(plugin);
		BankItemBlacklistConfiguration.load(plugin);
		BankItemDefaultConfiguration.load(plugin);
		BankSoundConfiguration.load(plugin);
	}

	private void moveConfigFile(String fileName) {
		File file = new File(BankPlugin.getInstance().getDataFolder(), fileName);
		if (!file.exists()) {
			return;
		}
		File folder = new File(BankPlugin.getInstance().getDataFolder(), "conf/");
		folder.mkdirs();
		try {
			Files.move(file.toPath(), new File(folder, fileName).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load bank command for the plugin.
	 */
	private void loadCommands() {
		BankCommand.setup(plugin);
		BankCommand.getInstance().load();
	}

	/**
	 * Loads other elements of the plugin, to be called last.
	 * Inventories, Implementations, Expire & Converters.
	 *
	 * @see BankInventoriesManager
	 * @see BankImplementationManager
	 * @see Converters
	 */
	private void loadOther() {
		BankInventoriesManager.getInstance().load();
		BankImplementationManager.getInstance().load();
		Converters.load();
	}

	/**
	 * Handler enable.
	 */
	public void onEnable() {
		// If the upgrade process failed don't continue to enable
		if (!upgraded) {
			return;
		}

		// Register listeners
		Bukkit.getPluginManager().registerEvents(this, plugin);

		if (UpgradeManager.getInstance().getPreviousVersion() <= 470) {
			converting = true;
			BankLog.info("Converting bank data to fix item types.");
			BankVersion1_20_6Converter.getInstance().convert(Bukkit::shutdown);
			return;
		} else if (BankUpgradeConfiguration.UPGRADE_DATABASE_CLEANUP.get()) {
			converting = true;
			BankLog.info("Cleaning up database.");
			BankUpgradeConfiguration.UPGRADE_DATABASE_CLEANUP.set(false);
			BankUpgradeConfiguration.getInstance().saveConfig();
			BankVersion1_20_6Converter.getInstance().convert(Bukkit::shutdown);
			return;
		}

		// Unset converting flag
		converting = false;

		// Handle first install
		if (firstInstall) {
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
		// Enable converts
		Converters.enable();
	}

	/**
	 * Enable elements of the plugin.
	 */
	private void enable() {
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
	private void disable() {
		BankLog.getAlerts().clear();
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
	public void onDisable() {
		// If plugin failed to upgrade
		if (!upgraded) {
			return;
		}

		// Disable elements of the plugin
		disable();

		//
		BankPlayerManager.getInstance().disable();
		LoaderManager.getInstance().stop();
	}

	/**
	 * Enable bStats metrics.
	 */
	private void addMetrics() {
		try {
			// Start metrics with plugin instance
			Metrics m = new Metrics(plugin);
			// Add custom chart for savetype
			m.addCustomChart(new Metrics.SimplePie("save_type", () -> BankPluginConfiguration.BANK_SAVE_TYPE.get().name()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent event) {
		if (converting) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Bank converting, please wait.");
		}
		if (event.getPlayer().isOp()) {
			BankLog.getAlerts().forEach(event.getPlayer()::sendMessage);
		}
	}

	/**
	 * Check for elements when first installed.
	 *
	 * @see #checkCitizens()
	 */
	private void handleFirstInstall() {
		firstInstall = false;
		// Notify console of first install
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "First install, detecting other plugins.");

		// Checks
		checkCitizens();
	}

	/**
	 * On first install check for citizens plugin to enable
	 */
	private void checkCitizens() {
		// If the Citizens plugin exists
		if (Bukkit.getPluginManager().getPlugin("Citizens") != null) {
			// Notify console of citizens detection
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Detected citizens enabling.");
			// Enable citizens support
			BankPluginConfiguration.BANK_TYPE_CITIZENS_ENABLED.set(true);
		}
	}
}
