package me.dablakbandit.bank.database.sql.transaction;


import me.dablakbandit.bank.database.sql.SQLTransactionDatabase;

public class MoneyTransactionDatabase extends SQLTransactionDatabase {
	public MoneyTransactionDatabase() {
		super("bank_money_transactions");
	}
}
