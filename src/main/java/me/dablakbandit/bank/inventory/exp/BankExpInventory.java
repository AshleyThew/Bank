package me.dablakbandit.bank.inventory.exp;

import org.bukkit.entity.Player;
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
import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.EXPUtils;

public class BankExpInventory extends BankInventoryHandler<BankExpInfo>{
	
	@Override
	public void init(){
		int size = descriptor.getSize();
		setAll(size, BankItemConfiguration.BANK_ITEM_BLANK);
		addBack();
		setItem(BankItemConfiguration.BANK_EXP_WITHDRAWALL, consumeSound(this::withdrawAll, BankSoundConfiguration.INVENTORY_EXP_WITHDRAW_ALL));
		setItem(BankItemConfiguration.BANK_EXP_WITHDRAW, consumeSound(this::withdraw, BankSoundConfiguration.INVENTORY_EXP_WITHDRAW));
		setItem(BankItemConfiguration.BANK_EXP_BALANCE, this::getBalance);
		setItem(BankItemConfiguration.BANK_EXP_DEPOSIT, consumeSound(this::deposit, BankSoundConfiguration.INVENTORY_EXP_DEPOSIT));
		setItem(BankItemConfiguration.BANK_EXP_DEPOSITALL, consumeSound(this::depositAll, BankSoundConfiguration.INVENTORY_EXP_DEPOSIT_ALL));
	}
	
	private ItemStack getBalance(BankItemPath path, BankExpInfo info){
		return clone(path.get(), path.getName().replaceAll("<exp>", "" + (int)Math.floor(info.getExp())), path.getLore());
	}
	
	private void withdrawAll(CorePlayers pl, BankExpInfo info){
		int withdraw = (int)Math.floor(info.getExp());
		info.withdrawExp(pl, withdraw);
		pl.refreshInventory();
	}
	
	private void withdraw(CorePlayers pl, BankExpInfo info){
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_EXP_WTIHDRAW.get(), "" + (int)Math.floor(info.getExp())){
			@Override
			public void cancel(CorePlayers pl){
				pl.setOpenInventory(BankExpInventory.this);
			}
			
			@Override
			public void close(CorePlayers pl){
				pl.setOpenInventory(BankExpInventory.this);
				
			}
			
			@Override
			public void onClick(CorePlayers pl, String value){
				int withdraw;
				try{
					withdraw = Integer.parseInt(value);
				}catch(Exception e){
					pl.setOpenInventory(BankExpInventory.this);
					return;
				}
				withdraw = Math.max(0, withdraw);
				withdraw = Math.min((int)Math.floor(info.getExp()), withdraw);
				info.withdrawExp(pl, withdraw);
				pl.setOpenInventory(BankExpInventory.this);
			}
		});
	}
	
	private void deposit(CorePlayers pl, BankExpInfo info){
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_EXP_DEPOSIT.get(), "" + EXPUtils.getTotalExperience(pl.getPlayer())){
			@Override
			public void cancel(CorePlayers pl){
				pl.setOpenInventory(BankExpInventory.this);
			}
			
			@Override
			public void close(CorePlayers pl){
				pl.setOpenInventory(BankExpInventory.this);
				
			}
			
			@Override
			public void onClick(CorePlayers pl, String value){
				int deposit;
				try{
					deposit = Integer.parseInt(value);
				}catch(Exception e){
					pl.setOpenInventory(BankExpInventory.this);
					return;
				}
				deposit = Math.max(0, deposit);
				info.depositExp(pl, deposit);
				pl.setOpenInventory(BankExpInventory.this);
			}
		});
	}
	
	private void depositAll(CorePlayers pl, BankExpInfo info){
		Player player = pl.getPlayer();
		int exp = EXPUtils.getTotalExperience(player);
		info.depositExp(pl, exp);
		pl.refreshInventory();
	}
	
	private void addBack(){
		if(BankPluginConfiguration.BANK_EXP_ONLY.get()){
			setItem(0, BankItemConfiguration.BANK_ITEM_BLANK);
		}else{
			setItem(0, BankItemConfiguration.BANK_BACK, consumeSound(this::returnToMainMenu, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
		}
	}
	
	protected void returnToMainMenu(CorePlayers pl){
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU);
	}
	
	@Override
	public BankExpInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankExpInfo.class);
	}
	
}
