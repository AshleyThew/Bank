package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.bank.player.info.BankPermissionInfo;
import me.dablakbandit.core.config.path.PermissionPath;
import me.dablakbandit.core.players.CorePlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BankIntegerPermissionPath extends PermissionPath {

	public BankIntegerPermissionPath(String def) {
		super(def);
	}

	@Override
	public boolean has(Player player) {
		Permission permission = this.get();
		return !permission.isCheck() || CorePlayerManager.getInstance().getPlayer(player).getInfo(BankPermissionInfo.class).checkPermission(permission.getPermission(), true);
	}

	public List<Integer> getValue(Collection<PermissionAttachmentInfo> list) {
		Permission permission = get();
		if (!permission.isCheck()) {
			return Collections.emptyList();
		}
		return getValues(permission.getPermission(), list);
	}

	private List<Integer> getValues(String path, Collection<PermissionAttachmentInfo> list) {
		List<Integer> values = new ArrayList<>();
		int length = path.length();
		for (PermissionAttachmentInfo info : list) {
			String check = info.getPermission();
			if (info.getValue() && check.startsWith(path)) {
				try {
					values.add(Integer.parseInt(check.substring(length)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return values;
	}
}
