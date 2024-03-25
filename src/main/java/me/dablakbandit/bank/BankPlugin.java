/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.bank;

import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.plugin.downloader.CorePluginDownloader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The entry point to the bank plugin.
 * Ensures core plugin is downloaded before continuing,
 * Else errors print to console.
 * @see me.dablakbandit.core.plugin.downloader.CorePluginDownloader
 */
public class BankPlugin extends JavaPlugin{
	
	private static BankPlugin	main;
	private BankCoreHandler		handler;
	
	/**
	 * Get bank plugin instance.
	 *
	 * @return the bank plugin
	 */
	public static BankPlugin getInstance(){
		return main;
	}
	
	/**
	 * Plugin load.
	 */
	public void onLoad(){
		// Assign instance
		main = this;
		
		// Check if the core plugin exists
		if(CorePluginDownloader.ensureCorePlugin()){
			// Assign and load handler
			handler = BankCoreHandler.getInstance();
			handler.onLoad();
		}
	}
	
	/**
	 * Plugin enabling.
	 */
	public void onEnable(){
		// Enable handler if assigned
		if(handler != null){
			handler.onEnable();
		}
	}
	
	/**
	 * Plugin disabling.
	 */
	public void onDisable(){
		// Disable handler if assigned else print error
		if(handler != null){
			handler.onDisable();
		}else{
			printError();
		}
	}
	
	/**
	 * Print error to console when core plugin is missing.
	 */
	private void printError(){
		for(int i = 0; i < 5; i++){
			BankLog.errorAlways("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			BankLog.errorAlways("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		}
		BankLog.errorAlways("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		BankLog.errorAlways("Core plugin not found, has been automatically downloaded.");
		BankLog.errorAlways("Please read the setup page next time!");
		BankLog.errorAlways("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		for(int i = 0; i < 5; i++){
			BankLog.errorAlways("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			BankLog.errorAlways("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		}
	}
	
}
