package me.dablakbandit.bank.database.mysql;

import me.dablakbandit.bank.database.sql.SQLChequeDatabase;

public class ChequeMySQLDatabase extends SQLChequeDatabase {

	private static final String TABLE_NAME = "bank_cheques";

	public ChequeMySQLDatabase() {
	}

	@Override
	protected String getCreateTableSql() {
		return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + "cheque_id VARCHAR(255) PRIMARY KEY, " + "issuer VARCHAR(36) NOT NULL, " + "recipient VARCHAR(36), " + "amount DOUBLE NOT NULL, " + "issue_time BIGINT NOT NULL, " + "redeemed BOOLEAN DEFAULT FALSE, " + "redeemed_by VARCHAR(36), " + "redeemed_time BIGINT DEFAULT 0, " + "INDEX idx_redeemed (redeemed)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
	}
}
