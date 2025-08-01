package me.dablakbandit.bank.save.loader;

import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.save.loader.runner.LoadRunner;
import me.dablakbandit.bank.save.loader.runner.SaveRunner;
import me.dablakbandit.bank.save.loader.runner.SaveSingleRunner;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;

public class LoaderManager {

	private static final LoaderManager loaderManager = new LoaderManager();

	public static LoaderManager getInstance() {
		return loaderManager;
	}

	private LoaderThread loaderThread;
	private Thread running;

	private LoaderManager() {

	}

	public void start() {
		running = new Thread(loaderThread = new LoaderThread());
		running.setName("Bank - Loader Thread");
		running.start();
	}

	public void stop() {
		if (loaderThread != null) {
			loaderThread.terminate();
			while (!loaderThread.finished()) {
				loaderThread.runRunners();
			}
		}
	}

	public void save(CorePlayers pl, boolean unlock) {
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		if (bankInfo == null || bankInfo.isLocked(false)) {
			return;
		}
		loaderThread.add(new SaveRunner(pl, unlock));
	}

	public void saveSingle(CorePlayers pl, JSONInfo info) {
		loaderThread.add(new SaveSingleRunner(pl, info));
	}

	public void load(CorePlayers pl) {
		load(pl, false);
	}

	public void load(CorePlayers pl, boolean force) {
		load(pl, force, null);
	}

	public void load(CorePlayers pl, boolean force, Runnable runnable) {
		loaderThread.add(new LoadRunner(pl, force, runnable));
	}

	public void runAsync(Runnable runnable) {
		loaderThread.add(runnable);
	}

	public LoaderThread getLoaderThread() {
		return loaderThread;
	}
}
