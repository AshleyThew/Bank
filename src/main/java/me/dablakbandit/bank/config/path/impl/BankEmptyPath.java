package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.bank.config.path.BankExtendedPath;
import me.dablakbandit.core.config.path.EmptyPath;

import java.util.Map;

public class BankEmptyPath extends EmptyPath implements BankExtendedPath {

    private Map<String, Object> extendedValues;

    public void setExtendedValues(Map<String, Object> extendedValues){
        this.extendedValues = extendedValues;
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtendValue(String key, Class<T> clazz){
        if(this.extendedValues == null){
            return null;
        }
        return (T) this.extendedValues.get(key);
    }
}
