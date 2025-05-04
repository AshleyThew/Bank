package me.dablakbandit.bank.database.sql.transaction;


import me.dablakbandit.bank.database.sql.SQLTransactionDatabase;

public class ExpTransactionDatabase extends SQLTransactionDatabase {
	public ExpTransactionDatabase() {
		super("bank_exp_transactions");
	}
}
