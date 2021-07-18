package me.dablakbandit.bank.database;

import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.core.database.Database;

public class BankDatabase{
	
	private final Database		database;
	private final IInfoDatabase	infoDatabase;
	
	public BankDatabase(Database database, IInfoDatabase infoDatabase){
		this.database = database;
		this.infoDatabase = infoDatabase;
	}
	
	public void close(){
		database.closeConnection();
	}
	
	public Database getDatabase(){
		return database;
	}
	
	public IInfoDatabase getInfoDatabase(){
		return infoDatabase;
	}
	
}
