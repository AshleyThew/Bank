package me.dablakbandit.bank.player.info;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.utils.Format;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.vault.Eco;
import net.milkbowl.vault.economy.EconomyResponse;

public class BankMoneyInfo extends IBankInfo implements JSONInfo{
	
	private double	money;
	private double	offlineMoney;
	
	public BankMoneyInfo(CorePlayers pl){
		super(pl);
	}
	
	public double getMoney(){
		return money;
	}
	
	@Override
	public void jsonInit(){
		if(pl.getPlayer() == null){ return; }
		Bukkit.getScheduler().scheduleSyncDelayedTask(BankPlugin.getInstance(), this::finishLoad);
	}
	
	@Override
	public void jsonFinal(){
		
	}
	
	private void finishLoad(){
		if(offlineMoney > 0){
			double newExp = this.money + offlineMoney;
			if(newExp < 0.0 || newExp > BankPluginConfiguration.BANK_MONEY_MAX.get()){
				this.offlineMoney = Math.max(0, Math.floor(BankPluginConfiguration.BANK_MONEY_MAX.get() - this.money));
			}
			addMoney(offlineMoney);
			this.offlineMoney = 0;
		}
	}
	
	public double getOfflineMoney(){
		return offlineMoney;
	}
	
	public void addOfflineMoney(double offlineMoney){
		this.offlineMoney += offlineMoney;
	}
	
	public void setOfflineMoney(double offlineMoney){
		this.offlineMoney = offlineMoney;
	}
	
	public boolean withdrawMoney(CorePlayers pl, double withdraw){
		boolean complete = withdrawMoney(pl.getPlayer().getName(), withdraw);
		if(complete){
			BankLanguageConfiguration.sendMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_WITHDRAW.get().replaceAll("<money>", Format.formatMoney(withdraw)));
		}else{
			// player.sendMessage(LanguageConfiguration.MESSAGE_NOT_ENOUGH_MONEY_IN_BANK.getMessage());
		}
		return complete;
	}
	
	private boolean withdrawMoney(String name, double d){
		if(d <= money){
			EconomyResponse er = Eco.getInstance().getEconomy().depositPlayer(name, d);
			if(!er.transactionSuccess()){ return false; }
			money -= d;
			// if(BankPluginConfiguration.LOGS_ENABLED.get() && BankPluginConfiguration.LOGS_MONEY.get()){
			// log(name + " withdrew: " + format(d) + ", new amount: " + format(money));
			// }
			save(BankPluginConfiguration.BANK_SAVE_MONEY_WITHDRAW);
			return true;
		}
		return false;
	}
	
	public void deposit(CorePlayers pl, double amount){
		if(BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get()){
			amount = Math.floor(amount);
		}
		boolean full = false;
		if(this.money > BankPluginConfiguration.BANK_MONEY_MAX.get()){
			amount = 0.0;
		}else{
			double d1 = this.money + amount;
			if(d1 < 0.0 || d1 > BankPluginConfiguration.BANK_MONEY_MAX.get()){
				amount = Math.max(0, BankPluginConfiguration.BANK_MONEY_MAX.get() - this.money);
				full = true;
			}
		}
		amount = Math.max(0, amount);
		if(BankPluginConfiguration.BANK_MONEY_DEPOSIT_FULL.get()){
			amount = Math.floor(amount);
		}
		if(amount == 0.0 || depositMoney(pl.getPlayer().getName(), amount)){
			if(amount != 0.0){
				BankLanguageConfiguration.sendMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_DEPOSIT.get().replaceAll("<money>", Format.formatMoney(amount)));
				// player.sendMessage(LanguageConfiguration.MESSAGE_MONEY_DEPOSIT.getMessage().replace("<a>", Format.formatMoney(d)));
			}
			if(full){
				// player.sendMessage(LanguageConfiguration.MESSAGE_MONEY_IS_FULL.getMessage());
			}
		}else{
			pl.getPlayer().sendMessage(ChatColor.AQUA + "[Bank] " + ChatColor.RED + "There was a problem depositing all your money, please contact an administrator");
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean depositMoney(String name, double d){
		if(Eco.getInstance().getEconomy() == null){ return false; }
		EconomyResponse er = Eco.getInstance().getEconomy().withdrawPlayer(name, d);
		if(!er.transactionSuccess()){ return false; }
		money += d;
		// if(BankPluginConfiguration.LOGS_ENABLED.get() && BankPluginConfiguration.LOGS_MONEY.get()){
		// log(name + " deposited: " + format(d) + ", new amount: " + format(money));
		// }
		save(BankPluginConfiguration.BANK_SAVE_MONEY_DEPOSIT);
		return true;
	}
	
	@Deprecated
	public boolean subtractMoney(double d){
		if(d <= money){
			money -= d;
			// if(BankPluginConfiguration.LOGS_ENABLED.get() && BankPluginConfiguration.LOGS_MONEY.get()){
			// log(pl.getName() + " subtracted: " + format(d) + ", new amount: " + format(money));
			// }
			save(BankPluginConfiguration.BANK_SAVE_MONEY_WITHDRAW);
			return true;
		}
		return false;
	}
	
	@Deprecated
	public void setMoney(double money){
		this.money = money;
	}
	
	@Deprecated
	public void addMoney(double omoney){
		this.money += omoney;
	}
}
