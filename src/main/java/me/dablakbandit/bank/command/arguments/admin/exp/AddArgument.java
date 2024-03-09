package me.dablakbandit.bank.command.arguments.admin.exp;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.player.info.BankExpInfo;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddArgument extends BankEndArgument {

    public AddArgument(CommandConfiguration.Command command) {
        super(command);
    }

    @Override
    protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
        if (args.length <= 1) {
            base.sendArguments(s, cmd, args, original, arguments.entrySet());
            return;
        }
        try {
            Player player = Bukkit.getPlayerExact(args[0]);
            CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
            if (pl == null) {
                base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
                return;
            }
            BankInfo bankInfo = pl.getInfo(BankInfo.class);
            if (bankInfo.isLocked(false)) {
                BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_PLAYER_LOCKED.get().replaceAll("<player>", args[0]));
                return;
            }
            double added = Double.parseDouble(args[1]);
            BankExpInfo expInfo = bankInfo.getExpInfo();
            expInfo.addExp(added);
            BankLanguageConfiguration.sendFormattedMessage(s, BankLanguageConfiguration.BANK_ADMIN_EXP_ADD.get()
                    .replaceAll("<exp>", Format.formatExp(expInfo.getExp()))
                    .replaceAll("<player>", args[0])
                    .replaceAll("<amount>", Format.formatExp(added)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        addTabCompleter(0, TabCompleter.PLAYERS);
        addTabCompleter(1, TabCompleter.ofValues("<amount>"));
    }
}
