package me.dablakbandit.bank.implementations.citizens;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.OpenTypes;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class CitizensType extends BankImplementation implements Listener {

	private static final CitizensType citizensType = new CitizensType();

	public static CitizensType getInstance() {
		return citizensType;
	}

	private CitizensType() {

	}

	@Override
	public void load() {

	}

	private TraitInfo traitInfo;
	private boolean registered;

	@Override
	public void enable() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Citizens");
		if (plugin != null && plugin.isEnabled()) {
			register();
		}
		Bukkit.getPluginManager().registerEvents(this, BankPlugin.getInstance());
	}

	private void register() {
		if (registered) {
			return;
		}
		if (traitInfo == null) {
			traitInfo = TraitInfo.create(BankTrait.class);
		}
		if (!CitizensAPI.hasImplementation()) {
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Citizens API not yet available, waiting for CitizensEnableEvent");
			return;
		}
		try {
			CitizensAPI.getTraitFactory().registerTrait(traitInfo);
			registered = true;
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Citizens bank trait registered");
		} catch (IllegalArgumentException e) {
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Citizens bank trait failed to register: " + e.getMessage());
		}
	}

	@Override
	public void disable() {
		HandlerList.unregisterAll(this);
		if (traitInfo != null && registered) {
			if (CitizensAPI.hasImplementation()) {
				CitizensAPI.getTraitFactory().deregisterTrait(traitInfo);
			}
			registered = false;
		}
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Citizens disabled");
	}

	@EventHandler
	public void onNPCLeftClick(NPCLeftClickEvent event) {
		onNPCClick(event);
	}

	public void onNPCClick(NPCClickEvent event) {
		if (!event.getNPC().hasTrait(BankTrait.class)) {
			return;
		}
		if (!registered) {
			BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Citizens bank trait not registered, ignoring NPC click");
			return;
		}
		event.setCancelled(true);
		Player player = event.getClicker();
		if (!BankPermissionConfiguration.PERMISSION_OPEN_CITIZENS.has(player)) {
			return;
		}

		OpenTypes[] open = new OpenTypes[]{OpenTypes.ALL};
		if (BankPluginConfiguration.BANK_OPENTYPE_SUBSET_CITIZENS_ENABLED.get()) {
			BankTrait trait = event.getNPC().getTrait(BankTrait.class);
			open = trait.getTypes();
			if (open == null || open.length == 0) {
				BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Citizens NPC " + event.getNPC().getId() + " has no open types set");
				BankLanguageConfiguration.sendFormattedMessage(player, ChatColor.RED + "This NPC has no bank types configured.");
				return;
			}
		}
		CorePlayers pl = CorePlayerManager.getInstance().getPlayer(player);
		BankInventories inventories = BankInventoriesManager.getInstance().getBankInventories(open);
		if (BankInventoriesManager.getInstance().open(pl, inventories, open)) {
			BankSoundConfiguration.CITIZENS_OPEN.play(pl);
		}
	}

	@EventHandler
	public void onNPCRightClick(NPCRightClickEvent event) {
		onNPCClick(event);
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().getName().equals("Citizens")) {
			register();
		}
	}

	@EventHandler
	public void onCitizensEnable(CitizensEnableEvent ev) {
		register();
	}
}
