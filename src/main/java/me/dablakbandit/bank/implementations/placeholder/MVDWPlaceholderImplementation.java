package me.dablakbandit.bank.implementations.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.Format;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class MVDWPlaceholderImplementation extends BankImplementation{
	
	private static MVDWPlaceholderImplementation manager = new MVDWPlaceholderImplementation();
	
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
			return Format.formatMoney(pl.getInfo(BankMoneyInfo.class).getMoney());
		});
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_exp", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			return Format.formatMoney(pl.getInfo(BankExpInfo.class).getExp());
		});
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_slots", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			return "" + pl.getInfo(BankItemsInfo.class).getTotalSlots();
		});
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_used_slots", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			return "" + pl.getInfo(BankItemsInfo.class).getTotalUsedSlots();
		});
		/*-PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_loans_total", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			return "" + pl.getInfo(LoansInfo.class).getTotal();
		});
		PlaceholderAPI.registerPlaceholder(BankPlugin.getInstance(), "bank_loans_left", event -> {
			if(!event.isOnline()){ return "Offline"; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(pl == null){ return "Offline"; }
			return "" + pl.getInfo(LoansInfo.class).getLeft();
		});*/
	}
	
	@Override
	public void disable(){
		
	}
}
