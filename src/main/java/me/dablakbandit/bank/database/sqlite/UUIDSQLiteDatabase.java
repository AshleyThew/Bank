package me.dablakbandit.bank.database.sqlite;

import me.dablakbandit.bank.database.sql.SQLUUIDDatabase;

import java.sql.Connection;
import java.sql.Statement;


public class UUIDSQLiteDatabase extends SQLUUIDDatabase {

	@Override
	public void setup(Connection con) {
		try {
			Statement statement = con.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS `bank_uuids` (`username` VARCHAR(32) NOT NULL, `uuid` VARCHAR(36) NOT NULL, PRIMARY KEY ( `username` ));");
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.setup(con);
	}

}
