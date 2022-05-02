package me.dablakbandit.bank.player.info;

import me.dablakbandit.bank.log.BankLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.utils.calculation.TaxCalculator;
import me.dablakbandit.bank.utils.format.Format;
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
			double maxAdd = getMaxAdd(offlineMoney);
			addMoney(maxAdd);
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
		withdraw = Math.max(0, withdraw);

		TaxCalculator taxCalculator = new TaxCalculator(withdraw, 0, Double.MAX_VALUE, BankPluginConfiguration.BANK_MONEY_WITHDRAW_TAX_PERCENT.get());
		withdraw = taxCalculator.getCombined();

		boolean complete = withdrawMoney(pl.getPlayer().getName(), withdraw, taxCalculator.getResult());
		if(complete){
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_WITHDRAW.get()
					.replaceAll("<money>", Format.formatMoney(withdraw))
					.replaceAll("<tax>", Format.formatMoney(taxCalculator.getTax())));
		}else{
			// player.sendMessage(LanguageConfiguration.MESSAGE_NOT_ENOUGH_MONEY_IN_BANK.getMessage());
		}
		return complete;
	}
	
	private boolean withdrawMoney(String name, double total, double deposit){
		if(Eco.getInstance().getEconomy() == null){
			BankLog.debug(name + " withdraw transaction failed with error: Economy instance not set");
			return false;
		}
		if(total <= money){
			EconomyResponse er = Eco.getInstance().getEconomy().depositPlayer(name, deposit);
			if(!er.transactionSuccess()){
				BankLog.debug(name + " withdraw transaction failed with error: " + er.errorMessage);
				return false;
			}
			money -= total;
			// if(BankPluginConfiguration.LOGS_ENABLED.get() && BankPluginConfiguration.LOGS_MONEY.get()){
			// log(name + " withdrew: " + format(d) + ", new amount: " + format(money));
			// }
			save(BankPluginConfiguration.BANK_SAVE_MONEY_WITHDRAW);
			return true;
		}
		return false;
	}
	
	public void deposit(CorePlayers pl, double amount){
		amount = Math.max(0, amount);
		if(BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get()){
			amount = Math.floor(amount);
		}
		
		TaxCalculator taxCalculator = new TaxCalculator(amount, this.money, BankPluginConfiguration.BANK_MONEY_MAX.get(), BankPluginConfiguration.BANK_MONEY_DEPOSIT_TAX_PERCENT.get());
		
		amount = taxCalculator.getCombined();
		double tax = taxCalculator.getTax();
		
		if(BankPluginConfiguration.BANK_MONEY_DEPOSIT_FULL.get()){
			amount = Math.floor(amount);
			tax = Math.floor(tax);
		}
		
		if(amount == 0.0 || depositMoney(pl.getPlayer().getName(), amount, tax)){
			if(amount != 0.0){
				BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_DEPOSIT	.get().replaceAll("<money>", Format.formatMoney(amount))
																													.replaceAll("<tax>", Format.formatMoney(tax)));
				// player.sendMessage(LanguageConfiguration.MESSAGE_MONEY_DEPOSIT.getMessage().replace("<a>", Format.formatMoney(d)));
			}
			if(taxCalculator.isFull()){
				// player.sendMessage(LanguageConfiguration.MESSAGE_MONEY_IS_FULL.getMessage());
			}
		}else{
			BankLanguageConfiguration.sendFormattedMessage(pl.getPlayer(), ChatColor.AQUA	+ "[Bank] " + ChatColor.RED
																			+ "There was a problem depositing all your money, please contact an administrator");
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean depositMoney(String name, double amount, double tax){
		if(Eco.getInstance().getEconomy() == null){
			BankLog.debug(name + " deposit transaction failed with error: Economy instance not set");
			return false;
		}
		EconomyResponse er = Eco.getInstance().getEconomy().withdrawPlayer(name, amount);
		if(!er.transactionSuccess()){
			BankLog.debug(name + " deposit transaction failed with error: " +  er.errorMessage);
			return false;
		}
		money += amount - tax;
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
	
	public TaxCalculator calculate(double add){
		return new TaxCalculator(add, this.money, BankPluginConfiguration.BANK_MONEY_MAX.get(), 0);
	}
	
	public double getMaxAdd(double amount){
		TaxCalculator taxCalculator = calculate(amount);
		return taxCalculator.getResult();
	}
	
	@Deprecated
	public void addMoney(double omoney){
		this.money += omoney;
	}
	
	public void send(CorePlayers to, double amount){
		BankMoneyInfo toInfo = to.getInfo(BankMoneyInfo.class);
		amount = Math.min(amount, toInfo.getMaxAdd(amount));
		if(amount <= 0 || amount > money){
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_NOT_ENOUGH.get());
			return;
		}
		if(BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get()){
			amount = Math.floor(amount);
		}
		if(subtractMoney(amount)){
			BankSoundConfiguration.MONEY_SEND_OTHER.play(pl);
			BankSoundConfiguration.MONEY_SEND_RECEIVE.play(to);
			toInfo.addMoney(amount);
			String formatted = Format.formatMoney(amount);
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_SENT	.get().replaceAll("<money>", formatted)
																											.replaceAll("<name>", to.getPlayer().getName()));
			BankLanguageConfiguration.sendFormattedMessage(to, BankLanguageConfiguration.MESSAGE_MONEY_RECEIVED	.get().replaceAll("<money>", formatted)
																												.replaceAll("<name>", pl.getPlayer().getName()));
		}
	}
}
