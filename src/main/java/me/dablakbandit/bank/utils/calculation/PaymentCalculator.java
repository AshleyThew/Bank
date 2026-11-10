package me.dablakbandit.bank.utils.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentCalculator {

	private static final BigDecimal CENT = new BigDecimal("0.01");
	private static final BigDecimal FLOATING_POINT_NOISE = new BigDecimal("0.000001");

	/**
	 * Converts an amount to whole cents. Values that are a cent amount plus floating-point noise
	 * (0.09999999999999999) snap to that cent; anything with a real fraction of a cent
	 * (83582.17142857143) rounds down. The result prints back exactly as that cent value.
	 */
	public static double toCents(double amount) {
		if (!(amount > 0) || Double.isInfinite(amount)) {
			return 0;
		}
		BigDecimal value = BigDecimal.valueOf(amount);
		BigDecimal nearest = value.setScale(2, RoundingMode.HALF_UP);
		if (nearest.subtract(value).abs().compareTo(FLOATING_POINT_NOISE) <= 0) {
			return nearest.doubleValue();
		}
		return value.setScale(2, RoundingMode.DOWN).doubleValue();
	}

	/**
	 * @return the cent below a cent amount, or 0 if there is none
	 */
	public static double previousCent(double cents) {
		BigDecimal previous = BigDecimal.valueOf(cents).setScale(2, RoundingMode.HALF_UP).subtract(CENT);
		return previous.signum() > 0 ? previous.doubleValue() : 0;
	}

	private final double current, max, taxRate;

	private double calculation, tax;
	private boolean full;

	public PaymentCalculator(double calculation, double current, double max, double taxRate) {
		this.calculation = Math.max(0, calculation);
		this.current = current;
		this.max = max;
		this.taxRate = Math.max(0, taxRate);
		calculate();
	}

	private void calculate() {
		if (this.calculation < 0.0 || this.current > this.max) {
			this.calculation = 0.0;
			this.full = true;
			return;
		}
		double newAmount = this.current + this.calculation;

		// If overflow or over max
		if (newAmount < 0.0 || newAmount >= this.max) {
			double tempDeposit = Math.max(0, this.max - this.current);
			double tempTax = tempDeposit * this.taxRate;

			double maxDeposit = tempDeposit + tempTax;
			// Attempting to full bank
			if (maxDeposit <= this.calculation) {
				this.calculation = tempDeposit;
				this.tax = tempTax;
				return;
			} else {
				this.calculation = tempDeposit;
			}
		}
		this.tax = this.calculation * this.taxRate;
		this.calculation -= this.tax;
	}

	public double getCalculation() {
		return calculation;
	}

	public double getTax() {
		return tax;
	}

	public double getTotalCalculation() {
		return calculation + tax;
	}

	public boolean isFull() {
		return full;
	}

	public double getMax() {
		return max;
	}

	public void floor() {
		this.calculation = Math.floor(this.calculation);
		this.tax = Math.floor(this.tax);
	}
}
