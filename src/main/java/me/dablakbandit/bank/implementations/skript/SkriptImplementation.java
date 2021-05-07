package me.dablakbandit.bank.implementations.skript;

import me.dablakbandit.bank.api.SkriptAPI;
import me.dablakbandit.bank.implementations.BankImplementation;

public class SkriptImplementation extends BankImplementation{
	
	private static SkriptImplementation vaultImplementation = new SkriptImplementation();
	
	public static SkriptImplementation getInstance(){
		return vaultImplementation;
	}
	
	private SkriptImplementation(){
		
	}
	
	@Override
	public void load(){
		
	}
	
	@Override
	public void enable(){
		SkriptAPI.getInstance().init();
	}
	
	@Override
	public void disable(){
	}
	
}
