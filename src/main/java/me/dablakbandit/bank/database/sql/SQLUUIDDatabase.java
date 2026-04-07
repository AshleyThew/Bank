package me.dablakbandit.bank.database.sql;

import me.dablakbandit.bank.database.base.IUUIDDatabase;
import me.dablakbandit.core.database.listener.SQLListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLUUIDDatabase extends SQLListener implements IUUIDDatabase {
	private static final int SQLITE_BUSY_RETRIES = 5;
	private static final long SQLITE_BUSY_RETRY_DELAY_MS = 25L;

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
				executeWithBusyRetry(delete_uuid);
			}
			synchronized (delete_name) {
				delete_name.setString(1, username);
				executeWithBusyRetry(delete_name);
			}
			synchronized (savename) {
				savename.setString(1, username);
				savename.setString(2, uuid);
				executeWithBusyRetry(savename);
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

	private void executeWithBusyRetry(PreparedStatement statement) throws SQLException {
		for (int i = 0; i <= SQLITE_BUSY_RETRIES; i++) {
			try {
				statement.execute();
				return;
			} catch (SQLException e) {
				if (!isSqliteBusy(e) || i == SQLITE_BUSY_RETRIES) {
					throw e;
				}
				try {
					Thread.sleep(SQLITE_BUSY_RETRY_DELAY_MS * (i + 1));
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new SQLException("Interrupted while retrying sqlite busy uuid update", ie);
				}
			}
		}
	}

	private boolean isSqliteBusy(SQLException e) {
		return e.getMessage() != null && e.getMessage().contains("SQLITE_BUSY");
	}
}
