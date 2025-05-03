package me.dablakbandit.bank.player.info.transaction;

import java.util.Date;

/**
 * Represents a single transaction in the bank
 */
public class Transaction {

	private TransactionType type;
	private double amount;
	private long timestamp;
	private TransactionDescription description;
	private String descriptionParam; // Optional parameter for formatting

	public Transaction(TransactionType type, double amount, TransactionDescription description) {
		this(type, amount, description, null);
	}

	public Transaction(TransactionType type, double amount, TransactionDescription description, String descriptionParam) {
		this.type = type;
		this.amount = amount;
		this.timestamp = System.currentTimeMillis();
		this.description = description;
		this.descriptionParam = descriptionParam;
	}

	public TransactionType getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Date getDate() {
		return new Date(timestamp);
	}

	public TransactionDescription getDescription() {
		return description;
	}

	public String getDescriptionParam() {
		return descriptionParam;
	}
}