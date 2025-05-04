package me.dablakbandit.bank.database.mongo.config;

import me.dablakbandit.core.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public class MongoConfiguration {
	protected String host, port, database, user, password, extra;

	public MongoConfiguration(Configuration file) {
		FileConfiguration conf = file.getConfig();
		if (!conf.isSet("Mongo.host")) {
			conf.set("Mongo.host", "localhost");
		}
		if (!conf.isSet("Mongo.port")) {
			conf.set("Mongo.port", "27017");
		}
		if (!conf.isSet("Mongo.database")) {
			conf.set("Mongo.database", "bank");
		}
		if (!conf.isSet("Mongo.user")) {
			conf.set("Mongo.user", "");
		}
		if (!conf.isSet("Mongo.password")) {
			conf.set("Mongo.password", "");
		}
		if (!conf.isSet("Mongo.extra")) {
			conf.set("Mongo.extra", "");
		}
		file.saveConfig();
		this.host = conf.getString("Mongo.host");
		this.port = conf.getString("Mongo.port");
		this.database = conf.getString("Mongo.database");
		this.user = conf.getString("Mongo.user");
		this.password = conf.getString("Mongo.password");
		this.extra = conf.getString("Mongo.extra");
	}

	public String getConnectionString() {
		if (user != null && !user.isEmpty()) {
			return "mongodb://" + user + ":" + password + "@" + host + ":" + port + "/" + database + extra;
		} else {
			return "mongodb://" + host + ":" + port + "/" + database + extra;
		}
	}

	public String getDatabase() {
		return database;
	}
}
