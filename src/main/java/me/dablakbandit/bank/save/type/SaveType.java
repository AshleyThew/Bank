package me.dablakbandit.bank.save.type;

import java.util.function.Supplier;

import me.dablakbandit.bank.database.base.IInfoDatabase;
import me.dablakbandit.bank.database.sqlite.BankInfoSQLiteDatabase;

public enum SaveType{
	//@formatter:off
	SQLITE(BankInfoSQLiteDatabase::new);
	//@formatter:on
	
	private Supplier<IInfoDatabase> infoDatabase;
	
	SaveType(Supplier<IInfoDatabase> infoDatabase){
		this.infoDatabase = infoDatabase;
	}
	
	public Supplier<IInfoDatabase> getInfoDatabase(){
		return infoDatabase;
	}
}
