package me.dablakbandit.bank.utils.format;

import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.core.config.path.TranslatedStringPath;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public enum FormatEnum {
	//@formatter:off
    THOUSAND(BankLanguageConfiguration.FORMAT_THOUSAND),
    MILLION(BankLanguageConfiguration.FORMAT_MILLION),
    BILLION(BankLanguageConfiguration.FORMAT_BILLION),
    TRILLION(BankLanguageConfiguration.FORMAT_TRILLION),
    QUADRILLION(BankLanguageConfiguration.FORMAT_QUADRILLION),
    QUINTILLION(BankLanguageConfiguration.FORMAT_QUINTILLION),
    SEXTILLION(BankLanguageConfiguration.FORMAT_SEXTILLION),
    SEPTILLION(BankLanguageConfiguration.FORMAT_SEPTILLION),
    OCTILLION(BankLanguageConfiguration.FORMAT_OCTILLION),
    NONILLION(BankLanguageConfiguration.FORMAT_NONILLION),
    DECILLION(BankLanguageConfiguration.FORMAT_DECILLION),
    UNDECILLION(BankLanguageConfiguration.FORMAT_UNDECILLION),
    DUODECILLION(BankLanguageConfiguration.FORMAT_DUODECILLION),
    TREDECILLION(BankLanguageConfiguration.FORMAT_TREDECILLION),
    QUATTUORDECILLION(BankLanguageConfiguration.FORMAT_QUATTUORDECILLION),
    QUINQUADECILLION(BankLanguageConfiguration.FORMAT_QUINQUADECILLION),
    SEDECILLION(BankLanguageConfiguration.FORMAT_SEDECILLION),
    SEPTENDECILLION(BankLanguageConfiguration.FORMAT_SEPTENDECILLION),
    OCTODECILLION(BankLanguageConfiguration.FORMAT_OCTODECILLION),
    NOVENDECILLION(BankLanguageConfiguration.FORMAT_NOVENDECILLION),
    VIGINTILLION(BankLanguageConfiguration.FORMAT_VIGINTILLION),
    UNVIGINTILLION(BankLanguageConfiguration.FORMAT_UNVIGINTILLION),
    DUOVIGINTILLION(BankLanguageConfiguration.FORMAT_DUOVIGINTILLION),
    TRESVIGINTILLION(BankLanguageConfiguration.FORMAT_TRESVIGINTILLION),
    QUATTUORVIGINTILLION(BankLanguageConfiguration.FORMAT_QUATTUORVIGINTILLION),
    QUINQUAVIGINTILLION(BankLanguageConfiguration.FORMAT_QUINQUAVIGINTILLION),
    SESVIGINTILLION(BankLanguageConfiguration.FORMAT_SESVIGINTILLION),
    SEPTEMVIGINTILLION(BankLanguageConfiguration.FORMAT_SEPTEMVIGINTILLION),
    OCTOVIGINTILLION(BankLanguageConfiguration.FORMAT_OCTOVIGINTILLION),
    NOVEMVIGINTILLION(BankLanguageConfiguration.FORMAT_NOVEMVIGINTILLION),
    TRIGINTILLION(BankLanguageConfiguration.FORMAT_TRIGINTILLION),
    UNTRIGINTILLION(BankLanguageConfiguration.FORMAT_UNTRIGINTILLION),
    DUOTRIGINTILLION(BankLanguageConfiguration.FORMAT_DUOTRIGINTILLION),
    TRESTRIGINTILLION(BankLanguageConfiguration.FORMAT_TRESTRIGINTILLION),
    QUATTUORTRIGINTILLION(BankLanguageConfiguration.FORMAT_QUATTUORTRIGINTILLION),
    QUINQUATRIGINTILLION(BankLanguageConfiguration.FORMAT_QUINQUATRIGINTILLION),
    SESTRIGINTILLION(BankLanguageConfiguration.FORMAT_SESTRIGINTILLION),
    OCTOTRIGINTILLION(BankLanguageConfiguration.FORMAT_OCTOTRIGINTILLION),
    NOVENTRIGINTILLION(BankLanguageConfiguration.FORMAT_NOVENTRIGINTILLION),
    QUADRAGINTILLION(BankLanguageConfiguration.FORMAT_QUADRAGINTILLION),
    //@formatter:on
	;

	private final TranslatedStringPath namePath;
	private final double amount;

	FormatEnum(TranslatedStringPath namePath) {
		this.namePath = namePath;
		StringBuilder base = new StringBuilder("1");
		for (int i = 0; i <= this.ordinal(); i++) {
			base.append("000");
		}
		base.append(".0");
		amount = Double.parseDouble(base.toString());
	}

	private static final List<FormatEnum> reversed = Arrays.asList(values());

	static {
		Collections.reverse(reversed);
	}

	public static String formatNormal(String normalFormat, String roundFormat, double amount, boolean round, boolean thousand) {
		for (FormatEnum formatMoney : reversed) {
			if (formatMoney == THOUSAND && !thousand) {
				continue;
			}
			if (amount >= formatMoney.amount) {
				return String.format(Locale.ROOT, normalFormat + " " + formatMoney.namePath.get(), amount / formatMoney.amount);
			}
		}
		return String.format(Locale.ROOT, round ? roundFormat : normalFormat, round ? Math.floor(amount) : amount);
	}
}
