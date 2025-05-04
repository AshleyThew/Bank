package me.dablakbandit.bank.database;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.database.mongo.config.BankMongoDatabase;
import me.dablakbandit.bank.database.mongo.config.MongoConfiguration;
import me.dablakbandit.bank.database.sql.SQLInfoDatabase;
import me.dablakbandit.bank.save.type.SaveType;
import me.dablakbandit.bank.upgrade.infosave.UpgradeInfoSaveManager;
import me.dablakbandit.core.configuration.Configuration;
import me.dablakbandit.core.database.Database;
import me.dablakbandit.core.database.DatabaseManager;
import me.dablakbandit.core.database.mysql.MySQLConfiguration;

public class BankDatabaseManager {

	private static BankDatabaseManager databaseManager;

	public static BankDatabaseManager getInstance() {
		return databaseManager;
	}

	private BankDatabaseManager(BankPlugin bankPlugin) {
		mysqlConfiguration = new MySQLConfiguration(new Configuration(bankPlugin, "mysql.yml"));
		mongoConfiguration = new MongoConfiguration(new Configuration(bankPlugin, "mongo.yml"));
	}

	public static void load(BankPlugin bankPlugin, SaveType saveType) {
		databaseManager = new BankDatabaseManager(bankPlugin);
		databaseManager.load(saveType);
	}

	private BankDatabase bankDatabase;
	private final MySQLConfiguration mysqlConfiguration;
	private final MongoConfiguration mongoConfiguration;

	public void load(SaveType saveType) {
		if (bankDatabase != null) {
			bankDatabase.close();
			bankDatabase = null;
		}
		bankDatabase = loadDatabase(saveType);
		UpgradeInfoSaveManager.getInstance().upgrade(bankDatabase.getInfoDatabase());
	}

	public BankDatabase loadDatabase(SaveType saveType) {
		Database database = null;
		BankMongoDatabase mongoDatabase = null;
		IInfoDatabase infoDatabase = saveType.getInfoDatabase().get();
		switch (saveType) {
			case SQLITE:
				database = DatabaseManager.getInstance().createSQLiteDatabase(BankPlugin.getInstance(), "database.db");
				break;
		}

		switch (saveType) {
			case SQLITE:
				if (database != null && infoDatabase instanceof SQLInfoDatabase) {
					SQLInfoDatabase sqlInfoDatabase = (SQLInfoDatabase) infoDatabase;
					database.addListener(sqlInfoDatabase.getUUIDDatabase());
					database.addListener(sqlInfoDatabase.getPlayerLockDatabase());
					database.addListener(sqlInfoDatabase);
					database.addListener(sqlInfoDatabase.getMoneyTransactionDatabase());
					database.addListener(sqlInfoDatabase.getExpTransactionDatabase());
				}
				break;
		}

		bankDatabase = new BankDatabase(database, mongoDatabase, infoDatabase);

		return bankDatabase;
	}

	public String getMySQLDatabase() {
		return mysqlConfiguration.getMySQL().getDatabase();
	}

	public void setBankDatabase(BankDatabase bankDatabase) {
		this.bankDatabase = bankDatabase;
	}

	public BankDatabase getBankDatabase() {
		return bankDatabase;
	}

	public IInfoDatabase getInfoDatabase() {
		return bankDatabase.getInfoDatabase();
	}

}
