package me.dablakbandit.bank.save.loader.runner;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.event.BankPlayersLoadedEvent;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.IBankInfo;
import me.dablakbandit.bank.player.info.PermissionsInfo;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;

public class LoadRunner implements Runnable{
	
	private static final BankDatabaseManager	bankDatabaseManager	= BankDatabaseManager.getInstance();
	
	private final CorePlayers					pl;
	private boolean								force, lock = true, log = true;
	private Runnable							runnable;
	
	public LoadRunner(CorePlayers pl){
		this.pl = pl;
	}
	
	public LoadRunner(CorePlayers pl, boolean force){
		this.pl = pl;
		this.force = force;
	}
	
	public LoadRunner(CorePlayers pl, boolean force, Runnable runnable){
		this.pl = pl;
		this.force = force;
		this.runnable = runnable;
	}
	
	public LoadRunner log(boolean log){
		this.log = log;
		return this;
	}
	
	public LoadRunner lock(boolean lock){
		this.lock = lock;
		return this;
	}
	
	@Override
	public void run(){
		if(log){
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLAYER_LEVEL, "Loading " + pl.getUUIDString());
		}
		IInfoDatabase infoDatabase = bankDatabaseManager.getInfoDatabase();
		infoDatabase.ensureConnection();
		infoDatabase.getPlayerLockDatabase().ensureConnection();
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		if(bankInfo == null){ return; }
		if(!bankInfo.isLocked(false)){ return; }
		if(!force && infoDatabase.getPlayerLockDatabase().isLocked(pl, true)){ return; }
		long start = System.currentTimeMillis();
		if(pl.getName() != null){
			infoDatabase.getUUIDDatabase().saveUUID(pl.getUUIDString(), pl.getName());
		}
		List<JSONInfo> infoList = pl.getAllInfo().stream().filter(info -> (info instanceof IBankInfo && info instanceof JSONInfo)).map(info -> (JSONInfo)info).collect(Collectors.toList());
		infoList.forEach(info -> {
			infoDatabase.getInfoTypeDatabase(info).loadPlayer(pl, info);
			info.jsonInit();
			if(info instanceof PermissionsInfo){
				Bukkit.getScheduler().runTask(BankPlugin.getInstance(), ((PermissionsInfo)info)::checkPermissions);
			}
		});
		bankInfo.setLocked(false);
		if(lock){
			infoDatabase.getPlayerLockDatabase().setLocked(pl, true);
		}
		if(force){
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_BANK_UNLOCKED.get());
		}
		BankPlayersLoadedEvent event = new BankPlayersLoadedEvent(pl, pl.getPlayer());
		Bukkit.getPluginManager().callEvent(event);
		if(log){
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLAYER_LEVEL, "Loaded " + pl.getUUIDString() + " after " + (System.currentTimeMillis() - start) + "ms");
		}
		if(runnable != null){
			Bukkit.getScheduler().runTask(BankPlugin.getInstance(), runnable);
		}
	}
}
