package me.dablakbandit.bank.convert;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankUpgradeConfiguration;
import me.dablakbandit.bank.database.BankDatabase;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.bank.player.BankPlayerManager;
import me.dablakbandit.bank.save.loader.runner.LoadRunner;
import me.dablakbandit.bank.save.loader.runner.SaveRunner;
import me.dablakbandit.bank.save.type.SaveType;
import me.dablakbandit.bank.upgrade.UpgradeManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BankVersion1_20_6Converter {

	private static final BankVersion1_20_6Converter bankSaveTypeConverter = new BankVersion1_20_6Converter();

	private BankVersion1_20_6Converter() {

	}

	public static BankVersion1_20_6Converter getInstance() {
		return bankSaveTypeConverter;
	}

	public void convert(Runnable runnable) {
		conversionStart(runnable);
	}

	private void conversionStart(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(BankPlugin.getInstance(), () -> conversionAsync(runnable));
	}

	private void conversionAsync(Runnable runnable) {
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Kicking all players.");
		while (!Bukkit.getOnlinePlayers().isEmpty()) {
			for (Player player : new ArrayList<Player>(Bukkit.getOnlinePlayers())) {
				player.kickPlayer("Bank converting.");
			}
		}
		SaveType saveTypeFrom = BankPluginConfiguration.BANK_SAVE_TYPE.get();
		BankDatabase from = BankDatabaseManager.getInstance().loadDatabase(saveTypeFrom);

		List<String> uuids = from.getInfoDatabase().getDistinctUUIDS();
		conversionCycle(uuids, (list) -> conversionPlayers(list, from));

		BankUpgradeConfiguration.UPGRADE_VERSION.set(UpgradeManager.getInstance().getCurrentVersion());
		BankUpgradeConfiguration.getInstance().saveConfig();
		Bukkit.getScheduler().scheduleSyncDelayedTask(BankPlugin.getInstance(), runnable);
	}

	private void conversionCycle(List<String> uuids, Consumer<List<CorePlayers>> consumer) {
		BankPlayerManager bankPlayerManager = BankPlayerManager.getInstance();
		List<CorePlayers> list = new ArrayList<>();
		int size = uuids.size();
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Converting " + size + " accounts.");
		for (int count = 0; count < uuids.size(); count++) {
			int temp = count + 1;
			if (temp % 100 == 0) {
				consumer.accept(list);
				BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Converted " + temp + "/" + size + " accounts.");
			}
			CorePlayers pl = new CorePlayers(uuids.get(count));
			bankPlayerManager.addCorePlayers(pl);
			list.add(pl);
		}
		consumer.accept(list);
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Converted " + size + "/" + size + " accounts.");
	}

	private void conversionPlayers(List<CorePlayers> playersList, BankDatabase from) {
		load(playersList, from);
		save(playersList, from);
		playersList.clear();
	}

	private void load(List<CorePlayers> playersList, BankDatabase load) {
		BankDatabaseManager.getInstance().setBankDatabase(load);
		for (CorePlayers pl : playersList) {
			new LoadRunner(pl, true).run();
		}
	}

	private void save(List<CorePlayers> playersList, BankDatabase save) {
		BankDatabaseManager.getInstance().setBankDatabase(save);
		for (CorePlayers pl : playersList) {
			new SaveRunner(pl, true).run();
		}
	}

}
