package me.dablakbandit.bank.config;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.command.config.annotation.CommandBase;
import org.bukkit.plugin.java.JavaPlugin;

public class BalTopCommandConfiguration extends CommandConfiguration {

    @CommandBase
    public static final Command BALTOP = new Command("baltop", "bank.baltop", new String[0], new String[]{"<page>", "force"});
    public static final Command BALTOP_FORCE = new Command("force", "bank.baltop.force");
    private static BalTopCommandConfiguration config;

    private BalTopCommandConfiguration(JavaPlugin plugin) {
        super(plugin, "conf/command_baltop.yml");
    }

    public static void load(BankPlugin bankPlugin) {
        config = new BalTopCommandConfiguration(bankPlugin);
        config.load();
    }

    public static BalTopCommandConfiguration getInstance() {
        return config;
    }
}
