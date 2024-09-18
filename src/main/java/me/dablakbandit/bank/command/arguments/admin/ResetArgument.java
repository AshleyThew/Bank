package me.dablakbandit.bank.command.arguments.admin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.player.BankPlayerManager;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ResetArgument extends BankEndArgument {

    public ResetArgument(CommandConfiguration.Command command) {
        super(command);
    }

    private final List<String> confirmationList = new ArrayList<>();

    @Override
    protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
        if (args.length == 0) {
            base.sendArguments(s, cmd, args, original, arguments.entrySet());
            return;
        }
        if (!confirmationList.remove(args[0])) {
            confirmationList.add(args[0]);
            BankLanguageConfiguration.sendFormattedMessage(s, ChatColor.RED + "This will reset all of " + args[0] + "'s bank data.");
            BankLanguageConfiguration.sendFormattedMessage(s, ChatColor.GREEN + "Please run the command again to confirm.");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(BankPlugin.getInstance(), () -> {
            Player player = Bukkit.getPlayerExact(args[0]);
            CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
            if (player == null || pl == null) {
                String uuid = BankDatabaseManager.getInstance().getInfoDatabase().getUUIDDatabase().getUUID(args[0]);
                if (uuid == null) {
                    Bukkit.getScheduler().runTask(BankPlugin.getInstance(), () -> {
                        base.sendFormattedMessage(s, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", args[0]));
                    });
                    return;
                }
                pl = new CorePlayers(uuid);
            }
            CorePlayers finalPl = pl;
            Bukkit.getScheduler().runTask(BankPlugin.getInstance(), () -> {
                BankPlayerManager.getInstance().addCorePlayers(finalPl);
                finalPl.getType(BankInfo.class).setLocked(false);
                LoaderManager.getInstance().save(finalPl, false);
                BankLanguageConfiguration.sendMessage(s, ChatColor.GREEN + args[0] + "'s bank data reset.");
            });
        });
    }

    @Override
    public void init() {
        addTabCompleter(0, TabCompleter.PLAYERS);
    }

}
