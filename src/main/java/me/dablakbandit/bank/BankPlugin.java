/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.bank;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.plugin.downloader.CorePluginDownloader;

public class BankPlugin extends JavaPlugin{
	
	private static BankPlugin	main;
	private BankCoreHandler		handler;
	
	public static BankPlugin getInstance(){
		return main;
	}
	
	public void onLoad(){
		main = this;
		if(CorePluginDownloader.ensureCorePlugin()){
			handler = BankCoreHandler.getInstance();
			handler.onLoad();
		}
	}
	
	public void onEnable(){
		if(handler != null){
			handler.onEnable();
		}
	}
	
	public void onDisable(){
		if(handler != null){
			handler.onDisable();
		}else{
			printError();
		}
	}
	
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
