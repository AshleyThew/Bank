package me.dablakbandit.bank.upgrade.infosave.database;

import me.dablakbandit.bank.database.base.IUUIDDatabase;
import me.dablakbandit.core.database.listener.SQLListener;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;

import java.util.List;

public abstract class IUpgradeInfoDatabase extends SQLListener {

	public abstract <T extends JSONInfo> boolean loadPlayer(CorePlayers pl, T t);

	public abstract IUpgradeBankInfoTypeDatabase getTypeDatabase();

	public abstract IUUIDDatabase getUUIDDatabase();

	public abstract List<String> getDistinctUUIDS();

}
