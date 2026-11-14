package me.dablakbandit.bank.utils.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormatParseAmountTest {

	@Test
	void plainNumbers() {
		assertEquals(1500, Format.parseAmount("1500"));
		assertEquals(2.5, Format.parseAmount("2.5"));
		assertEquals(0.5, Format.parseAmount(".5"));
		assertEquals(1234.56, Format.parseAmount(" 1234.56 "));
		assertEquals(-5, Format.parseAmount("-5"));
	}

	@Test
	void suffixes() {
		assertEquals(150_000, Format.parseAmount("150K"));
		assertEquals(150_000, Format.parseAmount("150k"));
		assertEquals(2_000_000, Format.parseAmount("2M"));
		assertEquals(1_000_000_000, Format.parseAmount("1B"));
		assertEquals(3_000_000_000_000d, Format.parseAmount("3t"));
		assertEquals(2_500_000, Format.parseAmount("2.5m"));
		assertEquals(500, Format.parseAmount(".5k"));
		assertEquals(150_000, Format.parseAmount("150 k"));
	}

	@Test
	void decimalsAreExact() {
		// 1.1 * 1000 in doubles is 1100.0000000000002
		assertEquals(1100, Format.parseAmount("1.1k"));
		assertEquals(1_234_567.89, Format.parseAmount("1.23456789m"));
		assertEquals(10_100, Format.parseAmount("10.1K"));
	}

	@Test
	void separatorsAndCurrency() {
		assertEquals(1_500_000, Format.parseAmount("1,500,000"));
		assertEquals(1_500_000.25, Format.parseAmount("$1,500,000.25"));
		assertEquals(2_000_000, Format.parseAmount("$2M"));
	}

	@Test
	void wholeAmounts() {
		assertEquals(1100, Format.parseWholeAmount("1.1k"));
		assertEquals(12, Format.parseWholeAmount("12.9"));
		assertEquals(Integer.MAX_VALUE, Format.parseWholeAmount("5t"));
	}

	@Test
	void rejectsNonsense() {
		for (String bad : new String[]{"", "abc", "k", "1.2.3", "1kk", "NaN", "Infinity", "1e5", "0x10", "1d", "--1", null}) {
			assertThrows(NumberFormatException.class, () -> Format.parseAmount(bad), String.valueOf(bad));
		}
	}
}
