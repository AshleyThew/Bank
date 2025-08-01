package me.dablakbandit.bank.player.converter.old.mysql;

import me.dablakbandit.bank.player.converter.old.base.SQLBaseLoader;

import java.sql.Connection;

public class OldMySQLLoader extends SQLBaseLoader {

	private static final OldMySQLLoader loader = new OldMySQLLoader();

	public static OldMySQLLoader getInstance() {
		return loader;
	}

	private OldMySQLLoader() {

	}

	public boolean isConnected() {
		return database.isConnected();
	}

	@Override
	public void close(Connection connection) {
		super.close(connection);
		closeStatements();
	}

}
