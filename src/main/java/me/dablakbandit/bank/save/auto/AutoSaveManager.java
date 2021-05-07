package me.dablakbandit.bank.save.auto;

import org.bukkit.Bukkit;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.players.CorePlayerManager;

public class AutoSaveManager{
	
	private static AutoSaveManager instance = new AutoSaveManager();
	
	public static AutoSaveManager getInstance(){
		return instance;
	}
	
	private int task_id = -1;
	
	private AutoSaveManager(){
		
	}
	
	public void enable(){
		if(!BankPluginConfiguration.BANK_SAVE_AUTO_ENABLED.get()){ return; }
		int time = BankPluginConfiguration.BANK_SAVE_AUTO_TIMER.get() * 20;
		task_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(BankPlugin.getInstance(), this::saveAll, time, time);
	}
	
	private void saveAll(){
		CorePlayerManager.getInstance().getPlayers().values().forEach(pl -> {
			LoaderManager.getInstance().save(pl, false);
		});
	}
	
	public void disable(){
		Bukkit.getScheduler().cancelTask(task_id);
		task_id = -1;
	}
}
