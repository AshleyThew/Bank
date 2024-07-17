package me.dablakbandit.bank.player.info;

import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.config.path.BooleanPath;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.players.info.CorePlayersInfo;
import me.dablakbandit.core.players.info.JSONInfo;

public abstract class IBankInfo extends CorePlayersInfo{

	protected int version = 1;
	
	public IBankInfo(CorePlayers pl){
		super(pl);
	}
	
	@Override
	public void load(){
		
	}
	
	@Override
	public void save(){
		
	}
	
	public void save(BooleanPath path){
		if(path.get()){
			if(this instanceof JSONInfo){
				LoaderManager.getInstance().saveSingle(pl, (JSONInfo)this);
			}
		}
	}
	
	public String replaceInfo(String info){
		return info;
	}
}
