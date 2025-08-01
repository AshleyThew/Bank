package me.dablakbandit.bank.log;

public enum BankLogLevel {
	LOWEST, LOW, MEDIUM, HIGH, HIGHEST;

	public boolean isAtleast(BankLogLevel atleast) {
		return ordinal() <= atleast.ordinal();
	}

	// 1 0
	// Saving is 0
	// level set to 1
}
