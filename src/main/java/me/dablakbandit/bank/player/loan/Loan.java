package me.dablakbandit.bank.player.loan;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Loan {

	private double original, amount, interest, payback;
	private long time, deadline;
	private int paybackFailedCount;

	public Loan(double original, double amount, double interest, long time, double payback, int paybackFailedCount, long deadline) {
		this.original = original;
		this.amount = amount;
		this.interest = interest;
		this.time = time;
		this.payback = payback;
		this.paybackFailedCount = paybackFailedCount;
		this.deadline = deadline;
	}

	public double getOriginal() {
		return original;
	}

	public void setOriginal(double original) {
		this.original = original;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getAmount() {
		return amount;
	}

	public double getInterest() {
		return interest;
	}

	public double applyInterest() {
		double interest = amount * this.interest;
		this.amount += interest;
		return interest;
	}

	public long getTime() {
		return time;
	}

	public long getDeadLine() {
		return deadline;
	}

	public void setDeadline(long l) {
		deadline = l;
	}

	public double getPayback() {
		return payback;
	}

	public void setPayback(double d) {
		payback = d;
	}

	public int getPaybackFailedCount() {
		return paybackFailedCount;
	}

	public void incrementPaybackFailed() {
		paybackFailedCount++;
	}

	public void resetPaybackFailed() {
		paybackFailedCount = 0;
	}

	public void payback(double d) {
		amount -= d;
	}

	public boolean isPayedBack() {
		return new BigDecimal(amount).setScale(2, RoundingMode.DOWN).doubleValue() <= 0.0;
	}

}
