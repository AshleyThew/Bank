package me.dablakbandit.bank.save.loader.runner;

import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;

public class SaveSingleRunner implements Runnable{
	
	private static final BankDatabaseManager	bankDatabaseManager	= BankDatabaseManager.getInstance();
	
	private final CorePlayers					pl;
	private final JSONInfo						info;
	
	public SaveSingleRunner(CorePlayers pl, JSONInfo info){
		this.pl = pl;
		this.info = info;
	}
	
	@Override
	public void run(){
		IInfoDatabase infoDatabase = bankDatabaseManager.getInfoDatabase();
		infoDatabase.ensureConnection();
		info.jsonFinal();
		infoDatabase.getInfoTypeDatabase(info).savePlayer(pl, info, System.currentTimeMillis());
	}
}
