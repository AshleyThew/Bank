package me.dablakbandit.bank.implementations.headdb;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.implementations.BankImplementation;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class HeadDBImplementation extends BankImplementation {

	private static final HeadDBImplementation implementation = new HeadDBImplementation();

	public static HeadDBImplementation getInstance() {
		return implementation;
	}

	private HeadDBImplementation() {

	}

	@Override
	public void load() {

	}

	@Override
	public void enable() {
		try {
			Class.forName("me.arcaniax.hdb.api.DatabaseLoadEvent");
		} catch (Exception e) {
			return;
		}
		registerListener();
	}

	private void registerListener() {
		Bukkit.getPluginManager().registerEvents(HeadDBListener.getInstance(), BankPlugin.getInstance());
	}

	@Override
	public void disable() {
		HandlerList.unregisterAll(HeadDBListener.getInstance());
	}

}
