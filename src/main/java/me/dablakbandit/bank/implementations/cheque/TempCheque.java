package me.dablakbandit.bank.implementations.cheque;

import java.util.UUID;

public class TempCheque {

	private double amount;
	private UUID recipient; // null if not made out to specific player
	private String recipientName; // Store name for display purposes
	private boolean fromChequeBook; // true if opened from right-clicking a cheque book

	public TempCheque() {
		this.amount = 0.0;
		this.recipient = null;
		this.recipientName = null;
		this.fromChequeBook = false;
	}

	public TempCheque(double amount) {
		this.amount = amount;
		this.recipient = null;
		this.recipientName = null;
		this.fromChequeBook = false;
	}

	public TempCheque(double amount, boolean fromChequeBook) {
		this.amount = amount;
		this.recipient = null;
		this.recipientName = null;
		this.fromChequeBook = fromChequeBook;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public UUID getRecipient() {
		return recipient;
	}

	public void setRecipient(UUID recipient) {
		this.recipient = recipient;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public boolean hasRecipient() {
		return recipient != null;
	}

	public String getRecipientDisplayName() {
		if (recipientName != null && !recipientName.isEmpty()) {
			return recipientName;
		}
		return hasRecipient() ? "Someone" : "Anyone";
	}

	public boolean isFromChequeBook() {
		return fromChequeBook;
	}

	public void setFromChequeBook(boolean fromChequeBook) {
		this.fromChequeBook = fromChequeBook;
	}
}
