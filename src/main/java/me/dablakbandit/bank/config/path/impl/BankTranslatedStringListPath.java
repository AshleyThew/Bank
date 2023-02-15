package me.dablakbandit.bank.config.path.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.path.ListPath;

public class BankTranslatedStringListPath extends ListPath<String>{
	
	public BankTranslatedStringListPath(List<String> def){
		super(def);
	}
	
	public BankTranslatedStringListPath(String... def){
		super(new ArrayList<>(Arrays.asList(def)));
	}
	
	protected List<String> get(RawConfiguration config, String path){
		return config.getStringList(path).stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
	}
	
	protected Object setAs(List<String> list){
		return list.stream().map(s -> s.replaceAll("§", "&")).collect(Collectors.toList());
	}
}
