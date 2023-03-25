package me.dablakbandit.bank.implementations.other;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;

public class SignType extends BankImplementation implements Listener{
	
	private static final SignType citizensType = new SignType();
	
	public static SignType getInstance(){
		return citizensType;
	}
	
	private SignType(){
		
	}
	
	@Override
	public void load(){
	}
	
	@Override
	public void enable(){
		Bukkit.getPluginManager().registerEvents(this, BankPlugin.getInstance());
	}
	
	@Override
	public void disable(){
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		if(event.getLine(0).equalsIgnoreCase("[bank]")){
			if(!BankPermissionConfiguration.PERMISSION_SIGN_PLACE.has(event.getPlayer())){ return; }
			event.setLine(0, BankPluginConfiguration.BANK_TYPE_SIGN_TEXT.get());
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getClickedBlock() == null){ return; }
		if(isSign(event.getClickedBlock())){
			event.setCancelled(true);
			if(!BankPermissionConfiguration.PERMISSION_OPEN_SIGN.has(event.getPlayer())){ return; }
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
			if(BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU)){
				BankSoundConfiguration.SIGN_OPEN.play(pl);
			}
		}
	}
	
	public boolean isSign(Block b){
		if(!b.getType().name().contains("SIGN") || !(b.getState() instanceof Sign)){ return false; }
		Sign s = (Sign)b.getState();
		if(BankPluginConfiguration.BANK_TYPE_SIGN_TEXT.hasChangedDefault() && s.getLine(0).equals(BankPluginConfiguration.BANK_TYPE_SIGN_TEXT.getDefault())){
			s.setLine(0, BankPluginConfiguration.BANK_TYPE_SIGN_TEXT.get());
			return true;
		}
		return s.getLine(0).equals(BankPluginConfiguration.BANK_TYPE_SIGN_TEXT.get());
	}
}
