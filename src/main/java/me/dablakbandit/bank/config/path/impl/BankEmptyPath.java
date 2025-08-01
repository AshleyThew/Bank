package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.bank.config.path.BankExtendedPath;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.config.path.EmptyPath;

import java.util.Map;

public class BankEmptyPath extends EmptyPath implements BankExtendedPath {

	private Map<String, Object> extendedValues;

	private String file;

	@Override
	public void setFile(String file) {
		this.file = file;
	}

	public void setExtendedValues(Map<String, Object> extendedValues) {
		this.extendedValues = extendedValues;
	}

	@SuppressWarnings("unchecked")
	public <T> T getExtendValue(String key, Class<T> clazz) {
		if (this.extendedValues == null || !this.extendedValues.containsKey(key)) {
			BankLog.errorAlert("No extended value found for key " + key + " in path " + getPath() + " in file " + this.file);
			return null;
		}
		return (T) this.extendedValues.get(key);
	}
}
