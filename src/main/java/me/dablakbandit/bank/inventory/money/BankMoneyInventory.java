package me.dablakbandit.bank.inventory.money;

import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.BankItemPath;
import me.dablakbandit.bank.inventory.AnvilInventory;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.vault.Eco;

public class BankMoneyInventory extends BankInventoryHandler<BankMoneyInfo>{
	
	@Override
	public void init(){
		int size = descriptor.getSize();
		setAll(size, BankItemConfiguration.BANK_ITEM_BLANK);
		addBack();
		setItem(BankItemConfiguration.BANK_MONEY_WITHDRAWALL, consumeSound(this::withdrawAll, BankSoundConfiguration.INVENTORY_MONEY_WITHDRAW_ALL));
		setItem(BankItemConfiguration.BANK_MONEY_WITHDRAW, consumeSound(this::withdraw, BankSoundConfiguration.INVENTORY_MONEY_WITHDRAW));
		setItem(BankItemConfiguration.BANK_MONEY_BALANCE, this::getBalance);
		setItem(BankItemConfiguration.BANK_MONEY_DEPOSIT, consumeSound(this::deposit, BankSoundConfiguration.INVENTORY_MONEY_DEPOSIT));
		setItem(BankItemConfiguration.BANK_MONEY_DEPOSITALL, consumeSound(this::depositAll, BankSoundConfiguration.INVENTORY_MONEY_DEPOSIT_ALL));
	}
	
	private ItemStack getBalance(BankItemPath path, BankMoneyInfo info){
		return clone(path.get(), path.getName().replaceAll("<money>", Format.formatMoney(info.getMoney())), path.getLore());
	}
	
	private void withdrawAll(CorePlayers pl, BankMoneyInfo info){
		double withdraw = info.getMoney();
		if(BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get()){
			withdraw = Math.floor(withdraw);
		}
		info.withdrawMoney(pl, withdraw);
		pl.refreshInventory();
	}
	
	private void withdraw(CorePlayers pl, BankMoneyInfo info){
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_MONEY_WTIHDRAW.get(), Format.round(info.getMoney(), 2)){
			@Override
			public void cancel(CorePlayers pl){
				pl.setOpenInventory(BankMoneyInventory.this);
			}
			
			@Override
			public void close(CorePlayers pl){
				pl.setOpenInventory(BankMoneyInventory.this);
			}
			
			@Override
			public void onClick(CorePlayers pl, String value){
				double withdraw;
				try{
					withdraw = Double.parseDouble(value);
				}catch(Exception e){
					pl.setOpenInventory(BankMoneyInventory.this);
					return;
				}
				withdraw = Math.max(0, withdraw);
				withdraw = Math.min(info.getMoney(), withdraw);
				if(info.withdrawMoney(pl, withdraw)){
					// if(PlayerManager.hasLocationChanged(bi.getLast(), player.getLocation())){ return; }
					// bi.openMoney(player);
				}else{
					// (PlayerManager.hasLocationChanged(bi.getLast(), player.getLocation())){ return; }
					// bi.openMoney(player);
				}
				pl.setOpenInventory(BankMoneyInventory.this);
			}
		});
	}
	
	private void deposit(CorePlayers pl, BankMoneyInfo info){
		double balance = 0;
		if(Eco.getInstance().getEconomy() != null){
			balance = Eco.getInstance().getEconomy().getBalance(info.getPlayers().getPlayer());
		}
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_MONEY_DEPOSIT.get(), "" + Format.round(balance, 2)){
			@Override
			public void cancel(CorePlayers pl){
				pl.setOpenInventory(BankMoneyInventory.this);
			}
			
			@Override
			public void close(CorePlayers pl){
				pl.setOpenInventory(BankMoneyInventory.this);
			}
			
			@Override
			public void onClick(CorePlayers pl, String value){
				double deposit;
				try{
					deposit = Double.parseDouble(value);
				}catch(Exception e){
					pl.setOpenInventory(BankMoneyInventory.this);
					return;
				}
				deposit = Math.max(0d, deposit);
				double max = 0;
				if(Eco.getInstance().getEconomy() != null){
					max = Eco.getInstance().getEconomy().getBalance(pl.getPlayer());
				}
				deposit = Math.min(max, deposit);
				info.deposit(pl, deposit);
			}
		});
	}
	
	private void depositAll(CorePlayers pl, BankMoneyInfo info){
		double deposit = 0;
		if(Eco.getInstance().getEconomy() != null){
			deposit = Eco.getInstance().getEconomy().getBalance(pl.getPlayer());
		}
		info.deposit(pl, deposit);
		pl.refreshInventory();
	}
	
	private void addBack(){
		if(BankPluginConfiguration.BANK_MONEY_ONLY.get()){
			setItem(0, BankItemConfiguration.BANK_ITEM_BLANK);
		}else{
			setItem(0, BankItemConfiguration.BANK_BACK, consumeSound(this::returnToMainMenu, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
		}
	}
	
	protected void returnToMainMenu(CorePlayers pl){
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU);
	}
	
	@Override
	public BankMoneyInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankMoneyInfo.class);
	}
	
}
