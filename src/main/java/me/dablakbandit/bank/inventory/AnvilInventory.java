/*
 * Copyright (c) 2019 Ashley Thew
 */

package me.dablakbandit.bank.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.inventory.OpenInventory;
import me.dablakbandit.core.utils.EXPUtils;
import me.dablakbandit.core.utils.anvil.AnvilUtil;

public abstract class AnvilInventory extends OpenInventory{
	
	private final String	input;
	private boolean			returned	= false, entered = false;
	private int				exp;
	private String			ret;
	
	public AnvilInventory(String input){
		this.input = input;
	}
	
	public void onClose(final CorePlayers pl, final Player player){
		player.getOpenInventory().setItem(0, null);
		if(!returned){
			Bukkit.getScheduler().scheduleSyncDelayedTask(BankPlugin.getInstance(), () -> close(pl));
		}else{
			Bukkit.getScheduler().scheduleSyncDelayedTask(BankPlugin.getInstance(), () -> {
				EXPUtils.setTotalExperience(player, exp);
				if(entered){
					AnvilInventory.this.onClick(pl, ret);
				}
			});
		}
	}
	
	public abstract void cancel(CorePlayers pl);
	
	public abstract void close(CorePlayers pl);
	
	public abstract void onClick(CorePlayers pl, String value);
	
	public void load(){
	}
	
	@Override
	public boolean open(CorePlayers pl, Player player){
		returned = false;
		AnvilUtil.open(player, () -> {
			player.getOpenInventory().setItem(0, clone(BankItemConfiguration.BANK_ANVIL_INPUT.get(), input));
			exp = EXPUtils.getTotalExperience(player);
		});
		return true;
	}
	
	@Override
	public void set(CorePlayers pl, Player player, Inventory inv){
		
	}
	
	@Override
	public void parseClick(CorePlayers pl, Player player, Inventory inv, Inventory top, InventoryClickEvent event){
		event.setCancelled(true);
		event.setResult(Event.Result.DENY);
		if(returned){ return; }
		if(inv.equals(top)){
			switch(event.getRawSlot()){
			case 1:{
				inv.clear();
				returned = true;
				Bukkit.getScheduler().scheduleSyncDelayedTask(BankPlugin.getInstance(), () -> cancel(pl));
				return;
			}
			case 2:{
				returned = true;
				ItemStack item = event.getCurrentItem();
				String name = null;
				if((item != null) && (item.hasItemMeta())){
					ItemMeta meta = item.getItemMeta();
					if(meta.hasDisplayName()){
						name = meta.getDisplayName();
					}
				}
				inv.clear();
				if(name == null){
					name = input;
				}
				if(name.startsWith(" ")){
					name = name.substring(1);
				}
				entered = true;
				ret = name;
				Bukkit.getScheduler().scheduleSyncDelayedTask(BankPlugin.getInstance(), player::closeInventory);
			}
			}
		}
	}
	
	@Override
	public void parseInventoryDrag(CorePlayers pl, Player player, Inventory inv, Inventory top, InventoryDragEvent event){
		event.setCancelled(true);
	}
	
}
