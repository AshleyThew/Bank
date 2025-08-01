package me.dablakbandit.bank.database.base;

import me.dablakbandit.core.players.info.JSONInfo;

import java.util.List;

public interface IInfoDatabase {
	<T extends JSONInfo> IInfoTypeDatabase<T> getInfoTypeDatabase(T t);

	<T extends JSONInfo> IInfoTypeDatabase<T> getInfoTypeDatabase(Class<T> typeClass);

	boolean playerExists(String uuid);

	int expire(long time);

	IUUIDDatabase getUUIDDatabase();

	IPlayerLockDatabase getPlayerLockDatabase();

	ITransactionDatabase getMoneyTransactionDatabase();

	ITransactionDatabase getExpTransactionDatabase();

	IChequeDatabase getChequeDatabase();

	List<String> getDistinctUUIDS();

	void fixMissingUsernames();

	void ensureConnection();
}
