package me.dablakbandit.bank.command.arguments.admin.item;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DefaultArgument extends BankEndArgument {

	public DefaultArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		BankInventoriesManager.getInstance().openBypass(CorePlayerManager.getInstance().getPlayer((Player) s), BankInventories.BANK_ADMIN_ITEM_DEFAULT);
	}
}
