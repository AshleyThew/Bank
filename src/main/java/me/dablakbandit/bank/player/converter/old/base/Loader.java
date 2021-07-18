package me.dablakbandit.bank.player.converter.old.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.core.database.listener.SQLListener;
import me.dablakbandit.core.json.JSONArray;
import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.Version;
import me.dablakbandit.core.utils.itemutils.EnchantmentFixer;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;

public abstract class Loader extends SQLListener{
	
	public abstract void load(CorePlayers pl);
	
	public abstract void delete(String uuid);
	
	public abstract boolean exists(String uuid);
	
	public abstract String getUUID(String name);
	
	public abstract String getUsername(String uuid);
	
	public abstract void setUsername(String uuid, String name);
	
	public abstract void forceLoad(CorePlayers pl);
	
	public abstract boolean isConnected();
	
	protected final Map<Integer, List<ItemStack>> convertJSONToItems(CorePlayers pl, String s) throws Exception{
		Map<Integer, List<ItemStack>> items = new HashMap<>();
		if(s == null){ return items; }
		JSONObject jo = new JSONObject(s);
		for(int tab = 1; tab <= 9; tab++){
			items.put(tab, new ArrayList<>());
			if(jo.has("" + tab)){
				JSONArray ja = new JSONArray(jo.get("" + tab).toString());
				for(int ii = 0; ii < ja.length(); ii++){
					JSONObject jo1 = null;
					try{
						jo1 = ja.getJSONObject(ii);
						ItemStack is;
						is = convertJSONToItemStack(jo1);
						items.get(tab).add(is);
					}catch(Exception e){
						e.printStackTrace();
						if(jo1 != null){
							System.out.println("Failed to load item: " + e.getMessage() + " : " + jo1.toString());
						}
						throw e;
					}
				}
			}
		}
		return items;
	}
	
	protected final void fixTags(JSONObject jo1) throws Exception{
		if(jo1.has("display")){
			JSONArray ja2 = jo1.getJSONArray("display");
			JSONObject jo2 = ja2.getJSONObject(1);
			if(jo2.has("Name")){
				JSONArray ja3 = jo2.getJSONArray("Name");
				String s = ja3.getString(1);
				try{
					JSONObject jo3 = new JSONObject(s);
				}catch(Exception e){
					BankLog.error("Fixing name for " + ja2.toString());
					JSONFormatter jf = new JSONFormatter();
					jf.append(s);
					ja3.remove(1);
					ja3.put(jf.toJSON());
					BankLog.error("Fixed name for " + ja2.toString());
				}
			}
			if(isFourteen()){
				if(jo2.has("Lore")){
					JSONArray ja3 = jo2.getJSONArray("Lore");
					JSONArray ja4 = ja3.getJSONArray(1);
					for(int i = 0; i < ja4.length(); i++){
						JSONArray ja5 = ja4.getJSONArray(i);
						String s = ja5.getString(1);
						try{
							JSONObject jo3 = new JSONObject(s);
						}catch(Exception e){
							BankLog.error("Fixing lore for " + ja2.toString());
							JSONFormatter jf = new JSONFormatter();
							jf.append(s);
							ja5.remove(1);
							ja5.put(jf.toJSON());
							BankLog.error("Fixed lore for " + ja2.toString());
						}
					}
				}
			}
		}
		if(jo1.has("ench")){
			JSONArray jsonArray = new JSONArray();
			try{
				jsonArray = jo1.getJSONArray("ench").getJSONArray(1);
			}catch(Exception ignored){
			}
			fixEnchantments(jsonArray);
		}
		if(jo1.has("StoredEnchantments")){
			fixEnchantments(jo1.getJSONArray("StoredEnchantments").getJSONArray(1));
		}
	}
	
	private static final boolean fourteen = isFourteen();
	
	private static boolean isFourteen(){
		try{
			Material m = Material.BARREL;
			return true;
		}catch(Throwable var1){
			return false;
		}
	}
	
	public static boolean isAtleastFourteen(){
		return fourteen;
	}
	
