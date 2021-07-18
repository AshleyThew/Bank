package me.dablakbandit.bank.config;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.path.TranslatedStringListPath;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.path.EmptyPath;
import me.dablakbandit.core.config.path.Path;
import me.dablakbandit.core.config.path.TranslatedStringPath;
import me.dablakbandit.core.players.CorePlayers;

public class BankLanguageConfiguration extends CommentAdvancedConfiguration{
	
	private static final BankLanguageConfiguration configuration = new BankLanguageConfiguration(BankPlugin.getInstance());
	
	public static BankLanguageConfiguration getInstance(){
		return configuration;
	}
	
	@Comment("Edit messages from the plugin here")
	private static final Path						BANK							= new EmptyPath();
	
	@Comment("Available: <block>")
	public static final TranslatedStringPath		BANK_ADMIN_BLOCK_SET			= new TranslatedStringPath(ChatColor.GREEN + "Block <block> set as bank");
	@Comment("Available: <block>")
	public static final TranslatedStringPath		BANK_ADMIN_BLOCK_NOT			= new TranslatedStringPath(ChatColor.GREEN + "Block <block> is not a bank");
	@Comment("Available: <block>")
	public static final TranslatedStringPath		BANK_ADMIN_BLOCK_REMOVE			= new TranslatedStringPath(ChatColor.GREEN + "Block <block> removed as bank");
	@Comment("Available: <player>, <new_amount>")
	public static final TranslatedStringPath		BANK_ADMIN_SLOTS_ADD			= new TranslatedStringPath("Added <amount> slots to <player>, new amount <new_amount>");
	@Comment("Available: <player>")
	public static final TranslatedStringPath		BANK_ADMIN_SLOTS_SET			= new TranslatedStringPath("Set <player> slots to <amount>");
	@Comment("Available: <version>")
	public static final TranslatedStringPath		BANK_ADMIN_RELOAD				= new TranslatedStringPath("Bank v<version> reloaded.");
	@Comment("Available: <player>")
	public static final TranslatedStringPath		BANK_ADMIN_LOADING				= new TranslatedStringPath(ChatColor.GRAY + "Offline loading <player>, do '/bank admin save' when complete");
	public static final TranslatedStringPath		BANK_ADMIN_SAVED				= new TranslatedStringPath(ChatColor.GREEN + "Saved opened player.");
	public static final TranslatedStringPath		BANK_ADMIN_INVENTORY_PREFIX		= new TranslatedStringPath(ChatColor.RED + "Admin");
	@Comment("Available: <player>")
	public static final TranslatedStringPath		BANK_ADMIN_PERMISSION_UPDATED	= new TranslatedStringPath(ChatColor.GREEN + "Updated permissions for <player>");
	
