package me.dablakbandit.bank.player;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.player.converter.Converters;
import me.dablakbandit.bank.player.event.BankPlayersLoadedEvent;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.admin.BankAdminInfo;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.listener.CorePlayersListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BankPlayerManager extends CorePlayersListener implements Listener {

	private static final BankPlayerManager playerManager = new BankPlayerManager();

	public static BankPlayerManager getInstance() {
		return playerManager;
	}

	private BankPlayerManager() {

	}

	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, BankPlugin.getInstance());
	}

	public void disable() {
		CorePlayerManager.getInstance().getPlayers().values().forEach(pl -> LoaderManager.getInstance().save(pl, true));
	}

	@Override
	public void addCorePlayers(CorePlayers pl) {
		pl.addInfo(new BankInfo(pl));
		pl.addInfo(new BankAdminInfo(pl));
	}

	@Override
	public void loadCorePlayers(CorePlayers pl) {
		LoaderManager.getInstance().load(pl);
		if (BankPluginConfiguration.BANK_SAVE_LOAD_DELAY.get() != -1) {
			Bukkit.getScheduler().runTaskLater(BankPlugin.getInstance(), () -> {
				delayedLoad(pl);
			}, BankPluginConfiguration.BANK_SAVE_LOAD_DELAY.get());
		}
	}

	private void delayedLoad(CorePlayers pl) {
		if (pl.getPlayer().isOnline()) {
			pl.getInfo(BankInfo.class).isLocked(true);
		}
	}

	@Override
	public void saveCorePlayers(CorePlayers pl) {
		LoaderManager.getInstance().save(pl, true);
	}

	@Override
	public void removeCorePlayers(CorePlayers pl) {

	}

	@EventHandler
	public void onPlayerLoad(BankPlayersLoadedEvent event) {
		Converters.convert(event.getPlayers());
	}
}
