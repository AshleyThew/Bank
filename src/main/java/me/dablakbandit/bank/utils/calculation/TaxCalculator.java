package me.dablakbandit.bank.utils.calculation;

public class TaxCalculator{
	
	private final double	current, max, taxRate;
	
	private double			deposit, tax;
	private boolean			full;
	
	public TaxCalculator(double deposit, double current, double max, double taxRate){
		this.deposit = Math.max(0, deposit);
		this.current = current;
		this.max = max;
		this.taxRate = Math.max(0, taxRate);
		calculate();
	}
	
	private void calculate(){
		if(this.deposit < 0.0 || this.current > this.max){
			this.deposit = 0.0;
			this.full = true;
			return;
		}
		double newAmount = this.current + this.deposit;
		
		// If overflow or over max
		if(newAmount < 0.0 || newAmount >= this.max){
			double tempDeposit = Math.max(0, this.max - this.current);
			double tempTax = tempDeposit * this.taxRate;
			
			// Attempting to full bank
			if(tempDeposit <= this.deposit){
				this.deposit = tempDeposit;
				this.tax = tempTax;
				return;
			}else{
				this.deposit = tempDeposit;
			}
		}
		this.tax = this.deposit * this.taxRate;
		this.deposit -= this.tax;
		
	}
	
	public double getDeposit(){
		return deposit;
	}
	
	public double getTax(){
		return tax;
	}
	
	public double getCombined(){
		return deposit + tax;
	}
	
	public boolean isFull(){
		return full;
	}
}
