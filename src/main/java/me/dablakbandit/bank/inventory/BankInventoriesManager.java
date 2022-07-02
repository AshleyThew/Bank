package me.dablakbandit.bank.inventory;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.player.PlayerChecks;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankPinInfo;
import me.dablakbandit.core.inventory.InventoryHandler;
import me.dablakbandit.core.inventory.InventoryHandlers;
import me.dablakbandit.core.players.CorePlayers;

public class BankInventoriesManager{
	
	private static final BankInventoriesManager	inventoryHandler	= new BankInventoriesManager();
	private static final PlayerChecks			playerChecks		= PlayerChecks.getInstance();
	
	public static BankInventoriesManager getInstance(){
		return inventoryHandler;
	}
	
	private InventoryHandlers								handlers;
	private final Map<BankInventories, InventoryHandler<?>>	handlerMap	= new EnumMap<>(BankInventories.class);
	
	private BankInventoriesManager(){
		
	}
	
	public void load(){
		handlers = InventoryHandlers.createHandlers(BankInventories.class, BankInventories::getInventory);
		Arrays.stream(BankInventories.values()).forEach((i) -> handlerMap.put(i, handlers.createInventory(i)));
	}
	
	public boolean open(CorePlayers pl, final BankInventories inventories){
		if(playerChecks.checkWorldDisabled(pl.getPlayer())){
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_WORLD_DISABLED.get());
			return false;
		}
		if(playerChecks.checkGamemodeDisabled(pl.getPlayer())){
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_WORLD_DISABLED.get());
			return false;
		}
		BankInventories checkedInventories = checkOnlys(pl, inventories);
		InventoryHandler<?> handler = handlerMap.get(checkedInventories);
		if(!handler.hasPermission(pl.getPlayer())){ return false; }
		if(pl.getInfo(BankInfo.class).isLocked(true, () -> open(pl, inventories))){ return false; }
		pl.setOpenInventory(handler);
		return true;
	}
	
	public boolean openBypass(CorePlayers pl, BankInventories inventories){
		InventoryHandler<?> handler = handlerMap.get(inventories);
		if(!handler.hasPermission(pl.getPlayer())){ return false; }
		pl.setOpenInventory(handler);
		return true;
	}
	
	private BankInventories checkOnlys(CorePlayers pl, BankInventories inventories){
		if(inventories != BankInventories.BANK_MAIN_MENU){ return inventories; }
		if(BankPluginConfiguration.BANK_MONEY_ONLY.get()){
			inventories = BankInventories.BANK_MONEY;
		}else if(BankPluginConfiguration.BANK_EXP_ONLY.get()){
			inventories = BankInventories.BANK_EXP;
		}else if(BankPluginConfiguration.BANK_ITEMS_ONLY.get()){
			inventories = BankInventories.BANK_ITEMS;
		}else{
			inventories = checkPin(pl, inventories);
		}
		return inventories;
	}
	
	private BankInventories checkPin(CorePlayers pl, BankInventories inventories){
		if(!BankPluginConfiguration.BANK_PIN_ENABLED.get()){ return inventories; }
		if(!pl.getInfo(BankPinInfo.class).hasPassed()){
			BankInventories finalInventories = inventories;
			pl.getInfo(BankPinInfo.class).setAfter(() -> open(pl, finalInventories));
			inventories = BankInventories.BANK_PIN_ENTER;
		}
		return inventories;
	}
}
