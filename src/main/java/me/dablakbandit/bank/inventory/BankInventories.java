package me.dablakbandit.bank.inventory;

import java.util.function.Consumer;
import java.util.function.Supplier;

import me.dablakbandit.bank.config.BankInventoryConfiguration;
import me.dablakbandit.bank.inventory.admin.blacklist.BankBlacklistInventory;
import me.dablakbandit.bank.inventory.admin.blacklist.BankBlacklistItemInventory;
import me.dablakbandit.bank.inventory.admin.item.def.BankItemDefaultInventory;
import me.dablakbandit.bank.inventory.admin.permission.BankPermissionHistoryInventory;
import me.dablakbandit.bank.inventory.exp.BankExpInventory;
import me.dablakbandit.bank.inventory.item.*;
import me.dablakbandit.bank.inventory.main.BankMainMenuInventory;
import me.dablakbandit.bank.inventory.money.BankMoneyInventory;
import me.dablakbandit.bank.inventory.pin.BankPinEnterInventory;
import me.dablakbandit.bank.inventory.pin.BankPinMenuInventory;
import me.dablakbandit.bank.inventory.pin.BankPinSetInventory;
import me.dablakbandit.core.config.path.InventoryDescriptionPath;
import me.dablakbandit.core.inventory.InventoryHandler;
import me.dablakbandit.core.inventory.PlayerInventoryHandler;
import me.dablakbandit.core.players.CorePlayers;

public enum BankInventories implements Consumer<CorePlayers>{
	//@formatter:off
	BANK_ITEMS(BankItemsInventory::new, BankInventoryConfiguration.BANK_ITEMS, PlayerInventoryHandlers.BANK_ITEMS),
	BANK_ITEMS_ADD(BankAddItemsInventory::new, BankInventoryConfiguration.BANK_ITEMS_ADD, PlayerInventoryHandlers.DENY),
	BANK_ITEMS_REMOVE(BankRemoveItemsInventory::new, BankInventoryConfiguration.BANK_ITEMS_REMOVE, PlayerInventoryHandlers.DENY),
	BANK_ITEMS_TRASHCAN(BankTrashcanInventory::new, BankInventoryConfiguration.BANK_ITEMS_TRASHCAN, PlayerInventoryHandlers.TRASHCAN),
	BANK_ITEMS_BUY_TABS(BankBuyTabsInventory::new, BankInventoryConfiguration.BANK_ITEMS_BUY_TABS, PlayerInventoryHandlers.DENY),
	BANK_ITEMS_BUY_SLOTS(BankBuySlotsInventory::new, BankInventoryConfiguration.BANK_ITEMS_BUY_SLOTS, PlayerInventoryHandlers.DENY),
	BANK_MAIN_MENU(BankMainMenuInventory::new, BankInventoryConfiguration.BANK_MAIN_MENU, PlayerInventoryHandlers.DENY),
	BANK_EXP(BankExpInventory::new, BankInventoryConfiguration.BANK_EXP, PlayerInventoryHandlers.DENY),
	BANK_MONEY(BankMoneyInventory::new, BankInventoryConfiguration.BANK_MONEY, PlayerInventoryHandlers.DENY),
	BANK_PIN_MENU(BankPinMenuInventory::new, BankInventoryConfiguration.BANK_PIN_MENU, PlayerInventoryHandlers.DENY),
	BANK_PIN_SET(BankPinSetInventory::new, BankInventoryConfiguration.BANK_PIN_SET, PlayerInventoryHandlers.DENY),
	BANK_PIN_ENTER(BankPinEnterInventory::new, BankInventoryConfiguration.BANK_PIN_ENTER, PlayerInventoryHandlers.DENY),
	
	BANK_ADMIN_PERMISSION_HISTORY(BankPermissionHistoryInventory::new, BankInventoryConfiguration.BANK_ADMIN_PERMISSION_HISTORY, PlayerInventoryHandlers.DENY),
	BANK_ADMIN_BLACKLIST(BankBlacklistInventory::new, BankInventoryConfiguration.BANK_ADMIN_BLACKLIST, PlayerInventoryHandlers.ADMIN_BLACKLIST),
	BANK_ADMIN_BLACKLIST_ITEM(BankBlacklistItemInventory::new, BankInventoryConfiguration.BANK_ADMIN_BLACKLIST_ITEM, PlayerInventoryHandlers.DENY),
	BANK_ADMIN_ITEM_DEFAULT(BankItemDefaultInventory::new, BankInventoryConfiguration.BANK_ADMIN_ITEM_DEFAULT, PlayerInventoryHandlers.ITEM_DEFAULT),
    ;
    //@formatter:on
	
	private final Supplier<InventoryHandler<?>>	supplier;
	private final InventoryDescriptionPath		descriptionPath;
	private final PlayerInventoryHandler			playerInventoryHandler;
	
	BankInventories(Supplier<InventoryHandler<?>> supplier, InventoryDescriptionPath descriptionPath, Supplier<PlayerInventoryHandler> playerInventoryHandler){
		this.supplier = supplier;
		this.descriptionPath = descriptionPath;
		this.playerInventoryHandler = playerInventoryHandler.get();
	}
	
	public InventoryHandler<?> getInventory(){
		InventoryHandler<?> handler = supplier.get();
		handler.setDescriptor(descriptionPath.get());
		handler.setup();
		handler.setPlayerInventoryHandler(playerInventoryHandler);
		return handler;
	}
	
	@Override
	public void accept(CorePlayers pl){
		BankInventoriesManager.getInstance().open(pl, this);
	}
}
