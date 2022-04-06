package me.dablakbandit.bank.player.info;

import org.bukkit.Bukkit;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.utils.calculation.TaxCalculator;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.EXPUtils;

public class BankExpInfo extends IBankInfo implements JSONInfo{
	
	private double	exp;
	private double	offlineExp;
	
	public BankExpInfo(CorePlayers pl){
		super(pl);
	}
	
	public double getExp(){
		return exp;
	}
	
	public int getExpLevel(){
		return EXPUtils.getLevelFromExp((long)exp);
	}
	
	@Override
	public void jsonInit(){
		if(pl.getPlayer() == null){ return; }
		Bukkit.getScheduler().scheduleSyncDelayedTask(BankPlugin.getInstance(), this::finishLoad);
	}
	
	@Override
	public void jsonFinal(){
		if(pl.getPlayer() == null){
		}
		
	}
	
	private void finishLoad(){
		if(offlineExp > 0){
			double maxAdd = getMaxAdd(offlineExp);
			addExp(maxAdd);
			this.offlineExp = 0;
		}
	}
	
	public void withdrawExp(CorePlayers pl, double withdraw){
		withdraw = Math.max(0, withdraw);

		TaxCalculator taxCalculator = new TaxCalculator(withdraw, 0, Double.MAX_VALUE, BankPluginConfiguration.BANK_EXP_WITHDRAW_TAX_PERCENT.get());
		withdraw = taxCalculator.getCombined();

		if(withdrawExp(withdraw)){
			EXPUtils.addExp(pl.getPlayer(), (int)Math.min(Integer.MAX_VALUE, taxCalculator.getResult()));
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_EXP_WITHDRAW.get()
					.replaceAll("<exp>", Format.formatExp(withdraw))
					.replaceAll("<tax>", Format.formatExp(taxCalculator.getTax())));
		}
	}
	
	private boolean withdrawExp(double withdraw){
		if(withdraw <= this.exp){
			this.exp -= withdraw;
			// if(BankPluginConfiguration.LOGS_ENABLED.get() && BankPluginConfiguration.LOGS_EXP.get()){
			// log("Exp withdrew: " + i + ", new amount: " + exp);
			// }
			save(BankPluginConfiguration.BANK_SAVE_EXP_DEPOSIT);
			return true;
		}
		return false;
	}
	
	private void depositExp(double add){
		this.exp += add;
		save(BankPluginConfiguration.BANK_SAVE_EXP_DEPOSIT);
		// if(BankPluginConfiguration.LOGS_ENABLED.get() && BankPluginConfiguration.LOGS_EXP.get()){
		// log("Exp deposited: " + format(d) + ", new amount: " + exp);
		// }
	}
	
	public void depositExp(CorePlayers pl, double deposit){
		
		deposit = Math.max(0, deposit);
		
		int total = EXPUtils.getExp(pl.getPlayer());
		deposit = Math.min(total, deposit);
		
		TaxCalculator taxCalculator = new TaxCalculator(deposit, this.exp, BankPluginConfiguration.BANK_EXP_MAX.get(), BankPluginConfiguration.BANK_EXP_DEPOSIT_TAX_PERCENT.get());
		
		deposit = taxCalculator.getCombined();
		double tax = taxCalculator.getTax();
		
		depositExp(taxCalculator.getResult());
		EXPUtils.setExp(pl.getPlayer(), (int)Math.min(Integer.MAX_VALUE, total - deposit));
		if(deposit != 0){
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_EXP_DEPOSIT.get().replaceAll("<exp>", Format.formatExp(deposit))
																											.replaceAll("<tax>", Format.formatExp(tax)));
		}
		if(taxCalculator.isFull()){
			// player.sendMessage(LanguageConfiguration.MESSAGE_EXP_IS_FULL.getMessage());
		}
	}
	
	public TaxCalculator calculate(double add){
		return new TaxCalculator(add, this.exp, BankPluginConfiguration.BANK_EXP_MAX.get(), 0);
	}
	
	@Deprecated
	public void setExp(double exp){
		this.exp = exp;
	}
	
	@Deprecated
	public double getMaxAdd(double amount){
		TaxCalculator taxCalculator = calculate(amount);
		return taxCalculator.getResult();
	}
	
	@Deprecated
	public void addExp(double oexp){
		this.exp += oexp;
	}
	
	public void addOfflineExp(double exp){
		this.offlineExp += exp;
	}
	
	public double getOfflineExp(){
		return offlineExp;
	}
	
	public void setOfflineExp(double min){
		this.offlineExp = min;
	}
	
	@Deprecated
	public boolean subtractExp(int amount){
		if(amount <= exp){
			exp -= amount;
			save(BankPluginConfiguration.BANK_SAVE_EXP_WITHDRAW);
			return true;
		}
		return false;
	}
	
	public boolean send(CorePlayers to, int amount){
		BankExpInfo toInfo = to.getInfo(BankExpInfo.class);
		amount = Math.min((int)toInfo.getMaxAdd(amount), amount);
		if(amount <= 0 || amount > exp){
			BankLanguageConfiguration.sendFormattedMessage(pl.getPlayer(), BankLanguageConfiguration.MESSAGE_EXP_NOT_ENOUGH.get());
			return false;
		}
		if(subtractExp(amount)){
			BankSoundConfiguration.EXP_SEND_OTHER.play(pl);
			BankSoundConfiguration.EXP_SEND_RECEIVE.play(to);
			toInfo.addExp(amount);
			String formatted = Format.formatExp(amount);
			BankLanguageConfiguration.sendFormattedMessage(pl.getPlayer(), BankLanguageConfiguration.MESSAGE_EXP_SENT	.get().replaceAll("<exp>", formatted)
																														.replaceAll("<name>", to.getPlayer().getName()));
			BankLanguageConfiguration.sendFormattedMessage(to.getPlayer(), BankLanguageConfiguration.MESSAGE_EXP_RECEIVED	.get().replaceAll("<exp>", formatted)
																															.replaceAll("<name>", pl.getPlayer().getName()));
			return true;
		}
		return false;
	}
	
}
