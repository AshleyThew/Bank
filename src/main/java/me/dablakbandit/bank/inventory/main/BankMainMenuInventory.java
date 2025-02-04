package me.dablakbandit.bank.inventory.main;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.inventory.OpenTypes;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class BankMainMenuInventory extends BankInventoryHandler<BankInfo>{
	
	@Override
	public void init(){
		int size = descriptor.getSize();
		setAll(size, BankItemConfiguration.BANK_ITEM_BLANK);
		addPin();
		addMoney();
		addItem();
		addExp();
	}
	
	private void addPin(){
		if(!BankPluginConfiguration.BANK_PIN_ENABLED.get()){ return; }
		setItem(BankItemConfiguration.BANK_MAIN_PIN, consumeSound(getPin(), BankSoundConfiguration.INVENTORY_MENU_OPEN_PIN));
	}
	
	protected Consumer<CorePlayers> getPin(){
		return BankInventories.BANK_PIN_MENU;
	}
	
	private void addMoney(){
		if(!BankPluginConfiguration.BANK_MONEY_ENABLED.get()){ return; }
		setItem(BankItemConfiguration.BANK_MAIN_MONEY, (path, t) -> getItem(path, t, this::addMoneyItem, OpenTypes.MONEY), consumeSound(getMoney(), BankSoundConfiguration.INVENTORY_MENU_OPEN_MONEY));
	}
	
	protected Consumer<CorePlayers> getMoney(){
		return BankInventories.BANK_MONEY;
	}
	
	private ItemStack addMoneyItem(BankItemPath path, BankInfo bankInfo){
		return replaceNameLore(path, "<money>", Format.formatMoney(bankInfo.getMoneyInfo().getMoney()));
	}
	
	private void addItem(){
		if(!BankPluginConfiguration.BANK_ITEMS_ENABLED.get()){ return; }
		setItem(BankItemConfiguration.BANK_MAIN_ITEM, (path, t) -> getItem(path, t, this::addItem, OpenTypes.ITEMS), consumeSound(getItem(), BankSoundConfiguration.INVENTORY_MENU_OPEN_ITEMS));
	}

	private ItemStack addItem(BankItemPath path, BankInfo bankInfo) {
		return path.get();
	}
	
	protected Consumer<CorePlayers> getItem(){
		return BankInventories.BANK_ITEMS;
	}
	
	private void addExp(){
		if(!BankPluginConfiguration.BANK_EXP_ENABLED.get()){ return; }
		setItem(BankItemConfiguration.BANK_MAIN_EXP, (path, t) -> getItem(path, t, this::addExpItem, OpenTypes.EXP), consumeSound(getExp(), BankSoundConfiguration.INVENTORY_MENU_OPEN_EXP));
	}
	
	protected Consumer<CorePlayers> getExp(){
		return BankInventories.BANK_EXP;
	}

	private ItemStack getItem(BankItemPath path, BankInfo bankInfo, BiFunction<BankItemPath, BankInfo, ItemStack> supplier, OpenTypes check) {
		if (bankInfo.getOpenTypes().contains(OpenTypes.ALL) || bankInfo.getOpenTypes().contains(check)) {
			return supplier.apply(path, bankInfo);
		}
		return BankItemConfiguration.BANK_ITEM_BLANK.get();
	}
	
	private ItemStack addExpItem(BankItemPath path, BankInfo bankInfo){
		return replaceNameLore(path, "<exp>", "" + (int)Math.floor(bankInfo.getExpInfo().getExp()));
	}
	
	@Override
	public BankInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankInfo.class);
	}
	
}
