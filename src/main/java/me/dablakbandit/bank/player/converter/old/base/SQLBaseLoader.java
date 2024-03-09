package me.dablakbandit.bank.player.converter.old.base;

import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.info.*;
import me.dablakbandit.bank.player.info.item.BankItem;
import me.dablakbandit.bank.player.loan.Loan;
import me.dablakbandit.bank.save.loader.runner.SaveRunner;
import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SQLBaseLoader extends Loader{
	
	protected PreparedStatement	getplayers, removeplayers, get, total;
	protected PreparedStatement	list_uuid, list_name, delete_uuid, delete_name, savename;
	protected PreparedStatement	get_player_loans, remove_loans;
	protected Connection		con;
	
	@Override
	public void setup(Connection connection){
		try{
			BankLog.errorAlways("SETTING UP OLD CONNECTION");
			removeplayers = connection.prepareStatement("DELETE FROM `bankplayers` WHERE `uuid` = ?;");
			getplayers = connection.prepareStatement("SELECT * FROM `bankplayers` WHERE `uuid` = ?;");
			get = connection.prepareStatement("SELECT `uuid` FROM `bankplayers` LIMIT 1 OFFSET ?;");
			total = connection.prepareStatement("SELECT COUNT(*) AS `total` FROM `bankplayers`;");
			
			list_uuid = connection.prepareStatement("SELECT * FROM `uuids` WHERE `username` = ?;");
			list_name = connection.prepareStatement("SELECT * FROM `uuids` WHERE `uuid` = ?;");
			
			delete_uuid = connection.prepareStatement("DELETE FROM `uuids` WHERE `uuid` = ?;");
			delete_name = connection.prepareStatement("DELETE FROM `uuids` WHERE `username` = ?;");
			savename = connection.prepareStatement("INSERT INTO `uuids`(`username`, `uuid`) VALUES (?,?);");
			
			get_player_loans = connection.prepareStatement("SELECT * FROM `bank_loans` WHERE `uuid` = ?;");
			remove_loans = connection.prepareStatement("DELETE FROM `bank_loans` WHERE `uuid` = ?;");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getTotal(){
		int t = 0;
		try{
			ensureConnection();
			ResultSet rs = total.executeQuery();
			if(rs.next()){
				t = rs.getInt("total");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return t;
	}
	
	public void delete(String uuid){
		ensureConnection();
		Bukkit.getConsoleSender().sendMessage("Deleting user " + uuid);
		try{
			removeplayers.setString(1, uuid);
			removeplayers.execute();
			remove_loans.setString(1, uuid);
			remove_loans.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void forceLoad(CorePlayers pl){
		load(pl, false);
	}
	
	public void load(CorePlayers pl, boolean check){
		ensureConnection();
		long mili = System.currentTimeMillis();
		try{
			getplayers.setString(1, pl.getUUIDString());
			ResultSet rs = getplayers.executeQuery();
			
			BankItemsInfo bii = pl.getInfo(BankItemsInfo.class);
			BankExpInfo ei = pl.getInfo(BankExpInfo.class);
			BankPinInfo pi = pl.getInfo(BankPinInfo.class);
			BankMoneyInfo mi = pl.getInfo(BankMoneyInfo.class);
			BankLoansInfo li = pl.getInfo(BankLoansInfo.class);
			if(rs.next()){
				bii.setCommandSlots(rs.getInt("command_slots"));
				bii.setPermissionSlots(rs.getInt("permission_slots"));
				mi.setMoney(rs.getDouble("money"));
				pi.setPin(rs.getString("pin"));
				ei.setExp(rs.getDouble("exp"));
				bii.getTabItemMap().putAll(convertJSONToTabs(bii, rs.getString("tabs")));
				for (Map.Entry<Integer, List<BankItem>> entry : convertJSONToItems(pl, rs.getString("items")).entrySet()) {
					bii.getTabBankItems(entry.getKey()).addAll(entry.getValue());
				}
				String bought = rs.getString("bought_slots_map");
				if(bought != null && !bought.equals("0")){
					JSONObject object = new JSONObject(bought);
					for(int i = 0; i < 9; i++){
						if(object.has("" + i)){
							bii.getBoughtSlotsMap().put(i, object.getInt("" + i));
						}
					}
				}
				final double omoney = rs.getDouble("offline_money");
				if(omoney > 0.0){
					mi.addMoney(omoney);
				}
				final double oexp = rs.getDouble("offline_exp");
				if(oexp > 0.0){
					ei.addExp(oexp);
				}
			}
			rs.close();
			get_player_loans.setString(1, pl.getUUIDString());
			rs = get_player_loans.executeQuery();
			while(rs.next()){
				Loan l = new Loan(rs.getDouble("original"), rs.getDouble("amount"), rs.getDouble("interest"), rs.getTimestamp("time").getTime(), rs.getDouble("payback"), rs.getInt("payback_failed"), rs.getTimestamp("deadline").getTime());
				li.getLoans().add(l);
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		pl.getInfo(BankInfo.class).setLocked(false);
	}
	
	@Override
	public void load(CorePlayers pl){
		load(pl, true);
	}
	
	@Override
	public String getUUID(String name){
		String uuid = null;
		try{
			ensureConnection();
			list_uuid.setString(1, name);
			ResultSet rs = list_uuid.executeQuery();
			if(rs.next()){
				uuid = rs.getString("uuid");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return uuid;
	}
	
	@Override
	public String getUsername(String uuid){
		String username = null;
		try{
			ensureConnection();
			list_name.setString(1, uuid);
			ResultSet rs = list_name.executeQuery();
			if(rs.next()){
				username = rs.getString("username");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return username;
	}
	
	@Override
	public void setUsername(String uuid, String name){
		try{
			delete_uuid.setString(1, uuid);
			delete_uuid.execute();
			delete_name.setString(1, name);
			delete_name.execute();
			savename.setString(1, name);
			savename.setString(2, uuid);
			savename.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean exists(String uuid){
		boolean b = false;
		try{
			ensureConnection();
			getplayers.setString(1, uuid);
			ResultSet rs = getplayers.executeQuery();
			b = rs.next();
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}
	
	public void convert(IInfoDatabase type){
		int total = getTotal();
		Bukkit.getConsoleSender().sendMessage("Converting " + total + " banks");
		List<String> uuids = new ArrayList<>();
		for(int current = 0; current < total; current++){
			try{
				get.setInt(1, current);
				ResultSet rs = get.executeQuery();
				if(!rs.next()){
					continue;
				}
				String uuid = rs.getString("uuid");
				Bukkit.getConsoleSender().sendMessage(uuid);
				CorePlayers pl = new CorePlayers(uuid);
				BankInfo bi = new BankInfo(pl);
				pl.addInfo(bi);
				load(pl);
				while(bi.isLocked(false));
				rs.close();
				new SaveRunner(pl, true).run();
				uuids.add(uuid);
				Bukkit.getConsoleSender().sendMessage("Converted " + (current + 1) + "/" + total);
				Thread.sleep(1);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		uuids.forEach(this::delete);
	}
	
	@Override
	public void close(Connection connection){
		closeStatements();
	}
	
}
