package me.dablakbandit.bank.save.type;

import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.database.sqlite.BankInfoSQLiteDatabase;

import java.util.function.Supplier;

public enum SaveType {
	//@formatter:off
	SQLITE(BankInfoSQLiteDatabase::new);
	//@formatter:on

	private final Supplier<IInfoDatabase> infoDatabase;

	SaveType(Supplier<IInfoDatabase> infoDatabase) {
		this.infoDatabase = infoDatabase;
	}

	public Supplier<IInfoDatabase> getInfoDatabase() {
		return infoDatabase;
	}

	public boolean isSingle() {
		return this == SQLITE;
	}
}
