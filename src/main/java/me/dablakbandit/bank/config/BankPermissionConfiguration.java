package me.dablakbandit.bank.config;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.path.impl.BankDoublePermissionPath;
import me.dablakbandit.bank.config.path.impl.BankIntegerPermissionPath;
import me.dablakbandit.bank.config.path.impl.BankPermissionPath;
import me.dablakbandit.bank.config.path.impl.BankPermissionStringListPath;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.*;
import org.bukkit.plugin.java.JavaPlugin;

public class BankPermissionConfiguration extends CommentAdvancedConfiguration{

	private static BankPermissionConfiguration configuration;


	public static void load(BankPlugin plugin) {
		configuration = new BankPermissionConfiguration(plugin);
		configuration.load();
	}
	
	public static BankPermissionConfiguration getInstance(){
		return configuration;
	}

	@SuppressWarnings({"rawtypes", "unused"})
	@CommentArray({ "Edit additional permissions", "Permission node parameters", "Required: Permission, Check", })
	private static final Path						PERMISSION						= new EmptyPath();
	
	public static final BooleanPath					PERMISSION_SEND_MESSAGE			= new BooleanPath(true);
	
	public static final PermissionPath				PERMISSION_OPEN_SIGN			= new BankPermissionPath("bank.open.sign", true);
	public static final PermissionPath				PERMISSION_OPEN_CITIZENS		= new BankPermissionPath("bank.open.citizens", true);
	public static final PermissionPath				PERMISSION_OPEN_BLOCK			= new BankPermissionPath("bank.open.block", true);
	
	public static final PermissionPath				PERMISSION_SIGN_PLACE			= new BankPermissionPath("bank.sign.place", true);
	
	@CommentArray({ "Give a player the permission: bank.loan.interest.0.05 for 5% interest",
			"Lowest value will be taken from the user bank.loan.interest.0.05 & bank.loan.interest.0.01 will select 1% interest" })
	public static final BankDoublePermissionPath	PERMISSION_LOAN_INTEREST		= new BankDoublePermissionPath("bank.loan.interest.");
	
	@CommentArray({ "Give a player the permission: bank.loan.amount.1000.5 for a $1000.50 maximum loans",
			"Use main config to enable combined totals, bank.loan.amount.1000.5 & bank.loan.amount.10.5 would be a total of $1011.00" })
	public static final BankDoublePermissionPath	PERMISSION_LOAN_AMOUNT			= new BankDoublePermissionPath("bank.loan.amount.");
	
	@CommentArray({ "Give a player the permission: bank.item.slots.1000 for a 1000 slots",
			"Use main config to enable combined totals, bank.item.slots.1000 & bank.item.slots.10 would be a total of 1010 slots" })
	public static final BankIntegerPermissionPath	PERMISSION_SLOTS				= new BankIntegerPermissionPath("bank.item.slots.");
	@CommentArray({"Give a player the permission: bank.item.slot.merge.1000 for max 1000 merge size"})
	public static final BankIntegerPermissionPath PERMISSION_SLOT_MERGE = new BankIntegerPermissionPath("bank.item.slot.merge");

	@Comment("Give a player the permission: bank.tabs.12 for 12 item tabs")
	public static final BankIntegerPermissionPath	PERMISSION_TABS				= new BankIntegerPermissionPath("bank.tabs.");
	@Comment("Permission to rename tabs in items inventory")
	public static final PermissionPath				PERMISSION_TAB_RENAME						= new PermissionPath("bank.item.tab.rename");

	@Comment("Permission to send money from inventory")
	public static final PermissionPath				PERMISSION_INVENTORY_MONEY_SEND	= new BankPermissionPath("bank.open.money.send", true);
	
	@Comment("Permission to send exp from inventory")
	public static final PermissionPath				PERMISSION_INVENTORY_EXP_SEND	= new BankPermissionPath("bank.open.exp.send", true);

	@Comment("Permission to withdraw items from bank")
	public static final PermissionPath				PERMISSION_ITEMS_DEPOSIT	= new BankPermissionPath("bank.items.deposit", false);

	@Comment("Permission to withdraw items from bank")
	public static final PermissionPath				PERMISSION_ITEMS_WITHDRAW	= new BankPermissionPath("bank.items.withdraw", false);

	@Comment("Extra perms for admins when opening players banks")
	public static final BankPermissionStringListPath PERMISSION_ADMIN_PERMISSIONS				= new BankPermissionStringListPath("bank.tabs.9");

	@Comment("Note this permission can't be disabled for security reasons.")
	public static final StringPath					PERMISSION_ADMIN_INVENTORY		= new StringPath("bank.admin.inventory.open");
	
	@CommentArray({ "Give a player the permission: bank.money.interest.online.0.05 for 5% interest",
			"Highest value will be taken from the user bank.money.interest.online.0.05 & bank.money.interest.online.0.01 will select 5% interest" })
	public static final BankDoublePermissionPath		PERMISSION_MONEY_INTEREST_ONLINE	= new BankDoublePermissionPath("bank.money.interest.online.");
	
	@CommentArray({ "Give a player the permission: bank.money.interest.offline.0.05 for 5% interest",
			"Highest value will be taken from the user bank.money.interest.offline.0.05 & bank.money.interest.offline.0.01 will select 5% interest" })
	public static final BankDoublePermissionPath		PERMISSION_MONEY_INTEREST_OFFLINE	= new BankDoublePermissionPath("bank.money.interest.offline.");
	
	@CommentArray({ "Give a player the permission: bank.exp.interest.online.0.05 for 5% interest",
			"Highest value will be taken from the user bank.exp.interest.online.0.05 & bank.exp.interest.online.0.01 will select 5% interest" })
	public static final BankDoublePermissionPath		PERMISSION_EXP_INTEREST_ONLINE		= new BankDoublePermissionPath("bank.exp.interest.online.");
	
	@CommentArray({ "Give a player the permission: bank.exp.interest.offline.0.05 for 5% interest",
			"Highest value will be taken from the user bank.exp.interest.offline.0.05 & bank.exp.interest.offline.0.01 will select 5% interest" })
	public static final BankDoublePermissionPath		PERMISSION_EXP_INTEREST_OFFLINE		= new BankDoublePermissionPath("bank.exp.interest.offline.");


	
	private BankPermissionConfiguration(JavaPlugin plugin){
		super(plugin, "conf/permissions.yml");
	}
	
}
