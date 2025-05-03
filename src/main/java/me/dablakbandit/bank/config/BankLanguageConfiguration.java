package me.dablakbandit.bank.config;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.path.impl.BankTranslatedStringListPath;
import me.dablakbandit.bank.config.path.impl.BankTranslatedStringPath;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.EmptyPath;
import me.dablakbandit.core.config.path.Path;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BankLanguageConfiguration extends CommentAdvancedConfiguration{

	private static BankLanguageConfiguration configuration;

	public static void load(BankPlugin plugin) {
		configuration = new BankLanguageConfiguration(plugin);
		configuration.load();
	}
	
	public static BankLanguageConfiguration getInstance(){
		return configuration;
	}

	// @formatter:off
	@SuppressWarnings({"rawtypes", "unused"})
	@CommentArray({ "Edit messages from the plugin here.", "Use <nl> for new lines" })
	private static final Path						BANK							= new EmptyPath();
	
	@Comment("Available: <block>")
	public static final BankTranslatedStringPath	BANK_ADMIN_BLOCK_SET			= new BankTranslatedStringPath(ChatColor.GREEN + "Block <block> set as bank");
	@Comment("Available: <block>")
	public static final BankTranslatedStringPath	BANK_ADMIN_BLOCK_NOT			= new BankTranslatedStringPath(ChatColor.GREEN + "Block <block> is not a bank");
	@Comment("Available: <block>")
	public static final BankTranslatedStringPath	BANK_ADMIN_BLOCK_REMOVE			= new BankTranslatedStringPath(ChatColor.GREEN + "Block <block> removed as bank");

	@Comment("Available: <url>")
	public static final BankTranslatedStringPath	BANK_ADMIN_DEBUG_ITEM_URL			= new BankTranslatedStringPath(ChatColor.GREEN + "Uploaded item info to <url>, click to open/copy");

	public static final BankTranslatedStringPath	BANK_ADMIN_DEBUG_ITEM_ERROR			= new BankTranslatedStringPath(ChatColor.RED + "Unable to upload item, please view console logs for errors");

	public static final BankTranslatedStringPath	BANK_ADMIN_DEBUG_ITEM_NONE			= new BankTranslatedStringPath(ChatColor.RED + "Please hold an item in your hand");
	@Comment("Available: <player>, <new_amount>")
	public static final BankTranslatedStringPath	BANK_ADMIN_SLOTS_ADD			= new BankTranslatedStringPath("Added <amount> slots to <player>, new amount <new_amount>");
	@Comment("Available: <player>")
	public static final BankTranslatedStringPath	BANK_ADMIN_SLOTS_SET			= new BankTranslatedStringPath("Set <player> slots to <amount>");
	@Comment("Available: <version>")
	public static final BankTranslatedStringPath	BANK_ADMIN_RELOAD				= new BankTranslatedStringPath("Bank v<version> reloaded.");
	@Comment("Available: <player>")
	public static final BankTranslatedStringPath	BANK_ADMIN_LOADING				=
		new BankTranslatedStringPath(ChatColor.GRAY + "Offline loading <player>, do '/bank admin save' when complete");
	public static final BankTranslatedStringPath	BANK_ADMIN_SAVED				= new BankTranslatedStringPath(ChatColor.GREEN + "Saved opened player.");
	public static final BankTranslatedStringPath	BANK_ADMIN_INVENTORY_PREFIX		= new BankTranslatedStringPath(ChatColor.RED + "Admin");
	@Comment("Available: <player>")
	public static final BankTranslatedStringPath BANK_ADMIN_PERMISSION_UPDATED = new BankTranslatedStringPath(ChatColor.GREEN + "Updated permissions for <player>.");
	@Comment("Available: <player>")
	public static final BankTranslatedStringPath BANK_ADMIN_PLAYER_LOCKED = new BankTranslatedStringPath(ChatColor.GREEN + "<player>'s account is locked.");
	@Comment("Available: <player>, <money>")
	public static final BankTranslatedStringPath BANK_ADMIN_MONEY_BALANCE = new BankTranslatedStringPath(ChatColor.GREEN + "<player> has $<money> in the bank.");
	@Comment("Available: <player>, <amount>, <money>")
	public static final BankTranslatedStringPath BANK_ADMIN_MONEY_ADD = new BankTranslatedStringPath(ChatColor.GREEN + "Added $<amount> to <player>, new balance $<money>.");
	@Comment("Available: <player>, <amount>, <money>")
	public static final BankTranslatedStringPath BANK_ADMIN_MONEY_SUBTRACT = new BankTranslatedStringPath(ChatColor.GREEN + "Subtracted $<amount> from <player>, new balance $<money>.");

	@Comment("Available: <player>, <exp>")
	public static final BankTranslatedStringPath BANK_ADMIN_EXP_BALANCE = new BankTranslatedStringPath(ChatColor.GREEN + "<player> has <exp> in the bank.");
	@Comment("Available: <player>, <amount>, <exp>")
	public static final BankTranslatedStringPath BANK_ADMIN_EXP_ADD = new BankTranslatedStringPath(ChatColor.GREEN + "Added <exp> exp to <player>, new balance <exp>.");
	@Comment("Available: <player>, <amount>, <exp>")
	public static final BankTranslatedStringPath BANK_ADMIN_EXP_SUBTRACT = new BankTranslatedStringPath(ChatColor.GREEN + "Subtracted <amount> exp from <player>, balance <exp>.");


	@Comment("Available: <base>, <args>")
	public static final BankTranslatedStringPath	COMMAND_TITLE_FORMAT			=
		new BankTranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "<base>" + ChatColor.AQUA + "<args>" + ChatColor.GRAY + "]");
	@Comment("Available: <base>, <args>")
	public static final BankTranslatedStringPath	COMMAND_COMMANDS_FORMAT			=
		new BankTranslatedStringPath(ChatColor.WHITE + "/" + ChatColor.GRAY + "<base>" + ChatColor.AQUA + "<args>");
	@Comment("Available: <message>")
	public static final BankTranslatedStringPath	COMMAND_MESSAGE_FORMAT			=
		new BankTranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "Bank" + ChatColor.GRAY + "] <message>");
	@Comment("Available: <base>, <args>")
	public static final BankTranslatedStringPath	COMMAND_UNKNOWN_FORMAT			= new BankTranslatedStringPath(ChatColor.RED + "Unknown command /<base> <args>");
	@Comment("Available: <player>")
	public static final BankTranslatedStringPath	COMMAND_UNKNOWN_PLAYER			= new BankTranslatedStringPath(ChatColor.RED + "Unknown player <player>");
	public static final BankTranslatedStringPath	COMMAND_NO_PERMISSION			= new BankTranslatedStringPath(ChatColor.RED + "You do not have the permission to perform this command.");
	public static final BankTranslatedStringPath 	COMMAND_NOT_AVAILABLE 			= new BankTranslatedStringPath(ChatColor.RED + "This command is not available.");
	@Comment("Available: <value>")
	public static final BankTranslatedStringPath	COMMAND_UNABLE_PARSE			= new BankTranslatedStringPath(ChatColor.RED + "Unable to parse <value>.");

	public static final BankTranslatedStringPath	ANVIL_ITEM_TAB_RENAME			= new BankTranslatedStringPath("Rename Tab <i>");
	public static final BankTranslatedStringPath	ANVIL_EXP_WTIHDRAW				= new BankTranslatedStringPath("Withdraw");
	public static final BankTranslatedStringPath	ANVIL_EXP_DEPOSIT				= new BankTranslatedStringPath("Deposit");
	public static final BankTranslatedStringPath	ANVIL_EXP_SEND_NAME				= new BankTranslatedStringPath("Enter Player Name");
	public static final BankTranslatedStringPath	ANVIL_EXP_SEND_AMOUNT			= new BankTranslatedStringPath("Enter Amount");
	public static final BankTranslatedStringPath	ANVIL_MONEY_WTIHDRAW			= new BankTranslatedStringPath("Withdraw");
	public static final BankTranslatedStringPath	ANVIL_MONEY_DEPOSIT				= new BankTranslatedStringPath("Deposit");
	public static final BankTranslatedStringPath	ANVIL_MONEY_SEND_NAME			= new BankTranslatedStringPath("Enter Player Name");
	public static final BankTranslatedStringPath	ANVIL_MONEY_SEND_AMOUNT			= new BankTranslatedStringPath("Enter Amount");
	
	@Comment("Available: <message>")
	public static final BankTranslatedStringPath	MESSAGE_FORMAT					=
		new BankTranslatedStringPath(ChatColor.GRAY + "[" + ChatColor.GREEN + "Bank" + ChatColor.GRAY + "] <message>");
	@Comment("Available: <money>, <tax>")
	public static final BankTranslatedStringPath	MESSAGE_MONEY_DEPOSIT			= new BankTranslatedStringPath(ChatColor.GREEN + "You have deposited $<money> to the bank.");
	@Comment("Available: <money>, <tax>")
	public static final BankTranslatedStringPath	MESSAGE_MONEY_WITHDRAW			= new BankTranslatedStringPath(ChatColor.GREEN + "You have withdrawn $<money> from the bank.");
	@Comment("Available: <money>")
	public static final BankTranslatedStringPath	MESSAGE_MONEY_BALANCE			= new BankTranslatedStringPath(ChatColor.GREEN + "You have $<money> in the bank.");
	@Comment("Available: <money>")
	public static final BankTranslatedStringPath		MESSAGE_MONEY_NOT_ENOUGH		=
		new BankTranslatedStringPath(ChatColor.RED + "You do not have enough money to do that, your balance is $<money>.");
	@Comment("Available: <money>, <name>")
	public static final BankTranslatedStringPath	MESSAGE_MONEY_SENT				= new BankTranslatedStringPath(ChatColor.GREEN + "Successfully sent $<money> to <name>");
	@Comment("Available: <money>, <name>")
	public static final BankTranslatedStringPath	MESSAGE_MONEY_RECEIVED			= new BankTranslatedStringPath(ChatColor.GREEN + "Received $<money> from <name>");
	@Comment("Available: <amount>")
	public static final BankTranslatedStringPath	MESSAGE_MONEY_INTEREST_GAINED	= new BankTranslatedStringPath(ChatColor.GREEN + "You gained $<amount> in interest");
	
	@Comment("Available: <exp>, <tax>")
	public static final BankTranslatedStringPath	MESSAGE_EXP_DEPOSIT				= new BankTranslatedStringPath(ChatColor.GREEN + "You have deposited <exp> exp to the bank.");
	@Comment("Available: <exp>, <tax>")
	public static final BankTranslatedStringPath	MESSAGE_EXP_WITHDRAW			= new BankTranslatedStringPath(ChatColor.GREEN + "You have withdrawn <exp> exp from the bank.");
	@Comment("Available: <exp>")
	public static final BankTranslatedStringPath	MESSAGE_EXP_BALANCE				= new BankTranslatedStringPath(ChatColor.GREEN + "You have <exp> exp in the bank.");
	@Comment("Available: <exp>")
	public static final BankTranslatedStringPath	MESSAGE_EXP_NOT_ENOUGH			= new BankTranslatedStringPath(ChatColor.RED + "You do not have enough exp to do that.");
	@Comment("Available: <exp>, <name>")
	public static final BankTranslatedStringPath	MESSAGE_EXP_SENT				= new BankTranslatedStringPath(ChatColor.GREEN + "Successfully sent <exp> to <name>");
	@Comment("Available: <exp>, <name>")
	public static final BankTranslatedStringPath	MESSAGE_EXP_RECEIVED			= new BankTranslatedStringPath(ChatColor.GREEN + "Received <exp> from <name>");
	@Comment("Available: <amount>")
	public static final BankTranslatedStringPath	MESSAGE_EXP_INTEREST_GAINED		= new BankTranslatedStringPath(ChatColor.GREEN + "You gained <amount> in exp interest");
	
	@Comment("Available: <permission>")
	public static final BankTranslatedStringPath	MESSAGE_PERMISSION_REQUIREMENT	= new BankTranslatedStringPath(ChatColor.RED + "You require permission: <permission> to do that.");
	
	@Comment("Available: <amount>")
	public static final BankTranslatedStringPath	MESSAGE_LOANS_PAYBACK_AMOUNT	= new BankTranslatedStringPath(ChatColor.GREEN + "You payed back $<amount> of your loans");
	@Comment("Available: <amount>")
	public static final BankTranslatedStringPath	MESSAGE_LOANS_INTEREST_GAINED	= new BankTranslatedStringPath(ChatColor.GREEN + "Your loans gained $<amount> in interest");
	
	public static final BankTranslatedStringPath	MESSAGE_BANK_LOCKED				= new BankTranslatedStringPath(ChatColor.RED + "Bank locked/loading please wait");
	public static final BankTranslatedStringPath	MESSAGE_BANK_UNLOCKED			= new BankTranslatedStringPath(ChatColor.GREEN + "Bank unlocked!");
	public static final BankTranslatedStringPath	MESSAGE_PIN_ENTER_BEFORE		= new BankTranslatedStringPath(ChatColor.RED + "You must enter your pin before doing that.");
	@Comment("Available: <amount>, <formatted_amount>")
	public static final BankTranslatedStringPath MESSAGE_ITEM_MERGE_COUNT = new BankTranslatedStringPath(ChatColor.YELLOW + "x<formatted_amount>");

	public static final BankTranslatedStringPath	MESSAGE_INFO_BORDER				=
		new BankTranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	//@formatter:off
	public static final BankTranslatedStringListPath MESSAGE_INFO_VIEW				= new BankTranslatedStringListPath(
			ChatColor.AQUA + " Welcome <player_name>",
				ChatColor.AQUA + " Bank balance $<bank_money>",
				ChatColor.AQUA + " Bank exp <bank_exp>, <bank_exp_level> levels",
				ChatColor.AQUA + " Money interest in <bank_interest_money_minutes> minutes (<bank_interest_money_seconds> seconds)",
				ChatColor.AQUA + " Exp interest in <bank_interest_exp_minutes> minutes (<bank_interest_exp_seconds> seconds)");
	//@formatter:on
	
	public static final BankTranslatedStringPath	MESSAGE_TOP_MONEY_BORDER		=
		new BankTranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	@Comment("Available: <number>, <name>, <amount>")
	public static final BankTranslatedStringPath	MESSAGE_TOP_MONEY_INFO			=
		new BankTranslatedStringPath(ChatColor.GRAY + "<number>. " + ChatColor.GREEN + "<name>: " + ChatColor.GOLD + "$<amount>");
	
	public static final BankTranslatedStringPath	MESSAGE_TOP_EXP_BORDER			=
		new BankTranslatedStringPath(ChatColor.GREEN + "-----------------------------------------------------");
	@Comment("Available: <number>, <name>, <amount>")
	public static final BankTranslatedStringPath	MESSAGE_TOP_EXP_INFO			=
		new BankTranslatedStringPath(ChatColor.GRAY + "<number>. " + ChatColor.GREEN + "<name>: " + ChatColor.GOLD + "$<amount>");
	
	public static final BankTranslatedStringPath	MESSAGE_WORLD_DISABLED			= new BankTranslatedStringPath(ChatColor.RED + "Bank disabled in this world.");
	
	@Comment("Available: <gamemode>")
	public static final BankTranslatedStringPath	MESSAGE_GAMEMODE_DISABLED		= new BankTranslatedStringPath(ChatColor.RED + "Bank disabled for <gamemode>.");
	@Comment("Available: <size>")
	public static final BankTranslatedStringPath MESSAGE_BALTOP_ORDERING = new BankTranslatedStringPath(ChatColor.GOLD + "Ordering balances of " + ChatColor.RED + "<size>" + ChatColor.GOLD + " users, please wait...");
	@Comment("Available: <command>, <next>, ")
	public static final BankTranslatedStringListPath MESSAGE_BALTOP_HEADER = new BankTranslatedStringListPath(ChatColor.GOLD + "Top balances (<date>)",
			ChatColor.YELLOW + "---- " + ChatColor.GOLD + "Balancetop " + ChatColor.YELLOW + "-- " + ChatColor.GOLD + "Page " + ChatColor.RED + "<page>" + ChatColor.GOLD + "/" + ChatColor.RED + "<max_page> " + ChatColor.YELLOW + "----",
			ChatColor.GOLD + "Server Total: " + ChatColor.RED + "$<total>");
	@Comment("Available: <index>, <name>, <amount>")
	public static final BankTranslatedStringPath MESSAGE_BALTOP_FORMAT = new BankTranslatedStringPath(ChatColor.WHITE + "<index>. <name>: $<amount>");
	@Comment("Available: <command>, <next>, <date>, <size>, <total>, <page>, <max_page>")
	public static final BankTranslatedStringListPath MESSAGE_BALTOP_FOOTER = new BankTranslatedStringListPath(ChatColor.GOLD + "Type " + ChatColor.RED + "/<command> <next>" + ChatColor.GOLD + " to read the next page.");

	@Comment("Transaction: Money deposit. Available: <amount>")
	public static final BankTranslatedStringPath TRANSACTION_MONEY_DEPOSIT = new BankTranslatedStringPath("Deposited $<amount> to your bank.");
	@Comment("Transaction: Money withdrawal. Available: <amount>")
	public static final BankTranslatedStringPath TRANSACTION_MONEY_WITHDRAWAL = new BankTranslatedStringPath("Withdrew $<amount> from your bank.");
	@Comment("Transaction: Money sent to. Available: <amount>, <name>")
	public static final BankTranslatedStringPath TRANSACTION_MONEY_SEND_TO = new BankTranslatedStringPath("Sent $<amount> to <name>.");
	@Comment("Transaction: Money received from. Available: <amount>, <name>")
	public static final BankTranslatedStringPath TRANSACTION_MONEY_RECEIVE_FROM = new BankTranslatedStringPath("Received $<amount> from <param>.");
	@Comment("Transaction: Money interest. Available: <amount>")
	public static final BankTranslatedStringPath TRANSACTION_MONEY_INTEREST = new BankTranslatedStringPath("Gained $<amount> in interest.");
	@Comment("Transaction: Money tax. Available: <amount>, <desc>")
	public static final BankTranslatedStringPath TRANSACTION_MONEY_TAX = new BankTranslatedStringPath("Paid $<amount> in tax.");
	@Comment("Transaction: EXP deposit. Available: <amount>")
	public static final BankTranslatedStringPath TRANSACTION_EXP_DEPOSIT = new BankTranslatedStringPath("Deposited <amount> exp to your bank.");
	@Comment("Transaction: EXP withdrawal. Available: <amount>")
	public static final BankTranslatedStringPath TRANSACTION_EXP_WITHDRAWAL = new BankTranslatedStringPath("Withdrew <amount> exp from your bank.");
	@Comment("Transaction: EXP sent to. Available: <amount>, <name>")
	public static final BankTranslatedStringPath TRANSACTION_EXP_SEND_TO = new BankTranslatedStringPath("Sent <amount> exp to <param>.");
	@Comment("Transaction: EXP received from. Available: <amount>, <name>")
	public static final BankTranslatedStringPath TRANSACTION_EXP_RECEIVE_FROM = new BankTranslatedStringPath("Received <amount> exp from <param>.");
	@Comment("Transaction: EXP interest. Available: <amount>")
	public static final BankTranslatedStringPath TRANSACTION_EXP_INTEREST = new BankTranslatedStringPath("Gained <amount> exp in interest.");
	@Comment("Transaction: EXP tax. Available: <amount>, <desc>")
	public static final BankTranslatedStringPath TRANSACTION_EXP_TAX = new BankTranslatedStringPath("Paid <amount> exp in tax.");

	public static final BankTranslatedStringPath	FORMAT_THOUSAND					= new BankTranslatedStringPath("K");
	public static final BankTranslatedStringPath	FORMAT_MILLION					= new BankTranslatedStringPath("Million");
	public static final BankTranslatedStringPath	FORMAT_BILLION					= new BankTranslatedStringPath("Billion");
	public static final BankTranslatedStringPath	FORMAT_TRILLION					= new BankTranslatedStringPath("Trillion");
	public static final BankTranslatedStringPath	FORMAT_QUADRILLION				= new BankTranslatedStringPath("Quadrillion");
	public static final BankTranslatedStringPath	FORMAT_QUINTILLION				= new BankTranslatedStringPath("Quintillion");
	public static final BankTranslatedStringPath	FORMAT_SEXTILLION				= new BankTranslatedStringPath("Sextillion");
	public static final BankTranslatedStringPath	FORMAT_SEPTILLION				= new BankTranslatedStringPath("Septillion");
	public static final BankTranslatedStringPath	FORMAT_OCTILLION				= new BankTranslatedStringPath("Octillion");
	public static final BankTranslatedStringPath	FORMAT_NONILLION				= new BankTranslatedStringPath("Nonillion");
	public static final BankTranslatedStringPath	FORMAT_DECILLION				= new BankTranslatedStringPath("Decillion");
	public static final BankTranslatedStringPath	FORMAT_UNDECILLION				= new BankTranslatedStringPath("Undecillion");
	public static final BankTranslatedStringPath	FORMAT_DUODECILLION				= new BankTranslatedStringPath("Duodecillion");
	public static final BankTranslatedStringPath	FORMAT_TREDECILLION				= new BankTranslatedStringPath("Tredecillion");
	public static final BankTranslatedStringPath	FORMAT_QUATTUORDECILLION		= new BankTranslatedStringPath("Quattuordecillion");
	public static final BankTranslatedStringPath	FORMAT_QUINQUADECILLION			= new BankTranslatedStringPath("Quinquadecillion");
	public static final BankTranslatedStringPath	FORMAT_SEDECILLION				= new BankTranslatedStringPath("Sedecillion");
	public static final BankTranslatedStringPath	FORMAT_SEPTENDECILLION			= new BankTranslatedStringPath("Septendecillion");
	public static final BankTranslatedStringPath	FORMAT_OCTODECILLION			= new BankTranslatedStringPath("Octodecillion");
	public static final BankTranslatedStringPath	FORMAT_NOVENDECILLION			= new BankTranslatedStringPath("Novendecillion");
	public static final BankTranslatedStringPath	FORMAT_VIGINTILLION				= new BankTranslatedStringPath("Vigintillion");
	public static final BankTranslatedStringPath	FORMAT_UNVIGINTILLION			= new BankTranslatedStringPath("Unvigintillion");
	public static final BankTranslatedStringPath	FORMAT_DUOVIGINTILLION			= new BankTranslatedStringPath("Duovigintillion");
	public static final BankTranslatedStringPath	FORMAT_TRESVIGINTILLION			= new BankTranslatedStringPath("Tresvegintillion");
	public static final BankTranslatedStringPath	FORMAT_QUATTUORVIGINTILLION		= new BankTranslatedStringPath("Quattuorvigintillion");
	public static final BankTranslatedStringPath	FORMAT_QUINQUAVIGINTILLION		= new BankTranslatedStringPath("Quinquavigintillion");
	public static final BankTranslatedStringPath	FORMAT_SESVIGINTILLION			= new BankTranslatedStringPath("Sesvigintillion");
	public static final BankTranslatedStringPath	FORMAT_SEPTEMVIGINTILLION		= new BankTranslatedStringPath("Septemvigintillion");
	public static final BankTranslatedStringPath	FORMAT_OCTOVIGINTILLION			= new BankTranslatedStringPath("Octovigintillion");
	public static final BankTranslatedStringPath	FORMAT_NOVEMVIGINTILLION		= new BankTranslatedStringPath("Novemvigintillion");
	public static final BankTranslatedStringPath	FORMAT_TRIGINTILLION			= new BankTranslatedStringPath("Trigintillion");
	public static final BankTranslatedStringPath	FORMAT_UNTRIGINTILLION			= new BankTranslatedStringPath("Untrigintillion");
	public static final BankTranslatedStringPath	FORMAT_DUOTRIGINTILLION			= new BankTranslatedStringPath("Duotrigintillion");
	public static final BankTranslatedStringPath	FORMAT_TRESTRIGINTILLION		= new BankTranslatedStringPath("Trestrigintillion");
	public static final BankTranslatedStringPath	FORMAT_QUATTUORTRIGINTILLION	= new BankTranslatedStringPath("Quattuortrigintillion");
	public static final BankTranslatedStringPath	FORMAT_QUINQUATRIGINTILLION		= new BankTranslatedStringPath("Quinquatrigintillion");
	public static final BankTranslatedStringPath	FORMAT_SESTRIGINTILLION			= new BankTranslatedStringPath("Sestrigintillion");
	public static final BankTranslatedStringPath	FORMAT_OCTOTRIGINTILLION		= new BankTranslatedStringPath("Octotrigintillion");
	public static final BankTranslatedStringPath	FORMAT_NOVENTRIGINTILLION		= new BankTranslatedStringPath("Noventrigintillion");
	public static final BankTranslatedStringPath	FORMAT_QUADRAGINTILLION			= new BankTranslatedStringPath("Quadragintillion");
	
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
		super(plugin, "conf/language.yml");
	}

}
