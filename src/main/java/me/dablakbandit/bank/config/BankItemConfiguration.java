package me.dablakbandit.bank.config;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.path.BankExtendedPath;
import me.dablakbandit.bank.config.path.BankItemBase;
import me.dablakbandit.bank.config.path.BankItemDelete;
import me.dablakbandit.bank.config.path.PathExtended;
import me.dablakbandit.bank.config.path.impl.BankEmptyPath;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.CommentConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.comment.annotation.Delete;
import me.dablakbandit.core.config.path.EmptyPath;
import me.dablakbandit.core.config.path.Path;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BankItemConfiguration extends CommentAdvancedConfiguration{

	private static BankItemConfiguration config;


	public static void load(BankPlugin plugin) {
		config = new BankItemConfiguration(plugin);
		config.load();
	}
	
	public static BankItemConfiguration getInstance(){
		return config;
	}
	
	private BankItemConfiguration(Plugin plugin){
		super(plugin, "conf/items.yml");
		CommentConfiguration.addCommentSupplier(BankItemBase.class, BankItemBase::value);
		CommentConfiguration.addDeleteSupplier(BankItemDelete.class, BankItemDelete::value);
	}
	
	private static final Material	stained				= getStainedMaterial();
	
	private static final ItemStack	black_stained		= getStained("BLACK");
	private static final ItemStack	red_stained			= getStained("RED");
	private static final ItemStack	green_stained		= getStained("GREEN");
	private static final ItemStack	light_blue_stained	= getStained("LIGHT_BLUE");
	private static final ItemStack	orange_stained		= getStained("ORANGE");
	private static final ItemStack	cyan_stained		= getStained("CYAN");
	private static final ItemStack	yellow_stained		= getStained("YELLOW");
	
	private static Material getStainedMaterial(){
		Material mat = Material.getMaterial("STAINED_GLASS_PANE");
		if(mat != null){ return mat; }
		mat = Material.getMaterial("THIN_GLASS");
		return mat;
	}

	private static ItemStack getStained(String name){
		Material m = getMaterial(name + "_STAINED_GLASS_PANE");
		if(m != null){ return new ItemStack(m, 1); }
		return new ItemStack(stained, 1, (short)DyeColor.valueOf(name).ordinal());
	}

	public static Material getMaterial(String... possible) {
		for (String var : possible) {
			try {
				Material m = Material.valueOf(var);
				if (m != null) {
					return m;
				}
			} catch (Exception var7) {
			}
		}
		return null;
	}

	@SuppressWarnings({"rawtypes", "unused"})
	@BankItemBase
	private static final Path			BANK								= new EmptyPath();
	
	public static final BankItemPath	BANK_ITEM_BACK						= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");

	@CommentArray({"Available: <tab>, <items>", ".Tabs: Inventory slots to show tabs in. If tabs are allowed to exceed 9 the first and last slot will be used for scrolling."})
	@PathExtended(key = "Tabs", value = "45,46,47,48,49,50,51,52,53", classType = Integer[].class)
	public static final BankItemPath	BANK_ITEM_TAB_NUMBER				= new BankItemPath(light_blue_stained, ChatColor.AQUA + "Tab <tab>", ChatColor.BLUE + "<items> Items");

	@Comment("Slot is handled by BANK.ITEM.TAB.NUMBER")
	public static final BankItemPath BANK_ITEM_TAB_LEFT = new BankItemPath(-1, cyan_stained, ChatColor.AQUA + "Tab <tab>", ChatColor.BLUE + "<items> Items");
	@Comment("Slot is handled by BANK.ITEM.TAB.NUMBER")
	public static final BankItemPath BANK_ITEM_TAB_RIGHT = new BankItemPath(-1, cyan_stained, ChatColor.AQUA + "Tab <tab>", ChatColor.BLUE + "<items> Items");
	@Comment("Available: <tab>")
	public static final BankItemPath	BANK_ITEM_TAB_LOCKED				= new BankItemPath(red_stained, ChatColor.AQUA + "Tab <tab>", ChatColor.RED + "Locked");
	@Comment("Available: <tab>, <items>")
	public static final BankItemPath	BANK_ITEM_TAB_CURRENT				= new BankItemPath(green_stained, ChatColor.GREEN + "Current <tab>", ChatColor.BLUE + "<items> Items");
	public static final BankItemPath	BANK_ITEM_BLANK						= new BankItemPath(black_stained, " ");
	public static final BankItemPath	BANK_ITEM_SCROLL_UP					= new BankItemPath(7, black_stained, ChatColor.AQUA + "Scroll Up", ChatColor.GREEN + "Scrolls up the tab");
	public static final BankItemPath	BANK_ITEM_SCROLL_DOWN				=
		new BankItemPath(8, black_stained, ChatColor.AQUA + "Scroll Down", ChatColor.GREEN + "Scrolls down the tab");
	
	public static final BankItemPath	BANK_ITEM_ADD						=
		new BankItemPath(4, black_stained, ChatColor.AQUA + "Add to this tab", ChatColor.GREEN + "Add item options");
	public static final BankItemPath	BANK_ITEM_REMOVE					=
		new BankItemPath(5, black_stained, ChatColor.AQUA + "Remove from this tab", ChatColor.GREEN + "Remove item options");
	public static final BankItemPath	BANK_ITEM_SORT						=
		new BankItemPath(6, black_stained, ChatColor.AQUA + "Sort this tab", ChatColor.GREEN + "Sort item options");
	
	@Comment("Available: <used>, <available>, <total>")
	public static final BankItemPath	BANK_ITEM_SLOTS						=
		new BankItemPath(	1, black_stained, ChatColor.AQUA + "Slots", ChatColor.GREEN + "Used Slots: <used>", ChatColor.GREEN + "Available Slots: <available>",
							ChatColor.GREEN + "Total Slots: <total>", ChatColor.GREEN + "Click to buy more slots");
	@Comment("Available: <tabs>")
	public static final BankItemPath	BANK_ITEM_TABS						=
		new BankItemPath(2, black_stained, ChatColor.AQUA + "Tabs", ChatColor.GREEN + "Available Tabs: <tabs>", ChatColor.GREEN + "Click to buy more tabs");
	public static final BankItemPath	BANK_ITEM_TRASHCAN					= new BankItemPath(3, black_stained, ChatColor.RED + "Trashcan", ChatColor.RED + "Items are destroyed");
	@CommentArray({".Start: Start slot for setting items", ".Width: Amount of columns to show", ".Rows: Amount of rows to show"})
	@PathExtended(key = "Start", value = "9", classType = Integer.class)
	@PathExtended(key = "Width", value = "9", classType = Integer.class)
	@PathExtended(key = "Rows", value = "4", classType = Integer.class)
	public static final BankEmptyPath	BANK_ITEM_ITEMS						= new BankEmptyPath();

	@Comment("Available: <cost>")
	public static final BankItemPath	BANK_ITEM_LOCKED					= new BankItemPath(-1, red_stained, ChatColor.RED + "Slot locked", ChatColor.GREEN + "Purchase more slots to unlock.", ChatColor.GREEN + "Click to unlock for $<cost>");
	
	@CommentArray({".Slots: amount of checks to show", ".Start: Start slot for showing history"})
	@PathExtended(key = "Slots", value = "45", classType = Integer.class)
	@PathExtended(key = "Start", value = "9", classType = Integer.class)
	public static final BankEmptyPath	BANK_PERMISSION_HISTORY_LIST		= new BankEmptyPath();
	public static final BankItemPath	BANK_PERMISSION_HISTORY_TRUE		= new BankItemPath(green_stained, "");
	public static final BankItemPath	BANK_PERMISSION_HISTORY_FALSE		= new BankItemPath(red_stained, "");
	public static final BankItemPath	BANK_PERMISSION_HISTORY_SCROLL_UP	= new BankItemPath(7, black_stained, ChatColor.AQUA + "Scroll Up", ChatColor.GREEN + "Scrolls up the tab");
	public static final BankItemPath	BANK_PERMISSION_HISTORY_SCROLL_DOWN	= new BankItemPath(8, black_stained, ChatColor.AQUA + "Scroll Down", ChatColor.GREEN + "Scrolls down the tab");

	@CommentArray({".Slots: amount of checks to show", ".Start: Start slot for showing history"})
	@PathExtended(key = "Slots", value = "45", classType = Integer.class)
	@PathExtended(key = "Start", value = "9", classType = Integer.class)
	public static final BankEmptyPath	BANK_ITEM_BLACKLIST_ITEMS			= new BankEmptyPath();
	public static final BankItemPath	BANK_ITEM_BLACKLIST_BACK			= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_ITEM_BLACKLIST_GREEN			= new BankItemPath(green_stained, ChatColor.GREEN + "<name>", ChatColor.BLUE + "Click to toggle");
	public static final BankItemPath	BANK_ITEM_BLACKLIST_RED				= new BankItemPath(red_stained, ChatColor.RED + "<name>", ChatColor.BLUE + "Click to toggle");
	public static final BankItemPath	BANK_ITEM_BLACKLIST_SCROLL_UP		= new BankItemPath(7, black_stained, ChatColor.AQUA + "Scroll Up", ChatColor.GREEN + "Scrolls up the tab");
	public static final BankItemPath	BANK_ITEM_BLACKLIST_SCROLL_DOWN		= new BankItemPath(8, black_stained, ChatColor.AQUA + "Scroll Down", ChatColor.GREEN + "Scrolls down the tab");

	@CommentArray({".Slots: amount of checks to show", ".Start: Start slot for showing history"})
	@PathExtended(key = "Slots", value = "45", classType = Integer.class)
	@PathExtended(key = "Start", value = "9", classType = Integer.class)
	public static final BankEmptyPath	BANK_ITEM_DEFAULT_ITEMS			= new BankEmptyPath();
	public static final BankItemPath	BANK_ITEM_DEFAULT_BACK			= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_ITEM_DEFAULT_SCROLL_UP		= new BankItemPath(7, black_stained, ChatColor.AQUA + "Scroll Up", ChatColor.GREEN + "Scrolls up the tab");
	public static final BankItemPath	BANK_ITEM_DEFAULT_SCROLL_DOWN		= new BankItemPath(8, black_stained, ChatColor.AQUA + "Scroll Down", ChatColor.GREEN + "Scrolls down the tab");

	public static final BankItemPath	BANK_ADD_BACK						= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_ADD_ALL						=
		new BankItemPath(3, light_blue_stained, ChatColor.AQUA + "Add All", ChatColor.GREEN + "Adds all items in your whole inventory");
	public static final BankItemPath	BANK_ADD_INVENTORY					=
		new BankItemPath(4, light_blue_stained, ChatColor.AQUA + "Add Inventory", ChatColor.GREEN + "Adds all items in your inventory");
	public static final BankItemPath	BANK_ADD_HOTBAR						=
		new BankItemPath(5, light_blue_stained, ChatColor.AQUA + "Add Hotbar", ChatColor.GREEN + "Adds all items in your hotbar");

	public static final BankItemPath	BANK_REMOVE_BACK							= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_REMOVE_ALL						=
		new BankItemPath(3, light_blue_stained, ChatColor.AQUA + "Remove All", ChatColor.GREEN + "Removes all items to your whole inventory");
	public static final BankItemPath	BANK_REMOVE_INVENTORY				=
		new BankItemPath(4, light_blue_stained, ChatColor.AQUA + "Remove Inventory", ChatColor.GREEN + "Removes all items to your inventory");
	public static final BankItemPath	BANK_REMOVE_HOTBAR					=
		new BankItemPath(5, light_blue_stained, ChatColor.AQUA + "Remove Hotbar", ChatColor.GREEN + "Removes all items to your hotbar");
	
	public static final BankItemPath	BANK_SORT_MATERIAL					=
		new BankItemPath(3, light_blue_stained, ChatColor.AQUA + "Sort Alphabetically By Material", ChatColor.GREEN + "Sorts tab by the material of the items");
	public static final BankItemPath	BANK_SORT_NAME						=
		new BankItemPath(4, light_blue_stained, ChatColor.AQUA + "Sort Alphabetically By Name", ChatColor.GREEN + "Sorts tab by item names");
	public static final BankItemPath	BANK_SORT_AMOUNT					=
		new BankItemPath(5, light_blue_stained, ChatColor.AQUA + "Sort By Item Amount", ChatColor.GREEN + "Sorts tab by item amounts");
	
	public static final BankItemPath	BANK_MAIN_ITEM						= new BankItemPath(4, cyan_stained, ChatColor.AQUA + "Items", ChatColor.GREEN + "Click to view items");
	@Comment("Available: <money>")
	public static final BankItemPath	BANK_MAIN_MONEY						=
		new BankItemPath(3, green_stained, ChatColor.AQUA + "Balance $<money>", ChatColor.GREEN + "Click to withdraw/deposit");
	public static final BankItemPath	BANK_MAIN_PIN						=
		new BankItemPath(2, red_stained, ChatColor.AQUA + "Pin options", ChatColor.GREEN + "Click to set/edit your pin");
	@Comment("Available: <exp>")
	public static final BankItemPath	BANK_MAIN_EXP						=
		new BankItemPath(5, yellow_stained, ChatColor.AQUA + "<exp> exp", ChatColor.GREEN + "Your current amount of exp");
	public static final BankItemPath	BANK_MAIN_LOANS						=
		new BankItemPath(6, light_blue_stained, ChatColor.AQUA + "Loans", ChatColor.GREEN + "Click to take and view current loans");
	
	@Comment("Available: <exp>")
	public static final BankItemPath	BANK_EXP_BALANCE					=
		new BankItemPath(4, cyan_stained, ChatColor.AQUA + "<exp> exp", ChatColor.GREEN + "Your current amount of exp");

	public static final BankItemPath	BANK_EXP_BACK						= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_EXP_WITHDRAW					=
		new BankItemPath(3, red_stained, ChatColor.AQUA + "Click to withdraw exp", ChatColor.GREEN + "Enter the amount to withdraw");
	public static final BankItemPath	BANK_EXP_WITHDRAWALL				=
		new BankItemPath(2, red_stained, ChatColor.AQUA + "Click to withdraw all exp", ChatColor.GREEN + "Withdraws all exp");
	public static final BankItemPath	BANK_EXP_DEPOSIT					=
		new BankItemPath(5, green_stained, ChatColor.AQUA + "Click to deposit exp", ChatColor.GREEN + "Enter the amount to deposit");
	public static final BankItemPath	BANK_EXP_DEPOSITALL					=
		new BankItemPath(6, green_stained, ChatColor.AQUA + "Click to deposit all exp", ChatColor.GREEN + "Deposits all exp");
	public static final BankItemPath	BANK_EXP_SEND						=
		new BankItemPath(8, green_stained, ChatColor.AQUA + "Send exp", ChatColor.GREEN + "Send exp to another player.");
	
	@Comment("Available: <money>")
	public static final BankItemPath	BANK_MONEY_BALANCE					=
		new BankItemPath(4, cyan_stained, ChatColor.AQUA + "Balance: $<money>", ChatColor.GREEN + "Your current amount of $$");

	public static final BankItemPath	BANK_MONEY_BACK						= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_MONEY_WITHDRAW					=
		new BankItemPath(3, red_stained, ChatColor.AQUA + "Click to withdraw money", ChatColor.GREEN + "Enter the amount to withdraw");
	public static final BankItemPath	BANK_MONEY_WITHDRAWALL				=
		new BankItemPath(2, red_stained, ChatColor.AQUA + "Click to withdraw all money", ChatColor.GREEN + "Withdraws all money");
	public static final BankItemPath	BANK_MONEY_DEPOSIT					=
		new BankItemPath(5, green_stained, ChatColor.AQUA + "Click to deposit money", ChatColor.GREEN + "Enter the amount to deposit");
	public static final BankItemPath	BANK_MONEY_DEPOSITALL				=
		new BankItemPath(6, green_stained, ChatColor.AQUA + "Click to deposit all money", ChatColor.GREEN + "Deposits all money");
	public static final BankItemPath	BANK_MONEY_SEND						=
		new BankItemPath(8, green_stained, ChatColor.AQUA + "Send money", ChatColor.GREEN + "Send money to another player.");

	public static final BankItemPath	BANK_PIN_BACK						= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_PIN_SET						= new BankItemPath(4, green_stained, ChatColor.GREEN + "Set Pin", ChatColor.GREEN + "Click to set your pin");
	public static final BankItemPath	BANK_PIN_REMOVE						= new BankItemPath(5, red_stained, ChatColor.RED + "Remove Pin", ChatColor.RED + "Click to remove your pin");
	public static final BankItemPath	BANK_PIN_CLEAR						= new BankItemPath(39, yellow_stained, ChatColor.GREEN + "Clear Pin", ChatColor.GREEN + "Click to clear");
	@CommentArray({ "Each pin slot is randomized except for Zero", "Available: <number>" })
	@BankItemDelete
	public static final BankEmptyPath	BANK_PIN_NUMBER						= new BankEmptyPath();
	public static final BankItemPath	BANK_PIN_NUMBER_ONE					= new BankItemPath(12, green_stained, 1, ChatColor.AQUA + "1");
	public static final BankItemPath	BANK_PIN_NUMBER_TWO					= new BankItemPath(13, green_stained, 2, ChatColor.AQUA + "2");
	public static final BankItemPath	BANK_PIN_NUMBER_THREE				= new BankItemPath(14, green_stained, 3, ChatColor.AQUA + "3");
	public static final BankItemPath	BANK_PIN_NUMBER_FOUR				= new BankItemPath(21, green_stained, 4, ChatColor.AQUA + "4");
	public static final BankItemPath	BANK_PIN_NUMBER_FIVE				= new BankItemPath(22, green_stained, 5, ChatColor.AQUA + "5");
	public static final BankItemPath	BANK_PIN_NUMBER_SIX					= new BankItemPath(23, green_stained, 6, ChatColor.AQUA + "6");
	public static final BankItemPath	BANK_PIN_NUMBER_SEVEN				= new BankItemPath(30, green_stained, 7, ChatColor.AQUA + "7");
	public static final BankItemPath	BANK_PIN_NUMBER_EIGHT				= new BankItemPath(31, green_stained, 8, ChatColor.AQUA + "8");
	public static final BankItemPath	BANK_PIN_NUMBER_NINE				= new BankItemPath(32, green_stained, 9, ChatColor.AQUA + "9");
	public static final BankItemPath	BANK_PIN_NUMBER_ZERO				= new BankItemPath(40, light_blue_stained, 1, ChatColor.AQUA + "0");

	@CommentArray({ "Available: <length>" })
	@Delete("Unfinished")
	@BankItemDelete
	public static final BankEmptyPath	BANK_PIN_PROGRESS					= new BankEmptyPath();
	@Comment("Finished.Slot: Starting progress slot")
	public static final BankItemPath	BANK_PIN_PROGRESS_FINISHED			= new BankItemPath(-1, green_stained, ChatColor.AQUA + "<length>/4");
	public static final BankItemPath	BANK_PIN_PROGRESS_SLOT_1			= new BankItemPath(17, yellow_stained, ChatColor.AQUA + "<length>/4");
	public static final BankItemPath	BANK_PIN_PROGRESS_SLOT_2			= new BankItemPath(26, yellow_stained, ChatColor.AQUA + "<length>/4");
	public static final BankItemPath	BANK_PIN_PROGRESS_SLOT_3			= new BankItemPath(35, yellow_stained, ChatColor.AQUA + "<length>/4");
	public static final BankItemPath	BANK_PIN_PROGRESS_SLOT_4			= new BankItemPath(44, yellow_stained, ChatColor.AQUA + "<length>/4");

	public static final BankItemPath	BANK_ITEM_BUY_BACK					= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_ITEM_BUY_SLOT_MINUS			= new BankItemPath(3, red_stained, ChatColor.RED + "-");
	@Comment("Available: <slots>, <cost>")
	public static final BankItemPath	BANK_ITEM_BUY_SLOT_BUY				= new BankItemPath(4, light_blue_stained, ChatColor.AQUA + "Buy <slots> for $<cost>");
	public static final BankItemPath	BANK_ITEM_BUY_SLOT_ADD				= new BankItemPath(5, green_stained, ChatColor.GREEN + "+");

	public static final BankItemPath	BANK_ITEM_BUY_TAB_BACK				= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");
	public static final BankItemPath	BANK_ITEM_BUY_TAB_MINUS				= new BankItemPath(3, red_stained, ChatColor.RED + "-");
	@Comment("Available: <tabs>, <cost>")
	public static final BankItemPath	BANK_ITEM_BUY_TAB_BUY				= new BankItemPath(4, light_blue_stained, ChatColor.AQUA + "Buy <tabs> for $<cost>");
	public static final BankItemPath	BANK_ITEM_BUY_TAB_ADD				= new BankItemPath(5, green_stained, ChatColor.GREEN + "+");
	
	public static final BankItemPath	BANK_ANVIL_INPUT					= new BankItemPath(5, black_stained, "");
	@Comment("Premium version")
	public static final EmptyPath		BANK_LOANS							= new EmptyPath();
	
	public static final BankItemPath	BANK_LOANS_VIEW						= new BankItemPath(5, green_stained, ChatColor.AQUA + "View loans", ChatColor.GREEN + "Click to view loans");
	
	public static final BankItemPath	BANK_LOANS_TAKE_OUT					=
		new BankItemPath(4, green_stained, ChatColor.AQUA + "Take out loan", ChatColor.GREEN + "Click to take out a loan");
	@Comment("Available: <total>")
	public static final BankItemPath	BANK_LOANS_PAYBACK_ALL				=
		new BankItemPath(3, red_stained, ChatColor.AQUA + "Pay back all loans", ChatColor.GREEN + "Total: <total>", ChatColor.GREEN + "Click to pay back all current loans");
	
	@Comment("Available: <amount>, <original>, <interest>, <payback>, <deadline>, <created>")
	
	public static final BankItemPath	BANK_LOAN_INFO						=
		new BankItemPath(	4, cyan_stained, ChatColor.AQUA + "Loan: <amount>", ChatColor.GREEN + "Original: <original>", ChatColor.GREEN + "Interest: <interest>",
							ChatColor.GREEN + "Payback: <payback>", ChatColor.GREEN + "Deadline: <deadline> days", ChatColor.GREEN + "Created: <created>",
							ChatColor.BLUE + "Click to payback");
	public static final BankItemPath	BANK_LOAN_VIEW_PREVIOUS				=
		new BankItemPath(7, light_blue_stained, ChatColor.AQUA + "Previous loan", ChatColor.GREEN + "Click to view previous loan");
	public static final BankItemPath	BANK_LOAN_VIEW_NEXT					=
		new BankItemPath(8, light_blue_stained, ChatColor.AQUA + "Next loan", ChatColor.GREEN + "Click to view next loan");
	
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_PAYBACK			=
		new BankItemPath(3, light_blue_stained, ChatColor.AQUA + "Set payback", ChatColor.GREEN + "Click to set payback");
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_AMOUNT			=
		new BankItemPath(4, light_blue_stained, ChatColor.AQUA + "Set amount", ChatColor.GREEN + "Click to set amount");
	@Comment("Available: <amount>, <original>, <interest>, <payback>, <deadline>, <created>")
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_INFO				=
		new BankItemPath(	7, cyan_stained, ChatColor.AQUA + "Loan: <amount>", ChatColor.GREEN + "Original: <original>", ChatColor.GREEN + "Interest: <interest>",
							ChatColor.GREEN + "Payback: <payback>", ChatColor.GREEN + "Deadline: <deadline> days", ChatColor.GREEN + "Created: <created>");
	@Comment("All errors replace this accept button")
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_ACCEPT			=
		new BankItemPath(8, green_stained, ChatColor.AQUA + "Accept loan", ChatColor.GREEN + "Take out loan for $<amount>");
	@Comment("Below minimum in config")
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_ERROR_MINIMUM	=
		new BankItemPath(-1, red_stained, ChatColor.RED + "Loan below minimum", ChatColor.RED + "Minimum: <minimum>");
	@Comment("Above maximum in config")
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_ERROR_MAXIMUM	=
		new BankItemPath(-1, red_stained, ChatColor.RED + "Loan above maximum", ChatColor.RED + "Maximum: <maximum>");
	@Comment("Above max set via permissions")
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_ERROR_PERMITTED	=
		new BankItemPath(-1, red_stained, ChatColor.RED + "Loan above permitted", ChatColor.RED + "Permitted: <permitted>");
	@Comment("Above is less than 0")
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_ERROR_ZERO		=
		new BankItemPath(-1, red_stained, ChatColor.RED + "Loan set an amount", ChatColor.RED + "Set an amount to take out");
	@Comment("Taken out too loans")
	public static final BankItemPath	BANK_LOAN_TAKE_OUT_ERROR_TOO_MANY	=
		new BankItemPath(-1, red_stained, ChatColor.RED + "Max loans reached", ChatColor.RED + "You have already taken out the maximum amount of loans");
	public static final BankItemPath	BANK_TRASHCAN_BACK					= new BankItemPath(0, red_stained, ChatColor.RED + "Back", ChatColor.RED + "Go back");

	@SuppressWarnings("unchecked")
	protected boolean loadPath(Field field, Path path) {
		boolean save = super.loadPath(field, path);
		if(path instanceof BankExtendedPath){
			((BankExtendedPath) path).setFile(this.getConfig().getFile().getAbsolutePath());
			PathExtended[] extendeds = field.getAnnotationsByType(PathExtended.class);
			if (extendeds.length > 0) {
				CommentConfiguration config = BankItemConfiguration.config.getConfig();
				Map<String, Object> map = new HashMap<>();
				for (PathExtended extend : extendeds) {
					String pathKey = path.getActualPath() + "." + extend.key();
					try {
						Object value = null;
						if (Integer.class.equals(extend.classType())) {
							value = Integer.valueOf(extend.value());
							if (config.isSet(pathKey)) {
								value = config.getInt(pathKey);
							} else {
								config.set(pathKey, value);
								save = true;
							}
						} else if (Integer[].class.equals(extend.classType())) {
							value = Arrays.stream(extend.value().split(",")).map(Integer::valueOf).collect(Collectors.toList());
							if (config.isSet(pathKey)) {
								value = config.getIntegerList(pathKey);
							} else {
								config.set(pathKey, value);
								save = true;
							}
							value = ((List<Integer>) value).toArray(new Integer[0]);
						}
						if (value != null) {
							map.put(extend.key(), value);
						}
					}catch (Exception e){
						BankLog.errorAlert("No extended value found for key " + pathKey + " in path " + path.getActualPath() + " in file " + this.getConfig().getFile().getAbsolutePath());
						e.printStackTrace();
					}
				}
				if (!map.isEmpty()) {
					((BankExtendedPath) path).setExtendedValues(map);
				}
			}
		}
		if (path instanceof BankItemPath) {
			if (!((BankItemPath) path).isValidItem()) {
				BankLog.errorAlert("Invalid item for " + path.getActualPath() + " in " + this.getConfig().getFile().getAbsolutePath());
			}
		}
		return save;
	}

}
