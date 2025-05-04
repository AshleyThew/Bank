/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.bank.database.base;

import me.dablakbandit.core.players.CorePlayers;

import java.util.List;

public interface IPlayerLockDatabase {


	boolean isLocked(String uuid);

	boolean isLocked(CorePlayers pl, boolean set);

	void setLocked(CorePlayers pl, boolean locked);

	List<String> getUnlocked();

	void ensureConnection();
}
