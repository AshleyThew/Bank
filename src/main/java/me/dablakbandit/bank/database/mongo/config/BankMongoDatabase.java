package me.dablakbandit.bank.database.mongo.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class BankMongoDatabase {
	private final MongoClient mongoClient;
	private final MongoDatabase mongoDatabase;

	public BankMongoDatabase(MongoConfiguration config) {
		this.mongoClient = MongoClients.create(config.getConnectionString());
		this.mongoDatabase = mongoClient.getDatabase(config.getDatabase());
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public MongoDatabase getMongoDatabase() {
		return mongoDatabase;
	}

	public boolean isConnected() {
		return true;
	}

	public void closeConnection() {
		mongoClient.close();
	}
}
