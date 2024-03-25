package me.dablakbandit.bank.save.auto;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.players.CorePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class AutoSaveManager{
	
	private static final AutoSaveManager instance = new AutoSaveManager();
	
	public static AutoSaveManager getInstance(){
		return instance;
	}

	private BukkitTask task = null;
	
	private AutoSaveManager(){
		
	}
	
	public void enable(){
		if(!BankPluginConfiguration.BANK_SAVE_AUTO_ENABLED.get()){ return; }
		int time = BankPluginConfiguration.BANK_SAVE_AUTO_TIMER.get() * 20;
		task = Bukkit.getScheduler().runTaskTimer(BankPlugin.getInstance(), this::saveAll, time, time);
	}
	
	private void saveAll(){
		CorePlayerManager.getInstance().getPlayers().values().forEach(pl -> LoaderManager.getInstance().save(pl, false));
	}
	
	public void disable(){
		if (task != null && !task.isCancelled()) {
			task.cancel();
		}
		task = null;
	}
}
