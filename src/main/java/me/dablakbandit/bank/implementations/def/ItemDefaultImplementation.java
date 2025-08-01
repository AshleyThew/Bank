package me.dablakbandit.bank.implementations.def;

import me.dablakbandit.bank.config.BankItemDefaultConfiguration;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.core.utils.json.JSONParser;

import java.util.List;
import java.util.stream.Collectors;

public class ItemDefaultImplementation extends BankImplementation {

	private static final ItemDefaultImplementation implementation = new ItemDefaultImplementation();

	public static ItemDefaultImplementation getInstance() {
		return implementation;
	}

	private boolean enabled = false;
	private final List<ItemDefault> defaults;

	private ItemDefaultImplementation() {
		this.defaults = BankItemDefaultConfiguration.ITEMS.get().stream().map(string -> JSONParser.fromJSON(string, ItemDefault.class)).collect(Collectors.toList());
	}

	@Override
	public void load() {

	}

	public void save() {
		BankItemDefaultConfiguration.ITEMS.set(defaults.stream().map(bi -> JSONParser.toJson(bi).toString()).collect(Collectors.toList()));
	}

	public List<ItemDefault> getDefault() {
		return defaults;
	}

	@Override
	public void enable() {
		enabled = true;
	}

	@Override
	public void disable() {
		enabled = false;
	}
}
