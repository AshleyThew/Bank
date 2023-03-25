package me.dablakbandit.bank.config;

import me.dablakbandit.core.config.comment.annotation.CommentArray;
import org.bukkit.plugin.Plugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.inventory.InventoryConfiguration;
import me.dablakbandit.core.config.inventory.annotation.InventoryBase;
import me.dablakbandit.core.config.path.EmptyPath;
import me.dablakbandit.core.config.path.InventoryDescriptionPath;
import me.dablakbandit.core.config.path.Path;


public class BankInventoryConfiguration extends CommentAdvancedConfiguration{
	
	private static final BankInventoryConfiguration config = new BankInventoryConfiguration(BankPlugin.getInstance());
	
	public static BankInventoryConfiguration getInstance(){
		return config;
	}
	
	private BankInventoryConfiguration(Plugin plugin){
		super(plugin, "conf/inventories.yml", InventoryConfiguration::new);
	}

	@SuppressWarnings({"rawtypes", "unused"})
	@InventoryBase
	@CommentArray({"Available Title Placeholders:", "<player>", "<bank_money>", "<bank_exp>", "<bank_exp_level>", "<bank_slots>", "<bank_used_slots>"})
	private static final Path						BANK							= new EmptyPath();
	
	public static final InventoryDescriptionPath	BANK_ITEMS						= new InventoryDescriptionPath(54, "Bank Items", "bank.open.items");
	public static final InventoryDescriptionPath	BANK_ITEMS_ADD					= new InventoryDescriptionPath(9, "Bank Items Add", "bank.items.add");
	public static final InventoryDescriptionPath	BANK_ITEMS_REMOVE				= new InventoryDescriptionPath(9, "Bank Items Remove", "bank.items.remove");
	public static final InventoryDescriptionPath	BANK_ITEMS_SORT					= new InventoryDescriptionPath(9, "Bank Items Sort", "bank.items.sort");
	public static final InventoryDescriptionPath	BANK_ITEMS_TRASHCAN				= new InventoryDescriptionPath(9, "Bank Items Trashcan", "bank.items.trashcan");
	public static final InventoryDescriptionPath	BANK_ITEMS_BUY_TABS				= new InventoryDescriptionPath(9, "Bank Items Buy Tabs", "bank.items.tabs.buy");
	public static final InventoryDescriptionPath	BANK_ITEMS_BUY_SLOTS			= new InventoryDescriptionPath(9, "Bank Items Buy Slot", "bank.items.tabs.slot");
	public static final InventoryDescriptionPath	BANK_MAIN_MENU					= new InventoryDescriptionPath(9, "Bank Main Menu");
	public static final InventoryDescriptionPath	BANK_EXP						= new InventoryDescriptionPath(9, "Bank Exp", "bank.open.exp");
	public static final InventoryDescriptionPath	BANK_MONEY						= new InventoryDescriptionPath(9, "Bank Money", "bank.open.money");
	public static final InventoryDescriptionPath	BANK_PIN_MENU					= new InventoryDescriptionPath(9, "Bank Pin", "bank.open.pin");
	public static final InventoryDescriptionPath	BANK_PIN_SET					= new InventoryDescriptionPath(54, "Bank Pin Set", "bank.pin.set");
	public static final InventoryDescriptionPath	BANK_PIN_ENTER					= new InventoryDescriptionPath(54, "Bank Pin Enter");
	
	public static final InventoryDescriptionPath	BANK_LOANS						= new InventoryDescriptionPath(9, "Bank Loans", "bank.open.loans");
	public static final InventoryDescriptionPath	BANK_LOAN_VIEW					= new InventoryDescriptionPath(9, "Bank View Loan", "bank.open.loans.view");
	public static final InventoryDescriptionPath	BANK_LOAN_TAKE_OUT				= new InventoryDescriptionPath(9, "Bank Take Out Loan", "bank.open.loans.takeout");
	
	public static final InventoryDescriptionPath	BANK_ADMIN_PERMISSION_HISTORY	= new InventoryDescriptionPath(54, "Bank Admin Permission History", "bank.admin.permission.history");
	public static final InventoryDescriptionPath	BANK_ADMIN_BLACKLIST			= new InventoryDescriptionPath(54, "Bank Admin Blacklist", "bank.admin.blacklist.open");
	public static final InventoryDescriptionPath	BANK_ADMIN_BLACKLIST_ITEM		= new InventoryDescriptionPath(9, "Bank Admin Blacklist Item Editor", "bank.admin.blacklist.edit");
	public static final InventoryDescriptionPath	BANK_ADMIN_ITEM_DEFAULT			= new InventoryDescriptionPath(54, "Bank Admin Default Items", "bank.admin.item.default.open");

}
