package me.dablakbandit.bank.player.info.transaction;

import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.path.impl.BankTranslatedStringPath;

/**
 * Represents configurable descriptions for transactions
 */
public enum TransactionDescription {
	MONEY_DEPOSIT(BankLanguageConfiguration.TRANSACTION_MONEY_DEPOSIT),
	MONEY_WITHDRAWAL(BankLanguageConfiguration.TRANSACTION_MONEY_WITHDRAWAL),
	MONEY_SEND_TO(BankLanguageConfiguration.TRANSACTION_MONEY_SEND_TO),
	MONEY_RECEIVE_FROM(BankLanguageConfiguration.TRANSACTION_MONEY_RECEIVE_FROM),
	MONEY_INTEREST(BankLanguageConfiguration.TRANSACTION_MONEY_INTEREST),
	MONEY_TAX(BankLanguageConfiguration.TRANSACTION_MONEY_TAX),
	EXP_DEPOSIT(BankLanguageConfiguration.TRANSACTION_EXP_DEPOSIT),
	EXP_WITHDRAWAL(BankLanguageConfiguration.TRANSACTION_EXP_WITHDRAWAL),
	EXP_SEND_TO(BankLanguageConfiguration.TRANSACTION_EXP_SEND_TO),
	EXP_RECEIVE_FROM(BankLanguageConfiguration.TRANSACTION_EXP_RECEIVE_FROM),
	EXP_INTEREST(BankLanguageConfiguration.TRANSACTION_EXP_INTEREST),
	EXP_TAX(BankLanguageConfiguration.TRANSACTION_EXP_TAX);

	private final BankTranslatedStringPath message;

	TransactionDescription(BankTranslatedStringPath message) {
		this.message = message;
	}

	public BankTranslatedStringPath getMessage() {
		return message;
	}
}
