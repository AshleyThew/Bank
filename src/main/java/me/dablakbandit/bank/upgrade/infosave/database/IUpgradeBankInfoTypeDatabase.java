package me.dablakbandit.bank.upgrade.infosave.database;

import java.util.HashMap;
import java.util.Map;

import me.dablakbandit.core.database.listener.SQLListener;

public abstract class IUpgradeBankInfoTypeDatabase extends SQLListener{
	
	protected final Map<Class<?>, Integer> cache = new HashMap<>();
	
	public abstract int getInfo(Class<?> clazz) throws Exception;
	
}
