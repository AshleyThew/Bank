package me.dablakbandit.bank.upgrade.infosave.database.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import me.dablakbandit.bank.database.base.IUUIDDatabase;
import me.dablakbandit.bank.database.base.PlayerLockDatabase;
import me.dablakbandit.bank.database.sqlite.UUIDSQLiteDatabase;
import me.dablakbandit.bank.upgrade.infosave.database.IUpgradeInfoDatabase;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.json.JSONParser;

public class UpgradeBankInfoSQLiteDatabase extends IUpgradeInfoDatabase{
	
	private static UpgradeBankInfoSQLiteDatabase database = new UpgradeBankInfoSQLiteDatabase();
	
	public static UpgradeBankInfoSQLiteDatabase getInstance(){
		return database;
	}
	
	private UpgradeBankInfoSQLiteDatabase(){
		
	}
	
	private static PreparedStatement					getPlayerInfo;
	private static PreparedStatement					getDistinctUUIDS;
	
	private static PlayerLockDatabase					playerLockDatabase	= new PlayerLockDatabase();
	private static UpgradeBankInfoTypeSQLiteDatabase	cache				= new UpgradeBankInfoTypeSQLiteDatabase();
	private static UUIDSQLiteDatabase					uuidsqLiteDatabase	= new UUIDSQLiteDatabase();
	
	@Override
	public void setup(Connection con){
		try{
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `bank_player_info`( `uuid` VARCHAR(36) NOT NULL, `info_id` INT NOT NULL, `value` LONGTEXT NOT NULL, `last_modified` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`uuid`, `info_id`));").execute();
			getPlayerInfo = con.prepareStatement("SELECT * FROM `bank_player_info` WHERE `uuid` = ? AND `info_id` = ?;");
			getDistinctUUIDS = con.prepareStatement("SELECT DISTINCT(`uuid`) FROM `bank_player_info`;");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public <T extends JSONInfo> boolean loadPlayer(CorePlayers pl, T t){
		boolean has = false;
		try{
			int info_id = cache.getInfo(t.getClass());
			synchronized(getPlayerInfo){
				getPlayerInfo.setString(1, pl.getUUIDString());
				getPlayerInfo.setInt(2, info_id);
				ResultSet rs = getPlayerInfo.executeQuery();
				if(has = rs.next()){
					JSONParser.loadAndCopy(t, rs.getString("value"));
				}
				rs.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return has;
	}
	
	@Override
	public UpgradeBankInfoTypeSQLiteDatabase getTypeDatabase(){
		return cache;
	}
	
	@Override
	public IUUIDDatabase getUUIDDatabase(){
		return uuidsqLiteDatabase;
	}
	
	@Override
	public List<String> getDistinctUUIDS(){
		List<String> uuids = new ArrayList<>();
		try{
			synchronized(getDistinctUUIDS){
				ResultSet rs = getDistinctUUIDS.executeQuery();
				while(rs.next()){
					uuids.add(rs.getString(1));
				}
				rs.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return uuids;
	}
	
	@Override
	public void close(Connection connection){
		closeStatements();
	}
}
