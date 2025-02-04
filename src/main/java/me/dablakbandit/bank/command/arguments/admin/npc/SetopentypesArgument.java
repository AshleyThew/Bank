package me.dablakbandit.bank.command.arguments.admin.npc;

import me.dablakbandit.bank.command.base.BankEndArgument;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.implementations.citizens.BankTrait;
import me.dablakbandit.bank.inventory.OpenTypes;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.commands.tabcompleter.TabCompleter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetopentypesArgument extends BankEndArgument {

	public SetopentypesArgument(CommandConfiguration.Command command) {
		super(command);
	}

	@Override
	protected void onArgument(CommandSender s, Command cmd, String label, String[] args, String[] original) {
		if (!checkPlayer(s)) {
			return;
		}

		// Get citizens npc the user has selected
		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(s);
		if (npc == null || !npc.hasTrait(BankTrait.class)) {
			BankLanguageConfiguration.sendFormattedMessage(s, ChatColor.RED + "You must select a valid NPC.");
			return;
		}
		BankTrait trait = npc.getTrait(BankTrait.class);

		if (args.length == 0) {
			trait.setOpenTypes(new OpenTypes[0]);
			BankLanguageConfiguration.sendFormattedMessage(s, ChatColor.GREEN + "Successfully cleared the open types for the NPC.");
			return;
		}

		OpenTypes[] openTypes = new OpenTypes[args.length];
		for (int i = 0; i < args.length; i++) {
			openTypes[i] = OpenTypes.getOpenType(args[i]);
		}
		trait.setOpenTypes(openTypes);
		BankLanguageConfiguration.sendFormattedMessage(s, ChatColor.GREEN + "Successfully set the open types for the NPC.");
	}

	@Override
	public void init() {
		for (int i = 0; i < OpenTypes.values().length; i++) {
			addTabCompleter(i, TabCompleter.ofEnum(OpenTypes.class));
		}
	}

	@Override
	public boolean hasPermission(CommandSender s) {
		return isPlayer(s) && super.hasPermission(s);
	}

}
