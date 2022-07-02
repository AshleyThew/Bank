package me.dablakbandit.bank.player.converter.old;

import java.sql.Connection;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.converter.old.mysql.OldMySQLLoader;
import me.dablakbandit.bank.player.converter.old.sqlite.OldSQLLiteLoader;
import me.dablakbandit.bank.save.type.SaveType;
import me.dablakbandit.core.configuration.Configuration;
import me.dablakbandit.core.database.Database;
import me.dablakbandit.core.database.DatabaseManager;
import me.dablakbandit.core.database.mysql.MySQLConfiguration;

public class OldBankConverter{
	
	private static final OldBankConverter instance = new OldBankConverter();
	
	public static OldBankConverter getInstance(){
		return instance;
	}
	
	private OldBankConverter(){
		
	}
	
	public boolean convert(String from, SaveType to){
		BankDatabaseManager.getInstance().load(to);
		if ("MYSQL".equalsIgnoreCase(from)) {
			return convertMySQL();
		}
		return convertSQLite();
	}
	
	private boolean convertMySQL(){
		BankLog.errorAlways("Converting from MYSQL");
		MySQLConfiguration mySQLConfiguration = new MySQLConfiguration(new Configuration(BankPlugin.getInstance(), "mysql.yml"));
		Database database = DatabaseManager.getInstance().createMySQLDatabase(mySQLConfiguration, false);
		Connection connection = database.openConnection();
		if(connection == null){ return false; }
		database.addListener(OldMySQLLoader.getInstance());
		OldMySQLLoader.getInstance().convert(BankDatabaseManager.getInstance().getInfoDatabase());
		database.closeConnection();
		return true;
	}
	
	private boolean convertSQLite(){
		BankLog.errorAlways("Converting from SQLITE");
		Database database = DatabaseManager.getInstance().createSQLiteDatabase(BankPlugin.getInstance(), "uuids.db", false);
		Connection connection = database.openConnection();
		if(connection == null){ return false; }
		database.addListener(OldSQLLiteLoader.getInstance());
		OldSQLLiteLoader.getInstance().convert(BankDatabaseManager.getInstance().getInfoDatabase());
		database.closeConnection();
		return true;
	}
}
