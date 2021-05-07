package me.dablakbandit.bank.config;

import org.bukkit.plugin.Plugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.inventory.InventoryConfiguration;
import me.dablakbandit.core.config.inventory.annotation.InventoryBase;
import me.dablakbandit.core.config.path.EmptyPath;
import me.dablakbandit.core.config.path.InventoryDescriptionPath;
import me.dablakbandit.core.config.path.Path;

public class BankInventoryConfiguration extends CommentAdvancedConfiguration{
	
	private static BankInventoryConfiguration config = new BankInventoryConfiguration(BankPlugin.getInstance(), "inventories.yml");
	
	public static BankInventoryConfiguration getInstance(){
		return config;
	}
	
	private BankInventoryConfiguration(Plugin plugin, String filename){
		super(plugin, filename, InventoryConfiguration::new);
	}
	
	@InventoryBase
	private static Path						BANK							= new EmptyPath();
	
	public static InventoryDescriptionPath	BANK_ITEMS						= new InventoryDescriptionPath(54, "Bank Items", "bank.open.items");
	public static InventoryDescriptionPath	BANK_ITEMS_ADD					= new InventoryDescriptionPath(9, "Bank Items Add", "bank.items.add");
	public static InventoryDescriptionPath	BANK_ITEMS_REMOVE				= new InventoryDescriptionPath(9, "Bank Items Remove", "bank.items.remove");
	@Comment("Premium version")
	public static InventoryDescriptionPath	BANK_ITEMS_SORT					= new InventoryDescriptionPath(9, "Bank Items Sort", "bank.items.sort");
	public static InventoryDescriptionPath	BANK_ITEMS_TRASHCAN				= new InventoryDescriptionPath(9, "Bank Items Trashcan", "bank.items.trashcan");
	public static InventoryDescriptionPath	BANK_ITEMS_BUY_TABS				= new InventoryDescriptionPath(9, "Bank Items Buy Tabs", "bank.items.tabs.buy");
	public static InventoryDescriptionPath	BANK_ITEMS_BUY_SLOTS			= new InventoryDescriptionPath(9, "Bank Items Buy Slot", "bank.items.tabs.slot");
	public static InventoryDescriptionPath	BANK_MAIN_MENU					= new InventoryDescriptionPath(9, "Bank Main Menu");
	public static InventoryDescriptionPath	BANK_EXP						= new InventoryDescriptionPath(9, "Bank Exp", "bank.open.exp");
	public static InventoryDescriptionPath	BANK_MONEY						= new InventoryDescriptionPath(9, "Bank Money", "bank.open.money");
	public static InventoryDescriptionPath	BANK_PIN_MENU					= new InventoryDescriptionPath(9, "Bank Pin", "bank.open.pin");
	public static InventoryDescriptionPath	BANK_PIN_SET					= new InventoryDescriptionPath(54, "Bank Pin Set", "bank.pin.set");
	public static InventoryDescriptionPath	BANK_PIN_ENTER					= new InventoryDescriptionPath(54, "Bank Pin Enter");
	
	@Comment("Premium version")
	public static InventoryDescriptionPath	BANK_LOANS						= new InventoryDescriptionPath(9, "Bank Loans", "bank.open.loans");
	public static InventoryDescriptionPath	BANK_LOAN_VIEW					= new InventoryDescriptionPath(9, "Bank View Loan", "bank.open.loans.view");
	public static InventoryDescriptionPath	BANK_LOAN_TAKE_OUT				= new InventoryDescriptionPath(9, "Bank Take Out Loan", "bank.open.loans.takeout");
	
	public static InventoryDescriptionPath	BANK_ADMIN_PERMISSION_HISTORY	= new InventoryDescriptionPath(54, "Bank Admin Permission History", "bank.admin.permission.history");
	public static InventoryDescriptionPath	BANK_ADMIN_BLACKLIST			= new InventoryDescriptionPath(54, "Bank Admin Blacklist", "bank.admin.blacklist.open");
	public static InventoryDescriptionPath	BANK_ADMIN_BLACKLIST_ITEM		= new InventoryDescriptionPath(9, "Bank Admin Blacklist Item Editor", "bank.admin.blacklist.edit");
	
}
