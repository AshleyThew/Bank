package me.dablakbandit.bank.inventory.path;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.bank.config.path.BankItemPath;
import me.dablakbandit.bank.utils.format.Format;

public class BankItemPathReplacer<T>{
	
	private List<PathReplacer<T>> replacers = new ArrayList();
	
	public void add(PathReplacer replacer){
		replacers.add(replacer);
	}
	
	public void addMoney(String from, Function<T, Double> function){
		add(new PathReplacer<T>(from, (t) -> Format.formatMoney(function.apply(t))));
	}
	
	protected static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
	
	public void addDate(String from, Function<T, Long> function){
		add(new PathReplacer<T>(from, (t) -> sdf.format(new Date(function.apply(t)))));
	}
	
	public void add(String from, Function<T, String> function){
		add(new PathReplacer<T>(from, (t) -> function.apply(t)));
	}
	
	public ItemStack apply(BankItemPath path, T t){
		ItemStack is = path.get();
		String name = path.getName();
		ItemMeta itemMeta = is.getItemMeta();
		if(name != null){
			for(PathReplacer<T> replacer : replacers){
				name = replacer.apply(name, t);
			}
			itemMeta.setDisplayName(name);
		}
		List<String> lore = path.getLore();
		if(lore.size() > 0){
			List<String> newLore = new ArrayList<>();
			for(String s : lore){
				for(PathReplacer<T> replacer : replacers){
					s = replacer.apply(s, t);
				}
				newLore.add(s);
			}
			itemMeta.setLore(newLore);
		}
		is.setItemMeta(itemMeta);
		return is;
	}
	
	public static class PathReplacer<T>{
		
		private String				from;
		private Function<T, String>	function;
		
		public PathReplacer(String from, Function<T, String> function){
			this.from = from;
			this.function = function;
		}
		
		public String apply(String string, T value){
			return string.replaceAll(from, function.apply(value));
		}
	}
}
