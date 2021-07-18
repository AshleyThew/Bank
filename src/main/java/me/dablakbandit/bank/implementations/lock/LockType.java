package me.dablakbandit.bank.implementations.lock;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.players.CorePlayerManager;

public class LockType extends BankImplementation{
	
	private static final LockType citizensType = new LockType();
	
	public static LockType getInstance(){
		return citizensType;
	}
	
	private LockType(){
		
	}
	
	@Override
	public void load(){
		
	}
	
	private BukkitTask bukkitTask;
	
	@Override
	public void enable(){
		int time = 20 * 60;
		bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(BankPlugin.getInstance(), this::setOnlineLocked, time, time);
	}
	
	@Override
	public void disable(){
		if(bukkitTask != null){
			bukkitTask.cancel();
			bukkitTask = null;
		}
	}
	
	private void setOnlineLocked(){
		for(BankInfo bankInfo : CorePlayerManager.getInstance().getInfo(BankInfo.class)){
			if(!bankInfo.isLocked(false)){
				BankDatabaseManager.getInstance().getPlayerLockDatabase().setLocked(bankInfo.getPlayers(), true);
			}
		}
	}
}
