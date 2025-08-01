package me.dablakbandit.bank.save.loader.runner;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.info.IBankInfo;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;

public class SaveRunner implements Runnable {

	private static final BankDatabaseManager bankDatabaseManager = BankDatabaseManager.getInstance();

	private final CorePlayers pl;
	private final boolean unlock;

	public SaveRunner(CorePlayers pl, boolean unlock) {
		this.pl = pl;
		this.unlock = unlock;
	}

	@Override
	public void run() {
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLAYER_LEVEL, "Saving " + pl.getUUIDString());
		long start = System.currentTimeMillis();
		IInfoDatabase infoDatabase = bankDatabaseManager.getInfoDatabase();
		infoDatabase.getUUIDDatabase().ensureConnection();
		infoDatabase.getPlayerLockDatabase().ensureConnection();
		pl.getAllInfo().stream().filter(info -> (info instanceof IBankInfo && info instanceof JSONInfo)).map(info -> (JSONInfo) info).forEach(info -> {
			info.jsonFinal();
			infoDatabase.getInfoTypeDatabase(info).savePlayer(pl, info, System.currentTimeMillis());
		});
		if (unlock) {
			infoDatabase.getPlayerLockDatabase().setLocked(pl, false);
		}
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLAYER_LEVEL, "Saved " + pl.getUUIDString() + " after " + (System.currentTimeMillis() - start) + "ms");
	}
}
