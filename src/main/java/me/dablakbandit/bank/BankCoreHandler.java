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

public class BankCoreHandler extends CoreHandler implements Listener{
	
	private static BankCoreHandler main = new BankCoreHandler();
	
	public static BankCoreHandler getInstance(){
		return main;
	}
	
	private BankPlugin		plugin;
	private UpgradeManager	upgradeManager	= UpgradeManager.getInstance();
	private boolean			upgraded		= false;
	private boolean			firstInstall	= false;
	
	public void onLoad(){
		plugin = BankPlugin.getInstance();
		PluginUpdater.getInstance().checkUpdate(plugin, "18968");
		upgraded = checkUpgrade();
		checkFirstInstall();
		load();
		CorePlayerManager.getInstance().addListener(BankPlayerManager.getInstance());
	}
	
	private void checkFirstInstall(){
		firstInstall = !new File(plugin.getDataFolder(), "config.yml").exists();
	}
	
	private void load(){
		LoaderManager.getInstance();
		loadConfigurations();
		loadDatabase();
		loadCommands();
		loadOther();
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Bank loaded");
	}
	
	public void reload(){
		disable();
		load();
		enable();
	}
	
	private boolean checkUpgrade(){
		if(upgradeManager.hasUpgrade()){
			if(!upgradeManager.confirmUpgrade()){
				BankUpgradeConfiguration.getInstance().notifyUpgrade();
				upgradeManager.printUpgradeAndShutdown();
				return false;
			}
			upgradeManager.upgrade();
			BankUpgradeConfiguration.getInstance().saveConfig();
		}
		return true;
	}
	
	private void loadDatabase(){
		BankDatabaseManager.getInstance().load(BankPluginConfiguration.BANK_SAVE_TYPE.get());
	}
	
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
	
	private void loadCommands(){
		BankCommand.getInstance().load();
	}
	
	private void loadOther(){
		BankInventoriesManager.getInstance().load();
		BankImplementationManager.getInstance().load();
		Converters.load();
	}
	
	public void onEnable(){
		if(!upgraded){ return; }
		
		if(firstInstall){
			handleFirstInstall();
		}
		
		enable();
		LoaderManager.getInstance().start();
		addMetrics();
		BankPlayerManager.getInstance().enable();
		BankPlaceholderManager.getInstance();
	}
	
	public void enable(){
		BankImplementationManager.getInstance().enable();
		AutoSaveManager.getInstance().enable();
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Bank Enabled");
	}
	
	public void disable(){
		AbstractCommand.removePluginCommands(plugin);
		BankImplementationManager.getInstance().disable();
		AutoSaveManager.getInstance().disable();
		Converters.disable();
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Bank Disabled");
	}
	
	public void onDisable(){
		if(!upgraded){ return; }
		disable();
		BankPlayerManager.getInstance().disable();
		LoaderManager.getInstance().stop();
	}
	
	private void addMetrics(){
		try{
			Metrics m = new Metrics(plugin);
			m.addCustomChart(new Metrics.SimplePie("save_type", () -> BankPluginConfiguration.BANK_SAVE_TYPE.get().name()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void handleFirstInstall(){
		firstInstall = false;
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "First install, detecting other plugins.");
		checkCitizens();
	}
	
	private void checkCitizens(){
		if(Bukkit.getPluginManager().getPlugin("Citizens") != null){
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Detected citizens enabling.");
			BankPluginConfiguration.BANK_TYPE_CITIZENS_ENABLED.set(true);
		}
	}
}
