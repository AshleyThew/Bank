package me.dablakbandit.bank.implementations.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;

public class BankPlaceholderManager{
	
	private static BankPlaceholderManager manager = new BankPlaceholderManager();
	
	public static BankPlaceholderManager getInstance(){
		return manager;
	}
	
	private Map<String, Placeholder>	placeholderMap		= new HashMap<>();
	private Map<String, Placeholder>	shortPlaceholderMap	= new HashMap<>();
	
	private BankPlaceholderManager(){
		init();
	}
	
	public String replace(CorePlayers pl, String info){
		for(Map.Entry<String, Placeholder> entry : placeholderMap.entrySet()){
			info = entry.getValue().replace(pl, info);
		}
		return info;
	}
	
	private void init(){
		addCoreDouble("bank_money", BankMoneyInfo.class, BankMoneyInfo::getMoney, Format::formatMoney);
		addCoreDouble("bank_exp", BankExpInfo.class, BankExpInfo::getExp, Format::formatExp);
		addCoreInteger("bank_exp_level", BankExpInfo.class, BankExpInfo::getExpLevel, String::valueOf);
		addCoreInteger("bank_slots", BankItemsInfo.class, BankItemsInfo::getTotalSlots, String::valueOf);
		addCoreInteger("bank_used_slots", BankItemsInfo.class, BankItemsInfo::getTotalUsedSlots, String::valueOf);
	}
	
	private <T extends CorePlayersInfo> void addCoreDouble(String placeholder, Class<T> clazz, Function<T, Double> function, Function<Double, String> converter){
		add(placeholder, pl -> converter.apply(function.apply(pl.getInfo(clazz))));
	}
	
	private <T extends CorePlayersInfo> void addCoreInteger(String placeholder, Class<T> clazz, Function<T, Integer> function, Function<Integer, String> converter){
		add(placeholder, pl -> converter.apply(function.apply(pl.getInfo(clazz))));
	}
	
	private <T> void addInteger(String placeholder, T t, Function<T, Integer> function, Function<Integer, String> converter){
		add(placeholder, pl -> converter.apply(function.apply(t)));
	}
	
	private <T extends CorePlayersInfo> void addCorePlaceholder(String placeholder, Class<T> clazz, Function<T, String> function){
		add(placeholder, (pl) -> function.apply(pl.getInfo(clazz)));
	}
	
	private void add(String placeholder, Function<CorePlayers, String> function){
		Placeholder value = new Placeholder(placeholder, function);
		placeholderMap.put(placeholder, value);
		shortPlaceholderMap.put(placeholder.replaceFirst("bank_", ""), value);
	}
	
	public static class Placeholder{
		
		private String							placeholder;
		private Function<CorePlayers, String>	replaced;
		
		public Placeholder(String placeholder, Function<CorePlayers, String> replaced){
			this.placeholder = placeholder;
			this.replaced = replaced;
		}
		
		public String get(CorePlayers pl){
			return replaced.apply(pl);
		}
		
		public String replace(CorePlayers pl, String info){
			return info.replaceAll("<" + placeholder + ">", get(pl));
		}
	}
}
