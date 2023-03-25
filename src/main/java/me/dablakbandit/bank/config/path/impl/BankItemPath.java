package me.dablakbandit.bank.config.path.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.dablakbandit.bank.config.path.BankExtendedPath;
import me.dablakbandit.bank.inventory.head.PlayerHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.path.ItemPath;
import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class BankItemPath extends ItemPath implements BankExtendedPath {
	
	private int				slot;
	private String			name;
	private List<String>	lore;
	private int[]			slots	= new int[0];

	private Map<String, Object> extendedValues;
	
	public BankItemPath(Material material, String name){
		this(material, 1, name);
	}
	
	public BankItemPath(Material material, int amount, String name){
		this(material, amount, 0, name);
	}
	
	public BankItemPath(Material material, int amount, int durability, String name){
		this(material, amount, durability, name, Collections.emptyList());
	}
	
	public BankItemPath(ItemStack def, String name){
		this(-1, def, name);
	}
	
	public BankItemPath(int slot, ItemStack def, String name){
		this(slot, def, name, Collections.emptyList());
	}
	
	public BankItemPath(Material material, String name, List<String> lore){
		this(material, 1, name, lore);
	}
	
	public BankItemPath(Material material, int amount, String name, List<String> lore){
		this(-1, material, amount, name, lore);
	}
	
	public BankItemPath(int slot, Material material, int amount, String name, List<String> lore){
		this(slot, material, amount, 0, name, lore);
	}
	
	public BankItemPath(Material material, int amount, int durability, String name, List<String> lore){
		this(-1, material, amount, durability, name, lore);
	}
	
	public BankItemPath(int slot, Material material, int amount, int durability, String name, List<String> lore){
		this(slot, new ItemStack(material, amount, (short)durability), name, lore);
	}
	
	public BankItemPath(Material material, String name, String... lore){
		this(-1, material, name, lore);
	}
	
	public BankItemPath(int slot, Material material, String name, String... lore){
		this(slot, material, 1, name, lore);
	}
	
	public BankItemPath(Material material, int amount, String name, String... lore){
		this(-1, material, amount, name, lore);
	}
	
	public BankItemPath(int slot, Material material, int amount, String name, String... lore){
		this(slot, material, amount, 0, name, lore);
	}
	
	public BankItemPath(Material material, int amount, int durability, String name, String... lore){
		this(-1, material, amount, durability, name, lore);
	}
	
	public BankItemPath(int slot, Material material, int amount, int durability, String name, String... lore){
		this(slot, material, amount, durability, name, Arrays.asList(lore));
	}
	
	public BankItemPath(ItemStack def, String name, String... lore){
		this(def, name, Arrays.asList(lore));
	}
	
	public BankItemPath(ItemStack def, String name, List<String> lore){
		this(-1, def, name, lore);
	}
	
	public BankItemPath(int slot, ItemStack def, String name, String... lore){
		this(slot, def, name, Arrays.asList(lore));
	}
	
	public BankItemPath(int slot, ItemStack def, String name, List<String> lore){
		super(addNameLore(def, name, lore));
		this.slot = slot;
		this.name = name;
		this.lore = lore;
	}
	
	private static ItemStack addNameLore(ItemStack is, String name, List<String> lore){
		is = is.clone();
		ItemMeta im = is.getItemMeta();
		if(im != null){
			im.setDisplayName(name);
			im.setLore(lore);
			is.setItemMeta(im);
		}
		return is;
	}
	
	public String getName(){
		return name;
	}
	
	public List<String> getLore(){
		return lore;
	}
	
	public int getSlot(){
		return slot;
	}
	
	public int[] getSlots(){
		return slots;
	}
	
	protected ItemStack get(RawConfiguration config, String path){
		ItemStack is = super.get(config, path);
		int amount = is.getAmount();
		if(isSet(path, "Slot")){
			slot = config.getInt(path + ".Slot", slot);
		}else{
			config.set(path + ".Slot", slot);
			config.saveConfig();
		}
		if(isSet(path, "Slots")){
			slots = config.getIntegerList(path + ".Slots").stream().mapToInt(i -> i).toArray();
		}
		if(isSet(path, "PlayerHead") && config.getBoolean(path + ".PlayerHead")){
			PlayerHead.getInstance().set(is);
		}
		if(isSet(path, "Name")){
			name = ChatColor.translateAlternateColorCodes('&', config.getString(path + ".Name"));
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(name);
			is.setItemMeta(im);
		}
		if(isSet(path, "Lore")){
			lore = config.getStringList(path + ".Lore").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
			ItemMeta im = is.getItemMeta();
			im.setLore(lore);
			is.setItemMeta(im);
		}
		if(isSet(path, "Unbreakable")){
			try{
				Object nms = ItemUtils.getInstance().getNMSCopy(is);
				Object ta = ItemUtils.getInstance().getTag(nms);
				ItemUtils.getInstance().setUnbreakable(ta, true);
				ItemUtils.getInstance().setTag(nms, ta);
				is = ItemUtils.getInstance().asBukkitCopy(nms);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(isSet(path, "Enchant")){
			try{
				ItemMeta im = is.getItemMeta();
				im.addEnchant(Enchantment.getByName(config.getString(path + ".Enchant")), 1, true);
				is.setItemMeta(im);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(isSet(path, "CustomModelData") && customExists){
			try{
				setCustomModelData(is, config, path);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		is.setAmount(amount);
		return is;
	}
	
	private static boolean customExists = true;
	
	static{
		try{
			customExists = NMSUtils.getMethodSilent(ItemMeta.class, "getCustomModelData") != null;
		}catch(Exception ignored){
		}
	}
	
	protected void setCustomModelData(ItemStack is, RawConfiguration config, String path){
		ItemMeta im = is.getItemMeta();
		im.setCustomModelData(config.getInt(path + ".CustomModelData"));
		is.setItemMeta(im);
	}
	
	@Override
	protected Object setAs(RawConfiguration config, ItemStack itemStack){
		super.setAs(config, itemStack);
		String path = getActualPath();
		ItemMeta im = itemStack.getItemMeta();
		config.set(path + ".Slot", slot);
		if(im != null){
			if(im.hasDisplayName()){
				config.set(path + ".Name", im.getDisplayName().replaceAll("§", "&"));
			}
			if(im.hasLore()){
				config.set(path + ".Lore", im.getLore().stream().map(s -> s.replaceAll("§", "&")).collect(Collectors.toList()));
			}
		}
		return null;
	}

	public void setExtendedValues(Map<String, Object> extendedValues){
		this.extendedValues = extendedValues;
	}

	@SuppressWarnings("unchecked")
	public <T> T getExtendValue(String key, Class<T> clazz){
		if(this.extendedValues == null){
			return null;
		}
		return (T) this.extendedValues.get(key);
	}

}
