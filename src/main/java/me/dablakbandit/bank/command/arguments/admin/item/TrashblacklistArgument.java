package me.dablakbandit.bank.command.arguments.admin.item;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.implementations.blacklist.BlacklistType;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.player.info.admin.BankAdminInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrashblacklistArgument extends BankEndArgument {

	public TrashblacklistArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player) s);
		pl.getInfo(BankAdminInfo.class).setBlacklistType(BlacklistType.TRASH);
		BankInventoriesManager.getInstance().openBypass(pl, BankInventories.BANK_ADMIN_BLACKLIST);
	}

}
