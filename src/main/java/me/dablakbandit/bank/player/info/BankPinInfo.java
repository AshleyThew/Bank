package me.dablakbandit.bank.player.info;

import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.JSONInfo;
import me.dablakbandit.core.utils.json.strategy.Exclude;

public class BankPinInfo extends IBankInfo implements JSONInfo {

	private String pin;

	@Exclude
	public boolean passed;

	@Exclude
	public String tempPin = "";

	@Exclude
	public Runnable after;

	public BankPinInfo(CorePlayers pl) {
		super(pl);
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public boolean hasPassed() {
		boolean pass = !BankPluginConfiguration.BANK_PIN_ENABLED.get();
		pass |= !hasPin();
		pass |= this.passed;
		return pass;
	}

	public boolean hasPin() {
		return this.pin != null;
	}

	public String getTempPin() {
		return tempPin;
	}

	public void clearTempPin() {
		this.tempPin = "";
	}

	public boolean checkPinPass(String add) {
		this.tempPin += add;
		if (tempPin.equals(pin)) {
			passed = true;
			return true;
		}
		if (this.tempPin.length() >= 4) {
			clearTempPin();
			return true;
		}
		return false;
	}

	public boolean checkPinSet(String add) {
		this.tempPin += add;
		if (this.tempPin.length() == 4) {
			this.pin = this.tempPin;
			clearTempPin();
			return true;
		}
		return false;
	}

	public void checkPass(Runnable runnable) {
		if (pl.getInfo(BankInfo.class).isLocked(true, () -> checkPass(runnable))) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_BANK_LOCKED.get());
			return;
		}
		if (hasPassed()) {
			runnable.run();
		} else {
			setAfter(runnable);
			BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_PIN_ENTER);
		}
	}

	public void setAfter(Runnable runnable) {
		this.after = runnable;
	}

	public void run() {
		if (after != null) {
			after.run();
			after = null;
		}
	}

	@Override
	public void jsonInit() {

	}

	@Override
	public void jsonFinal() {

	}
}