	@Comment("Available: <base>, <args>")
	public static final TranslatedStringPath		COMMAND_TITLE_FORMAT			= new TranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "<base>" + ChatColor.AQUA + "<args>" + ChatColor.GRAY + "]");
	@Comment("Available: <base>, <args>")
	public static final TranslatedStringPath		COMMAND_COMMANDS_FORMAT			= new TranslatedStringPath(ChatColor.WHITE + "/" + ChatColor.GRAY + "<base>" + ChatColor.AQUA + "<args>");
	@Comment("Available: <message>")
	public static final TranslatedStringPath		COMMAND_MESSAGE_FORMAT			= new TranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "Bank" + ChatColor.GRAY + "] <message>");
	@Comment("Available: <base>, <args>")
	public static final TranslatedStringPath		COMMAND_UNKNOWN_FORMAT			= new TranslatedStringPath(ChatColor.RED + "Unknown command /<base> <args>");
	@Comment("Available: <player>")
	public static final TranslatedStringPath		COMMAND_UNKNOWN_PLAYER			= new TranslatedStringPath(ChatColor.RED + "Unknown player <player>");
	public static final TranslatedStringPath		COMMAND_NO_PERMISSION			= new TranslatedStringPath(ChatColor.RED + "You do not have the permission to perform this command.");
	@Comment("Available: <value>")
	public static final TranslatedStringPath		COMMAND_UNABLE_PARSE			= new TranslatedStringPath(ChatColor.RED + "Unable to parse <value>.");
	
	@Comment("Available: <message>")
	public static final TranslatedStringPath		MESSAGE_FORMAT					= new TranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "Bank" + ChatColor.GRAY + "] <message>");
	@Comment("Available: <money>, <tax>")
	public static final TranslatedStringPath		MESSAGE_MONEY_DEPOSIT			= new TranslatedStringPath(ChatColor.GREEN + "You have deposited $<money> to the bank.");
	@Comment("Available: <money>")
	public static final TranslatedStringPath		MESSAGE_MONEY_WITHDRAW			= new TranslatedStringPath(ChatColor.GREEN + "You have withdrawn $<money> from the bank.");
	@Comment("Available: <money>")
	public static final TranslatedStringPath		MESSAGE_MONEY_BALANCE			= new TranslatedStringPath(ChatColor.GREEN + "You have $<money> in the bank.");
	public static final TranslatedStringPath		MESSAGE_MONEY_NOT_ENOUGH		= new TranslatedStringPath(ChatColor.RED + "You do not have enough money to do that.");
	@Comment("Available: <money>, <name>")
	public static final TranslatedStringPath		MESSAGE_MONEY_SENT				= new TranslatedStringPath(ChatColor.GREEN + "Successfully sent $<money> to <name>");
	@Comment("Available: <amount>")
	public static final TranslatedStringPath		MESSAGE_MONEY_INTEREST_GAINED	= new TranslatedStringPath(ChatColor.GREEN + "You gained $<amount> in interest");
	
	@Comment("Available: <exp>, <tax>")
	public static final TranslatedStringPath		MESSAGE_EXP_DEPOSIT				= new TranslatedStringPath(ChatColor.GREEN + "You have deposited <exp> exp to the bank.");
	@Comment("Available: <exp>")
	public static final TranslatedStringPath		MESSAGE_EXP_WITHDRAW			= new TranslatedStringPath(ChatColor.GREEN + "You have withdrawn <exp> exp from the bank.");
	@Comment("Available: <exp>")
	public static final TranslatedStringPath		MESSAGE_EXP_BALANCE				= new TranslatedStringPath(ChatColor.GREEN + "You have <exp> exp in the bank.");
	public static final TranslatedStringPath		MESSAGE_EXP_NOT_ENOUGH			= new TranslatedStringPath(ChatColor.RED + "You do not have enough exp to do that.");
	@Comment("Available: <exp>, <name>")
	public static final TranslatedStringPath		MESSAGE_EXP_SENT				= new TranslatedStringPath(ChatColor.GREEN + "Successfully sent <exp> to <name>");
	@Comment("Available: <amount>")
	public static final TranslatedStringPath		MESSAGE_EXP_INTEREST_GAINED		= new TranslatedStringPath(ChatColor.GREEN + "You gained <amount> in exp interest");
	
	@Comment("Available: <permission>")
	public static final TranslatedStringPath		MESSAGE_PERMISSION_REQUIREMENT	= new TranslatedStringPath(ChatColor.RED + "You require permission: <permission> to do that.");
	
	@Comment("Available: <amount>")
	public static final TranslatedStringPath		MESSAGE_LOANS_PAYBACK_AMOUNT	= new TranslatedStringPath(ChatColor.GREEN + "You payed back $<amount> of your loans");
	@Comment("Available: <amount>")
	public static final TranslatedStringPath		MESSAGE_LOANS_INTEREST_GAINED	= new TranslatedStringPath(ChatColor.GREEN + "Your loans gained $<amount> in interest");
	
	public static final TranslatedStringPath		MESSAGE_BANK_LOCKED				= new TranslatedStringPath(ChatColor.RED + "Bank locked/loading please wait");
	public static final TranslatedStringPath		MESSAGE_BANK_UNLOCKED			= new TranslatedStringPath(ChatColor.GREEN + "Bank unlocked!");
	public static final TranslatedStringPath		MESSAGE_PIN_ENTER_BEFORE		= new TranslatedStringPath(ChatColor.RED + "You must enter your pin before doing that.");
	
	public static final TranslatedStringPath		MESSAGE_INFO_BORDER				= new TranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	//@formatter:off
	public static final TranslatedStringListPath	MESSAGE_INFO_VIEW				= new TranslatedStringListPath(
			ChatColor.AQUA + " Bank balance $<bank_money>",
				ChatColor.AQUA + " Bank exp <bank_exp>, <bank_exp_level> levels",
				ChatColor.AQUA + " Money interest in <bank_interest_money_minutes> minutes (<bank_interest_money_seconds> seconds)",
				ChatColor.AQUA + " Exp interest in <bank_interest_exp_minutes> minutes (<bank_interest_exp_seconds> seconds)");
	//@formatter:on
	
	public static final TranslatedStringPath		MESSAGE_TOP_MONEY_BORDER		= new TranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	@Comment("Available: <number>, <name>, <amount>")
	public static final TranslatedStringPath		MESSAGE_TOP_MONEY_INFO			= new TranslatedStringPath(ChatColor.GRAY + "<number>. " + ChatColor.GREEN + "<name>: " + ChatColor.GOLD + "$<amount>");
	
	public static final TranslatedStringPath		MESSAGE_TOP_EXP_BORDER			= new TranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	@Comment("Available: <number>, <name>, <amount>")
	public static final TranslatedStringPath		MESSAGE_TOP_EXP_INFO			= new TranslatedStringPath(ChatColor.GRAY + "<number>. " + ChatColor.GREEN + "<name>: " + ChatColor.GOLD + "$<amount>");
	
	public static final TranslatedStringPath		FORMAT_THOUSAND					= new TranslatedStringPath("K");
	public static final TranslatedStringPath		FORMAT_MILLION					= new TranslatedStringPath("Million");
	public static final TranslatedStringPath		FORMAT_BILLION					= new TranslatedStringPath("Billion");
	public static final TranslatedStringPath		FORMAT_TRILLION					= new TranslatedStringPath("Trillion");
	public static final TranslatedStringPath		FORMAT_QUADRILLION				= new TranslatedStringPath("Quadrillion");
	public static final TranslatedStringPath		FORMAT_QUINTILLION				= new TranslatedStringPath("Quintillion");
	public static final TranslatedStringPath		FORMAT_SEXTILLION				= new TranslatedStringPath("Sextillion");
	public static final TranslatedStringPath		FORMAT_SEPTILLION				= new TranslatedStringPath("Septillion");
	public static final TranslatedStringPath		FORMAT_OCTILLION				= new TranslatedStringPath("Octillion");
	public static final TranslatedStringPath		FORMAT_NONILLION				= new TranslatedStringPath("Nonillion");
	public static final TranslatedStringPath		FORMAT_DECILLION				= new TranslatedStringPath("Decillion");
	public static final TranslatedStringPath		FORMAT_UNDECILLION				= new TranslatedStringPath("Undecillion");
	public static final TranslatedStringPath		FORMAT_DUODECILLION				= new TranslatedStringPath("Duodecillion");
	public static final TranslatedStringPath		FORMAT_TREDECILLION				= new TranslatedStringPath("Tredecillion");
	public static final TranslatedStringPath		FORMAT_QUATTUORDECILLION		= new TranslatedStringPath("Quattuordecillion");
	public static final TranslatedStringPath		FORMAT_QUINQUADECILLION			= new TranslatedStringPath("Quinquadecillion");
	public static final TranslatedStringPath		FORMAT_SEDECILLION				= new TranslatedStringPath("Sedecillion");
	public static final TranslatedStringPath		FORMAT_SEPTENDECILLION			= new TranslatedStringPath("Septendecillion");
	public static final TranslatedStringPath		FORMAT_OCTODECILLION			= new TranslatedStringPath("Octodecillion");
	public static final TranslatedStringPath		FORMAT_NOVENDECILLION			= new TranslatedStringPath("Novendecillion");
	public static final TranslatedStringPath		FORMAT_VIGINTILLION				= new TranslatedStringPath("Vigintillion");
	public static final TranslatedStringPath		FORMAT_UNVIGINTILLION			= new TranslatedStringPath("Unvigintillion");
	public static final TranslatedStringPath		FORMAT_DUOVIGINTILLION			= new TranslatedStringPath("Duovigintillion");
	public static final TranslatedStringPath		FORMAT_TRESVIGINTILLION			= new TranslatedStringPath("Tresvegintillion");
	public static final TranslatedStringPath		FORMAT_QUATTUORVIGINTILLION		= new TranslatedStringPath("Quattuorvigintillion");
	public static final TranslatedStringPath		FORMAT_QUINQUAVIGINTILLION		= new TranslatedStringPath("Quinquavigintillion");
	public static final TranslatedStringPath		FORMAT_SESVIGINTILLION			= new TranslatedStringPath("Sesvigintillion");
	public static final TranslatedStringPath		FORMAT_SEPTEMVIGINTILLION		= new TranslatedStringPath("Septemvigintillion");
	public static final TranslatedStringPath		FORMAT_OCTOVIGINTILLION			= new TranslatedStringPath("Octovigintillion");
	public static final TranslatedStringPath		FORMAT_NOVEMVIGINTILLION		= new TranslatedStringPath("Novemvigintillion");
	public static final TranslatedStringPath		FORMAT_TRIGINTILLION			= new TranslatedStringPath("Trigintillion");
	public static final TranslatedStringPath		FORMAT_UNTRIGINTILLION			= new TranslatedStringPath("Untrigintillion");
	public static final TranslatedStringPath		FORMAT_DUOTRIGINTILLION			= new TranslatedStringPath("Duotrigintillion");
	public static final TranslatedStringPath		FORMAT_TRESTRIGINTILLION		= new TranslatedStringPath("Trestrigintillion");
	public static final TranslatedStringPath		FORMAT_QUATTUORTRIGINTILLION	= new TranslatedStringPath("Quattuortrigintillion");
	public static final TranslatedStringPath		FORMAT_QUINQUATRIGINTILLION		= new TranslatedStringPath("Quinquatrigintillion");
	public static final TranslatedStringPath		FORMAT_SESTRIGINTILLION			= new TranslatedStringPath("Sestrigintillion");
	public static final TranslatedStringPath		FORMAT_OCTOTRIGINTILLION		= new TranslatedStringPath("Octotrigintillion");
	public static final TranslatedStringPath		FORMAT_NOVENTRIGINTILLION		= new TranslatedStringPath("Noventrigintillion");
	public static final TranslatedStringPath		FORMAT_QUADRAGINTILLION			= new TranslatedStringPath("Quadragintillion");
	
	public static void sendMessage(CommandSender sender, String message){
		for(String splitMessage : message.split("\n")){
			sender.sendMessage(splitMessage);
		}
	}
	
	public static void sendFormattedMessage(CommandSender sender, String message){
		sendMessage(sender, MESSAGE_FORMAT.get().replace("<message>", message));
	}
	
	public static void sendFormattedMessage(CorePlayers pl, String message){
		if(pl.getPlayer() != null){
			sendMessage(pl.getPlayer(), MESSAGE_FORMAT.get().replace("<message>", message));
		}
	}
	
	private BankLanguageConfiguration(JavaPlugin plugin){
		super(plugin, "language.yml");
	}
	
}
