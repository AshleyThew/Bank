package me.dablakbandit.bank.save.loader.runner;

import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.player.info.IBankInfo;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;

public class LoadSingleRunner implements Runnable{
	
	private static BankDatabaseManager	bankDatabaseManager	= BankDatabaseManager.getInstance();
	
	private CorePlayers					pl;
	
	public LoadSingleRunner(CorePlayers pl){
		this.pl = pl;
	}
	
	@Override
	public void run(){
		IInfoDatabase infoDatabase = bankDatabaseManager.getInfoDatabase();
		pl.getAllInfo().stream().filter(info -> (info instanceof IBankInfo && info instanceof JSONInfo)).map(info -> (JSONInfo)info).forEach(info -> {
			infoDatabase.getInfoTypeDatabase(info).loadPlayer(pl, info);
			info.jsonInit();
		});
	}
}
