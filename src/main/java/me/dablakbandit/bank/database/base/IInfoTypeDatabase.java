package me.dablakbandit.bank.database.base;

import java.util.Map;
import java.util.Set;

import me.dablakbandit.core.database.listener.SQLListener;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;

public abstract class IInfoTypeDatabase<T extends JSONInfo>extends SQLListener{
	
	protected final IInfoDatabase	infoDatabase;
	protected final Class<T>		typeClass;
	protected final String			database;
	
	public IInfoTypeDatabase(IInfoDatabase infoDatabase, Class<T> typeClass, String database){
		this.infoDatabase = infoDatabase;
		this.typeClass = typeClass;
		this.database = database;
	}
	
	public abstract void savePlayer(CorePlayers pl, T t, long time);
	
	public abstract boolean loadPlayer(CorePlayers pl, T t);
	
	public abstract Map<String, T> getModified(long since);
	
	public abstract Map<String, Long> getOffline();
	
	public abstract Set<String> getDistinctUUIDS();
	
	public abstract int expire(long time);
	
	public abstract boolean playerExists(String uuid);
}
