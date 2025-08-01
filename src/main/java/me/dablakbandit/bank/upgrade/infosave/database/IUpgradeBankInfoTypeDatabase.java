package me.dablakbandit.bank.upgrade.infosave.database;

import me.dablakbandit.core.database.listener.SQLListener;

import java.util.HashMap;
import java.util.Map;

public abstract class IUpgradeBankInfoTypeDatabase extends SQLListener {

	protected final Map<Class<?>, Integer> cache = new HashMap<>();

	public abstract int getInfo(Class<?> clazz) throws Exception;

}
