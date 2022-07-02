package me.dablakbandit.bank.player.converter;

import java.util.Arrays;

import me.dablakbandit.core.config.path.BooleanPath;
import me.dablakbandit.core.players.CorePlayers;

public enum Converters{
	//@formatter:off
	//@formatter:on
	;
	
	private final Converter	converter;
	private final BooleanPath	path;
	
	Converters(Converter converter, BooleanPath path){
		this.converter = converter;
		this.path = path;
	}
	
	public static void load(){
		Arrays.stream(values()).forEach(converters -> {
			if(converters.path == null || converters.path.get()){
				converters.converter.setup();
			}
		});
	}

	public static void enable(){
		Arrays.stream(values()).forEach(converters -> {
			if(converters.path == null || converters.path.get()){
				converters.converter.enable();
			}
		});
	}
	
	public static void disable(){
		Arrays.stream(values()).forEach(converters -> converters.converter.disable());
	}
	
	public static void convert(CorePlayers pl){
		Arrays.stream(values()).forEach(converters -> {
			if(converters.path == null || converters.path.get()){
				converters.converter.convert(pl);
			}
		});
	}
}
