package me.dablakbandit.bank.database.sqlite;

import me.dablakbandit.bank.database.base.IInfoTypeDatabase;
import me.dablakbandit.bank.database.sql.*;
import me.dablakbandit.bank.database.sql.transaction.ExpTransactionDatabase;
import me.dablakbandit.bank.database.sql.transaction.MoneyTransactionDatabase;
import me.dablakbandit.core.players.info.JSONInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankInfoSQLiteDatabase extends SQLInfoDatabase {

	public BankInfoSQLiteDatabase() {

	}

	@Override
	public void setup(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			statement.execute("PRAGMA journal_mode=WAL;");
			statement.execute("PRAGMA busy_timeout=30000;");
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close(Connection connection) {
		closeStatements();
	}

	private static final SQLPlayerLockDatabase playerLockDatabase = new SQLPlayerLockDatabase();
	private static final UUIDSQLiteDatabase uuidsqLiteDatabase = new UUIDSQLiteDatabase();
	private static final MoneyTransactionDatabase moneyTransactionDatabase = new MoneyTransactionDatabase();
	private static final ExpTransactionDatabase expTransactionDatabase = new ExpTransactionDatabase();
	private static final ChequeSQLiteDatabase chequeSQLiteDatabase = new ChequeSQLiteDatabase();

	@Override
	public boolean columnExists(Connection connection, String db, String column) {
		boolean exists = false;
		try {
			ResultSet rs = connection.getMetaData().getColumns(null, null, db, column);
			exists = rs.next();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exists;
	}

	@Override
	public boolean tableExists(Connection connection, String table) {
		boolean exists = false;
		try {
			ResultSet rsTables = connection.getMetaData().getTables(null, null, table, null);
			exists = rsTables.next();
			rsTables.close();
		} catch (Throwable ignored) {
			ignored.printStackTrace();
		}
		return exists;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends JSONInfo> IInfoTypeDatabase<T> getInfoTypeDatabase(T t) {
		return (IInfoTypeDatabase<T>) getInfoTypeDatabase(t.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T extends JSONInfo> IInfoTypeDatabase<T> getInfoTypeDatabase(Class<T> typeClass) {
		SQLInfoTypeDatabase<?> infoTypeDatabase = infoTypeDatabasesMap.get(typeClass);
		if (infoTypeDatabase == null) {
			String database = "bank_player_info_" + typeClass.getSimpleName().toLowerCase();
			if (infoTypeDatabaseSet.add(database)) {
				infoTypeDatabase = new BankInfoTypeSQLiteDatabase<>(this, typeClass, database);
				getDatabase().addListener(infoTypeDatabase);
				infoTypeDatabasesMap.put(typeClass, infoTypeDatabase);
			}
		}
		return (IInfoTypeDatabase<T>) infoTypeDatabase;
	}

	@Override
	public int expire(long time) {
		try {
			int expired = 0;
			for (Map.Entry<Class<?>, SQLInfoTypeDatabase<?>> databaseEntry : infoTypeDatabasesMap.entrySet()) {
				expired = Math.max(expired, databaseEntry.getValue().expire(time));
			}
			return expired;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void appendColumn(Connection connection, String db, String type) {
		try {
			connection.prepareStatement("ALTER TABLE `" + db + "` ADD " + type + ";").execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean playerExists(String uuid) {
		boolean exists = false;
		try {
			for (Map.Entry<Class<?>, SQLInfoTypeDatabase<?>> typeDatabaseEntry : infoTypeDatabasesMap.entrySet()) {
				exists |= typeDatabaseEntry.getValue().playerExists(uuid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exists;
	}

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public SQLUUIDDatabase getUUIDDatabase() {
		return uuidsqLiteDatabase;
	}

	@Override
	public SQLPlayerLockDatabase getPlayerLockDatabase() {
		return playerLockDatabase;
	}

	@Override
	public SQLTransactionDatabase getMoneyTransactionDatabase() {
		return moneyTransactionDatabase;
	}

	@Override
	public SQLTransactionDatabase getExpTransactionDatabase() {
		return expTransactionDatabase;
	}

	@Override
	public SQLChequeDatabase getChequeDatabase() {
		return chequeSQLiteDatabase;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDistinctUUIDS() {
		Set<String> uuids = new HashSet<>();
		try {
			for (Map.Entry<Class<?>, SQLInfoTypeDatabase<?>> typeDatabaseEntry : infoTypeDatabasesMap.entrySet()) {
				uuids.addAll(typeDatabaseEntry.getValue().getDistinctUUIDS());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList(uuids);
	}
}
