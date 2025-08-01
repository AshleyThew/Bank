package me.dablakbandit.bank.api;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.utils.format.Format;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.ArrayList;
import java.util.List;

public class Economy_Bank extends AbstractEconomy {

	private static Economy_Bank economy;

	public static void setup(BankPlugin bankPlugin) {
		economy = new Economy_Bank(bankPlugin);
	}

	public static Economy_Bank getInstance() {
		return economy;
	}

	private static BankPlugin PLUGIN;
	private static BankAPI API;

	public Economy_Bank(BankPlugin plugin) {
		PLUGIN = plugin;
		API = BankAPI.getInstance();
	}

	@Override
	public boolean isEnabled() {
		return PLUGIN.isEnabled();
	}

	@Override
	public String getName() {
		return "Bank Economy";
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	public String format(double amount) {
		return Format.formatMoney(amount);
	}

	@Override
	public String currencyNamePlural() {
		return "";
	}

	@Override
	public String currencyNameSingular() {
		return "";
	}

	@Override
	public boolean hasAccount(String name) {
		String uuid = API.getUUID(name);
		if (uuid == null) {
			return false;
		}
		return API.exists(uuid);
	}

	@Override
	public boolean hasAccount(String name, String world) {
		return hasAccount(name);
	}

	@Override
	public double getBalance(String name) {
		String uuid = API.getUUID(name);
		if (uuid == null) {
			return 0.0;
		}
		return BankAPI.getInstance().getMoney(uuid);
	}

	@Override
	public double getBalance(String name, String world) {
		return getBalance(name);
	}

	@Override
	public boolean has(String name, double amount) {
		return getBalance(name) >= amount;
	}

	@Override
	public boolean has(String name, String world, double amount) {
		return has(name, amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, double amount) {
		if (amount < 0.0D) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
		}
		String uuid = API.getUUID(name);
		if (uuid == null) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Unknown bank player " + name);
		}
		if (!API.isOnline(uuid) && API.isLocked(uuid)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank player " + name + " is locked");
		}
		double has = API.getMoney(uuid);
		if (has < amount) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Not enough balance");
		}
		has -= amount;
		API.setMoney(uuid, has);
		return new EconomyResponse(amount, has, EconomyResponse.ResponseType.SUCCESS, null);
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, String world, double amount) {
		return withdrawPlayer(name, amount);
	}

	@Override
	public EconomyResponse depositPlayer(String name, double amount) {
		if (amount < 0.0D) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
		}
		String uuid = API.getUUID(name);
		if (uuid == null) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Unknown bank player " + name);
		}
		if (!API.isOnline(uuid) && API.isLocked(uuid)) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Bank player " + name + " is locked");
		}
		double has = API.getMoney(uuid);
		has += amount;
		if (has <= 0.0) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Invalid bank transaction");
		}
		API.setMoney(uuid, has);
		return new EconomyResponse(amount, has, EconomyResponse.ResponseType.SUCCESS, null);
	}

	@Override
	public EconomyResponse depositPlayer(String name, String world, double amount) {
		return depositPlayer(name, amount);
	}

	@Override
	public EconomyResponse createBank(String name, String world) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank does not support bank accounts!");
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank does not support bank accounts!");
	}

	@Override
	public EconomyResponse isBankOwner(String name, String amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank does not support bank accounts!");
	}

	@Override
	public EconomyResponse isBankMember(String name, String amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank does not support bank accounts!");
	}

	@Override
	public List<String> getBanks() {
		return new ArrayList<>();
	}

	@Override
	public boolean createPlayerAccount(String name) {
		return true;
	}

	@Override
	public boolean createPlayerAccount(String name, String world) {
		return createPlayerAccount(name);
	}
}
