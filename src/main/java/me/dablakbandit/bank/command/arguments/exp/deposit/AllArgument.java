package me.dablakbandit.bank.command.arguments.exp.deposit;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.EXPUtils;
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
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer((Player) s);
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		bankInfo.getPinInfo().checkPass(() -> depositAll(bankInfo));
	}

	private void depositAll(BankInfo bankInfo) {
		double deposit = Math.max(0, EXPUtils.getExp(bankInfo.getPlayers().getPlayer()));
		bankInfo.getExpInfo().depositExp(bankInfo.getPlayers(), deposit);
	}
}
