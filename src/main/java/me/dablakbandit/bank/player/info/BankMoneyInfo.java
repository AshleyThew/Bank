package me.dablakbandit.bank.player.info;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.utils.calculation.PaymentCalculator;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.json.strategy.Exclude;
import me.dablakbandit.core.vault.Eco;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BankMoneyInfo extends IBankInfo implements JSONInfo, BankDefaultInfo, PermissionsInfo{
	
	private double	money;
	private double	offlineMoney;
	
	@Exclude
	private double	interestPercentageOnline;
	private double	interestPercentageOffline;
	
	public BankMoneyInfo(CorePlayers pl){
		super(pl);
	}
	
	public double getMoney(){
		return money;
	}
	
	public double getInterestPercentageOnline(){
		return interestPercentageOnline;
	}
	
	public double getInterestPercentageOffline(){
		return interestPercentageOffline;
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

		PaymentCalculator calculator = new PaymentCalculator(withdraw, 0, Double.MAX_VALUE, BankPluginConfiguration.BANK_MONEY_WITHDRAW_TAX_PERCENT.get());
		if (BankPluginConfiguration.BANK_MONEY_WITHDRAW_FULL.get()) {
			calculator.floor();
		}
		withdraw = calculator.getTotalCalculation();

		boolean complete = withdrawMoney(pl.getPlayer().getName(), withdraw, calculator.getCalculation());
		if(complete){
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_WITHDRAW.get()
					.replaceAll("<money>", Format.formatMoney(calculator.getCalculation()))
					.replaceAll("<tax>", Format.formatMoney(calculator.getTax())));
		}else{
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_NOT_ENOUGH.get().replaceAll("<money>", Format.formatMoney(money)));
		}
		return complete;
	}

	private boolean withdrawMoney(String name, double total, double withdraw) {
		if(Eco.getInstance().getEconomy() == null){
			BankLog.debug(name + " withdraw transaction failed with error: Economy instance not set");
			return false;
		}
		if(total <= money){
			EconomyResponse er = Eco.getInstance().getEconomy().depositPlayer(name, withdraw);
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


	public PaymentCalculator getPaymentCalculator(double amount, boolean tax){
		return getPaymentCalculator(amount, BankPluginConfiguration.BANK_MONEY_MAX.get(), tax);
	}

	public PaymentCalculator getPaymentCalculator(double amount, double max, boolean tax) {
		amount = Math.max(0, amount);
		if (BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get()) {
			amount = Math.floor(amount);
		}

		if (tax) {
			return new PaymentCalculator(amount, this.money, max, BankPluginConfiguration.BANK_MONEY_DEPOSIT_TAX_PERCENT.get());
		} else {
			return new PaymentCalculator(amount, this.money, max, 0);
		}
	}

	public void deposit(CommandSender sender, CorePlayers pl, double amount) {
		amount = Math.max(0, amount);
		if(BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get()){
			amount = Math.floor(amount);
		}
		
		PaymentCalculator calculator = getPaymentCalculator(amount, true);

		if (BankPluginConfiguration.BANK_MONEY_DEPOSIT_FULL.get()) {
			calculator.floor();
		}

		amount = calculator.getTotalCalculation();
		double tax = calculator.getTax();
		
		if(amount == 0.0 || depositMoney(pl.getPlayer().getName(), amount, tax)){
			if(amount != 0.0){
				BankLanguageConfiguration.sendFormattedMessage(sender, BankLanguageConfiguration.MESSAGE_MONEY_DEPOSIT.get().replaceAll("<money>", Format.formatMoney(calculator.getCalculation()))
																													.replaceAll("<tax>", Format.formatMoney(tax)));
			}
			if(calculator.isFull()){
				// player.sendMessage(LanguageConfiguration.MESSAGE_MONEY_IS_FULL.getMessage());
			}
		}else{
			BankLanguageConfiguration.sendFormattedMessage(sender, ChatColor.AQUA + "[Bank] " + ChatColor.RED
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
	
	public double getMaxAdd(double amount){
		PaymentCalculator calculator = getPaymentCalculator(amount, false);
		return calculator.getCalculation();
	}
	
	@Deprecated
	public void addMoney(double omoney){
		PaymentCalculator paymentCalculator = getPaymentCalculator(omoney, Double.MAX_VALUE, false);
		this.money += paymentCalculator.getCalculation();
		save(BankPluginConfiguration.BANK_SAVE_MONEY_DEPOSIT);
	}
	
	public void send(CorePlayers to, double amount){
		BankMoneyInfo toInfo = to.getInfo(BankMoneyInfo.class);
		amount = Math.min(amount, toInfo.getMaxAdd(amount));
		if(amount <= 0 || amount > money){
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_MONEY_NOT_ENOUGH.get().replaceAll("<money>", Format.formatMoney(money)));
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

	@Override
	public void initDefault() {
		if(BankPluginConfiguration.BANK_MONEY_DEFAULT_ENABLED.get()){
			money = BankPluginConfiguration.BANK_MONEY_DEFAULT_AMOUNT.get();
		}
	}
	
	@Override
	public void checkPermissions(Permissible permissible, boolean debug){
		Collection<PermissionAttachmentInfo> permissions = permissible.getEffectivePermissions();
		
		List<Double> onlineList = BankPermissionConfiguration.PERMISSION_MONEY_INTEREST_ONLINE.getValue(permissions);
		if(!onlineList.isEmpty()){
			interestPercentageOnline = Collections.max(onlineList);
		}else{
			interestPercentageOnline = BankPluginConfiguration.BANK_MONEY_INTEREST_ONLINE_PERCENT_GAINED.get();
		}
		
		List<Double> offlineList = BankPermissionConfiguration.PERMISSION_MONEY_INTEREST_OFFLINE.getValue(permissions);
		if(!offlineList.isEmpty()){
			interestPercentageOffline = Collections.max(offlineList);
		}else{
			interestPercentageOffline = BankPluginConfiguration.BANK_MONEY_INTEREST_OFFLINE_PERCENT_GAINED.get();
		}
	}
}
