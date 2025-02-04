package me.dablakbandit.bank.implementations.other;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.OpenTypes;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.location.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockType extends BankImplementation implements Listener{
	
	private static final BlockType citizensType = new BlockType();
	
	public static BlockType getInstance(){
		return citizensType;
	}
	
	private BlockType(){
		
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
	
	public boolean isBlock(Location loc){
		return BankPluginConfiguration.BANK_TYPE_BLOCK_LOCATIONS.get().contains(LocationUtils.locationToBasic(loc));
	}
	
	public void addBlock(Location location){
		BankPluginConfiguration.BANK_TYPE_BLOCK_LOCATIONS.add(LocationUtils.locationToBasic(location));
	}
	
	public void removeBlock(Location location){
		BankPluginConfiguration.BANK_TYPE_BLOCK_LOCATIONS.remove(LocationUtils.locationToBasic(location));
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Block b = event.getClickedBlock();
		if(b == null || b.getType() == Material.AIR){ return; }
		if(!isBlock(b.getLocation())){ return; }
		event.setCancelled(true);
		if(!BankPermissionConfiguration.PERMISSION_OPEN_BLOCK.has(event.getPlayer())){ return; }
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(event.getPlayer());
		if (BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU, OpenTypes.ALL)) {
			BankSoundConfiguration.BLOCK_OPEN.play(pl);
		}
	}
}
