package me.dablakbandit.bank.implementations;

import java.util.Arrays;

public class BankImplementationManager {

	private static final BankImplementationManager typeManager = new BankImplementationManager();

	public static BankImplementationManager getInstance() {
		return typeManager;
	}

	private BankImplementationManager() {

	}

	public void load() {
		Arrays.stream(BankImplementations.values()).forEach(BankImplementations::load);
	}

	public void enable() {
		Arrays.stream(BankImplementations.values()).forEach(BankImplementations::enable);
	}

	public void disable() {
		Arrays.stream(BankImplementations.values()).forEach(BankImplementations::disable);
	}

}
