package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.path.Path;

import java.util.Map;
import java.util.TreeMap;

public class BankIntegerMapPath extends Path<BankIntegerMapPath.IntegerMap> {

	public BankIntegerMapPath(IntegerMap def) {
		super(def);
	}

	public BankIntegerMapPath(Map<Integer, Integer> def) {
		super(new IntegerMap(def));
	}

	@Override
	protected Object setAs(RawConfiguration config, IntegerMap value) {
		String path = this.getActualPath();
		for (Map.Entry<Integer, Integer> entry : value.getMap().entrySet()) {
			config.set(path + "." + entry.getKey(), entry.getValue());
		}
		return new TreeMap<>(value.getMap());
	}

	@Override
	protected IntegerMap get(RawConfiguration config, String path) {
		IntegerMap value = new IntegerMap();
		if (this.isSet(path)) {
			for (String key : config.getConfigurationSection(path).getKeys(false)) {
				try {
					value.add(Integer.parseInt(key), config.getInt(path + "." + key));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	public static class IntegerMap {

		private final TreeMap<Integer, Integer> map = new TreeMap<>();

		public IntegerMap() {
		}

		public IntegerMap(Map<Integer, Integer> def) {
			if (def != null) {
				map.putAll(def);
			}
		}

		private void add(int key, int value) {
			map.put(key, value);
		}

		public Map<Integer, Integer> getMap() {
			return map;
		}

		public int getFloorValue(int at) {
			Map.Entry<Integer, Integer> entry = map.floorEntry(at);
			if (entry != null) {
				return entry.getValue();
			}
			return map.isEmpty() ? 0 : map.firstEntry().getValue();
		}
	}
}