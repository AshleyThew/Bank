package me.dablakbandit.bank.config;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.path.impl.BankLoansPaybackFailedPath;
import me.dablakbandit.bank.config.path.impl.BankSynchronizedDoubleNicePath;
import me.dablakbandit.bank.implementations.blacklist.BlacklistMode;
import me.dablakbandit.bank.log.BankLogLevel;
import me.dablakbandit.bank.save.type.SaveType;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class BankPluginConfiguration extends CommentAdvancedConfiguration {

	private static BankPluginConfiguration configuration;

	public static void load(BankPlugin plugin) {
		configuration = new BankPluginConfiguration(plugin);
		configuration.load();
	}

	public static BankPluginConfiguration getInstance() {
		return configuration;
	}

	//@formatter:off
	@SuppressWarnings({"rawtypes", "unused"})
	@Comment("Edit the plugin here")
	private static final Path						BANK										= new EmptyPath();
	@Comment("SaveType of the plugin: SQLITE, MYSQL")
	public static final EnumPath<SaveType>			BANK_SAVE_TYPE								= new EnumPath<>(SaveType.class, SaveType.SQLITE);
	@CommentArray({ "Use time lock instead of traditional sql lock", "Make sure to set to the same value on each server!" })
	public static final BooleanPath					BANK_SAVE_LOCK_TIME							= new BooleanPath(false);
	@Comment("Enable Auto Save")
	public static final BooleanPath					BANK_SAVE_AUTO_ENABLED						= new BooleanPath(true);
	@Comment("Auto Save Timer (seconds)")
	public static final IntegerPath					BANK_SAVE_AUTO_TIMER						= new IntegerPath(600);
	@Comment("Delay load when joining the server if first load is unsuccessful (in ticks), -1 to disable")
	public static final IntegerPath					BANK_SAVE_LOAD_DELAY						= new IntegerPath(-1);

	@Comment("Auto Save when item deposited")
	public static final BooleanPath					BANK_SAVE_ITEM_DEPOSIT						= new BooleanPath(false);
	@Comment("Auto Save when item withdrawn")
	public static final BooleanPath					BANK_SAVE_ITEM_WITHDRAW						= new BooleanPath(false);
	@Comment("Auto Save when exp deposited")
	public static final BooleanPath					BANK_SAVE_EXP_DEPOSIT						= new BooleanPath(false);
	@Comment("Auto Save when exp withdrawn")
	public static final BooleanPath					BANK_SAVE_EXP_WITHDRAW						= new BooleanPath(false);
	@Comment("Auto Save when money deposited")
	public static final BooleanPath					BANK_SAVE_MONEY_DEPOSIT						= new BooleanPath(false);
	@Comment("Auto Save when money withdrawn")
	public static final BooleanPath					BANK_SAVE_MONEY_WITHDRAW					= new BooleanPath(false);

	@Comment("Set bank mode to items only")
	public static final BooleanPath					BANK_ITEMS_ONLY								= new BooleanPath(false);
	@Comment("Enable bank items")
	public static final BooleanPath					BANK_ITEMS_ENABLED							= new BooleanPath(true);
	@Comment("Enable the trashcan in the item gui")
	public static final BooleanPath					BANK_ITEMS_TRASHCAN_ENABLED					= new BooleanPath(true);
	@Comment("Enable the trashcan blacklist, requires the normal item blacklist to also be enabled")
	public static final BooleanPath					BANK_ITEMS_TRASHCAN_BLACKLIST_ENABLED		= new BooleanPath(false);

	@Comment("Blacklist mode (blacklist/whitelist)")
	public static final EnumPath<BlacklistMode>		BANK_ITEMS_TRASHCAN_BLACKLIST_MODE			= new EnumPath<>(BlacklistMode.class, BlacklistMode.BLACKLIST);
	@Comment("Enable the tabs in the item gui")
	public static final BooleanPath					BANK_ITEMS_TABS_ENABLED						= new BooleanPath(true);
	@Comment("Enable permissions for tabs: bank.tabs.<number>")
	public static final BooleanPath					BANK_ITEMS_TABS_PERMISSION_ENABLED			= new BooleanPath(false);
	@Comment("Default amount of item tabs")
	public static final IntegerPath					BANK_ITEMS_TABS_DEFAULT						= new IntegerPath(9);

	@Comment("Maximum amount of item tabs")
	public static final IntegerPath 				BANK_ITEMS_TABS_MAX 						= new IntegerPath(9);

	@Comment("Combine permissions to calculate tabs (permissions.yml)")
	public static final BooleanPath					BANK_ITEMS_TABS_PERMISSION_COMBINE			= new BooleanPath(false);
	@Comment("Maximum amount of items able to be stored in a tab")
	public static final IntegerPath					BANK_ITEMS_TABS_SIZE_MAX					= new IntegerPath(Integer.MAX_VALUE);
	@Comment("Enable buying of tabs")
	public static final BooleanPath					BANK_ITEMS_TABS_BUY_ENABLED					= new BooleanPath(false);
	@Comment("Cost for each new tabs")
	public static final IntegerPath					BANK_ITEMS_TABS_BUY_COST					= new IntegerPath(50);
	@Comment("Maximum amount of bought tabs")
	public static final IntegerPath 				BANK_ITEMS_TABS_BUY_MAX 					= new IntegerPath(9);
	@Comment("Enable tab renaming")
	public static final BooleanPath					BANK_ITEMS_TABS_RENAME_ENABLED				= new BooleanPath(false);
	@Comment("Money cost for renaming a tab")
	public static final IntegerPath					BANK_ITEMS_TABS_RENAME_COST_MONEY			= new IntegerPath(0);
	@Comment("Exp cost for renaming a tab")
	public static final IntegerPath					BANK_ITEMS_TABS_RENAME_COST_EXP				= new IntegerPath(0);
	@Comment("Enable tab amount setting")
	public static final BooleanPath					BANK_ITEMS_TABS_AMOUNT_SET					= new BooleanPath(true);
	@Comment("Default amount of item slots")
	public static final IntegerPath					BANK_ITEMS_SLOTS_DEFAULT					= new IntegerPath(50);
	@Comment("Enable merging of the same items in the bank")
	public static final BooleanPath 				BANK_ITEMS_SLOTS_MERGE_ENABLED 				= new BooleanPath(false);
	@Comment("Set the max stack count for merged items")
	public static final IntegerPath 				BANK_ITEMS_SLOTS_MERGE_MAX 					= new IntegerPath(Integer.MAX_VALUE);
	@Comment("Enable slots per tab instead of overall")
	public static final BooleanPath 				BANK_ITEMS_SLOTS_PER_TAB = new BooleanPath(false);
	@Comment("Set bought slots unique to each tab")
	public static final BooleanPath					BANK_ITEMS_SLOTS_BUY_PER_TAB				= new BooleanPath(true);
	@Comment("Enable buying of slots")
	public static final BooleanPath					BANK_ITEMS_SLOTS_BUY_ENABLED				= new BooleanPath(false);

	@Comment("Enable locked slots")
	public static final BooleanPath					BANK_ITEMS_SLOTS_LOCKED_ENABLED				= new BooleanPath(false);

	@Comment("Enable click to buy locked slots (also requires buy enabled above)")
	public static final BooleanPath					BANK_ITEMS_SLOTS_LOCKED_CLICK				= new BooleanPath(false);
	@Comment("Cost for each new slots")
	public static final IntegerPath					BANK_ITEMS_SLOTS_BUY_COST					= new IntegerPath(50);
	@Comment("Max amount of bought slots")
	public static final IntegerPath					BANK_ITEMS_SLOTS_BUY_MAX					= new IntegerPath(300);
	@Comment("Combine permissions to calculate max slots (permissions.yml)")
	public static final BooleanPath					BANK_ITEMS_SLOTS_PERMISSION_COMBINE			= new BooleanPath(false);
	@Comment("Enable item blacklist")
	public static final BooleanPath					BANK_ITEMS_BLACKLIST_ENABLED				= new BooleanPath(false);

	@Comment("Enable item defaults")
	public static final BooleanPath					BANK_ITEMS_DEFAULT_ENABLED					= new BooleanPath(false);

	@Comment("Blacklist mode (blacklist/whitelist)")
	public static final EnumPath<BlacklistMode> 	BANK_ITEMS_BLACKLIST_MODE					= new EnumPath<>(BlacklistMode.class, BlacklistMode.BLACKLIST);

	@Comment("Enable bank pin")
	public static final BooleanPath					BANK_PIN_ENABLED							= new BooleanPath(true);

	@Comment("Enable bank exp")
	public static final BooleanPath					BANK_EXP_ENABLED							= new BooleanPath(true);
	@Comment("Set bank mode to exp only")
	public static final BooleanPath					BANK_EXP_ONLY								= new BooleanPath(false);
	@Comment("Enabled default exp")
	public static final BooleanPath					BANK_EXP_DEFAULT_ENABLED					= new BooleanPath(false);
	@Comment("Default amount of exp to give new banks")
	public static final DoublePath					BANK_EXP_DEFAULT_AMOUNT						= new DoublePath(0);
	@Comment("Format exp in messages")
	public static final BooleanPath					BANK_EXP_FORMAT_ENABLED						= new BooleanPath(true);
	@Comment("Format exp in thousands eg 300K")
	public static final BooleanPath					BANK_EXP_FORMAT_THOUSANDS					= new BooleanPath(false);
	@Comment("Format exp in display")
	public static final StringPath					BANK_EXP_FORMAT_NORMAL						= new StringPath("%,.2f");
	@Comment("Format exp rounded in display")
	public static final StringPath					BANK_EXP_FORMAT_ROUND						= new StringPath("%,.0f");
	@Comment("Set max exp in bank")
	public static final DoubleNicePath				BANK_EXP_MAX								= new DoubleNicePath(Double.MAX_VALUE);
	
	@Comment("Premium version")
	public static EmptyPath							BANK_EXP_INTEREST							= new EmptyPath();

	@Comment("Storage of taxes taken from players")
	public static final BankSynchronizedDoubleNicePath BANK_EXP_TAX_STORAGE						= new BankSynchronizedDoubleNicePath(0);
	@Comment("Percentage to tax players when depositing, 0.01 = 1%")
	public static final DoublePath					BANK_EXP_DEPOSIT_TAX_PERCENT				= new DoublePath(0);
	@Comment("Percentage to tax players when withdrawing, 0.01 = 1%")
	public static final DoublePath					BANK_EXP_WITHDRAW_TAX_PERCENT				= new DoublePath(0);
	@Comment("Enable bank exp interest")
	public static final BooleanPath					BANK_EXP_INTEREST_ENABLED					= new BooleanPath(false);
	@Comment("If running mysql set only 1 server as main")
	public static final BooleanPath					BANK_EXP_INTEREST_MAIN						= new BooleanPath(false);
	@Comment("Enable message on exp interest gained")
	public static final BooleanPath					BANK_EXP_INTEREST_MESSAGE_ENABLED			= new BooleanPath(false);
	@Comment("Set the default amount as a % to gain, 0.01 is 1%, check permissions.yml for more")
	public static final DoublePath					BANK_EXP_INTEREST_ONLINE_PERCENT_GAINED		= new DoublePath(0.01);
	@Comment("Set the default amount as a % to gain, 0.001 is 0.1%, check permissions.yml for more")
	public static final DoublePath					BANK_EXP_INTEREST_OFFLINE_PERCENT_GAINED	= new DoublePath(0.001);
	@Comment("Set the max amount of exp payout online")
	public static final DoubleNicePath				BANK_EXP_INTEREST_ONLINE_MAX				= new DoubleNicePath(Double.MAX_VALUE);
	@Comment("Set the max amount of exp gained offline")
	public static final DoubleNicePath				BANK_EXP_INTEREST_OFFLINE_MAX				= new DoubleNicePath(Double.MAX_VALUE);
	@Comment("Set the max amount of exp payout online")
	public static final DoubleNicePath				BANK_EXP_INTEREST_OFFLINE_PAYOUT_MAX		= new DoubleNicePath(Double.MAX_VALUE);
	@Comment("Set the account minimum to give interest")
	public static final DoubleNicePath				BANK_EXP_INTEREST_ONLINE_MINIMUM			= new DoubleNicePath(0);
	@Comment("Set the account minimum to give interest")
	public static final DoubleNicePath				BANK_EXP_INTEREST_OFFLINE_MINIMUM			= new DoubleNicePath(0);
	@Comment("Set the time in seconds to give interest, default (1800) 30 mins")
	public static final IntegerPath					BANK_EXP_INTEREST_TIMER_TIME				= new IntegerPath(1800);
	@CommentArray({ "Set the amount of time till the next interest given", "Once set this cannot be modified in the config whilst the server is running" })
	public static final IntegerPath					BANK_EXP_INTEREST_TIMER_NEXT				= new IntegerPath(1800);
	@CommentArray("Enable sync payouts for multiple servers")
	public static final BooleanPath					BANK_EXP_INTEREST_TIMER_SYNC_ENABLED		= new BooleanPath(false);
	@CommentArray({"Set sync seed for servers", "Both servers require the same seed number for this to work!", "You can use a website like https://currentmillis.com/ to seed this number if you want payouts at certain times."})
	public static final LongPath					BANK_EXP_INTEREST_TIMER_SYNC_SEED			= new LongPath(0);

	@Comment("Enable bank money")
	public static final BooleanPath					BANK_MONEY_ENABLED							= new BooleanPath(true);
	@Comment("Format money in messages")
	public static final BooleanPath					BANK_MONEY_FORMAT_ENABLED					= new BooleanPath(true);
	@Comment("Format money in thousands eg 300K")
	public static final BooleanPath					BANK_MONEY_FORMAT_THOUSANDS					= new BooleanPath(false);
	@Comment("Format money in display")
	public static final StringPath					BANK_MONEY_FORMAT_NORMAL					= new StringPath("%,.2f");
	@Comment("Format money rounded in display")
	public static final StringPath					BANK_MONEY_FORMAT_ROUND						= new StringPath("%,.0f");
	@Comment("Show only full dollars")
	public static final BooleanPath					BANK_MONEY_FULL_DOLLARS						= new BooleanPath(false);
	@Comment("Only able to deposit full $ (no decimal)")
	public static final BooleanPath 				BANK_MONEY_DEPOSIT_FULL 					= new BooleanPath(false);
	@Comment("Only able to withdraw full $ (no decimal)")
	public static final BooleanPath 				BANK_MONEY_WITHDRAW_FULL 					= new BooleanPath(false);
	@Comment("Enabled default money")
	public static final BooleanPath					BANK_MONEY_DEFAULT_ENABLED					= new BooleanPath(false);
	@Comment("Default amount of money to give new banks")
	public static final DoublePath					BANK_MONEY_DEFAULT_AMOUNT					= new DoublePath(0);
	@Comment("Storage of taxes taken from players")
	public static final BankSynchronizedDoubleNicePath BANK_MONEY_TAX_STORAGE					= new BankSynchronizedDoubleNicePath(0);
	@Comment("Percentage to tax players when depositing, 0.01 = 1%")
	public static final DoublePath					BANK_MONEY_DEPOSIT_TAX_PERCENT				= new DoublePath(0);
	@Comment("Percentage to tax players when withdrawing, 0.01 = 1%")
	public static final DoublePath					BANK_MONEY_WITHDRAW_TAX_PERCENT				= new DoublePath(0);
	@Comment("Set bank mode to money only")
	public static final BooleanPath					BANK_MONEY_ONLY								= new BooleanPath(false);
	@Comment("Set max money in bank")
	public static final DoubleNicePath				BANK_MONEY_MAX								= new DoubleNicePath(Double.MAX_VALUE);
	@Comment("Premium version")
	public static EmptyPath							BANK_MONEY_INTEREST							= new EmptyPath();
	@Comment("Enable bank money interest")
	public static final BooleanPath					BANK_MONEY_INTEREST_ENABLED					= new BooleanPath(false);
	@Comment("If running mysql set only 1 server as main")
	public static final BooleanPath					BANK_MONEY_INTEREST_MAIN					= new BooleanPath(false);
	@Comment("Enable message on money interest gained")
	public static final BooleanPath					BANK_MONEY_INTEREST_MESSAGE_ENABLED			= new BooleanPath(false);
	@Comment("Set the default amount as a % to gain, 0.01 is 1%, check permissions.yml for more")
	public static final DoublePath					BANK_MONEY_INTEREST_ONLINE_PERCENT_GAINED	= new DoublePath(0.01);
	@Comment("Set the default amount as a % to gain, 0.001 is 0.1%, check permissions.yml for more")
	public static final DoublePath					BANK_MONEY_INTEREST_OFFLINE_PERCENT_GAINED	= new DoublePath(0.001);
	@Comment("Set the max amount of money payout offline")
	public static final DoubleNicePath				BANK_MONEY_INTEREST_ONLINE_MAX				= new DoubleNicePath(Double.MAX_VALUE);
	@Comment("Set the max amount of money gained offline")
	public static final DoubleNicePath				BANK_MONEY_INTEREST_OFFLINE_MAX				= new DoubleNicePath(Double.MAX_VALUE);
	@Comment("Set the max amount of money payout offline")
	public static final DoubleNicePath				BANK_MONEY_INTEREST_OFFLINE_PAYOUT_MAX		= new DoubleNicePath(Double.MAX_VALUE);
	@Comment("Set the account minimum to give interest")
	public static final DoubleNicePath				BANK_MONEY_INTEREST_ONLINE_MINIMUM			= new DoubleNicePath(0);
	@Comment("Set the account minimum to give interest")
	public static final DoubleNicePath				BANK_MONEY_INTEREST_OFFLINE_MINIMUM			= new DoubleNicePath(0);
	@Comment("Set the time in seconds to give interest, default (1800) 30 mins")
	public static final IntegerPath					BANK_MONEY_INTEREST_TIMER_TIME				= new IntegerPath(1800);
	@CommentArray({ "Set the amount of time till the next interest given", "Once set this cannot be modified in the config whilst the server is running" })
	public static final IntegerPath					BANK_MONEY_INTEREST_TIMER_NEXT				= new IntegerPath(1800);
	@CommentArray("Enable sync payouts for multiple servers")
	public static final BooleanPath					BANK_MONEY_INTEREST_TIMER_SYNC_ENABLED		= new BooleanPath(false);
	@CommentArray({"Set sync seed for servers", "Both servers require the same seed number for this to work!", "You can use a website like https://currentmillis.com/ to seed this number if you want payouts at certain times."})
	public static final LongPath					BANK_MONEY_INTEREST_TIMER_SYNC_SEED			= new LongPath(0);

	@Comment("Enable Citizens / Use /trait add bank-trait")
	public static final BooleanPath					BANK_TYPE_CITIZENS_ENABLED					= new BooleanPath(false);
	@Comment("Enable blocks /bank admin add block")
	public static final BooleanPath					BANK_TYPE_BLOCK_ENABLED						= new BooleanPath(true);
	@CommentArray({"Subsets:", "ALL, MENU, ITEMS, MONEY, EXP, LOANS"})
	public static final EmptyPath 					BANK_OPENTYPE_SUBSET					= new EmptyPath();
	@Comment("Enable advanced open types overall")
	public static final BooleanPath 				BANK_OPENTYPE_SUBSET_ENABLED = new BooleanPath(false);
	@Comment("Enable advanced open types on citizens, /bank admin npc setopentypes [types...]")
	public static final BooleanPath 				BANK_OPENTYPE_SUBSET_CITIZENS_ENABLED = new BooleanPath(false);
	@Comment("Enable advanced open types on command, enables /bank money open, /bank exp open, or /bank open <player> [types...] from console")
	public static final BooleanPath 				BANK_OPENTYPE_SUBSET_COMMAND_ENABLED = new BooleanPath(false);
//	@Comment("Enable subset open types on blocks")
//	public static final BooleanPath 				BANK_OPENTYPE_SUBSET_BLOCK_ENABLED = new BooleanPath(false);
	@Comment("Enable subset open types on signs, Add [opentype] to the second line of the sign, i.e ITEMS, if wanting main menu put it first, i.e MENU,ITEMS")
	public static final BooleanPath 				BANK_OPENTYPE_SUBSET_SIGN_ENABLED = new BooleanPath(false);


	@Comment("Enabled block locations")
	public static final StringListPath				BANK_TYPE_BLOCK_LOCATIONS					= new StringListPath();
	@Comment("Enable Signs")
	public static final BooleanPath					BANK_TYPE_SIGN_ENABLED						= new BooleanPath(true);
	@Comment("Sign Text")
	public static final TranslatedStringPath		BANK_TYPE_SIGN_TEXT							= new TranslatedStringPath(ChatColor.DARK_GREEN + "[Bank]");

	@Comment("Disable worlds")
	public static final StringListPath				BANK_DISABLE_WORLDS							= new StringListPath();
	@Comment("Disable gamemodes (CREATIVE, SURVIVAL, ADVENTURE, SPECTATOR)")
	public static final StringListPath				BANK_DISABLE_GAMEMODES						= new StringListPath();

	@Comment("Enable skript implementation")
	public static final BooleanPath					BANK_IMPLEMENTATION_SKRIPT_ENABLED			= new BooleanPath(false);
	@Comment("Enable bank as the vault override")
	public static final BooleanPath					BANK_IMPLEMENTATION_VAULT_OVERRIDE			= new BooleanPath(false);
	@Comment("Enable bank commands for vault eco (/bank eco)")
	public static final BooleanPath					BANK_IMPLEMENTATION_VAULT_COMMANDS			= new BooleanPath(false);

	@Comment("Prefix for placeholders")
	public static final StringPath					BANK_IMPLEMENTATION_PLACEHOLDER_PREFIX		= new StringPath("bank");
	@Comment("Enable MVDWPlaceholderAPI")
	public static final BooleanPath					BANK_IMPLEMENTATION_PLACEHOLDER_MVDW		= new BooleanPath(false);
	@Comment("Enable PlaceholderAPI")
	public static final BooleanPath					BANK_IMPLEMENTATION_PLACEHOLDER_API			= new BooleanPath(false);
	@Comment("Premium version")
	public static EmptyPath							BANK_EXPIRE									= new EmptyPath();
	@Comment("Enable the bank expire manager")
	public static final BooleanPath					BANK_EXPIRE_ENABLED							= new BooleanPath(false);
	@Comment("Time in days before a players data expires")
	public static final IntegerPath					BANK_EXPIRE_DAYS							= new IntegerPath(30);

	@CommentArray({ "Enable converters from other plugins here", "Keep the other plugins folder!", "And delete the other plugin jar" })
	@SuppressWarnings("unused")
	private static final Path<?>					BANK_CONVERTER								= new EmptyPath();
	@Comment("Enable bankcraft converter (make sure to backup bankcraft data)")
	public static final BooleanPath					BANK_CONVERTER_BANKCRAFT_ENABLED			= new BooleanPath(false);
	@Comment("Enable chestbank converter (make sure to backup chestbank data)")
	public static final BooleanPath					BANK_CONVERTER_CHESTBANK_ENABLED			= new BooleanPath(false);
	@Comment("Enable economybank converter (make sure to backup economybank data)")
	public static final BooleanPath					BANK_CONVERTER_ECONOMYBANK_ENABLED			= new BooleanPath(false);
	@Comment("Enable timeismoney converter (make sure to backup timeismoney data)")
	public static final BooleanPath					BANK_CONVERTER_TIMEISMONEY_ENABLED			= new BooleanPath(false);
	@Comment("Enable bankplus converter (make sure to backup bankplus data)")
	public static final BooleanPath					BANK_CONVERTER_BANKPLUS_ENABLED				= new BooleanPath(false);

	@Comment("Force utf8mb4 to deal with unicode")
	public static final BooleanPath					BANK_MYSQL_UTF8MB4_FORCED					= new BooleanPath(true);

	@Comment("Enable sounds for the plugin")
	public static final BooleanPath					BANK_SOUNDS_ENABLED							= new BooleanPath(false);
	@Comment("Default volume modifier for all sounds")
	public static final DoubleNicePath				BANK_SOUNDS_VOLUME_MODIFIER					= new DoubleNicePath(1);

	@Comment("Premium version")
	public static EmptyPath							BANK_LOANS									= new EmptyPath();

	@Comment("Enable bank loans")
	public static final BooleanPath					BANK_LOANS_ENABLED							= new BooleanPath(false);

	@Comment("Enable multiple bank loans")
	public static final BooleanPath					BANK_LOANS_MULTIPLE_ENABLED					= new BooleanPath(false);
	@Comment("Global max multiple loans")
	public static final IntegerPath					BANK_LOANS_MULTIPLE_MAX						= new IntegerPath(5);

	@Comment("Base cost to take out a loan (set to 5, loan of $100 = $105 to pay back)")
	public static final DoublePath					BANK_LOANS_COST_BASE						= new DoublePath(0);
	@Comment("Base percent to take out a loan (set to 0.1, loan of $100 = $110 to pay back)")
	public static final DoublePath					BANK_LOANS_COST_PERCENT						= new DoublePath(0);

	@CommentArray({ "Default amount of loan given to players", "See permissions.yml to set amount per player" })
	public static final DoublePath					BANK_LOANS_AMOUNT_DEFAULT					= new DoublePath(100);
	@Comment("Minimum amount to take out loan")
	public static final DoublePath					BANK_LOANS_AMOUNT_MINIMUM					= new DoublePath(0);
	@Comment("Global maximum amount to take out loan")
	public static final DoubleNicePath				BANK_LOANS_AMOUNT_MAXIMUM					= new DoubleNicePath(Double.MAX_VALUE);
	@Comment("Combine permissions to calculate max loan (permissions.yml)")
	public static final BooleanPath					BANK_LOANS_AMOUNT_PERMISSION_COMBINE		= new BooleanPath(false);

	@Comment("Enable loan deadlines")
	public static final BooleanPath					BANK_LOANS_DEADLINE_ENABLED					= new BooleanPath(false);
	@Comment("Remove loan from player when reached")
	public static final BooleanPath					BANK_LOANS_DEADLINE_REMOVE					= new BooleanPath(false);
	@Comment("The amount of days to payback loan")
	public static final IntegerPath					BANK_LOANS_DEADLINE_DAYS					= new IntegerPath(30);
	@Comment("Time in minutes to check deadlines")
	public static final IntegerPath					BANK_LOANS_DEADLINE_CHECK					= new IntegerPath(5);
	@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
	@Comment("Commands to run when deadline reach (supports chat colors) (variables: <uuid>, <name>, <amount>)")
	public static final StringListPath				BANK_LOANS_DEADLINE_COMMANDS				= new StringListPath(Arrays.asList("tempban <uuid> 10m Failed to pay $<amount> loan"));

	@Comment("Enable loan automatic payback")
	public static final BooleanPath					BANK_LOANS_PAYBACK_ENABLED					= new BooleanPath(false);
	@Comment("Sync timer with interest")
	public static final BooleanPath					BANK_LOANS_PAYBACK_TIMER_SYNC_INTEREST		= new BooleanPath(false);
	@Comment("Time in seconds to check payback")
	public static final IntegerPath					BANK_LOANS_PAYBACK_TIMER_TIME				= new IntegerPath(1800);
	@Comment("Not modifiable unless the server is stopped")
	public static final IntegerPath					BANK_LOANS_PAYBACK_TIMER_NEXT				= new IntegerPath(0);
	@Comment("Set to override the timer next")
	public static final IntegerPath					BANK_LOANS_PAYBACK_TIMER_OVERRIDE			= new IntegerPath(0);
	@Comment("Minimum amount of payback")
	public static final DoublePath					BANK_LOANS_PAYBACK_MINIMUM					= new DoublePath(0);
	@Comment("Rate of payback (0.003 for $100 loan is $0.30 per timer)")
	public static final DoublePath					BANK_LOANS_PAYBACK_RATE						= new DoublePath(0.003);
	@Comment("Allow custom payback rate")
	public static final BooleanPath					BANK_LOANS_PAYBACK_CUSTOM_ENABLED			= new BooleanPath(false);
	@Comment("Set the minimum amount for loan custom payback")
	public static final DoublePath					BANK_LOANS_PAYBACK_CUSTOM_MINIMUM			= new DoublePath(0.0);
	@Comment("Enable payback message")
	public static final BooleanPath					BANK_LOANS_PAYBACK_MESSAGE_ENABLED			= new BooleanPath(false);
	@Comment("Enable commands on failed payback")
	public static final BooleanPath					BANK_LOANS_PAYBACK_FAILED_ENABLED			= new BooleanPath(false);
	@CommentArray({ "Commands to run on failed paybacks", "Gets lowest value from set, eg if 1, 5, 10 are set and failed payback is at 6, it will select commands at 5", "Available: <name>, <uuid>, <amount>" })
	public static final BankLoansPaybackFailedPath	BANK_LOANS_PAYBACK_FAILED_COMMANDS			= new BankLoansPaybackFailedPath(new BankLoansPaybackFailedPath.LoansPaybackFailed("jail <name> 10m Failed to pay loan"));

	@Comment("Enable loan interest")
	public static final BooleanPath					BANK_LOANS_INTEREST_ENABLED					= new BooleanPath(false);
	@Comment("Set 1 server to be main offline interest handler")
	public static final BooleanPath					BANK_LOANS_INTEREST_MAIN					= new BooleanPath(false);
	@Comment("Enable offline loan interest")
	public static final BooleanPath					BANK_LOANS_INTEREST_OFFLINE_ENABLED			= new BooleanPath(false);
	@Comment("Enable loan interest message")
	public static final BooleanPath					BANK_LOANS_INTEREST_MESSAGE_ENABLED			= new BooleanPath(false);
	@Comment("Time in seconds to add interest")
	public static final IntegerPath					BANK_LOANS_INTEREST_TIMER_TIME				= new IntegerPath(1800);
	@Comment("Not modifiable unless the server is stopped")
	public static final IntegerPath					BANK_LOANS_INTEREST_TIMER_NEXT				= new IntegerPath(0);
	@Comment("Set to override the timer next")
	public static final IntegerPath					BANK_LOANS_INTEREST_TIMER_OVERRIDE			= new IntegerPath(0);

	@Comment("Enable items lost on death")
	public static final BooleanPath					BANK_DEATH_ITEMS_REMOVE						= new BooleanPath(false);

	@Comment("Enable money lost on death")
	public static final BooleanPath					BANK_DEATH_MONEY_REMOVE						= new BooleanPath(false);

	@Comment("Enable exp lost on death")
	public static final BooleanPath					BANK_DEATH_EXP_REMOVE						= new BooleanPath(false);

	@Comment("Set the log levels. Values: LOWEST, LOW, MEDIUM, HIGH, HIGHEST")
	public static final EnumPath<BankLogLevel>		BANK_LOG_LEVEL								= new EnumPath<>(BankLogLevel.class, BankLogLevel.LOWEST);
	public static final EnumPath<BankLogLevel>		BANK_LOG_PLUGIN_LEVEL						= new EnumPath<>(BankLogLevel.class, BankLogLevel.HIGH);
	public static final EnumPath<BankLogLevel>		BANK_LOG_PLAYER_LEVEL						= new EnumPath<>(BankLogLevel.class, BankLogLevel.HIGH);
	public static final EnumPath<BankLogLevel>		BANK_LOG_EXPIRE_LEVEL						= new EnumPath<>(BankLogLevel.class, BankLogLevel.LOW);
	public static final EnumPath<BankLogLevel>		BANK_LOG_HISCORE_LEVEL						= new EnumPath<>(BankLogLevel.class, BankLogLevel.LOW);
	public static final EnumPath<BankLogLevel>		BANK_LOG_INTEREST_LEVEL						= new EnumPath<>(BankLogLevel.class, BankLogLevel.LOW);
	public static final EnumPath<BankLogLevel>		BANK_LOG_LOAN_LEVEL							= new EnumPath<>(BankLogLevel.class, BankLogLevel.LOW);

	@Comment("Enable '/baltop' (Premium, command_baltop.yml)")
	public static final BooleanPath BANK_BAL_TOP_ENABLED = new BooleanPath(false);
	@Comment("Time in seconds to update baltop")
	public static final IntegerPath BANK_BAL_TOP_UPDATE = new IntegerPath(120);

	private BankPluginConfiguration(JavaPlugin plugin){
		super(plugin, "config.yml");
	}


}
