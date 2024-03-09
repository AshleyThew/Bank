package me.dablakbandit.bank.implementations.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderAPIImplementation extends BankImplementation{
	
	private static final PlaceholderAPIImplementation manager = new PlaceholderAPIImplementation();
	
	public static PlaceholderAPIImplementation getInstance(){
		return manager;
	}
	
	private final BankPlaceHolderExpansion expansion = new BankPlaceHolderExpansion();

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

		private final Pattern patternItemsCount = Pattern.compile("items_count_([A-Z_]+)(:\\d+)?(#\\d+)?");
		;

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
			return BankPluginConfiguration.BANK_IMPLEMENTATION_PLACEHOLDER_PREFIX.get();
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
				BankMoneyInfo info = pl.getInfo(BankMoneyInfo.class);
				if(info == null){ return ""; }
				return Format.formatMoney(info.getMoney());
			}
				case "raw_money": {
					BankMoneyInfo info = pl.getInfo(BankMoneyInfo.class);
					if (info == null) {
						return "";
					}
					return Format.round(info.getMoney(), 2);
				}
			case "exp":{
				BankExpInfo info = pl.getInfo(BankExpInfo.class);
				if(info == null){ return ""; }
				return Format.formatExp(info.getExp());
			}
				case "raw_exp": {
					BankExpInfo info = pl.getInfo(BankExpInfo.class);
					if (info == null) {
						return "";
					}
					return Format.round(info.getExp(), 2);
				}
			case "slots":{
				BankItemsInfo info = pl.getInfo(BankItemsInfo.class);
				if(info == null){ return ""; }
				return "" + info.getBankItemsHandler().getTotalSlots();
			}
			case "used_slots":{
				BankItemsInfo info = pl.getInfo(BankItemsInfo.class);
				if(info == null){ return ""; }
				return "" + info.getBankItemsHandler().getTotalUsedSlots();
			}
			}
			if (holder.startsWith("items_count_")) {
				try {

					Matcher matcher = patternItemsCount.matcher(holder);
					if (matcher.matches()) {
						String matchMaterial = matcher.group(1);
						String matchDamage = matcher.group(2);
						String matchModel = matcher.group(3);
						Material material = Material.valueOf(matchMaterial);
						Integer damage = matchDamage != null ? Integer.parseInt(matchDamage.substring(1)) : null;
						Integer model = matchModel != null ? Integer.parseInt(matchModel.substring(1)) : null;
						return String.valueOf(pl.getInfo(BankItemsInfo.class).getBankItemsHandler().countTotal(material, damage, model));
					}
				} catch (Exception ignored) {
				}
			}
			return null;
		}
	}
}
