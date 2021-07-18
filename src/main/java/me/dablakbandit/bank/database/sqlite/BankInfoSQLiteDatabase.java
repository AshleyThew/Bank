package me.dablakbandit.bank.database.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.database.base.IInfoTypeDatabase;
import me.dablakbandit.bank.database.base.IUUIDDatabase;
import me.dablakbandit.bank.database.base.PlayerLockDatabase;
import me.dablakbandit.core.players.info.JSONInfo;

public class BankInfoSQLiteDatabase extends IInfoDatabase{
	
	public BankInfoSQLiteDatabase(){
		
	}
	
	@Override
	public void setup(Connection connection){
		
	}
	
	@Override
	public void close(Connection connection){
		closeStatements();
	}
	
	private static final PlayerLockDatabase	playerLockDatabase	= new PlayerLockDatabase();
	private static final UUIDSQLiteDatabase	uuidsqLiteDatabase	= new UUIDSQLiteDatabase();
	
	@Override
	public boolean columnExists(Connection connection, String db, String column){
		List<String> columns = new ArrayList<>();
		try{
			ResultSet rs = connection.prepareStatement("PRAGMA table_info(`" + db + "`);").executeQuery();
			while(rs.next()){
				columns.add(rs.getString(2));
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return columns.contains(column);
	}
	
	@Override
	public boolean tableExists(Connection connection, String table){
		boolean exists = false;
		try{
			ResultSet rs = connection.prepareStatement("PRAGMA table_info(`" + table + "`);").executeQuery();
			exists = rs.next();
			rs.close();
		}catch(Exception ignored){
		}
		return exists;
	}
	
	@Override
	public <T extends JSONInfo> IInfoTypeDatabase<T> getInfoTypeDatabase(T t){
		return (IInfoTypeDatabase<T>)getInfoTypeDatabase(t.getClass());
	}
	
	@Override
	public <T extends JSONInfo> IInfoTypeDatabase<T> getInfoTypeDatabase(Class<T> typeClass){
		IInfoTypeDatabase infoTypeDatabase = infoTypeDatabasesMap.get(typeClass);
		if(infoTypeDatabase == null){
			String database = "bank_player_info_" + typeClass.getSimpleName().toLowerCase();
			boolean add = infoTypeDatabaseSet.add(database);
			if(add){
				infoTypeDatabase = new BankInfoTypeSQLiteDatabase(this, typeClass, database);
				getDatabase().addListener(infoTypeDatabase);
				infoTypeDatabasesMap.put(typeClass, infoTypeDatabase);
			}
		}
		return (IInfoTypeDatabase<T>)infoTypeDatabase;
	}
	
	@Override
	public int expire(long time){
		try{
			int expired = 0;
			for(Map.Entry<Class<?>, IInfoTypeDatabase> databaseEntry : infoTypeDatabasesMap.entrySet()){
				expired = Math.max(expired, databaseEntry.getValue().expire(time));
			}
			return expired;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public void appendColumn(Connection connection, String db, String type){
		try{
			connection.prepareStatement("ALTER TABLE `" + db + "` ADD " + type + ";").execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean playerExists(String uuid){
		boolean exists = false;
		try{
			for(Map.Entry<Class<?>, IInfoTypeDatabase> typeDatabaseEntry : infoTypeDatabasesMap.entrySet()){
				exists |= typeDatabaseEntry.getValue().playerExists(uuid);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return exists;
	}
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public IUUIDDatabase getUUIDDatabase(){
		return uuidsqLiteDatabase;
	}
	
	@Override
	public PlayerLockDatabase getPlayerLockDatabase(){
		return playerLockDatabase;
	}
	
	@Override
	public List<String> getDistinctUUIDS(){
		Set<String> uuids = new HashSet();
		try{
			for(Map.Entry<Class<?>, IInfoTypeDatabase> typeDatabaseEntry : infoTypeDatabasesMap.entrySet()){
				uuids.addAll(typeDatabaseEntry.getValue().getDistinctUUIDS());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList(uuids);
	}
}
