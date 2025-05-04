package me.dablakbandit.bank.database.sql;

import me.dablakbandit.bank.database.base.IUUIDDatabase;
import me.dablakbandit.core.database.listener.SQLListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class SQLUUIDDatabase extends SQLListener implements IUUIDDatabase {
	protected PreparedStatement list_uuid, list_name, delete_uuid, delete_name, savename;

	@Override
	public void setup(Connection con) {
		try {
			list_uuid = con.prepareStatement("SELECT * FROM `bank_uuids` WHERE `username` = ?;");
			list_name = con.prepareStatement("SELECT * FROM `bank_uuids` WHERE `uuid` = ?;");
			delete_uuid = con.prepareStatement("DELETE FROM `bank_uuids` WHERE `uuid` = ?;");
			delete_name = con.prepareStatement("DELETE FROM `bank_uuids` WHERE `username` = ?;");
			savename = con.prepareStatement("INSERT INTO `bank_uuids`(`username`, `uuid`) VALUES (?,?);");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close(Connection connection) {
		closeStatements();
	}

	public void saveUUID(String uuid, String username) {
		try {
			synchronized (delete_uuid) {
				delete_uuid.setString(1, uuid);
				delete_uuid.execute();
			}
			synchronized (delete_name) {
				delete_name.setString(1, username);
				delete_name.execute();
			}
			synchronized (savename) {
				savename.setString(1, username);
				savename.setString(2, uuid);
				savename.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUsername(String uuid) {
		String username = null;
		try {
			ensureConnection();
			synchronized (list_name) {
				list_name.setString(1, uuid);
				ResultSet rs = list_name.executeQuery();
				if (rs.next()) {
					username = rs.getString("username");
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return username;
	}

	public String getUUID(String username) {
		String uuid = null;
		try {
			ensureConnection();
			synchronized (list_uuid) {
				list_uuid.setString(1, username);
				ResultSet rs = list_uuid.executeQuery();
				if (rs.next()) {
					uuid = rs.getString("uuid");
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uuid;
	}
}
