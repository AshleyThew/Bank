package me.dablakbandit.bank.command.arguments.money;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.vault.Eco;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositArgument extends BankEndArgument {

	public DepositArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (checkArguments(s, cmd, label, args, original)) {
			return;
		}
		if (args.length == 0) {
			base.sendArguments(s, cmd, args, original, arguments.entrySet());
			return;
		}
		if (!checkPlayer(s)) {
			return;
		}
		try {
			Player player = (Player) s;
			CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
			BankInfo bankInfo = pl.getInfo(BankInfo.class);
			bankInfo.getPinInfo().checkPass(() -> {
				try {
					deposit(player, bankInfo, Double.parseDouble(args[0]));
				} catch (Exception ignored) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deposit(Player player, BankInfo bankInfo, double amount) {
		if (Eco.getInstance().getEconomy() == null) {
			return;
		}
		amount = Math.max(0, amount);
		bankInfo.getMoneyInfo().deposit(player, bankInfo.getPlayers(), amount);
	}

	@Override
	public void init() {
		addBlank("<amount>");
	}
}
