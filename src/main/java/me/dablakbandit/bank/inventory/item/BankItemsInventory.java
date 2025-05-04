package me.dablakbandit.bank.inventory.item;

import me.dablakbandit.bank.config.*;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.inventory.*;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.item.BankItem;
import me.dablakbandit.bank.utils.format.BankColorUtil;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.EXPUtils;
import me.dablakbandit.core.vault.Eco;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class BankItemsInventory extends BankInventoryHandler<BankInfo> {

	@Override
	public void init() {
		addBack();
		setItem(BankItemConfiguration.BANK_ITEM_BLANK);
		addSlots();
		addTabs();
		addTrashcan();
		addItemUtils();
		addScrolls();
		int size = descriptor.getSize();
		addItems(size);
		addTabsItems();
	}

	public ItemStack getBack(BankItemPath path, BankInfo bankInfo) {
		if (bankInfo.getOpenTypes().contains(OpenTypes.ALL) || bankInfo.getOpenTypes().contains(OpenTypes.MENU)) {
			return path.get();
		}
		return BankItemConfiguration.BANK_ITEM_BLANK.get();
	}

	private void addBack() {
		if (!BankPluginConfiguration.BANK_ITEMS_ONLY.get()) {
			setItem(BankItemConfiguration.BANK_ITEM_BACK, this::getBack, consumeSound(this::returnToMainMenu, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
		}
	}

	protected void returnToMainMenu(CorePlayers pl) {
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU);
	}

	private void addSlots() {
		setItem(BankItemConfiguration.BANK_ITEM_SLOTS, this::getSlots, BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_ENABLED.get() ? consumeSound(getBuySlots(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_BUY_SLOTS) : this::doNothing);
	}

	@SuppressWarnings("EmptyMethod")
	private void doNothing(CorePlayers pl) {

	}

	private ItemStack getSlots(BankItemPath path, BankInfo bankInfo) {
		BankItemsInfo itemsInfo = bankInfo.getItemsInfo();
		int used = itemsInfo.getBankItemsHandler().getTotalBankSize(itemsInfo.getOpenTab());
		int total = itemsInfo.getBankItemsHandler().getBankSlots(itemsInfo.getOpenTab());
		int available = total - used;
		ItemStack itemStack = replaceNameLore(path, "<used>", "" + used, "<available>", "" + available, "<total>", "" + total);
		if (path.getExtendValue("Sized", Boolean.class)) {
			itemStack.setAmount(Math.max(1, available));
		}
		return itemStack;
	}

	private void addTabs() {
		setItem(BankItemConfiguration.BANK_ITEM_TABS, this::getTabs, BankPluginConfiguration.BANK_ITEMS_TABS_BUY_ENABLED.get() ? consumeSound(getBuyTabs(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_BUY_TABS) : this::doNothing);
	}

	private ItemStack getTabs(BankItemPath path, BankInfo bankInfo) {
		return replaceNameLore(path, "<tabs>", "" + bankInfo.getItemsInfo().getTotalTabCount(), "<bought_tabs>", "" + bankInfo.getItemsInfo().getBoughtTabs());
	}

	protected void addTrashcan() {
		if (!BankPluginConfiguration.BANK_ITEMS_TRASHCAN_ENABLED.get()) {
			return;
		}
		setItem(BankItemConfiguration.BANK_ITEM_TRASHCAN, consumeSound(getTrashcan(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_TRASHCAN));
	}

	protected Consumer<CorePlayers> getTrashcan() {
		return BankInventories.BANK_ITEMS_TRASHCAN;
	}

	protected void addItemUtils() {
		setItem(BankItemConfiguration.BANK_ITEM_ADD, consumeSound(getItemsAdd(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_ADD));
		setItem(BankItemConfiguration.BANK_ITEM_REMOVE, consumeSound(getItemsRemove(), BankSoundConfiguration.INVENTORY_ITEMS_OPEN_REMOVE));
		setItem(BankItemConfiguration.BANK_ITEM_SORT.getSlot(), BankItemConfiguration.BANK_ITEM_BLANK);
	}

	protected Consumer<CorePlayers> getBuySlots() {
		return BankInventories.BANK_ITEMS_BUY_SLOTS;
	}

	protected Consumer<CorePlayers> getBuyTabs() {
		return BankInventories.BANK_ITEMS_BUY_TABS;
	}

	protected Consumer<CorePlayers> getItemsAdd() {
		return BankInventories.BANK_ITEMS_ADD;
	}

	protected Consumer<CorePlayers> getItemsRemove() {
		return BankInventories.BANK_ITEMS_REMOVE;
	}

	private void addScrolls() {
		setItem(BankItemConfiguration.BANK_ITEM_SCROLL_UP, (a, b) -> {
			addScroll(a, b, -1);
			BankSoundConfiguration.INVENTORY_ITEMS_SCROLL_UP.play(a);
		});
		setItem(BankItemConfiguration.BANK_ITEM_SCROLL_DOWN, (a, b) -> {
			addScroll(a, b, 1);
			BankSoundConfiguration.INVENTORY_ITEMS_SCROLL_DOWN.play(a);
		});
	}

	private void addScroll(CorePlayers pl, BankInfo bi, int add) {
		bi.getItemsInfo().addScrolled(add);
		pl.refreshInventory();
	}

	private void addItems(int size) {
		//int show = BankItemConfiguration.BANK_ITEM_ITEMS.getExtendValue("Slots", Integer.class);
		int start = BankItemConfiguration.BANK_ITEM_ITEMS.getExtendValue("Start", Integer.class);
		int width = BankItemConfiguration.BANK_ITEM_ITEMS.getExtendValue("Width", Integer.class);
		int rows = BankItemConfiguration.BANK_ITEM_ITEMS.getExtendValue("Rows", Integer.class);
		int slot = 0;
		for (int row = 0; row < rows; row++) {
			for (int addX = 0; addX < width; addX++) {
				int inventorySlot = row * 9 + addX + start;
				int finalSlot = slot;
				if (inventorySlot > size) {
					break;
				}
				setItem(inventorySlot, (bi) -> getItemStack(bi, width, finalSlot), (pl, bi, inv, event) -> onClick(pl, bi, event, finalSlot, width));
				slot++;
			}
		}
	}

	private void addTabsItems() {
		if (!BankPluginConfiguration.BANK_ITEMS_TABS_ENABLED.get()) {
			return;
		}

		Integer[] tabs = BankItemConfiguration.BANK_ITEM_TAB_NUMBER.getExtendValue("Tabs", Integer[].class);

		// Determine if the player is able to scroll left or right based on tab count


		for (int index = 0; index < tabs.length; index++) {
			int slot = tabs[index];
			int tabIndex = index + 1;
			setItem(slot, (bi, i) -> createTab(bi, tabIndex, tabs.length), this::handleTab);
		}
	}

	@Override
	public BankInfo getInvoker(CorePlayers pl) {
		return pl.getInfo(BankInfo.class);
	}

	protected int determineTab(BankInfo bankInfo, int index, int tabsLength) {
		BankItemsInfo itemsInfo = bankInfo.getItemsInfo();
		int openTab = itemsInfo.getOpenTab();

		int maxTab = Math.max(itemsInfo.getMaxTabNotEmpty() + 1, BankPluginConfiguration.BANK_ITEMS_TABS_MAX.get());
		int half = (int) Math.ceil(tabsLength / 2.0);

		int result;
		if (openTab <= half) {
			result = index;
		} else if (openTab > maxTab - half) {
			result = maxTab - tabsLength + index;
		} else {
			result = openTab - half + index;
		}

		return result;
	}

	public ItemStack createTab(BankInfo bankInfo, int index, int tabsLength) {
		BankItemPath pathItem;
		BankItemPath pathDescription = null;
		BankItemsInfo itemsInfo = bankInfo.getItemsInfo();

		int tab = determineTab(bankInfo, index, tabsLength);
		if (itemsInfo.getOpenTab() == tab) {
			pathItem = BankItemConfiguration.BANK_ITEM_TAB_CURRENT;
		} else if (index == 1 && tab != index) {
			pathItem = BankItemConfiguration.BANK_ITEM_TAB_LEFT;
		} else if (index == tabsLength && tab != index) {
			pathItem = BankItemConfiguration.BANK_ITEM_TAB_RIGHT;
		} else if (tab > itemsInfo.getTotalTabCount()) {
			pathItem = BankItemConfiguration.BANK_ITEM_TAB_LOCKED;
		} else {
			pathItem = BankItemConfiguration.BANK_ITEM_TAB_NUMBER;
		}
		if (tab > itemsInfo.getTotalTabCount()) {
			pathDescription = BankItemConfiguration.BANK_ITEM_TAB_LOCKED;
		}
		if (pathDescription == null) {
			pathDescription = pathItem;
		}

		ItemStack is = pathItem.get();
		if (BankPluginConfiguration.BANK_ITEMS_TABS_AMOUNT_SET.get()) {
			is.setAmount(Math.min(tab, is.getMaxStackSize()));
		}
		String name = pathDescription.getName();
		if (BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_ENABLED.get()) {
			String set = bankInfo.getItemsInfo().getTabNameMap().get(tab);
			if (set != null) {
				name = set;
			}
		}
		return replaceCloneNameLore(is, name, pathDescription.getLore(), "<tab>", "" + tab, "<items>", "" + itemsInfo.getBankItemsHandler().getTabSize(tab));
	}

	public void onClick(CorePlayers pl, BankInfo bi, InventoryClickEvent event, int slot, int width) {
		event.setCancelled(true);
		if (isHotbarSwap(event)) {
			if (BankPluginConfiguration.BANK_ITEMS_HOTBAR_SWAP_ENABLED.get()) {
				handleHotbarSwap(pl, bi.getItemsInfo(), event, slot, width);
			}
			return;
		}
		ItemStack is = event.getCursor();
		if (is != null && is.getType() != Material.AIR) {
			handleItemInput(pl, bi.getItemsInfo(), is, event, slot, width);
		} else {
			handleItemTake(pl, bi.getItemsInfo(), event, slot, width);
		}
	}

	private void handleHotbarSwap(CorePlayers pl, BankItemsInfo bi, InventoryClickEvent event, int slot, int width) {
		// Swap items if possible
		int playerSlot = event.getHotbarButton();
		if (playerSlot == -1) {
			return;
		}
		if (BankItemBlacklistConfiguration.BLACKLISTED_PLAYER_SLOTS.get().contains(playerSlot)) {
			return;
		}
		int finalSlot = slot + bi.getScrolled() * width;
		bi.getBankItemsHandler().swapBankItems(pl.getPlayer(), bi.getOpenTab(), finalSlot, playerSlot);
		pl.refreshInventory();
	}

	private boolean isHotbarSwap(InventoryClickEvent event) {
		return event.getAction().equals(InventoryAction.HOTBAR_SWAP) || event.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD);
	}

	private void handleItemInput(CorePlayers pl, BankItemsInfo bi, ItemStack is, InventoryClickEvent event, int slot, int width) {
		int finalSlot = slot + bi.getScrolled() * width;

		ItemStack result;
		if (BankPluginConfiguration.BANK_ITEMS_ANYWHERE_ENABLED.get()) {
			result = bi.getBankItemsHandler().addBankItemAtSlot(pl.getPlayer(), is, bi.getOpenTab(), finalSlot, false);
		} else {
			result = bi.getBankItemsHandler().addBankItem(pl.getPlayer(), is, bi.getOpenTab(), false);
		}
		event.getWhoClicked().setItemOnCursor(result);
		pl.refreshInventory();
		if (result == null) {
			BankSoundConfiguration.INVENTORY_ITEMS_ITEM_ADD.play(pl);
		} else {
			BankSoundConfiguration.INVENTORY_ITEMS_ITEM_FULL.play(pl);
		}
	}

	private void handleItemTake(CorePlayers pl, BankItemsInfo bi, InventoryClickEvent event, int slot, int width) {
		ItemStack is = event.getCurrentItem();
		if (is == null || is.getType().equals(Material.AIR)) {
			return;
		}
		int finalSlot = slot + bi.getScrolled() * width;
		BankItem item = bi.getBankItemsHandler().getBankItemAtSlot(finalSlot, bi.getOpenTab());
		if (item == null) {
			if (BankPluginConfiguration.BANK_ITEMS_SLOTS_LOCKED_ENABLED.get() && finalSlot >= bi.getBankItemsHandler().getBankSlots(bi.getOpenTab())) {
				if (BankPluginConfiguration.BANK_ITEMS_SLOTS_LOCKED_CLICK.get()) {
					if (bi.getBankItemsHandler().buySlots(pl, 1)) {
						pl.refreshInventory();
					}
				}
			}
			return;
		}
		int total = Math.min(item.getItemStack().getMaxStackSize(), item.getAmount());
		if (event.isRightClick()) {
			total = (int) Math.ceil(total / 2.0);
		}
		if (bi.getBankItemsHandler().takeBankItemAt(pl.getPlayer(), bi.getOpenTab(), finalSlot, total)) {
			BankSoundConfiguration.INVENTORY_ITEMS_ITEM_TAKE.play(pl);
			pl.refreshInventory();
		}
	}

	protected void handleTab(CorePlayers pl, BankInfo bi, Inventory inv, InventoryClickEvent event) {
		int rawSlot = event.getRawSlot();
		List<Integer> tabs = Arrays.asList(BankItemConfiguration.BANK_ITEM_TAB_NUMBER.getExtendValue("Tabs", Integer[].class));
		int tabIndex = tabs.indexOf(rawSlot) + 1;
		int tab = determineTab(bi, tabIndex, tabs.size());
		if (event.isRightClick()) {
			if (BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_ENABLED.get() && BankPermissionConfiguration.PERMISSION_TAB_RENAME.has(pl.getPlayer())) {
				handleTabRename(pl, bi, tab);
				return;
			}
		}
		if (bi.getItemsInfo().getOpenTab() == tab) {
			return;
		}
		bi.getItemsInfo().setOpenTab(tab);
		bi.getItemsInfo().addScrolled(-bi.getItemsInfo().getScrolled());
		pl.refreshInventory();
		BankSoundConfiguration.INVENTORY_ITEMS_CHANGE_TAB.play(pl);
	}

	protected void handleTabRename(CorePlayers pl, BankInfo bi, int tab) {
		String current = bi.getItemsInfo().getTabNameMap().get(tab);
		if (current == null) {
			current = " ";
		}
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_ITEM_TAB_RENAME.get().replace("<i>", current), current) {
			@Override
			public void cancel(CorePlayers pl) {
				pl.setOpenInventory(BankItemsInventory.this);
			}

			@Override
			public void close(CorePlayers pl) {
				pl.setOpenInventory(BankItemsInventory.this);
			}

			@Override
			public void onClick(CorePlayers pl, String value) {
				if (value.startsWith(" ")) {
					value = value.substring(1);
				}
				if (!value.isEmpty()) {
					renameTab(pl, bi, tab, ChatColor.RESET + BankColorUtil.hex(value));
				}
				pl.setOpenInventory(BankItemsInventory.this);
			}
		});
	}

	protected void renameTab(CorePlayers pl, BankInfo bankInfo, int tab, String name) {
		if (BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_MONEY.get() > 0) {
			if (!Eco.getInstance().getEconomy().has(pl.getPlayer(), BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_MONEY.get())) {
				return;
			}
		}
		if (BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_EXP.get() > 0) {
			if (EXPUtils.getExp(pl.getPlayer()) < BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_EXP.get()) {
				return;
			}
		}
		if (BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_MONEY.get() > 0) {
			Eco.getInstance().getEconomy().withdrawPlayer(pl.getPlayer(), BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_MONEY.get());
		}
		if (BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_EXP.get() > 0) {
			int exp = EXPUtils.getExp(pl.getPlayer());
			exp -= BankPluginConfiguration.BANK_ITEMS_TABS_RENAME_COST_EXP.get();
			EXPUtils.setExp(pl.getPlayer(), exp);
		}
		bankInfo.getItemsInfo().getTabNameMap().put(tab, name);
	}

	protected ItemStack getItemStack(BankInfo bi, int width, Integer slot) {
		BankItemsInfo itemsInfo = bi.getItemsInfo();
		int finalSlot = slot + itemsInfo.getScrolled() * width;
		BankItem slotItem = itemsInfo.getBankItemsHandler().getBankItemAtSlot(finalSlot, itemsInfo.getOpenTab());
		if (BankPluginConfiguration.BANK_ITEMS_SLOTS_LOCKED_ENABLED.get() && slotItem == null) {
			if (finalSlot >= itemsInfo.getBankItemsHandler().getBankSlots(itemsInfo.getOpenTab())) {
				return replaceNameLore(BankItemConfiguration.BANK_ITEM_LOCKED, "<cost>", "" + BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_COST.get());
			}
		}
		ItemStack is = slotItem == null ? null : slotItem.getItemStack().clone();
		if (is != null) {
			if (BankPluginConfiguration.BANK_ITEMS_SLOTS_MERGE_ENABLED.get()) {
				is.setAmount(Math.max(1, Math.min(is.getMaxStackSize(), slotItem.getAmount())));
				String format = String.format("%,d", slotItem.getAmount());
				is = add(is, ChatColor.YELLOW + BankLanguageConfiguration.MESSAGE_ITEM_MERGE_COUNT.get().replaceAll("<amount>", String.valueOf(slotItem.getAmount())).replaceAll("<formatted_amount>", format));
			} else {
				is.setAmount(slotItem.getAmount());
			}
		}
		return is;
	}

	public void set(CorePlayers pl, Player player, Inventory inventory) {
		ensureScrolled(getInvoker(pl).getItemsInfo());
		super.set(pl, player, inventory);
	}

	protected void ensureScrolled(BankItemsInfo info) {
		if (info.getOpenTab() <= 0) {
			info.setOpenTab(1);
		}

		int width = BankItemConfiguration.BANK_ITEM_ITEMS.getExtendValue("Width", Integer.class);

		int max = Math.max(0, (int) Math.ceil(info.getBankItemsHandler().getTabMax(info.getOpenTab()) / (double) width));
		int current = info.getScrolled();
		int fixed = Math.min(max, current);
		if (current != fixed) {
			info.setScrolled(fixed);
		}
	}
}
