package me.dablakbandit.bank.inventory.main;

import java.util.function.Consumer;

import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.BankItemPath;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;

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
		setItem(BankItemConfiguration.BANK_MAIN_MONEY, this::addMoneyItem, consumeSound(getMoney(), BankSoundConfiguration.INVENTORY_MENU_OPEN_MONEY));
	}
	
	protected Consumer<CorePlayers> getMoney(){
		return BankInventories.BANK_MONEY;
	}
	
	private ItemStack addMoneyItem(BankItemPath path, BankInfo bankInfo){
		return clone(path.get(), path.getName().replaceAll("<money>", Format.formatMoney(bankInfo.getMoneyInfo().getMoney())));
	}
	
	private void addItem(){
		if(!BankPluginConfiguration.BANK_ITEMS_ENABLED.get()){ return; }
		setItem(BankItemConfiguration.BANK_MAIN_ITEM, consumeSound(getItem(), BankSoundConfiguration.INVENTORY_MENU_OPEN_ITEMS));
	}
	
	protected Consumer<CorePlayers> getItem(){
		return BankInventories.BANK_ITEMS;
	}
	
	private void addExp(){
		if(!BankPluginConfiguration.BANK_EXP_ENABLED.get()){ return; }
		setItem(BankItemConfiguration.BANK_MAIN_EXP, this::addExpItem, consumeSound(getExp(), BankSoundConfiguration.INVENTORY_MENU_OPEN_EXP));
	}
	
	protected Consumer<CorePlayers> getExp(){
		return BankInventories.BANK_EXP;
	}
	
	private ItemStack addExpItem(BankItemPath path, BankInfo bankInfo){
		return clone(path.get(), path.getName().replaceAll("<exp>", "" + (int)Math.floor(bankInfo.getExpInfo().getExp())), path.getLore());
	}
	
	@Override
	public BankInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankInfo.class);
	}
	
}
