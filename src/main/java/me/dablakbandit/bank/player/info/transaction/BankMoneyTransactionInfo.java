package me.dablakbandit.bank.player.info.transaction;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.player.info.IBankInfo;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.json.strategy.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Stores transaction history for money
 */
public class BankMoneyTransactionInfo extends IBankInfo implements JSONInfo {

	private List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());

	@Exclude
	private int scrolled = 0;

	public BankMoneyTransactionInfo(CorePlayers pl) {
		super(pl);
	}

	public void addTransaction(Transaction transaction) {
		transactions.add(0, transaction);
		int maxTransactions = BankPluginConfiguration.BANK_MONEY_TRANSACTION_MAX_HISTORY.get();
		if (transactions.size() > maxTransactions) {
			transactions = transactions.subList(0, maxTransactions);
		}
		// Async DB insert
		CorePlayers pl = getPlayers();
		LoaderManager.getInstance().runAsync(() -> {
			BankDatabaseManager.getInstance().getInfoDatabase().getMoneyTransactionDatabase().insertTransaction(pl.getUUIDString(), transaction.getType().name(), transaction.getAmount(), transaction.getDescription().name(), transaction.getDescriptionParam());
		});
	}

	public List<Transaction> getTransactions() {
		return new ArrayList<>(transactions);
	}

	public List<Transaction> getRecentTransactions(int count) {
		List<Transaction> sortedTransactions = new ArrayList<>(transactions);
		sortedTransactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
		int maxTransactions = BankPluginConfiguration.BANK_MONEY_TRANSACTION_MAX_HISTORY.get();
		int limit = Math.min(count, maxTransactions);
		return sortedTransactions.size() <= limit ? sortedTransactions : sortedTransactions.subList(0, limit);
	}

	public int getScrolled() {
		return scrolled;
	}

	public void setScrolled(int scrolled) {
		this.scrolled = Math.max(0, scrolled);
	}

	@Override
	public void jsonInit() {
		// Initialize from JSON data
	}

	@Override
	public void jsonFinal() {
		// Cleanup if needed
	}
}