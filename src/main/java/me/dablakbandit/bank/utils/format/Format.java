package me.dablakbandit.bank.utils.format;

import me.dablakbandit.bank.config.BankPluginConfiguration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format {

	public static final String NBSP_STRING = "\u00A0";
	public static final String NNBSP_STRING = "\u202F";
	public static final char NBSP = NBSP_STRING.charAt(0);
	public static final char NNBSP = NNBSP_STRING.charAt(0);
	public static final char REPLACE = ',';

	public static String formatMoney(double amount) {
		String ret;
		if (!BankPluginConfiguration.BANK_MONEY_FORMAT_ENABLED.get()) {
			ret = String.format(Locale.ROOT, BankPluginConfiguration.BANK_MONEY_FORMAT_NORMAL.get(), amount);
		} else {
			ret = FormatEnum.formatNormal(BankPluginConfiguration.BANK_MONEY_FORMAT_NORMAL.get(), BankPluginConfiguration.BANK_MONEY_FORMAT_ROUND.get(), amount, BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get(), BankPluginConfiguration.BANK_MONEY_FORMAT_THOUSANDS.get());
		}
		if (ret.endsWith(".00")) {
			ret = ret.substring(0, ret.length() - 3);
		}
		return ret.replace(NBSP, REPLACE).replace(NNBSP, REPLACE);
	}

	public static String formatExp(double amount) {
		String ret;
		if (!BankPluginConfiguration.BANK_EXP_FORMAT_ENABLED.get()) {
			ret = String.format(Locale.ROOT, BankPluginConfiguration.BANK_EXP_FORMAT_NORMAL.get(), amount);
		} else {
			ret = FormatEnum.formatNormal(BankPluginConfiguration.BANK_EXP_FORMAT_NORMAL.get(), BankPluginConfiguration.BANK_EXP_FORMAT_ROUND.get(), amount, false, BankPluginConfiguration.BANK_EXP_FORMAT_THOUSANDS.get());
		}
		if (ret.endsWith(".00")) {
			ret = ret.substring(0, ret.length() - 3);
		}
		return ret.replace(NBSP, REPLACE).replace(NNBSP, REPLACE);
	}

	public static String round(double amount, int points) {
		return String.format(Locale.ROOT, "%." + points + "f", amount);
	}

	private static final Pattern AMOUNT = Pattern.compile("([+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+))([\\p{L}]*)");
	private static final Map<String, BigDecimal> SHORT_SUFFIXES = new HashMap<>();

	static {
		SHORT_SUFFIXES.put("k", BigDecimal.valueOf(1_000L));
		SHORT_SUFFIXES.put("m", BigDecimal.valueOf(1_000_000L));
		SHORT_SUFFIXES.put("b", BigDecimal.valueOf(1_000_000_000L));
		SHORT_SUFFIXES.put("t", BigDecimal.valueOf(1_000_000_000_000L));
	}

	/**
	 * Reads an amount someone typed. As well as plain numbers (1500, 2.5) it takes thousands commas
	 * (1,500,000), a leading $, and a short suffix in any case with or without decimals: 150k = 150,000,
	 * 1.1k = 1,100, 2M = 2,000,000, 1b = 1,000,000,000, 3t = 3,000,000,000,000. The number names from
	 * language.yml are understood too ("2 Million"), so an amount the bank displays can be typed back in.
	 *
	 * @throws NumberFormatException if it is not an amount
	 */
	public static double parseAmount(String input) {
		if (input == null) {
			throw new NumberFormatException("No amount");
		}
		String cleaned = input.trim().replace(",", "").replace("_", "").replace(" ", "").replace(NBSP_STRING, "").replace(NNBSP_STRING, "");
		if (cleaned.startsWith("$")) {
			cleaned = cleaned.substring(1);
		}
		Matcher matcher = AMOUNT.matcher(cleaned);
		if (!matcher.matches()) {
			throw new NumberFormatException("Not an amount: " + input);
		}
		BigDecimal amount = new BigDecimal(matcher.group(1));
		String suffix = matcher.group(2).toLowerCase(Locale.ROOT);
		if (!suffix.isEmpty()) {
			BigDecimal multiplier = SHORT_SUFFIXES.get(suffix);
			if (multiplier == null) {
				multiplier = FormatEnum.multiplierFor(suffix);
			}
			if (multiplier == null) {
				throw new NumberFormatException("Unknown amount suffix: " + input);
			}
			amount = amount.multiply(multiplier);
		}
		double value = amount.doubleValue();
		if (Double.isNaN(value) || Double.isInfinite(value)) {
			throw new NumberFormatException("Amount too large: " + input);
		}
		return value;
	}

	/**
	 * {@link #parseAmount(String)} rounded down to a whole number, for exp.
	 */
	public static int parseWholeAmount(String input) {
		return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, Math.floor(parseAmount(input))));
	}
}
