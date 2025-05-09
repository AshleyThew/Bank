package me.dablakbandit.bank.database.sqlite;

import me.dablakbandit.bank.database.sql.SQLInfoDatabase;
import me.dablakbandit.bank.database.sql.SQLInfoTypeDatabase;
import me.dablakbandit.bank.player.info.BankDefaultInfo;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.json.JSONParser;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class BankInfoTypeSQLiteDatabase<T extends JSONInfo> extends SQLInfoTypeDatabase<T> {

	private PreparedStatement getPlayerInfo, insertPlayerInfo, updatePlayerInfo, expire;
	private PreparedStatement getModified, getDistinctUUIDS;

	public BankInfoTypeSQLiteDatabase(SQLInfoDatabase infoDatabase, Class<T> typeClass, String database) {
		super(infoDatabase, typeClass, database);
	}

	@Override
	public void setup(Connection con) {
		try {
			Statement statement = con.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`( `uuid` VARCHAR(36) NOT NULL, `value` LONGTEXT NOT NULL, `last_modified` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`uuid`));");
			getPlayerInfo = con.prepareStatement("SELECT * FROM `" + database + "` WHERE `uuid` = ?;");
			insertPlayerInfo = con.prepareStatement("INSERT INTO `" + database + "` (`uuid`, `value`) VALUES (?,?);");
			updatePlayerInfo = con.prepareStatement("UPDATE `" + database + "` SET `value` = ?, `last_modified` = ? WHERE `uuid` = ?;");
			expire = con.prepareStatement("DELETE FROM `" + database + "` WHERE `last_modified` < ?;");
			getModified = con.prepareStatement("SELECT * FROM `" + database + "` WHERE `last_modified` > ?;");
			getDistinctUUIDS = con.prepareStatement("SELECT DISTINCT(`uuid`) FROM `" + database + "`;");

			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close(Connection connection) {
		closeStatements();
	}

	@Override
	public void savePlayer(CorePlayers pl, T t, long time) {
		try {
			String value = t.toJson().toString();
			boolean exists;
			synchronized (getPlayerInfo) {
				getPlayerInfo.setString(1, pl.getUUIDString());
				ResultSet rs = getPlayerInfo.executeQuery();
				exists = rs.next();
				rs.close();
			}
			if (exists) {
				synchronized (updatePlayerInfo) {
					updatePlayerInfo.setString(1, value);
					updatePlayerInfo.setTimestamp(2, new Timestamp(new Date().getTime()));
					updatePlayerInfo.setString(3, pl.getUUIDString());
					updatePlayerInfo.execute();
				}
			} else {
				synchronized (insertPlayerInfo) {
					insertPlayerInfo.setString(1, pl.getUUIDString());
					insertPlayerInfo.setString(2, value);
					insertPlayerInfo.execute();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean loadPlayer(CorePlayers pl, T t) {
		boolean has = false;
		try {
			synchronized (getPlayerInfo) {
				getPlayerInfo.setString(1, pl.getUUIDString());
				ResultSet rs = getPlayerInfo.executeQuery();
				has = rs.next();
				if (has) {
					JSONParser.loadAndCopy(t, rs.getString("value"));
				} else if (t instanceof BankDefaultInfo) {
					((BankDefaultInfo) t).initDefault();
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return has;
	}

	@Override
	public Map<String, T> getModified(long since) {
		Map<String, T> map = new HashMap<>();
		try {
			synchronized (getModified) {
				getModified.setTimestamp(1, new Timestamp(since));
				ResultSet rs = getModified.executeQuery();
				while (rs.next()) {
					map.put(rs.getString("uuid"), JSONParser.fromJSON(rs.getString("value"), typeClass));
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Map<String, Long> getOffline() {
		Map<String, Long> map = new HashMap<>();
		try {
			List<String> offline = infoDatabase.getPlayerLockDatabase().getUnlocked();
			for (String uuid : offline) {
				synchronized (getPlayerInfo) {
					getPlayerInfo.setString(1, uuid);
					ResultSet rs = getPlayerInfo.executeQuery();
					if (rs.next()) {
						long time;
						try {
							time = dateFormat.parse(rs.getString("last_modified")).getTime();
						} catch (Exception e) {
							time = rs.getLong("last_modified");
						}
						map.put(uuid, time);
					}
					rs.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Set<String> getDistinctUUIDS() {
		Set<String> uuids = new HashSet<>();
		try {
			synchronized (getDistinctUUIDS) {
				ResultSet rs = getDistinctUUIDS.executeQuery();
				while (rs.next()) {
					uuids.add(rs.getString(1));
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uuids;
	}

	@Override
	public int expire(long time) {
		try {
			int expired;
			synchronized (expire) {
				expire.setTimestamp(1, new Timestamp(time));
				expired = expire.executeUpdate();
			}
			return expired;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean playerExists(String uuid) {
		boolean exists = false;
		try {
			synchronized (getPlayerInfo) {
				getPlayerInfo.setString(1, uuid);
				ResultSet rs = getPlayerInfo.executeQuery();
				exists = rs.next();
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exists;
	}
}
