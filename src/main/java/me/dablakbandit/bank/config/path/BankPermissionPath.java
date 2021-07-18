package me.dablakbandit.bank.config.path;

import org.bukkit.entity.Player;

import me.dablakbandit.bank.player.info.BankPermissionInfo;
import me.dablakbandit.core.config.path.PermissionPath;
import me.dablakbandit.core.players.CorePlayerManager;

public class BankPermissionPath extends PermissionPath{
	public BankPermissionPath(String def){
		super(def);
	}
	
	@Override
	public boolean has(Player player){
		PermissionPath.Permission permission = this.get();
		return !permission.isCheck() || CorePlayerManager.getInstance().getPlayer(player).getInfo(BankPermissionInfo.class).checkPermission(permission.getPermission(), true);
	}
}
