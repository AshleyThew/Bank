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
	
	private static BankLanguageConfiguration configuration = new BankLanguageConfiguration(BankPlugin.getInstance());
	
	public static BankLanguageConfiguration getInstance(){
		return configuration;
	}
	
	@Comment("Edit messages from the plugin here")
	private static Path						BANK							= new EmptyPath();
	
	@Comment("Available: <block>")
	public static TranslatedStringPath		BANK_ADMIN_BLOCK_SET			= new TranslatedStringPath(ChatColor.GREEN + "Block <block> set as bank");
	@Comment("Available: <block>")
	public static TranslatedStringPath		BANK_ADMIN_BLOCK_NOT			= new TranslatedStringPath(ChatColor.GREEN + "Block <block> is not a bank");
	@Comment("Available: <block>")
	public static TranslatedStringPath		BANK_ADMIN_BLOCK_REMOVE			= new TranslatedStringPath(ChatColor.GREEN + "Block <block> removed as bank");
	@Comment("Available: <player>, <new_amount>")
	public static TranslatedStringPath		BANK_ADMIN_SLOTS_ADD			= new TranslatedStringPath("Added <amount> slots to <player>, new amount <new_amount>");
	@Comment("Available: <player>")
	public static TranslatedStringPath		BANK_ADMIN_SLOTS_SET			= new TranslatedStringPath("Set <player> slots to <amount>");
	@Comment("Available: <version>")
	public static TranslatedStringPath		BANK_ADMIN_RELOAD				= new TranslatedStringPath("Bank v<version> reloaded.");
	@Comment("Available: <player>")
	public static TranslatedStringPath		BANK_ADMIN_LOADING				= new TranslatedStringPath(ChatColor.GRAY + "Offline loading <player>, do '/bank admin save' when complete");
	public static TranslatedStringPath		BANK_ADMIN_SAVED				= new TranslatedStringPath(ChatColor.GREEN + "Saved opened player.");
	public static TranslatedStringPath		BANK_ADMIN_INVENTORY_PREFIX		= new TranslatedStringPath(ChatColor.RED + "Admin");
	@Comment("Available: <player>")
	public static TranslatedStringPath		BANK_ADMIN_PERMISSION_UPDATED	= new TranslatedStringPath(ChatColor.GREEN + "Updated permissions for <player>");
	
	@Comment("Available: <base>, <args>")
	public static TranslatedStringPath		COMMAND_TITLE_FORMAT			= new TranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "<base>" + ChatColor.AQUA + "<args>" + ChatColor.GRAY + "]");
	@Comment("Available: <base>, <args>")
	public static TranslatedStringPath		COMMAND_COMMANDS_FORMAT			= new TranslatedStringPath(ChatColor.WHITE + "/" + ChatColor.GRAY + "<base>" + ChatColor.AQUA + "<args>");
	@Comment("Available: <message>")
	public static TranslatedStringPath		COMMAND_MESSAGE_FORMAT			= new TranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "Bank" + ChatColor.GRAY + "] <message>");
	@Comment("Available: <base>, <args>")
	public static TranslatedStringPath		COMMAND_UNKNOWN_FORMAT			= new TranslatedStringPath(ChatColor.RED + "Unknown command /<base> <args>");
	@Comment("Available: <player>")
	public static TranslatedStringPath		COMMAND_UNKNOWN_PLAYER			= new TranslatedStringPath(ChatColor.RED + "Unknown player <player>");
	public static TranslatedStringPath		COMMAND_NO_PERMISSION			= new TranslatedStringPath(ChatColor.RED + "You do not have the permission to perform this command.");
	@Comment("Available: <value>")
	public static TranslatedStringPath		COMMAND_UNABLE_PARSE			= new TranslatedStringPath(ChatColor.RED + "Unable to parse <value>.");
	
	@Comment("Available: <message>")
	public static TranslatedStringPath		MESSAGE_FORMAT					= new TranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "Bank" + ChatColor.GRAY + "] <message>");
	@Comment("Available: <money>")
	public static TranslatedStringPath		MESSAGE_MONEY_DEPOSIT			= new TranslatedStringPath(ChatColor.GREEN + "You have deposited $<money> to the bank.");
	@Comment("Available: <money>")
	public static TranslatedStringPath		MESSAGE_MONEY_WITHDRAW			= new TranslatedStringPath(ChatColor.GREEN + "You have withdrawn $<money> from the bank.");
	@Comment("Available: <money>")
	public static TranslatedStringPath		MESSAGE_MONEY_BALANCE			= new TranslatedStringPath(ChatColor.GREEN + "You have $<money> in the bank.");
	public static TranslatedStringPath		MESSAGE_MONEY_NOT_ENOUGH		= new TranslatedStringPath(ChatColor.RED + "You do not have enough money to do that.");
	@Comment("Available: <money>, <name>")
	public static TranslatedStringPath		MESSAGE_MONEY_SENT				= new TranslatedStringPath(ChatColor.GREEN + "Successfully sent $<money> to <name>");
	@Comment("Available: <amount>")
	public static TranslatedStringPath		MESSAGE_MONEY_INTEREST_GAINED	= new TranslatedStringPath(ChatColor.GREEN + "You gained $<amount> in interest");
	
	@Comment("Available: <exp>")
	public static TranslatedStringPath		MESSAGE_EXP_DEPOSIT				= new TranslatedStringPath(ChatColor.GREEN + "You have deposited <exp> exp to the bank.");
	@Comment("Available: <exp>")
	public static TranslatedStringPath		MESSAGE_EXP_WITHDRAW			= new TranslatedStringPath(ChatColor.GREEN + "You have withdrawn <exp> exp from the bank.");
	@Comment("Available: <exp>")
	public static TranslatedStringPath		MESSAGE_EXP_BALANCE				= new TranslatedStringPath(ChatColor.GREEN + "You have <exp> exp in the bank.");
	public static TranslatedStringPath		MESSAGE_EXP_NOT_ENOUGH			= new TranslatedStringPath(ChatColor.RED + "You do not have enough exp to do that.");
	@Comment("Available: <exp>, <name>")
	public static TranslatedStringPath		MESSAGE_EXP_SENT				= new TranslatedStringPath(ChatColor.GREEN + "Successfully sent <exp> to <name>");
	@Comment("Available: <amount>")
	public static TranslatedStringPath		MESSAGE_EXP_INTEREST_GAINED		= new TranslatedStringPath(ChatColor.GREEN + "You gained <amount> in exp interest");
	
	@Comment("Available: <permission>")
	public static TranslatedStringPath		MESSAGE_PERMISSION_REQUIREMENT	= new TranslatedStringPath(ChatColor.RED + "You require permission: <permission> to do that.");
	
	@Comment("Available: <amount>")
	public static TranslatedStringPath		MESSAGE_LOANS_PAYBACK_AMOUNT	= new TranslatedStringPath(ChatColor.GREEN + "You payed back $<amount> of your loans");
	@Comment("Available: <amount>")
	public static TranslatedStringPath		MESSAGE_LOANS_INTEREST_GAINED	= new TranslatedStringPath(ChatColor.GREEN + "Your loans gained $<amount> in interest");
	
	public static TranslatedStringPath		MESSAGE_BANK_LOCKED				= new TranslatedStringPath(ChatColor.RED + "Bank locked/loading please wait");
	public static TranslatedStringPath		MESSAGE_BANK_UNLOCKED			= new TranslatedStringPath(ChatColor.GREEN + "Bank unlocked!");
	public static TranslatedStringPath		MESSAGE_PIN_ENTER_BEFORE		= new TranslatedStringPath(ChatColor.RED + "You must enter your pin before doing that.");
	
	public static TranslatedStringPath		MESSAGE_INFO_BORDER				= new TranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	//@formatter:off
	public static TranslatedStringListPath	MESSAGE_INFO_VIEW				= new TranslatedStringListPath(
			ChatColor.AQUA + " Bank balance $<bank_money>",
				ChatColor.AQUA + " Bank exp <bank_exp>, <bank_exp_level> levels",
				ChatColor.AQUA + " Money interest in <bank_interest_money_minutes> minutes (<bank_interest_money_seconds> seconds)",
				ChatColor.AQUA + " Exp interest in <bank_interest_exp_minutes> minutes (<bank_interest_exp_seconds> seconds)");
	//@formatter:on
	
	public static TranslatedStringPath		MESSAGE_TOP_MONEY_BORDER		= new TranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	@Comment("Available: <number>, <name>, <amount>")
	public static TranslatedStringPath		MESSAGE_TOP_MONEY_INFO			= new TranslatedStringPath(ChatColor.GRAY + "<number>. " + ChatColor.GREEN + "<name>: " + ChatColor.GOLD + "$<amount>");
	
	public static TranslatedStringPath		MESSAGE_TOP_EXP_BORDER			= new TranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	@Comment("Available: <number>, <name>, <amount>")
	public static TranslatedStringPath		MESSAGE_TOP_EXP_INFO			= new TranslatedStringPath(ChatColor.GRAY + "<number>. " + ChatColor.GREEN + "<name>: " + ChatColor.GOLD + "$<amount>");
	
	public static TranslatedStringPath		FORMAT_MILLION					= new TranslatedStringPath("Million");
	public static TranslatedStringPath		FORMAT_BILLION					= new TranslatedStringPath("Billion");
	public static TranslatedStringPath		FORMAT_TRILLION					= new TranslatedStringPath("Trillion");
	public static TranslatedStringPath		FORMAT_QUADRILLION				= new TranslatedStringPath("Quadrillion");
	public static TranslatedStringPath		FORMAT_QUINTILLION				= new TranslatedStringPath("Quintillion");
	public static TranslatedStringPath		FORMAT_SEXTILLION				= new TranslatedStringPath("Sextillion");
	public static TranslatedStringPath		FORMAT_SEPTILLION				= new TranslatedStringPath("Septillion");
	public static TranslatedStringPath		FORMAT_OCTILLION				= new TranslatedStringPath("Octillion");
	public static TranslatedStringPath		FORMAT_NONILLION				= new TranslatedStringPath("Nonillion");
	public static TranslatedStringPath		FORMAT_DECILLION				= new TranslatedStringPath("Decillion");
	public static TranslatedStringPath		FORMAT_UNDECILLION				= new TranslatedStringPath("Undecillion");
	public static TranslatedStringPath		FORMAT_DUODECILLION				= new TranslatedStringPath("Duodecillion");
	public static TranslatedStringPath		FORMAT_TREDECILLION				= new TranslatedStringPath("Tredecillion");
	public static TranslatedStringPath		FORMAT_QUATTUORDECILLION		= new TranslatedStringPath("Quattuordecillion");
	public static TranslatedStringPath		FORMAT_QUINQUADECILLION			= new TranslatedStringPath("Quinquadecillion");
	public static TranslatedStringPath		FORMAT_SEDECILLION				= new TranslatedStringPath("Sedecillion");
	public static TranslatedStringPath		FORMAT_SEPTENDECILLION			= new TranslatedStringPath("Septendecillion");
	public static TranslatedStringPath		FORMAT_OCTODECILLION			= new TranslatedStringPath("Octodecillion");
	public static TranslatedStringPath		FORMAT_NOVENDECILLION			= new TranslatedStringPath("Novendecillion");
	public static TranslatedStringPath		FORMAT_VIGINTILLION				= new TranslatedStringPath("Vigintillion");
	public static TranslatedStringPath		FORMAT_UNVIGINTILLION			= new TranslatedStringPath("Unvigintillion");
	public static TranslatedStringPath		FORMAT_DUOVIGINTILLION			= new TranslatedStringPath("Duovigintillion");
	public static TranslatedStringPath		FORMAT_TRESVIGINTILLION			= new TranslatedStringPath("Tresvegintillion");
	public static TranslatedStringPath		FORMAT_QUATTUORVIGINTILLION		= new TranslatedStringPath("Quattuorvigintillion");
	public static TranslatedStringPath		FORMAT_QUINQUAVIGINTILLION		= new TranslatedStringPath("Quinquavigintillion");
	public static TranslatedStringPath		FORMAT_SESVIGINTILLION			= new TranslatedStringPath("Sesvigintillion");
	public static TranslatedStringPath		FORMAT_SEPTEMVIGINTILLION		= new TranslatedStringPath("Septemvigintillion");
	public static TranslatedStringPath		FORMAT_OCTOVIGINTILLION			= new TranslatedStringPath("Octovigintillion");
	public static TranslatedStringPath		FORMAT_NOVEMVIGINTILLION		= new TranslatedStringPath("Novemvigintillion");
	public static TranslatedStringPath		FORMAT_TRIGINTILLION			= new TranslatedStringPath("Trigintillion");
	public static TranslatedStringPath		FORMAT_UNTRIGINTILLION			= new TranslatedStringPath("Untrigintillion");
	public static TranslatedStringPath		FORMAT_DUOTRIGINTILLION			= new TranslatedStringPath("Duotrigintillion");
	public static TranslatedStringPath		FORMAT_TRESTRIGINTILLION		= new TranslatedStringPath("Trestrigintillion");
	public static TranslatedStringPath		FORMAT_QUATTUORTRIGINTILLION	= new TranslatedStringPath("Quattuortrigintillion");
	public static TranslatedStringPath		FORMAT_QUINQUATRIGINTILLION		= new TranslatedStringPath("Quinquatrigintillion");
	public static TranslatedStringPath		FORMAT_SESTRIGINTILLION			= new TranslatedStringPath("Sestrigintillion");
	public static TranslatedStringPath		FORMAT_OCTOTRIGINTILLION		= new TranslatedStringPath("Octotrigintillion");
	public static TranslatedStringPath		FORMAT_NOVENTRIGINTILLION		= new TranslatedStringPath("Noventrigintillion");
	public static TranslatedStringPath		FORMAT_QUADRAGINTILLION			= new TranslatedStringPath("Quadragintillion");
	
	public static void sendMessage(CommandSender sender, String message){
		sender.sendMessage(MESSAGE_FORMAT.get().replace("<message>", message));
	}
	
	public static void sendMessage(CorePlayers pl, String message){
		if(pl.getPlayer() != null){
			pl.getPlayer().sendMessage(MESSAGE_FORMAT.get().replace("<message>", message));
		}
	}
	
	private BankLanguageConfiguration(JavaPlugin plugin){
		super(plugin, "language.yml");
	}
	
}
