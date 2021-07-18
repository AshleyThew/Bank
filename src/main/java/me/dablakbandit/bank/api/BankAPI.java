package me.dablakbandit.bank.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.save.loader.runner.LoadSingleRunner;
import me.dablakbandit.bank.save.loader.runner.SaveRunner;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;

public class BankAPI{
	
	public static final BankAPI api = new BankAPI();
	
	public static BankAPI getInstance(){
		return api;
	}
	
	private static final BankDatabaseManager bankDatabaseManager = BankDatabaseManager.getInstance();
	
	private BankAPI(){
	}
	
	public int getSlots(Player player){
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		BankItemsInfo bif = pl.getInfo(BankItemsInfo.class);
		return bif.getBankSlots(bif.getOpenTab());
	}
	
	public int getSlots(String uuid){
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(uuid);
		if(pl != null){
			BankItemsInfo bif = pl.getInfo(BankItemsInfo.class);
			return bif.getBankSlots(bif.getOpenTab());
		}
		pl = new CorePlayers(uuid);
		return getAndLoad(pl, new BankItemsInfo(pl)).getBankSlots() + BankPluginConfiguration.BANK_ITEMS_SLOTS_DEFAULT.get();
	}
	
	protected <T extends CorePlayersInfo> T getAndLoad(CorePlayers pl, T t){
		pl.addInfo(t);
		new LoadSingleRunner(pl).run();
		return t;
	}
	
	public double getExp(Player player){
		return CorePlayerManager.getInstance().getPlayer(player).getInfo(BankExpInfo.class).getExp();
	}
	
	public double getExp(String uuid){
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(uuid);
		if(pl != null){ return pl.getInfo(BankExpInfo.class).getExp(); }
		pl = new CorePlayers(uuid);
		return getAndLoad(pl, new BankExpInfo(pl)).getExp();
	}
	
	public void setExp(Player player, double amount){
		CorePlayerManager.getInstance().getPlayer(player).getInfo(BankExpInfo.class).setExp(amount);
	}
	
	public void setExp(String uuid, double amount){
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(uuid);
		if(pl != null){
			pl.getInfo(BankExpInfo.class).setExp(amount);
			return;
		}
		pl = new CorePlayers(uuid);
		getAndLoad(pl, new BankExpInfo(pl)).setExp(amount);
		new SaveRunner(pl, false).run();
	}
	
	/*-
	public Map<String, Double> getTopExp(int amount){
		return getExp(0, amount);
	}
	
	public Map<String, Double> getExp(int offset, int amount){
		return BankCoreHandler.getInstance().getSaveType().getLoader().getExp(offset, amount);
	}*/
	
	public double getMoney(Player player){
		return CorePlayerManager.getInstance().getPlayer(player).getInfo(BankMoneyInfo.class).getMoney();
	}
	
	public String getUsername(String uuid){
		return bankDatabaseManager.getInfoDatabase().getUUIDDatabase().getUsername(uuid);
	}
	
	public Map<String, String> getUsernames(Collection<String> col){
		Map<String, String> names = new HashMap<>();
		for(String s : col){
			names.put(s, getUsername(s));
		}
		return names;
	}
	
	public double getMoney(String uuid){
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(uuid);
		if(pl != null){ return pl.getInfo(BankMoneyInfo.class).getMoney(); }
		pl = new CorePlayers(uuid);
		return getAndLoad(pl, new BankMoneyInfo(pl)).getMoney();
	}
	
	public void setMoney(Player player, double amount){
		CorePlayerManager.getInstance().getPlayer(player).getInfo(BankMoneyInfo.class).setMoney(amount);
	}
	
	public void setMoney(String uuid, double amount){
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(uuid);
		if(pl != null){
			pl.getInfo(BankMoneyInfo.class).setMoney(amount);
			return;
		}
		pl = new CorePlayers(uuid);
		pl.getInfo(BankMoneyInfo.class).setMoney(amount);
		new SaveRunner(pl, false).run();
	}
	/*-
	public Map<String, Double> getTopMoney(int amount){
	return getTopMoney(0, amount);
	}
	
	
	public Map<String, Double> getTopMoney(int offset, int amount){
	return BankCoreHandler.getInstance().getSaveType().getLoader().getMoney(offset, amount);
	}
	
	public Map<String, Double> getTopMoney(int offset, int amount, List<String> uuidIgnored){
	return BankCoreHandler.getInstance().getSaveType().getLoader().getMoney(offset, amount, uuidIgnored);
	}
	
	*/
	
	public boolean openBank(Player player){
		return BankInventoriesManager.getInstance().open(CorePlayerManager.getInstance().getPlayer(player), BankInventories.BANK_MAIN_MENU);
	}
	
	public double getOfflineMoney(String uuid){
		CorePlayers pl = new CorePlayers(uuid);
		return getAndLoad(pl, new BankMoneyInfo(pl)).getOfflineMoney();
	}
	
	public void setOfflineMoney(String uuid, double d){
		CorePlayers pl = new CorePlayers(uuid);
		getAndLoad(pl, new BankMoneyInfo(pl)).setOfflineMoney(d);
		new SaveRunner(pl, false).run();
	}
	
	public double getOfflineExp(String uuid){
		CorePlayers pl = new CorePlayers(uuid);
		return getAndLoad(pl, new BankExpInfo(pl)).getOfflineExp();
	}
	
	public void setOfflineExp(String uuid, double d){
		CorePlayers pl = new CorePlayers(uuid);
		getAndLoad(pl, new BankExpInfo(pl)).setOfflineExp(d);
		new SaveRunner(pl, false).run();
	}
	
	public String getUUID(String name){
		return bankDatabaseManager.getInfoDatabase().getUUIDDatabase().getUUID(name);
	}
	
	public boolean exists(String uuid){
		return bankDatabaseManager.getInfoDatabase().playerExists(uuid);
	}
	
	public boolean isOnline(String uuid){
		return CorePlayerManager.getInstance().getPlayer(uuid) != null;
	}
	
	public boolean isLocked(String uuid){
		return BankDatabaseManager.getInstance().getPlayerLockDatabase().isLocked(uuid);
	}
	
}
