package me.dablakbandit.bank.implementations.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class MVDWPlaceholderImplementation extends BankImplementation{
	
	private static final MVDWPlaceholderImplementation manager = new MVDWPlaceholderImplementation();
	
	public static MVDWPlaceholderImplementation getInstance(){
		return manager;
	}
	
	private MVDWPlaceholderImplementation(){
		
	}
	
	@Override
	public void load(){
		
	}
	
	@Override
	public void enable(){
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_money", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			BankMoneyInfo info = pl.getInfo(BankMoneyInfo.class);
			if(info==null) { return ""; }
			return Format.formatMoney(info.getMoney());
		});
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_exp", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			BankExpInfo info = pl.getInfo(BankExpInfo.class);
			if(info==null) { return ""; }
			return Format.formatMoney(info.getExp());
		});
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_slots", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			BankItemsInfo info = pl.getInfo(BankItemsInfo.class);
			if(info==null) { return ""; }
			return "" + info.getTotalSlots();
		});
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_used_slots", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			BankItemsInfo info = pl.getInfo(BankItemsInfo.class);
			if(info==null) { return ""; }
			return "" + info.getTotalUsedSlots();
		});
		/*-PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_loans_total", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			BankLoansInfo info = pl.getInfo(BankLoansInfo.class);
			if(info==null) { return ""; }
			return "" + info.getOriginal();
		});
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_loans_left", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			BankLoansInfo info = pl.getInfo(BankLoansInfo.class);
			if(info==null) { return ""; }
			return "" + info.getTotal();
		});*/
	}
	
	@Override
	public void disable(){
		
	}
}
