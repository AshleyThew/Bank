package me.dablakbandit.bank.player.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.player.loan.Loan;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.json.strategy.Exclude;
import me.dablakbandit.core.vault.Eco;
import net.milkbowl.vault.economy.EconomyResponse;

public class BankLoansInfo extends IBankInfo implements JSONInfo, PermissionsInfo{
	
	@Exclude
	private int			scrolled;
	@Exclude
	private Loan		tempLoan;
	
	private final List<Loan>	loans	= Collections.synchronizedList(new ArrayList<>());
	private double		loanMax, loanInterest;
	
	public BankLoansInfo(CorePlayers pl){
		super(pl);
	}
	
	public List<Loan> getLoans(){
		return loans;
	}
	
	public int getScrolled(){
		ensureScrolled();
		return scrolled;
	}
	
	private void ensureScrolled(){
		this.scrolled = Math.max(0, Math.min(loans.size() - 1, this.scrolled));
	}
	
	private void setScrolled(int to){
		this.scrolled = to;
		ensureScrolled();
	}
	
	public void incrementScrolled(){
		setScrolled(scrolled + 1);
	}
	
	public void decrementScrolled(){
		setScrolled(scrolled - 1);
	}
	
	public Loan getTempLoan(){
		return tempLoan;
	}
	
	public void setTempLoan(Loan tempLoan){
		this.tempLoan = tempLoan;
	}
	
	public boolean paybackLoan(CorePlayers pl, Loan loan, double amount){
		return paybackLoan(pl, loan, amount, (d) -> {
		});
	}
	
	public boolean takeOutLoan(CorePlayers pl, Loan loan){
		EconomyResponse er = Eco.getInstance().getEconomy().depositPlayer(pl.getName(), loan.getOriginal());
		if(er.transactionSuccess()){
			loans.add(loan);
			return true;
		}
		return false;
	}
	
	public boolean paybackLoan(CorePlayers pl, Loan loan, double amount, Consumer<Double> consumer){
		amount = Math.min(loan.getAmount(), amount);
		EconomyResponse er = Eco.getInstance().getEconomy().withdrawPlayer(pl.getPlayer(), amount);
		if(!er.transactionSuccess() && !pl.getInfo(BankMoneyInfo.class).subtractMoney(amount)){ return false; }
		loan.payback(amount);
		consumer.accept(amount);
		if(loan.isPayedBack()){
			loans.remove(loan);
		}
		return true;
	}
	
	public double getLoanMax(){
		return loanMax;
	}
	
	public double getPermitted(){
		return Math.max(0, loanMax - getTotal());
	}
	
	public double getTotal(){
		double total = 0;
		for(Loan loan : loans){
			total += loan.getAmount();
		}
		return total;
	}
	
	public double getLoanInterest(){
		return loanInterest;
	}
	
	public void checkPermissions(Permissible permissible, boolean debug){
		Collection<PermissionAttachmentInfo> permissions = permissible.getEffectivePermissions();
		
		List<Double> maxList = BankPermissionConfiguration.PERMISSION_LOAN_AMOUNT.getValue(permissions);
		if(!maxList.isEmpty()){
			if(BankPluginConfiguration.BANK_LOANS_AMOUNT_PERMISSION_COMBINE.get()){
				loanMax = maxList.stream().mapToDouble(Double::doubleValue).sum();
			}else{
				loanMax = Collections.max(maxList);
			}
		}else{
			loanMax = BankPluginConfiguration.BANK_LOANS_AMOUNT_DEFAULT.get();
		}
		
		List<Double> interestList = BankPermissionConfiguration.PERMISSION_LOAN_INTEREST.getValue(permissions);
		if(!interestList.isEmpty()){
			loanInterest = Collections.min(interestList);
		}else{
			loanInterest = 0;
		}
	}
	
	@Override
	public void jsonInit(){
		
	}
	
	@Override
	public void jsonFinal(){
		
	}
}
