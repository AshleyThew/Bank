package me.dablakbandit.bank.implementations.placeholder;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.Format;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

@SuppressWarnings("deprecation")
public class PlaceholderAPIImplementation extends BankImplementation{
	
	private static PlaceholderAPIImplementation manager = new PlaceholderAPIImplementation();
	
	public static PlaceholderAPIImplementation getInstance(){
		return manager;
	}
	
	private BankPlaceHolderExpansion expansion = new BankPlaceHolderExpansion();
	
	private PlaceholderAPIImplementation(){
	}
	
	@Override
	public void load(){
		
	}
	
	@Override
	public void enable(){
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Enabled placeholderapi expansion");
		expansion.register();
	}
	
	@Override
	public void disable(){
		
	}
	
	public static class BankPlaceHolderExpansion extends PlaceholderExpansion{
		
		@Override
		public boolean persist(){
			return true;
		}
		
		@Override
		public boolean canRegister(){
			return true;
		}
		
		@Override
		public String getIdentifier(){
			return "bank";
		}
		
		@Override
		public String getAuthor(){
			return "Dablakbandit";
		}
		
		@Override
		public String getVersion(){
			return BankPlugin.getInstance().getDescription().getVersion();
		}
		
		@Override
		public String onPlaceholderRequest(Player player, String holder){
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
			if(pl == null){ return null; }
			switch(holder){
			case "money":{
				return Format.formatMoney(pl.getInfo(BankMoneyInfo.class).getMoney());
			}
			case "exp":{
				return Format.formatMoney(pl.getInfo(BankExpInfo.class).getExp());
			}
			case "slots":{
				return "" + pl.getInfo(BankItemsInfo.class).getTotalSlots();
			}
			case "used_slots":{
				return "" + pl.getInfo(BankItemsInfo.class).getTotalUsedSlots();
			}
			/*-
			case "loans_total":{
				return "" + pl.getInfo(LoansInfo.class).getTotal();
			}
			case "loans_left":{
				return "" + pl.getInfo(LoansInfo.class).getLeft();
			}*/
			}
			return null;
		}
	}
}
