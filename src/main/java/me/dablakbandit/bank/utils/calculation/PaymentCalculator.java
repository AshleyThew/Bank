package me.dablakbandit.bank.utils.calculation;

public class PaymentCalculator {
	
	private final double	current, max, taxRate;

	private double calculation, tax;
	private boolean			full;

	public PaymentCalculator(double calculation, double current, double max, double taxRate) {
		this.calculation = Math.max(0, calculation);
		this.current = current;
		this.max = max;
		this.taxRate = Math.max(0, taxRate);
		calculate();
	}
	
	private void calculate(){
		if (this.calculation < 0.0 || this.current > this.max) {
			this.calculation = 0.0;
			this.full = true;
			return;
		}
		double newAmount = this.current + this.calculation;
		
		// If overflow or over max
		if(newAmount < 0.0 || newAmount >= this.max){
			double tempDeposit = Math.max(0, this.max - this.current);
			double tempTax = tempDeposit * this.taxRate;
			
			double maxDeposit = tempDeposit + tempTax;
			// Attempting to full bank
			if (maxDeposit <= this.calculation) {
				this.calculation = tempDeposit;
				this.tax = tempTax;
				return;
			}else{
				this.calculation = tempDeposit;
			}
		}
		this.tax = this.calculation * this.taxRate;
		this.calculation -= this.tax;
	}

	public double getCalculation() {
		return calculation;
	}
	
	public double getTax(){
		return tax;
	}

	public double getTotalCalculation() {
		return calculation + tax;
	}
	
	public boolean isFull(){
		return full;
	}

	public void floor() {
		this.calculation = Math.floor(this.calculation);
		this.tax = Math.floor(this.tax);
	}
}
