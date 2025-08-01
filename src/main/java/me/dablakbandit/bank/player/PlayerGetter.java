package me.dablakbandit.bank.player;

import com.google.common.base.Charsets;
import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerGetter {

	private static final boolean huuid = hasUUID();

	public static boolean hasUUID() {
		try {
			NMSUtils.getMethodSilent(Bukkit.class, "getPlayer", UUID.class);
			return true;
		} catch (Throwable ignored) {
		}
		return false;
	}

	public static Player getPlayer(String uuid, String name) {
		try {
			if (huuid) {
				return Bukkit.getPlayer(UUID.fromString(uuid));
			}
		} catch (Exception ignored) {
		}
		return Bukkit.getPlayerExact(name);
	}

	public static Player getPlayer(UUID uuid, String name) {
		try {
			if (huuid) {
				return Bukkit.getPlayer(uuid);
			}
		} catch (Exception ignored) {
		}
		return Bukkit.getPlayerExact(name);
	}

	public static String getUUID(Player player) {
		try {
			if (huuid) {
				return player.getUniqueId().toString();
			}
		} catch (Exception ignored) {
		}
		String name = player.getName();
		return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)).toString();
	}

}
