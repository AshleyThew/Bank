package me.dablakbandit.bank.database.base;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.*;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.database.listener.SQLListener;
import me.dablakbandit.core.json.JSONArray;
import me.dablakbandit.core.json.JSONException;
import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.players.info.JSONInfo;

public abstract class IInfoDatabase extends SQLListener{
	
	protected final Set<String>							infoTypeDatabaseSet		= new HashSet<>();
	protected final Map<Class<?>, IInfoTypeDatabase<?>>	infoTypeDatabasesMap	= new HashMap<>();
	
	public abstract <T extends JSONInfo> IInfoTypeDatabase<T> getInfoTypeDatabase(T t);
	
	public abstract <T extends JSONInfo> IInfoTypeDatabase<T> getInfoTypeDatabase(Class<T> typeClass);
	
	public abstract boolean playerExists(String uuid);
	
	public abstract boolean columnExists(Connection connection, String table, String column);
	
	public abstract boolean tableExists(Connection connection, String table);
	
	public abstract int expire(long time);
	
	public abstract IUUIDDatabase getUUIDDatabase();
	
	public abstract PlayerLockDatabase getPlayerLockDatabase();
	
	public abstract List<String> getDistinctUUIDS();
	
	public void fixMissingUsernames(){
		getUUIDDatabase().ensureConnection();
		List<String> uuids = getDistinctUUIDS();
		int totalCount = uuids.size();
		int current = 0;
		
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Finding " + uuids.size() + " usernames");
		IUUIDDatabase uuidDatabase = getUUIDDatabase();
		for(String uuid : uuids){
			try{
				String name = uuidDatabase.getUsername(uuid);
				if(name != null){
					BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Found " + (++current) + "/" + totalCount);
					continue;
				}
				JSONArray ja = readJsonArrayFromUrl("https://api.mojang.com/user/profiles/" + uuid.replaceAll("-", "") + "/names");
				if(ja.length() == 0 || !ja.getJSONObject(0).has("name")){
					BankLog.error("Unable to find username for " + uuid);
					continue;
				}
				JSONObject jo = ja.getJSONObject(0);
				name = jo.getString("name");
				uuidDatabase.saveUUID(uuid, name);
				BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Found " + (++current) + "/" + totalCount);
			}catch(IOException ie){
				BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Mojang limit hit, please try again in 10 minutes");
				return;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Finished fixing usernames");
	}
	
	public JSONArray readJsonArrayFromUrl(String url) throws Exception{
		try{
			return new JSONArray(readStringFromURL(url));
		}catch(JSONException e){
			return new JSONArray();
		}
	}
	
	public String readStringFromURL(String url) throws Exception{
		InputStream is = new URL(url).openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		String text = readAll(rd);
		is.close();
		return text;
	}
	
	public String readAll(Reader rd) throws Exception{
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp = rd.read()) != -1){
			if(cp != 65279){
				sb.append((char)cp);
			}
		}
		return sb.toString();
	}
	
}
