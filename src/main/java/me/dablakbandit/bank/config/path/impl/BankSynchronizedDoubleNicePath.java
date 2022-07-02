package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.core.config.path.DoubleNicePath;

public class BankSynchronizedDoubleNicePath extends DoubleNicePath {

    private final Object lock = new Object();

    public BankSynchronizedDoubleNicePath(double def) {
        super(def);
    }

    @Override
    public void set(Double value, boolean reload_save) {
        synchronized (lock) {
            super.set(value, reload_save);
        }
    }

    public void addValue(Double add){
        synchronized (lock){
            double set = super.get() + add;
            super.set(set, true);
        }
    }
}
