package me.dablakbandit.bank.database.base;

public interface IUUIDDatabase {

	void saveUUID(String uuid, String username);

	String getUsername(String uuid);

	String getUUID(String username);

	void ensureConnection();
}
