package me.dablakbandit.bank.implementations.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;

public class BankPlaceholderManager{
	
	private static final BankPlaceholderManager manager = new BankPlaceholderManager();
	
	public static BankPlaceholderManager getInstance(){
		return manager;
	}
	
	private final Map<String, Placeholder>	placeholderMap		= new HashMap<>();
	private final Map<String, Placeholder>	shortPlaceholderMap	= new HashMap<>();
	
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
		add(placeholder, clazz, (pl, val) -> converter.apply(function.apply(val)));
	}
	
	private <T extends CorePlayersInfo> void addCoreInteger(String placeholder, Class<T> clazz, Function<T, Integer> function, Function<Integer, String> converter){
		add(placeholder, clazz, (pl, val) -> converter.apply(function.apply(val)));
	}
	
	private <T> void addInteger(String placeholder, T t, Function<T, Integer> function, Function<Integer, String> converter){
		add(placeholder, Object.class, (pl, ignore) -> converter.apply(function.apply(t)));
	}
	
	private <T extends CorePlayersInfo> void addCorePlaceholder(String placeholder, Class<T> clazz, Function<T, String> function){
		add(placeholder, clazz, (pl, val) -> function.apply(val));
	}
	
	private <T> void add(String placeholder, Class<T> clazz, BiFunction<CorePlayers, T, String> function){
		Placeholder<T> value = new Placeholder(placeholder, clazz, function);
		placeholderMap.put(placeholder, value);
		shortPlaceholderMap.put(placeholder.replaceFirst("bank_", ""), value);
	}
	
	public static class Placeholder<T>{
		
		private final String							placeholder;
		private final Class<T> clazz;
		private final BiFunction<CorePlayers, T, String>	replaced;
		
		public Placeholder(String placeholder, Class<T> clazz, BiFunction<CorePlayers, T, String> replaced){
			this.placeholder = placeholder;
			this.clazz = clazz;
			this.replaced = replaced;
		}
		
		public String get(CorePlayers pl){
			T value = null;
			if(clazz != null && CorePlayersInfo.class.isAssignableFrom(clazz)){
				value = pl.getType(clazz);
			}
			return replaced.apply(pl, value);
		}
		
		public String replace(CorePlayers pl, String info){
			if(info.contains("<" + placeholder + ">")) {
				return info.replaceAll("<" + placeholder + ">", get(pl));
			}
			return info;
		}
	}
}
