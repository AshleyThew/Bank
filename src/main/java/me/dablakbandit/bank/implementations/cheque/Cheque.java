package me.dablakbandit.bank.implementations.cheque;

import java.util.UUID;

public class Cheque {

	private final String chequeId;
	private final UUID issuer;
	private final UUID recipient; // null if not made out to specific player
	private final double amount;
	private final long issueTime;
	private boolean redeemed;
	private UUID redeemedBy;
	private long redeemedTime;

	public Cheque(String chequeId, UUID issuer, UUID recipient, double amount, long issueTime) {
		this.chequeId = chequeId;
		this.issuer = issuer;
		this.recipient = recipient;
		this.amount = amount;
		this.issueTime = issueTime;
		this.redeemed = false;
	}

	public String getChequeId() {
		return chequeId;
	}

	public UUID getIssuer() {
		return issuer;
	}

	public UUID getRecipient() {
		return recipient;
	}

	public double getAmount() {
		return amount;
	}

	public long getIssueTime() {
		return issueTime;
	}

	public boolean isRedeemed() {
		return redeemed;
	}

	public UUID getRedeemedBy() {
		return redeemedBy;
	}

	public long getRedeemedTime() {
		return redeemedTime;
	}

	public boolean canBeRedeemedBy(UUID player) {
		// Already redeemed
		if (redeemed) {
			return false;
		}
		// If made out to specific player, only they can redeem
		return recipient == null || recipient.equals(player);
	}

	public void redeem(UUID redeemedBy) {
		this.redeemed = true;
		this.redeemedBy = redeemedBy;
		this.redeemedTime = System.currentTimeMillis();
	}
}
