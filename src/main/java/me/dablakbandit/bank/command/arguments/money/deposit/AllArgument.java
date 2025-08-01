package me.dablakbandit.bank.command.arguments.money.deposit;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.vault.Eco;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllArgument extends BankEndArgument {

	public AllArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (!checkPlayer(s)) {
			return;
		}
		Player player = (Player) s;
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		bankInfo.getPinInfo().checkPass(() -> depositAll(player, bankInfo));
	}

	private void depositAll(Player player, BankInfo bankInfo) {
		if (Eco.getInstance().getEconomy() == null) {
			return;
		}
		double deposit = Math.max(0, Eco.getInstance().getEconomy().getBalance(bankInfo.getPlayers().getPlayer()));
		bankInfo.getMoneyInfo().deposit(player, bankInfo.getPlayers(), deposit);
	}
}
