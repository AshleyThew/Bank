package me.dablakbandit.bank.inventory.exp;

import me.dablakbandit.bank.config.*;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.inventory.*;
import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.EXPUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BankExpInventory extends BankInventoryHandler<BankExpInfo>{
	
	@Override
	public void init(){
		addBack();
		setItem(BankItemConfiguration.BANK_EXP_BLANK);
		setItem(BankItemConfiguration.BANK_EXP_WITHDRAWALL, consumeSound(this::withdrawAll, BankSoundConfiguration.INVENTORY_EXP_WITHDRAW_ALL));
		setItem(BankItemConfiguration.BANK_EXP_WITHDRAW, consumeSound(this::withdraw, BankSoundConfiguration.INVENTORY_EXP_WITHDRAW));
		setItem(BankItemConfiguration.BANK_EXP_BALANCE, this::getBalance);
		setItem(BankItemConfiguration.BANK_EXP_DEPOSIT, consumeSound(this::deposit, BankSoundConfiguration.INVENTORY_EXP_DEPOSIT));
		setItem(BankItemConfiguration.BANK_EXP_DEPOSITALL, consumeSound(this::depositAll, BankSoundConfiguration.INVENTORY_EXP_DEPOSIT_ALL));
		setItem(BankItemConfiguration.BANK_EXP_SEND, consumePermissions(BankPermissionConfiguration.PERMISSION_INVENTORY_EXP_SEND, consumeSound(this::sendExp, BankSoundConfiguration.EXP_SEND_OTHER)));
	}
	
	private ItemStack getBalance(BankItemPath path, BankExpInfo info){
		return replaceNameLore(path, "<exp>", "" + (int)Math.floor(info.getExp()));
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
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_EXP_DEPOSIT.get(), "" + EXPUtils.getExp(pl.getPlayer())){
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
		int exp = EXPUtils.getExp(player);
		info.depositExp(pl, exp);
		pl.refreshInventory();
	}
	
	private void sendExp(CorePlayers pl, BankExpInfo info){
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_EXP_SEND_NAME.get(), " "){
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
				if(value.startsWith(" ")){
					value = value.substring(1);
				}
				if(Bukkit.getPlayerExact(value) == null){
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", value));
					return;
				}
				sendExpAmount(pl, info, value);
			}
		});
	}
	
	private void sendExpAmount(CorePlayers pl, BankExpInfo info, String name){
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_EXP_SEND_AMOUNT.get(), "1"){
			@Override
			public void cancel(CorePlayers pl){
				pl.setOpenInventory(BankExpInventory.this);
			}
			
			@Override
			public void close(CorePlayers pl){
				pl.setOpenInventory(BankExpInventory.this);
			}
			
			@Override
			public void onClick(CorePlayers from, String value){
				double amount;
				try{
					amount = Double.parseDouble(value);
				}catch(Exception e){
					e.printStackTrace();
					from.setOpenInventory(BankExpInventory.this);
					return;
				}
				Player p = Bukkit.getPlayerExact(name);
				final CorePlayers payTo = CorePlayerManager.getInstance().getPlayer(p);
				if(p == null || payTo == null || payTo.getInfo(BankInfo.class).isLocked(false)){
					BankSoundConfiguration.GLOBAL_ERROR.play(pl.getPlayer());
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", name));
					return;
				}
				info.send(payTo, amount);
				from.setOpenInventory(BankExpInventory.this);
			}
		});
	}

	public ItemStack getBack(BankItemPath path, BankExpInfo bankExpInfo) {
		BankInfo bankInfo = bankExpInfo.getPlayers().getInfo(BankInfo.class);
		if (bankInfo.getOpenTypes().contains(OpenTypes.ALL) || bankInfo.getOpenTypes().contains(OpenTypes.MENU)) {
			return path.get();
		}
		return BankItemConfiguration.BANK_EXP_BLANK.get();
	}
	
	private void addBack(){
		if (!BankPluginConfiguration.BANK_EXP_ONLY.get()) {
			setItem(BankItemConfiguration.BANK_EXP_BACK, this::getBack, consumeSound(this::returnToMainMenu, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
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
