package me.dablakbandit.bank.database.sql;

import me.dablakbandit.bank.database.base.ITransactionDatabase;
import me.dablakbandit.core.database.listener.SQLListener;

import java.sql.Connection;
import java.sql.PreparedStatement;

public abstract class SQLTransactionDatabase extends SQLListener implements ITransactionDatabase {
	private PreparedStatement insertTransaction;
	private final String tableName;

	public SQLTransactionDatabase(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public void setup(Connection con) {
		try {
			con.createStatement().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, uuid VARCHAR(36), type VARCHAR(16), amount DOUBLE, description VARCHAR(255), extra VARCHAR(255), timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
			insertTransaction = con.prepareStatement("INSERT INTO " + tableName + " (uuid, type, amount, description, extra) VALUES (?, ?, ?, ?, ?)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertTransaction(String uuid, String type, double amount, String description, String extra) {
		try {
			synchronized (insertTransaction) {
				insertTransaction.setString(1, uuid);
				insertTransaction.setString(2, type);
				insertTransaction.setDouble(3, amount);
				insertTransaction.setString(4, description);
				insertTransaction.setString(5, extra);
				insertTransaction.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close(Connection connection) {
		closeStatements();
	}
}