	protected final void fixEnchantments(JSONArray ja) throws Exception{
		for(int i = 0; i < ja.length(); i++){
			JSONArray ja2 = ja.getJSONArray(i);
			JSONObject jo = ja2.getJSONObject(1);
			if(jo.has("id")){
				JSONArray ja1 = jo.getJSONArray("id");
				if(ja1.getInt(0) == 2){
					BankLog.error("Fixing enchantment for " + jo.toString());
					int id = ja1.getInt(1);
					EnchantmentFixer ef = EnchantmentFixer.match(id);
					if(ef != null){
						ja1.remove(0);
						ja1.remove(0);
						ja1.put(8);
						ja1.put(ef.getMcname());
						BankLog.error("Fixed enchantment for " + jo.toString());
					}else{
						BankLog.error("Unable to fix enchantment for " + jo.toString());
					}
				}
			}
		}
	}
	
	protected final ItemStack convertJSONToItemStack(JSONObject jo) throws Exception{
		Material material;
		try{
			material = Material.valueOf(jo.getString("material"));
		}catch(Exception e){
			material = Material.valueOf("LEGACY_" + jo.getString("material"));
		}
		int amount = jo.getInt("amount");
		int durability = jo.getInt("durability");
		ItemStack is = new ItemStack(material, amount, (short)durability);
		if(Version.isAtleastThirteen() && material.isLegacy()){
			Object nmis = ItemUtils.getInstance().getNMSCopy(is);
			is = ItemUtils.getInstance().asBukkitCopy(nmis);
		}
		if(!jo.has("tag"))
			return is;
		JSONObject jo1 = jo.getJSONObject("tag");
		if(jo1.length() == 0)
			return is;
		Object tag = null;
		
		if(Version.isAtleastThirteen()){
			fixTags(jo1);
		}
		
		if(jo.has("helper")){
			JSONObject helper = jo.getJSONObject("helper");
			if(jo1.length() > 0){
				tag = ItemUtils.getInstance().convertJSONToCompoundTag(jo1, helper);
			}
		}else{
			tag = ItemUtils.getInstance().convertJSONToCompoundTag(jo1);
		}
		Object nmis = ItemUtils.getInstance().getNMSCopy(is);
		ItemUtils.getInstance().setTag(nmis, tag);
		is = ItemUtils.getInstance().asBukkitCopy(nmis);
		return is;
	}
	
	protected final String convertItemsToJSON(Map<Integer, List<ItemStack>> items){
		try{
			JSONObject jo = new JSONObject();
			for(Map.Entry<Integer, List<ItemStack>> e : items.entrySet()){
				int page = e.getKey();
				List<ItemStack> list = e.getValue();
				JSONArray ja = new JSONArray();
				for(ItemStack is : list){
					try{
						if(is != null && !is.getType().equals(Material.AIR)){
							JSONObject jo1 = new JSONObject();
							jo1.put("material", is.getType().name());
							jo1.put("amount", is.getAmount());
							jo1.put("durability", is.getDurability());
							JSONObject jo2 = new JSONObject();
							Object nmis = ItemUtils.getInstance().getNMSCopy(is);
							Object tag = ItemUtils.getInstance().getTag(nmis);
							if(tag != null){
								ItemUtils.getInstance().convertCompoundTagToJSON(tag, jo2);
								jo1.put("tag", jo2);
							}
							ja.put(jo1);
						}
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}
				if(ja.length() > 0){
					jo.put("" + page, ja);
				}
			}
			if(jo.length() > 0){ return jo.toString(); }
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	protected Map<Integer, ItemStack> convertJSONToTabs(BankItemsInfo bii, String s){
		Map<Integer, ItemStack> items = new HashMap<>();
		if(s == null){ return items; }
		try{
			JSONObject jo = new JSONObject(s);
			for(int i = 1; i <= 9; i++){
				if(jo.has("" + i)){
					JSONObject jo1 = jo.getJSONObject("" + i);
					items.put(i, new ItemStack(Material.valueOf(jo1.getString("Material")), 0, (short)jo1.getInt("Damage")));
				}
			}
			if(jo.has("bought")){
				bii.setBoughtTabs(jo.getInt("bought"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return items;
	}
	
	public abstract int getTotal();
}
