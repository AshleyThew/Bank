package me.dablakbandit.bank.implementations.headdb;

import org.bukkit.Bukkit;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.implementations.BankImplementation;

public class HeadDBImplementation extends BankImplementation{
	
	private static HeadDBImplementation implementation = new HeadDBImplementation();
	
	public static HeadDBImplementation getInstance(){
		return implementation;
	}
	
	private HeadDBImplementation(){
		
	}
	
	@Override
	public void load(){
		
	}
	
	@Override
	public void enable(){
		try{
			Class.forName("me.arcaniax.hdb.api.DatabaseLoadEvent");
		}catch(Exception e){
			return;
		}
		registerListener();
	}
	
	private void registerListener(){
		Bukkit.getPluginManager().registerEvents(HeadDBListener.getInstance(), BankPlugin.getInstance());
	}
	
	@Override
	public void disable(){
		
	}
	
}
