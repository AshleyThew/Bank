package me.dablakbandit.bank.player.info;

import org.bukkit.permissions.Permissible;

public interface PermissionsInfo {

	void checkPermissions(Permissible permissible, boolean debug);
}
