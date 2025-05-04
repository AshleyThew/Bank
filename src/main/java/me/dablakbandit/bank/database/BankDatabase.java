package me.dablakbandit.bank.database;

import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.database.mongo.config.BankMongoDatabase;
import me.dablakbandit.core.database.Database;

public class BankDatabase {

	private final Database database;
	private final BankMongoDatabase bankMongoDatabase;
	private final IInfoDatabase infoDatabase;

	public BankDatabase(Database database, BankMongoDatabase bankMongoDatabase, IInfoDatabase infoDatabase) {
		this.database = database;
		this.bankMongoDatabase = bankMongoDatabase;
		this.infoDatabase = infoDatabase;
	}


	public void close() {
		if (database != null) {
			database.closeConnection();
		} else if (bankMongoDatabase != null) {
			bankMongoDatabase.closeConnection();
		}
	}

	public IInfoDatabase getInfoDatabase() {
		return infoDatabase;
	}

	public boolean isConnected() {
		if (database != null) {
			return database.isConnected();
		} else if (bankMongoDatabase != null) {
			return bankMongoDatabase.isConnected();
		}
		return false;
	}

}
