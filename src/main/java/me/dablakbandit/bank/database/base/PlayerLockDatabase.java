/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.bank.database.base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.core.database.listener.SQLListener;
import me.dablakbandit.core.players.CorePlayers;

public class PlayerLockDatabase extends SQLListener{
	
	public PlayerLockDatabase(){
		
	}
	
	private static PreparedStatement	isLocked, addLocked, setLocked, getUnlocked;
	private static PreparedStatement	isTimeLocked, addTimeLocked, setTimeLocked, getTimeUnlocked;
	
	@Override
	public void setup(Connection c){
		try{
			Statement statement = c.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS `bank_player_lock` ( `uuid` VARCHAR(36) NOT NULL, `locked` BOOLEAN NOT NULL, PRIMARY KEY(`uuid`));");

			statement.execute("CREATE TABLE IF NOT EXISTS `bank_player_time_lock` ( `uuid` VARCHAR(36) NOT NULL, `locked` TIMESTAMP NOT NULL, PRIMARY KEY(`uuid`));");
			
			isLocked = c.prepareStatement("SELECT * FROM `bank_player_lock` WHERE `uuid` = ?;");
			setLocked = c.prepareStatement("UPDATE `bank_player_lock` SET `locked` = ? WHERE `uuid`=?;");
			addLocked = c.prepareStatement("INSERT INTO `bank_player_lock` (`uuid`, `locked`) VALUES (?,?);");
			getUnlocked = c.prepareStatement("SELECT * FROM `bank_player_lock` WHERE `locked`=0;");
			
			isTimeLocked = c.prepareStatement("SELECT * FROM `bank_player_time_lock` WHERE `uuid` = ?;");
			setTimeLocked = c.prepareStatement("UPDATE `bank_player_time_lock` SET `locked` = ? WHERE `uuid`=?;");
			addTimeLocked = c.prepareStatement("INSERT INTO `bank_player_time_lock` (`uuid`, `locked`) VALUES (?,?);");
			getTimeUnlocked = c.prepareStatement("SELECT * FROM `bank_player_time_lock` WHERE `locked` < ?;");

			statement.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized boolean isLocked(String uuid){
		if(BankPluginConfiguration.BANK_SAVE_LOCK_TIME.get()){
			return isTimeLocked(uuid);
		}else{
			return isStandardLocked(uuid);
		}
	}
	
	protected boolean isStandardLocked(String uuid){
		boolean ret = true;
		try{
			synchronized(isLocked){
				isLocked.setString(1, uuid);
				ResultSet rs = isLocked.executeQuery();
				if (rs.next()) {
					ret = rs.getBoolean("locked");
				}
				rs.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	protected boolean isTimeLocked(String uuid){
		boolean ret = true;
		try{
			synchronized(isTimeLocked){
				isTimeLocked.setString(1, uuid);
				ResultSet rs = isTimeLocked.executeQuery();
				if (rs.next()) {
					ret = rs.getTimestamp("locked").getTime() > System.currentTimeMillis();
				}
				rs.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public synchronized boolean isLocked(CorePlayers pl, boolean set){
		if(BankPluginConfiguration.BANK_SAVE_LOCK_TIME.get()){
			return isTimeLocked(pl, set);
		}else{
			return isStandardLocked(pl, set);
		}
	}
	
	protected boolean isStandardLocked(CorePlayers pl, boolean set){
		boolean ret = true;
		String uuid = pl.getUUIDString();
		try{
			ResultSet rs;
			synchronized(isLocked){
				isLocked.setString(1, uuid);
				rs = isLocked.executeQuery();
			}
			if(rs.next()){
				ret = rs.getBoolean("locked");
			}else{
				synchronized(addLocked){
					addLocked.setString(1, uuid);
					addLocked.setBoolean(2, set);
					addLocked.execute();
				}
				ret = false;
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	protected synchronized boolean isTimeLocked(CorePlayers pl, boolean set){
		boolean ret = true;
		String uuid = pl.getUUIDString();
		try{
			isTimeLocked.setString(1, uuid);
			ResultSet rs = isTimeLocked.executeQuery();
			if(rs.next()){
				ret = rs.getTimestamp("locked").getTime() > System.currentTimeMillis();
			}else{
				addTimeLocked.setString(1, uuid);
				addTimeLocked.setTimestamp(2, new Timestamp(System.currentTimeMillis() + 600000));
				addTimeLocked.execute();
				ret = false;
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public synchronized void setLocked(CorePlayers pl, boolean locked){
		if(BankPluginConfiguration.BANK_SAVE_LOCK_TIME.get()){
			setTimeLocked(pl, locked);
		}else{
			setStandardLocked(pl, locked);
		}
	}
	
	protected synchronized void setStandardLocked(CorePlayers pl, boolean locked){
		try{
			setLocked.setBoolean(1, locked);
			setLocked.setString(2, pl.getUUIDString());
			setLocked.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected synchronized void setTimeLocked(CorePlayers pl, boolean locked){
		try{
			long timestamp = System.currentTimeMillis() + (locked ? 600000 : 0);
			setTimeLocked.setTimestamp(1, new Timestamp(timestamp));
			setTimeLocked.setString(2, pl.getUUIDString());
			setTimeLocked.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized List<String> getUnlocked(){
		if(BankPluginConfiguration.BANK_SAVE_LOCK_TIME.get()){
			return getTimeUnlocked();
		}else{
			return getStandardUnlocked();
		}
	}
	
	protected synchronized List<String> getStandardUnlocked(){
		List<String> locked = new ArrayList<>();
		try{
			ResultSet rs = getUnlocked.executeQuery();
			while(rs.next()){
				locked.add(rs.getString(1));
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return locked;
	}
	
	protected synchronized List<String> getTimeUnlocked(){
		List<String> locked = new ArrayList<>();
		try{
			ResultSet rs = getTimeUnlocked.executeQuery();
			getTimeUnlocked.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			while(rs.next()){
				locked.add(rs.getString(1));
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return locked;
	}
	
	@Override
	public void close(Connection connection){
		closeStatements();
	}
	
}
