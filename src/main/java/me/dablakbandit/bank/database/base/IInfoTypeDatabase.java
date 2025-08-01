package me.dablakbandit.bank.database.base;

import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;

import java.util.Map;
import java.util.Set;

public interface IInfoTypeDatabase<T extends JSONInfo> {
	void savePlayer(CorePlayers pl, T t, long time);

	boolean loadPlayer(CorePlayers pl, T t);

	Map<String, T> getModified(long since);

	Map<String, Long> getOffline();

	Set<String> getDistinctUUIDS();

	int expire(long time);

	boolean playerExists(String uuid);
}
