package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.core.config.RawConfiguration;
import org.bukkit.entity.Player;

import me.dablakbandit.bank.player.info.BankPermissionInfo;
import me.dablakbandit.core.config.path.PermissionPath;
import me.dablakbandit.core.players.CorePlayerManager;

public class BankPermissionPath extends PermissionPath{

	public BankPermissionPath(String def, boolean defaultCheck){
		super(def);
		this.value = new Permission(def, defaultCheck);
		this.def = this.value;
	}

	@Override
	public boolean has(Player player){
		PermissionPath.Permission permission = this.get();
		return !permission.isCheck() || CorePlayerManager.getInstance().getPlayer(player).getInfo(BankPermissionInfo.class).checkPermission(permission.getPermission(), true);
	}
}
