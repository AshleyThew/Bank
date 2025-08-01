package me.dablakbandit.bank.player.info;

import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.player.permission.PermissionCheck;
import me.dablakbandit.bank.player.permission.PermissionCheckCache;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.entity.Player;

import java.util.Collection;

public class BankPermissionInfo extends IBankInfo {

	private final PermissionCheckCache<PermissionCheck> cache = new PermissionCheckCache<>(60);
	private Player imitating;

	public BankPermissionInfo(CorePlayers pl) {
		super(pl);
	}

	public Player getImitating() {
		return imitating;
	}

	public void setImitating(Player imitating) {
		this.imitating = imitating;
	}

	public boolean checkPermission(String permission, boolean print) {
		Player player = imitating != null && imitating.isOnline() ? imitating : pl.getPlayer();
		boolean check = player.hasPermission(permission);
		cache.add(System.currentTimeMillis(), new PermissionCheck(player.getName(), permission, check));
		if (print && !check && BankPermissionConfiguration.PERMISSION_SEND_MESSAGE.get()) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_PERMISSION_REQUIREMENT.get().replaceAll("<permission>", permission));
		}
		return check;
	}

	public Collection<PermissionCheck> getHistory() {
		return cache.getAsMap().values();
	}

}
