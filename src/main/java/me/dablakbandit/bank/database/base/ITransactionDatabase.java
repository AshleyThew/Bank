package me.dablakbandit.bank.database.base;

public interface ITransactionDatabase {

	void insertTransaction(String uuid, String type, double amount, String description, String extra);
}
