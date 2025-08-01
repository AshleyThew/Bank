package me.dablakbandit.bank.implementations.headdb;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.dablakbandit.bank.config.BankItemConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeadDBListener implements Listener {

	private static final HeadDBListener headDBListener = new HeadDBListener();

	public static HeadDBListener getInstance() {
		return headDBListener;
	}

	private HeadDBListener() {

	}

	@EventHandler
	public void onDatabaseLoad(DatabaseLoadEvent e) {
		BankItemConfiguration.getInstance().load();
	}
}
