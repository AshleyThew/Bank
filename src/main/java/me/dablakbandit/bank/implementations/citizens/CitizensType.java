package me.dablakbandit.bank.implementations.citizens;

import net.citizensnpcs.api.event.CitizensEnableEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

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
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.TraitInfo;

public class CitizensType extends BankImplementation implements Listener{
	
	private static final CitizensType citizensType = new CitizensType();
	
	public static CitizensType getInstance(){
		return citizensType;
	}
	
	private CitizensType(){
		
	}
	
	@Override
	public void load(){
		
	}
	
	private TraitInfo traitInfo;
	
	@Override
	public void enable(){
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Citizens");
		if(plugin != null && plugin.isEnabled()){
			register();
		}
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Citizens enabled");
		Bukkit.getPluginManager().registerEvents(this, BankPlugin.getInstance());
	}

    private void register(){
        if(traitInfo == null){
            traitInfo = TraitInfo.create(BankTrait.class);
        }
        if(CitizensAPI.hasImplementation()){
            try{
                CitizensAPI.getTraitFactory().registerTrait(traitInfo);
            }catch(IllegalArgumentException e){

            }
        }
    }
	
	@Override
	public void disable(){
		HandlerList.unregisterAll(this);
		if(traitInfo != null){
			if(CitizensAPI.hasImplementation()){
				CitizensAPI.getTraitFactory().deregisterTrait(traitInfo);
			}
		}
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Citizens disabled");
	}
	
	@EventHandler
	public void onNPCLeftClick(NPCLeftClickEvent event){
		onNPCClick(event);
	}
	
	public void onNPCClick(NPCClickEvent event){
		if(!event.getNPC().hasTrait(BankTrait.class)){ return; }
		event.setCancelled(true);
		Player player = event.getClicker();
		if(!BankPermissionConfiguration.PERMISSION_OPEN_CITIZENS.has(player)){ return; }
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		if(BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU)){
			BankSoundConfiguration.CITIZENS_OPEN.play(pl);
		}
	}
	
	@EventHandler
	public void onNPCRightClick(NPCRightClickEvent event){
		onNPCClick(event);
	}
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event){
		if(traitInfo != null){ return; }
		if(event.getPlugin().getName().equals("Citizens")){
			register();
		}
	}

    @EventHandler
    public void onCitizensEnable(CitizensEnableEvent ev) {
        register();
    }
}
