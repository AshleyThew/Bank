package me.dablakbandit.bank.upgrade.infosave.database.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import me.dablakbandit.bank.upgrade.infosave.database.IUpgradeBankInfoTypeDatabase;

public class UpgradeBankInfoTypeSQLiteDatabase extends IUpgradeBankInfoTypeDatabase{
	
	private static PreparedStatement get_info, add_info;
	
	@Override
	public void setup(Connection con){
		try{
			con.prepareStatement("CREATE TABLE IF NOT EXISTS `bank_info_type` ( `id` INTEGER PRIMARY KEY, `info` VARCHAR(30) NOT NULL, UNIQUE (`info`));").execute();
			get_info = con.prepareStatement("SELECT * FROM `bank_info_type` WHERE `info` = ?;");
			add_info = con.prepareStatement("INSERT INTO `bank_info_type` (`info`) VALUES (?);");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized int getInfo(Class<?> clazz) throws Exception{
		Integer id = cache.get(clazz);
		if(id != null){ return id; }
		
		String info = clazz.getSimpleName();
		synchronized(get_info){
			get_info.setString(1, info);
			ResultSet rs = get_info.executeQuery();
			if(rs.next()){
				id = rs.getInt("id");
			}
			rs.close();
		}
		if(id == null){
			synchronized(add_info){
				add_info.setString(1, info);
				add_info.execute();
			}
			return getInfo(clazz);
		}
		cache.put(clazz, id);
		return id;
	}
	
	@Override
	public void close(Connection connection){
		closeStatements();
	}
	
}