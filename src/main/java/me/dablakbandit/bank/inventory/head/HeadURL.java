package me.dablakbandit.bank.inventory.head;

import java.lang.reflect.Field;
import java.util.Base64;
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
	
	private static final HeadURL heads = new HeadURL();
	
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
	
	private static final Field profile = NMSUtils.getFirstFieldOfType(NMSUtils.getOBCClass("inventory.CraftMetaSkull"), GameProfile.class);
	
	public ItemStack get(String value){
		if(value.startsWith("http://") || value.startsWith("https://")){
			return getHeadUrl(value);
		}
		return getHead(value);
	}

	public ItemStack getHead(String hash){
		ItemStack is = clone.clone();
		try{
			SkullMeta sm = (SkullMeta)is.getItemMeta();
			GameProfile gp = new GameProfile(UUID.randomUUID(), "Skin");
			gp.getProperties().clear();
			gp.getProperties().put("textures", new Property("textures", hash));
			profile.set(sm, gp);
			is.setItemMeta(sm);
		}catch(Exception e){
			e.printStackTrace();
		}
		return is;
	}

	public ItemStack getHeadUrl(String url){
		String toHash = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
		String hash = Base64.getEncoder().encodeToString(toHash.getBytes());
		return getHead(hash);
	}
}
