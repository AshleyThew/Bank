package me.dablakbandit.bank.database;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.database.base.PlayerLockDatabase;
import me.dablakbandit.bank.save.type.SaveType;
import me.dablakbandit.bank.upgrade.infosave.UpgradeInfoSaveManager;
import me.dablakbandit.core.configuration.Configuration;
import me.dablakbandit.core.database.Database;
import me.dablakbandit.core.database.DatabaseManager;
import me.dablakbandit.core.database.mysql.MySQLConfiguration;

public class BankDatabaseManager{
	
	private static BankDatabaseManager databaseManager = new BankDatabaseManager();
	
	public static BankDatabaseManager getInstance(){
		return databaseManager;
	}
	
	private BankDatabase		bankDatabase;
	private MySQLConfiguration	mysqlConfiguration;
	
	private BankDatabaseManager(){
		mysqlConfiguration = new MySQLConfiguration(new Configuration(BankPlugin.getInstance(), "mysql.yml"));
	}
	
	public void load(SaveType saveType){
		if(bankDatabase != null){
			bankDatabase.close();
			bankDatabase = null;
		}
		bankDatabase = loadDatabase(saveType);
		
		UpgradeInfoSaveManager.getInstance().upgrade(bankDatabase.getInfoDatabase());
	}
	
	public BankDatabase loadDatabase(SaveType saveType){
		Database database = null;
		switch(saveType){
		case SQLITE:
			database = DatabaseManager.getInstance().createSQLiteDatabase(BankPlugin.getInstance(), "database.db");
			break;
		}
		IInfoDatabase infoDatabase = saveType.getInfoDatabase().get();
		
		database.addListener(infoDatabase.getUUIDDatabase());
		database.addListener(infoDatabase.getPlayerLockDatabase());
		database.addListener(infoDatabase);
		
		return new BankDatabase(database, infoDatabase);
	}
	
	public String getMySQLDatabase(){
		return mysqlConfiguration.getMySQL().getDatabase();
	}
	
	public void setBankDatabase(BankDatabase bankDatabase){
		this.bankDatabase = bankDatabase;
	}
	
	public BankDatabase getBankDatabase(){
		return bankDatabase;
	}
	
	public IInfoDatabase getInfoDatabase(){
		return bankDatabase.getInfoDatabase();
	}
	
	public PlayerLockDatabase getPlayerLockDatabase(){
		return bankDatabase.getInfoDatabase().getPlayerLockDatabase();
	}
}
