package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.bank.config.path.BankExtendedPath;
import me.dablakbandit.bank.inventory.head.PlayerHead;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.utils.format.BankColorUtil;
import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.path.ItemPath;
import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BankItemPath extends ItemPath implements BankExtendedPath {

	private int slot;
	private String name;
	private List<String> lore;
	private int[] slots = new int[0];

	private Map<String, Object> extendedValues;

	public BankItemPath(Material material, String name) {
		this(material, 1, name);
	}

	public BankItemPath(Material material, int amount, String name) {
		this(material, amount, 0, name);
	}

	public BankItemPath(Material material, int amount, int durability, String name) {
		this(material, amount, durability, name, Collections.emptyList());
	}

	public BankItemPath(ItemStack def, String name) {
		this(-1, def.clone(), name);
	}

	public BankItemPath(ItemStack def, String name, int[] slots) {
		this(def, name);
		this.slots = slots;
	}

	public BankItemPath(int slot, ItemStack def, String name) {
		this(slot, def.clone(), name, Collections.emptyList());
	}

	public BankItemPath(int slot, ItemStack def, int amount, String name) {
		this(slot, cloneAmount(def, amount), name, Collections.emptyList());
	}

	public BankItemPath(Material material, String name, List<String> lore) {
		this(material, 1, name, lore);
	}

	public BankItemPath(Material material, int amount, String name, List<String> lore) {
		this(-1, material, amount, name, lore);
	}

	public BankItemPath(int slot, Material material, int amount, String name, List<String> lore) {
		this(slot, material, amount, 0, name, lore);
	}

	public BankItemPath(Material material, int amount, int durability, String name, List<String> lore) {
		this(-1, material, amount, durability, name, lore);
	}

	public BankItemPath(int slot, Material material, int amount, int durability, String name, List<String> lore) {
		this(slot, new ItemStack(material, amount, (short) durability), name, lore);
	}

	public BankItemPath(Material material, String name, String... lore) {
		this(-1, material, name, lore);
	}

	public BankItemPath(int slot, Material material, String name, String... lore) {
		this(slot, material, 1, name, lore);
	}

	public BankItemPath(Material material, int amount, String name, String... lore) {
		this(-1, material, amount, name, lore);
	}

	public BankItemPath(int slot, Material material, int amount, String name, String... lore) {
		this(slot, material, amount, 0, name, lore);
	}

	public BankItemPath(Material material, int amount, int durability, String name, String... lore) {
		this(-1, material, amount, durability, name, lore);
	}

	public BankItemPath(int slot, Material material, int amount, int durability, String name, String... lore) {
		this(slot, material, amount, durability, name, Arrays.asList(lore));
	}

	public BankItemPath(ItemStack def, String name, String... lore) {
		this(def, name, Arrays.asList(lore));
	}

	public BankItemPath(ItemStack def, String name, List<String> lore) {
		this(-1, def, name, lore);
	}

	public BankItemPath(int slot, ItemStack def, String name, String... lore) {
		this(slot, def, name, Arrays.asList(lore));
	}

	public BankItemPath(int slot, ItemStack def, String name, List<String> lore) {
		super(addNameLore(def, name, lore));
		this.slot = slot;
		this.name = name;
		this.lore = lore;
	}

	private static ItemStack addNameLore(ItemStack is, String name, List<String> lore) {
		is = is.clone();
		ItemMeta im = is.getItemMeta();
		if (im != null) {
			im.setDisplayName(name);
			im.setLore(lore);
			is.setItemMeta(im);
		}
		return is;
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}

	public int getSlot() {
		return slot;
	}

	public int[] getSlots() {
		return slots;
	}

	protected ItemStack get(RawConfiguration config, String path) {
		ItemStack is = super.get(config, path);
		int amount = is.getAmount();
		if (isSet(path, "Slot")) {
			slot = config.getInt(path + ".Slot", slot);
		} else {
			config.set(path + ".Slot", slot);
			config.saveConfig();
		}
		if (isSet(path, "Slots")) {
			slots = config.getIntegerList(path + ".Slots").stream().mapToInt(i -> i).toArray();
		} else {
			slots = new int[0];
		}
		if (isSet(path, "PlayerHead") && config.getBoolean(path + ".PlayerHead")) {
			PlayerHead.getInstance().set(is);
		}
		if (isSet(path, "Name")) {
			name = BankColorUtil.hex(config.getString(path + ".Name"));
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(name);
			is.setItemMeta(im);
		}
		if (isSet(path, "Lore")) {
			lore = config.getStringList(path + ".Lore").stream().map(BankColorUtil::hex).collect(Collectors.toList());
			ItemMeta im = is.getItemMeta();
			im.setLore(lore);
			is.setItemMeta(im);
		}
		if (isSet(path, "Unbreakable")) {
			ItemMeta im = is.getItemMeta();
			im.setUnbreakable(config.getBoolean(path + ".Unbreakable"));
			is.setItemMeta(im);
		}
		if (isSet(path, "Enchant")) {
			try {
				ItemMeta im = is.getItemMeta();
				im.addEnchant(org.bukkit.enchantments.Enchantment.getByName(config.getString(path + ".Enchant")), 1, true);
				is.setItemMeta(im);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (itemFlagExists && isSet(path, "ItemFlags")) {
			try {
				setItemFlags(is, config, path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (hideTooltipExists && isSet(path, "HideTooltip")) {
			try {
				setHideTooltip(is, config, path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (itemModelExists && isSet(path, "ItemModel")) {
			try {
				setItemModel(is, config, path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (isSet(path, "CustomModelData") && customExists) {
			try {
				setCustomModelData(is, config, path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		is.setAmount(amount);
		return is;
	}

	private static boolean customExists = true;

	static {
		try {
			customExists = NMSUtils.getMethodSilent(ItemMeta.class, "getCustomModelData") != null;
		} catch (Exception ignored) {
		}
	}

	private static boolean itemFlagExists = true;

	static {
		try {
			itemFlagExists = NMSUtils.getClassSilent("org.bukkit.inventory.ItemFlag") != null;
		} catch (Exception ignored) {
		}
	}

	private static boolean hideTooltipExists = true;
	private static Method hideTooltipMethod;

	static {
		try {
			hideTooltipMethod = NMSUtils.getMethodSilent(ItemMeta.class, "setHideTooltip", boolean.class);
			hideTooltipExists = hideTooltipMethod != null;
		} catch (Exception ignored) {
		}
	}

	private static boolean itemModelExists = true;
	private static Method itemModelMethod;
	private static Method namespacedKeyFromString;


	static {
		try {
			Class classNamespacedKey = NMSUtils.getClassSilent("org.bukkit.NamespacedKey");
			namespacedKeyFromString = NMSUtils.getMethodSilent(classNamespacedKey, "fromString", String.class);
			itemModelMethod = NMSUtils.getMethodSilent(ItemMeta.class, "setItemModel", classNamespacedKey);
			itemModelExists = itemModelMethod != null;
		} catch (Exception ignored) {
		}
	}

	protected void setCustomModelData(ItemStack is, RawConfiguration config, String path) {
		ItemMeta im = is.getItemMeta();
		im.setCustomModelData(config.getInt(path + ".CustomModelData"));
		is.setItemMeta(im);
	}

	protected void setItemFlags(ItemStack is, RawConfiguration config, String path) {
		ItemMeta im = is.getItemMeta();
		config.getStringList(path + ".ItemFlags").forEach(flag -> {
			im.addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(flag));
		});
		is.setItemMeta(im);
	}

	protected void setHideTooltip(ItemStack is, RawConfiguration config, String path) {
		try {
			ItemMeta im = is.getItemMeta();
			hideTooltipMethod.invoke(im, config.getBoolean(path + ".HideTooltip"));
			is.setItemMeta(im);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setItemModel(ItemStack is, RawConfiguration config, String path) {
		try {
			ItemMeta im = is.getItemMeta();
			Object key = namespacedKeyFromString.invoke(null, config.getString(path + ".ItemModel"));
			itemModelMethod.invoke(im, key);
			is.setItemMeta(im);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Object setAs(RawConfiguration config, ItemStack itemStack) {
		super.setAs(config, itemStack);
		String path = getActualPath();
		ItemMeta im = itemStack.getItemMeta();
		config.set(path + ".Slot", slot);
		if (im != null) {
			if (im.hasDisplayName()) {
				config.set(path + ".Name", im.getDisplayName().replaceAll("§", "&"));
			}
			if (im.hasLore()) {
				config.set(path + ".Lore", im.getLore().stream().map(s -> s.replaceAll("§", "&")).collect(Collectors.toList()));
			}
		}
		if (slots.length > 0) {
			config.set(path + ".Slots", Arrays.stream(slots).boxed().collect(Collectors.toList()));
		}
		return null;
	}

	private String file;

	public String getFile() {
		return file;
	}

	@Override
	public void setFile(String file) {
		this.file = file;
	}

	public void setExtendedValues(Map<String, Object> extendedValues) {
		this.extendedValues = extendedValues;
	}

	@SuppressWarnings("unchecked")
	public <T> T getExtendValue(String key, Class<T> clazz) {
		if (this.extendedValues == null || !this.extendedValues.containsKey(key)) {
			BankLog.errorAlert("No extended value found for key " + key + " in path " + getActualPath() + " in file " + this.file);
			return null;
		}
		return (T) this.extendedValues.get(key);
	}

	public boolean isValidItem() {
		try {
			return get() != null;
		} catch (Exception e) {
			return false;
		}
	}

	private static ItemStack cloneAmount(ItemStack is, int amount) {
		ItemStack cloned = is.clone();
		cloned.setAmount(amount);
		return cloned;
	}

}
