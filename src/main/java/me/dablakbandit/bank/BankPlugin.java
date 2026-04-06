/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.bank;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.log.BankLog;

/**
 * The entry point to the bank plugin.
 */
public class BankPlugin extends JavaPlugin {

	private static BankPlugin main;
	private BankCoreHandler handler;

	/**
	 * Get bank plugin instance.
	 *
	 * @return the bank plugin
	 */
	public static BankPlugin getInstance() {
		return main;
	}

	/**
	 * Plugin load.
	 */
	public void onLoad() {
		// Assign instance
		main = this;

		// Check if the core plugin is loaded
		if (Bukkit.getPluginManager().getPlugin("Core") != null) {
			// Assign and load handler
			handler = BankCoreHandler.getInstance();
			handler.onLoad();
		} else {
			printError();
		}
	}

	/**
	 * Plugin enabling.
	 */
	public void onEnable() {
		if (handler != null) {
			handler.onEnable();
		}
	}

	/**
	 * Plugin disabling.
	 */
	public void onDisable() {
		if (handler != null) {
			handler.onDisable();
		}
	}

	private void printError() {
		for (int i = 0; i < 5; i++) {
			BankLog.errorAlways("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			BankLog.errorAlways("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		}
		BankLog.errorAlways("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		BankLog.errorAlways("Core plugin not found, has been automatically downloaded.");
		BankLog.errorAlways("Please read the setup page next time!");
		BankLog.errorAlways("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		for (int i = 0; i < 5; i++) {
			BankLog.errorAlways("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			BankLog.errorAlways("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		}
	}
}
