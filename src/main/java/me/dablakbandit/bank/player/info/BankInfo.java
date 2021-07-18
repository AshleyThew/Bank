package me.dablakbandit.bank.player.info;

import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;

public class BankInfo extends CorePlayersInfo{
	
	private boolean				locked	= true;
	private BankItemsInfo		itemsInfo;
	private BankExpInfo			expInfo;
	private BankMoneyInfo		moneyInfo;
	private BankPinInfo			pinInfo;
	private BankPermissionInfo	permissionInfo;
	private BankLoansInfo		loansInfo;
	
	private final long				joined	= System.currentTimeMillis();
	
	public BankInfo(CorePlayers pl){
		super(pl);
		init();
	}
	
	private void init(){
		pl.addInfo(itemsInfo = new BankItemsInfo(pl));
		pl.addInfo(expInfo = new BankExpInfo(pl));
		pl.addInfo(moneyInfo = new BankMoneyInfo(pl));
		pl.addInfo(pinInfo = new BankPinInfo(pl));
		pl.addInfo(permissionInfo = new BankPermissionInfo(pl));
		pl.addInfo(loansInfo = new BankLoansInfo(pl));
	}
	
	@Override
	public void load(){
	}
	
	public BankItemsInfo getItemsInfo(){
		return itemsInfo;
	}
	
	public BankExpInfo getExpInfo(){
		return expInfo;
	}
	
	public BankMoneyInfo getMoneyInfo(){
		return moneyInfo;
	}
	
	public BankPinInfo getPinInfo(){
		return pinInfo;
	}
	
	public BankLoansInfo getLoansInfo(){
		return loansInfo;
	}
	
	public BankPermissionInfo getPermissionInfo(){
		return permissionInfo;
	}
	
	public boolean isLocked(boolean check){
		return isLocked(check, null);
	}
	
	public boolean isLocked(boolean check, Runnable runnable){
		if(locked && check){
			boolean unlock = (System.currentTimeMillis() - joined) > 30000;
			LoaderManager.getInstance().load(pl, unlock, runnable);
		}
		return locked;
	}
	
	public void setLocked(boolean locked){
		this.locked = locked;
	}
	
	@Override
	public void save(){
		
	}
	
}
