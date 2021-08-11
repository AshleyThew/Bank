package me.dablakbandit.bank.player;

import org.bukkit.entity.Player;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.player.info.BankPermissionInfo;
import me.dablakbandit.core.players.CorePlayerManager;

public class PlayerChecks{
	
	public static PlayerChecks playerChecks = new PlayerChecks();
	
	public static PlayerChecks getInstance(){
		return playerChecks;
	}
	
	private PlayerChecks(){
		
	}
	
	public boolean checkGamemodeDisabled(Player player){
		return BankPluginConfiguration.BANK_DISABLE_GAMEMODES.get().contains(player.getGameMode().name());
	}
	
	public boolean checkWorldDisabled(Player player){
		return BankPluginConfiguration.BANK_DISABLE_WORLDS.get().contains(player.getWorld().getName());
	}
	
	public boolean checkPermissionInfo(Player player, String permission, boolean print){
		return CorePlayerManager.getInstance().getPlayer(player).getInfo(BankPermissionInfo.class).checkPermission(permission, print);
	}
}
