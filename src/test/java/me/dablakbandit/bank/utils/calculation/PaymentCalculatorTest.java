package me.dablakbandit.bank.utils.calculation;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentCalculatorTest {

	/** Minimal model of EssentialsX's Vault bridge: BigDecimal balance, round-down getBalance, valueOf on withdraw. */
	private static final class EssentialsWallet {
		BigDecimal balance;

		EssentialsWallet(BigDecimal balance) {
			this.balance = balance;
		}

		double getBalance() {
			double amount = balance.doubleValue();
			if (new BigDecimal(amount).compareTo(balance) > 0) {
				amount = Math.nextAfter(amount, Double.NEGATIVE_INFINITY);
			}
			return amount;
		}

		boolean withdraw(double amount) {
			BigDecimal result = balance.subtract(BigDecimal.valueOf(amount), MathContext.DECIMAL128);
			if (result.signum() < 0) {
				return false; // "Loan was not permitted!"
			}
			balance = result;
			return true;
		}
	}

	/** Mirrors BankMoneyInfo.deposit + withdrawForDeposit; returns the amount taken or -1. */
	private static double deposit(EssentialsWallet wallet, double offered, double taxRate) {
		double amount = PaymentCalculator.toCents(offered);
		PaymentCalculator calculator = new PaymentCalculator(amount, 0, Double.MAX_VALUE, taxRate);
		amount = Math.min(calculator.getTotalCalculation(), amount);
		if (amount == 0) {
			return 0;
		}
		if (wallet.withdraw(amount)) {
			return amount;
		}
		double retry = PaymentCalculator.previousCent(amount);
		if (retry > 0 && wallet.withdraw(retry)) {
			return retry;
		}
		return -1;
	}

	@Test
	void depositAllSucceedsForHighPrecisionBalances() {
		Random random = new Random(1);
		for (int i = 0; i < 200_000; i++) {
			BigDecimal start = BigDecimal.valueOf(random.nextInt(100_000_000)).divide(BigDecimal.valueOf(3 + random.nextInt(97)), MathContext.DECIMAL128);
			for (double taxRate : new double[] { 0, 0.05, 0.1, 0.333 }) {
				EssentialsWallet wallet = new EssentialsWallet(start);
				double taken = deposit(wallet, wallet.getBalance(), taxRate);
				assertTrue(taken >= 0, "deposit-all refused for balance " + start + " tax " + taxRate);
				assertTrue(wallet.balance.compareTo(new BigDecimal("0.01")) < 0 || taxRate > 0, "left a cent or more behind for " + start + ": " + wallet.balance);
			}
		}
	}

	@Test
	void depositAllEmptiesCentBalances() {
		Random random = new Random(2);
		for (int i = 0; i < 200_000; i++) {
			BigDecimal start = BigDecimal.valueOf(random.nextInt(1_000_000_000), 2);
			EssentialsWallet wallet = new EssentialsWallet(start);
			assertEquals(start.doubleValue(), deposit(wallet, wallet.getBalance(), 0), "balance " + start);
			assertEquals(0, wallet.balance.signum(), "balance " + start + " left " + wallet.balance);
		}
	}

	@Test
	void reportedFailingBalances() {
		EssentialsWallet longDecimal = new EssentialsWallet(new BigDecimal("83582.17142857142857142857142857143"));
		assertTrue(!longDecimal.withdraw(longDecimal.getBalance()), "precondition: the raw balance double is refused");
		assertEquals(83582.17, deposit(longDecimal, longDecimal.getBalance(), 0));

		EssentialsWallet justUnder = new EssentialsWallet(new BigDecimal("4071.299999999999999999999"));
		assertEquals(4071.29, deposit(justUnder, justUnder.getBalance(), 0));
	}

	@Test
	void typedAmountsAreNotRoundedUp() {
		assertEquals(10.55, PaymentCalculator.toCents(10.555));
		assertEquals(0.1, PaymentCalculator.toCents(0.09999999999999999));
		assertEquals(1234.56, PaymentCalculator.toCents(1234.56));
	}

	@Test
	void badInput() {
		assertEquals(0, PaymentCalculator.toCents(-5));
		assertEquals(0, PaymentCalculator.toCents(Double.NaN));
		assertEquals(0, PaymentCalculator.toCents(Double.POSITIVE_INFINITY));
		assertEquals(0, PaymentCalculator.previousCent(0.01));
		assertEquals(4071.29, PaymentCalculator.previousCent(4071.3));
	}
}
