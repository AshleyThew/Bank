package me.dablakbandit.bank.database.base;

import me.dablakbandit.bank.implementations.cheque.Cheque;

import java.util.concurrent.CompletableFuture;

public interface IChequeDatabase {


	/**
	 * Store a cheque in the database
	 */
	CompletableFuture<Boolean> storeCheque(Cheque cheque);

	/**
	 * Get a cheque by ID
	 */
	CompletableFuture<Cheque> getCheque(String chequeId);

	/**
	 * Mark a cheque as redeemed (returns true if successful, false if already
	 * redeemed or not found)
	 */
	CompletableFuture<Boolean> markChequeRedeemed(String chequeId, String redeemedBy);

	/**
	 * Check if a cheque exists
	 */
	CompletableFuture<Boolean> chequeExists(String chequeId);

	/**
	 * Check if a cheque has been redeemed
	 */
	CompletableFuture<Boolean> isChequeRedeemed(String chequeId);

	/**
	 * Get all cheques in the database
	 */
	CompletableFuture<java.util.List<Cheque>> getAllCheques();

	/**
	 * Close the database connection
	 */
	void close();
}
