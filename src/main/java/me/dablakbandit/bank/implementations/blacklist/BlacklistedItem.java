package me.dablakbandit.bank.implementations.blacklist;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.itemutils.IItemUtils;
import me.dablakbandit.core.utils.json.JSONInit;
import me.dablakbandit.core.utils.json.JSONParser;
import me.dablakbandit.core.utils.json.strategy.Exclude;

public class BlacklistedItem implements JSONInit{
	
	private static final IItemUtils	itemUtils	= ItemUtils.getInstance();
	
	private boolean				matchData, matchNBT;
	private final ItemStack			itemStack;
	
	@Exclude
	private JsonObject			nbt;
	
	public BlacklistedItem(ItemStack itemStack){
		this.itemStack = itemStack;
		this.itemStack.setAmount(1);
		initJson();
	}
	
	@Override
	public void initJson(){
		try{
			Object nmis = ItemUtils.getInstance().getNMSCopy(itemStack);
			Object tag = ItemUtils.getInstance().getTag(nmis);
			JSONObject jo = new JSONObject();
			if(tag != null){
				itemUtils.convertCompoundTagToJSON(tag, jo);
			}
			jo.remove("Damage");
			nbt = JSONParser.parse(jo.toString()).getAsJsonObject();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean equals(ItemStack is){
		if(is.getType() != itemStack.getType()){ return false; }
		if(matchData && itemStack.getDurability() != is.getDurability()){ return false; }
		if(matchNBT){
			try{
				Object nmis = ItemUtils.getInstance().getNMSCopy(is);
				Object tag = ItemUtils.getInstance().getTag(nmis);
				if(tag == null){
					if(nbt == null){ return true; }
				}else{
					if(nbt == null){ return false; }
					JSONObject jo = new JSONObject();
					ItemUtils.getInstance().convertCompoundTagToJSON(tag, jo);
					jo.remove("Damage");
					return nbt.equals(JSONParser.parse(jo.toString()).getAsJsonObject());
				}
			}catch(Exception e){
				return false;
			}
		}
		return true;
	}
	
	public ItemStack getItemStack(){
		return itemStack;
	}
	
	public boolean isMatchData(){
		return matchData;
	}
	
	public void toggleMatchData(){
		this.matchData = !matchData;
	}
	
	public void setMatchData(boolean matchData){
		this.matchData = matchData;
	}
	
	public boolean isMatchNBT(){
		return matchNBT;
	}
	
	public void toggleMatchNBT(){
		this.matchNBT = !matchNBT;
	}
	
	public void setMatchNBT(boolean matchNBT){
		this.matchNBT = matchNBT;
	}
	
}
