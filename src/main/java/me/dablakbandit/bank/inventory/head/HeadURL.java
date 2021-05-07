package me.dablakbandit.bank.inventory.head;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.NMSUtils;

/**
 * The type Head url.
 */
public class HeadURL{
	
	private static HeadURL heads = new HeadURL();
	
	/**
	 * Get instance head url.
	 *
	 * @return the head url
	 */
	public static HeadURL getInstance(){
		return heads;
	}
	
	private static ItemStack clone;
	
	private HeadURL(){
		Material m = ItemUtils.getInstance().getMaterial("SKULL_ITEM", "PLAYER_SKULL", "PLAYER_HEAD");
		if(m.name().equals("SKULL_ITEM")){
			clone = new ItemStack(m, 1, (short)3);
		}else{
			clone = new ItemStack(m);
		}
	}
	
	private static Field profile = NMSUtils.getFirstFieldOfType(NMSUtils.getOBCClass("inventory.CraftMetaSkull"), GameProfile.class);
	
	public ItemStack getHead(String url){
		ItemStack is = clone.clone();
		try{
			SkullMeta sm = (SkullMeta)is.getItemMeta();
			GameProfile gp = new GameProfile(UUID.randomUUID(), "Skin");
			gp.getProperties().clear();
			gp.getProperties().put("textures", new Property("textures", url));
			profile.set(sm, gp);
			is.setItemMeta(sm);
		}catch(Exception e){
			e.printStackTrace();
		}
		return is;
	}
}
