package me.dablakbandit.bank.database;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.save.type.SaveType;
import me.dablakbandit.bank.upgrade.infosave.UpgradeInfoSaveManager;
import me.dablakbandit.core.configuration.Configuration;
import me.dablakbandit.core.database.Database;
import me.dablakbandit.core.database.DatabaseManager;
import me.dablakbandit.core.database.mysql.MySQLConfiguration;

public class BankDatabaseManager{

	private static BankDatabaseManager databaseManager;
	
	public static BankDatabaseManager getInstance(){
		return databaseManager;
	}


	private BankDatabaseManager(BankPlugin bankPlugin) {
		mysqlConfiguration = new MySQLConfiguration(new Configuration(bankPlugin, "mysql.yml"));
	}

	public static void load(BankPlugin bankPlugin, SaveType saveType) {
		databaseManager = new BankDatabaseManager(bankPlugin);
		databaseManager.load(saveType);
	}
	
	private BankDatabase		bankDatabase;
	private final MySQLConfiguration	mysqlConfiguration;

	public void load(SaveType saveType) {
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
	
}
