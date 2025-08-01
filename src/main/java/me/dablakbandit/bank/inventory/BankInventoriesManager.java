package me.dablakbandit.bank.inventory;

import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.player.PlayerChecks;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankPinInfo;
import me.dablakbandit.core.inventory.InventoryHandler;
import me.dablakbandit.core.inventory.InventoryHandlers;
import me.dablakbandit.core.players.CorePlayers;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class BankInventoriesManager {

	private static final BankInventoriesManager inventoryHandler = new BankInventoriesManager();
	private static final PlayerChecks playerChecks = PlayerChecks.getInstance();

	public static BankInventoriesManager getInstance() {
		return inventoryHandler;
	}

	private InventoryHandlers handlers;
	private final Map<BankInventories, InventoryHandler<?>> handlerMap = new EnumMap<>(BankInventories.class);

	private BankInventoriesManager() {

	}

	public void load() {
		handlers = InventoryHandlers.createHandlers(BankInventories.class, BankInventories::getInventory);
		Arrays.stream(BankInventories.values()).forEach((i) -> handlerMap.put(i, handlers.createInventory(i)));
	}

	public boolean open(CorePlayers pl, final BankInventories inventories) {
		if (playerChecks.checkWorldDisabled(pl.getPlayer())) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_WORLD_DISABLED.get());
			return false;
		}
		if (playerChecks.checkGamemodeDisabled(pl.getPlayer())) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_WORLD_DISABLED.get());
			return false;
		}
		BankInventories checkedInventories = checkOnlys(pl, inventories);
		if (!checkTypes(pl, checkedInventories)) {
			return false;
		}
		InventoryHandler<?> handler = handlerMap.get(checkedInventories);
		if (!handler.hasPermission(pl.getPlayer())) {
			return false;
		}
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		if (bankInfo.isLocked(true, () -> open(pl, inventories))) {
			return false;
		}
		pl.setOpenInventory(handler);
		return true;
	}

	public BankInventories getBankInventories(OpenTypes... openTypes) {
		if (openTypes.length == 0) {
			return BankInventories.BANK_MAIN_MENU;
		}
		switch (openTypes[0]) {
			case ALL:
			case MENU:
				return BankInventories.BANK_MAIN_MENU;
			case EXP:
				return BankInventories.BANK_EXP;
			case ITEMS:
				return BankInventories.BANK_ITEMS;
			case MONEY:
				return BankInventories.BANK_MONEY;
		}
		return BankInventories.BANK_MAIN_MENU;
	}

	public boolean open(CorePlayers pl, final BankInventories inventories, OpenTypes... openTypes) {
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		bankInfo.setOpenTypes(openTypes);
		return open(pl, inventories);
	}

	public boolean openBypass(CorePlayers pl, BankInventories inventories) {
		InventoryHandler<?> handler = handlerMap.get(inventories);
		if (!handler.hasPermission(pl.getPlayer())) {
			return false;
		}
		pl.setOpenInventory(handler);
		return true;
	}

	private BankInventories checkOnlys(CorePlayers pl, BankInventories inventories) {
		if (inventories != BankInventories.BANK_MAIN_MENU) {
			return inventories;
		}
		if (BankPluginConfiguration.BANK_MONEY_ONLY.get()) {
			inventories = BankInventories.BANK_MONEY;
		} else if (BankPluginConfiguration.BANK_EXP_ONLY.get()) {
			inventories = BankInventories.BANK_EXP;
		} else if (BankPluginConfiguration.BANK_ITEMS_ONLY.get()) {
			inventories = BankInventories.BANK_ITEMS;
		} else {
			inventories = checkPin(pl, inventories);
		}
		return inventories;
	}

	private boolean checkTypes(CorePlayers pl, BankInventories inventories) {
		if (!BankPluginConfiguration.BANK_OPENTYPE_SUBSET_ENABLED.get() || inventories.getOpenType() == null) {
			return true;
		}
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		return bankInfo.getOpenTypes().contains(OpenTypes.ALL) || bankInfo.getOpenTypes().contains(inventories.getOpenType());
	}

	private BankInventories checkPin(CorePlayers pl, BankInventories inventories) {
		if (!BankPluginConfiguration.BANK_PIN_ENABLED.get()) {
			return inventories;
		}
		if (!pl.getInfo(BankPinInfo.class).hasPassed()) {
			BankInventories finalInventories = inventories;
			pl.getInfo(BankPinInfo.class).setAfter(() -> open(pl, finalInventories));
			inventories = BankInventories.BANK_PIN_ENTER;
		}
		return inventories;
	}
}
