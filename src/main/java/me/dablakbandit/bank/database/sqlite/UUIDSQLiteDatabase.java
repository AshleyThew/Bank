package me.dablakbandit.bank.database.sqlite;

import java.sql.Connection;

import me.dablakbandit.bank.database.base.IUUIDDatabase;

public class UUIDSQLiteDatabase extends IUUIDDatabase{
	
	@Override
	public void setup(Connection con){
		try{
			con.createStatement().execute("CREATE TABLE IF NOT EXISTS `bank_uuids` (`username` VARCHAR(32) NOT NULL, `uuid` VARCHAR(36) NOT NULL, PRIMARY KEY ( `username` ));");
		}catch(Exception e){
			e.printStackTrace();
		}
		super.setup(con);
	}
	
}
