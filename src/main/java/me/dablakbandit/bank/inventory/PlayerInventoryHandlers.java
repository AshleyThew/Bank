package me.dablakbandit.bank.inventory;

import me.dablakbandit.bank.inventory.admin.blacklist.BankBlacklistPlayerHandler;
import me.dablakbandit.bank.inventory.admin.item.def.BankItemDefaultPlayerHandler;
import me.dablakbandit.bank.inventory.item.BankItemsPlayerHandler;
import me.dablakbandit.bank.inventory.item.BankTrashcanPlayerHandler;
import me.dablakbandit.core.inventory.PlayerInventoryHandler;

import java.util.function.Supplier;

public enum PlayerInventoryHandlers implements Supplier<PlayerInventoryHandler> {
	//@formatter:off
	BANK_ITEMS(new BankItemsPlayerHandler()),
	TRASHCAN(new BankTrashcanPlayerHandler()),
	DENY(new DenyPlayerHandler()),
	ADMIN_BLACKLIST(new BankBlacklistPlayerHandler()),
	ITEM_DEFAULT(new BankItemDefaultPlayerHandler()),
	;
	//@formatter:on

	private final PlayerInventoryHandler inventoryHandler;

	PlayerInventoryHandlers(PlayerInventoryHandler handler) {
		this.inventoryHandler = handler;
	}

	public PlayerInventoryHandler get() {
		return inventoryHandler;
	}
}
