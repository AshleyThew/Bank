package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.path.Path;

import java.util.*;

public class BankLoansPaybackFailedPath extends Path<BankLoansPaybackFailedPath.LoansPaybackFailed> {

	public BankLoansPaybackFailedPath(LoansPaybackFailed def) {
		super(def);
	}

	protected Object setAs(RawConfiguration config, LoansPaybackFailed failed) {
		String path = this.getActualPath();
		for (Map.Entry<Integer, List<String>> e : failed.getMap().entrySet()) {
			config.set(path + ".Commands." + e.getKey(), e.getValue());
		}
		return null;
	}

	@Override
	protected LoansPaybackFailed get(RawConfiguration config, String path) {
		LoansPaybackFailed value = new LoansPaybackFailed();
		if (this.isSet(path, "Commands")) {
			for (String key : config.getConfigurationSection(path + ".Commands").getKeys(false)) {
				try {
					value.add(Integer.parseInt(key), config.getStringList(path + ".Commands." + key));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	public static class LoansPaybackFailed {

		private final Map<Integer, List<String>> map = new TreeMap<>();

		public LoansPaybackFailed() {
		}

		@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
		public LoansPaybackFailed(String command) {
			map.put(1, Arrays.asList(command));
		}

		private void add(int key, List<String> value) {
			map.put(key, value);
		}

		public Map<Integer, List<String>> getMap() {
			return map;
		}

		public List<String> getCommands(int at) {
			List<String> list = Collections.emptyList();
			for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
				if (entry.getKey() < at) {
					list = entry.getValue();
				}
			}
			return list;
		}
	}
}
