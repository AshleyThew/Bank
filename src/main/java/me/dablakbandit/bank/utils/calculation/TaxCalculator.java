package me.dablakbandit.bank.utils.calculation;

public class TaxCalculator{
	
	private final double	current, max, taxRate;
	
	private double attempt, tax;
	private boolean			full;
	
	public TaxCalculator(double attempt, double current, double max, double taxRate){
		this.attempt = Math.max(0, attempt);
		this.current = current;
		this.max = max;
		this.taxRate = Math.max(0, taxRate);
		calculate();
	}
	
	private void calculate(){
		if(this.attempt < 0.0 || this.current > this.max){
			this.attempt = 0.0;
			this.full = true;
			return;
		}
		double newAmount = this.current + this.attempt;
		
		// If overflow or over max
		if(newAmount < 0.0 || newAmount >= this.max){
			double tempDeposit = Math.max(0, this.max - this.current);
			double tempTax = tempDeposit * this.taxRate;
			
			double maxDeposit = tempDeposit + tempTax;
			// Attempting to full bank
			if(maxDeposit <= this.attempt){
				this.attempt = tempDeposit;
				this.tax = tempTax;
				return;
			}else{
				this.attempt = tempDeposit;
			}
		}
		this.tax = this.attempt * this.taxRate;
		this.attempt -= this.tax;
	}
	
	public double getResult(){
		return attempt;
	}
	
	public double getTax(){
		return tax;
	}
	
	public double getCombined(){
		return attempt + tax;
	}
	
	public boolean isFull(){
		return full;
	}
}
