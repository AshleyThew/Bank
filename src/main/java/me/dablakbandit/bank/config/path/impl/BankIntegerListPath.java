package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.path.ListPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankIntegerListPath extends ListPath<Integer> {

	public BankIntegerListPath(List<Integer> def) {
		super(def);
	}

	public BankIntegerListPath(Integer... def) {
		super(new ArrayList<>(Arrays.asList(def)));
	}

	protected List<Integer> get(RawConfiguration config, String path) {
		return new ArrayList<>(config.getIntegerList(path));
	}

	protected Object setAs(List<Integer> list) {
		return new ArrayList<>(list);
	}
}
