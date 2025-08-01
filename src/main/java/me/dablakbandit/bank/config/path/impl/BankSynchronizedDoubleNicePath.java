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

	public void addValue(Double amount) {
		synchronized (lock) {
			double set = super.get() + amount;
			if (set > 0) {
				super.set(set, true);
			}
		}
	}

	public boolean takeValue(Double amount) {
		synchronized (lock) {
			double set = super.get() - amount;
			if (set <= 0) {
				return false;
			}
			super.set(set, true);
		}
		return true;
	}
}
