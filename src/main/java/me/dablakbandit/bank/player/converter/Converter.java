package me.dablakbandit.bank.player.converter;

import me.dablakbandit.core.players.CorePlayers;

public abstract class Converter{
	
	public abstract void convert(CorePlayers pl);
	
	public abstract void setup();
	
	public abstract void disable();
	
}
