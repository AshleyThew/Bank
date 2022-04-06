package me.dablakbandit.bank.config;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.path.BankDoublePermissionPath;
import me.dablakbandit.bank.config.path.BankIntegerPermissionPath;
import me.dablakbandit.bank.config.path.BankPermissionPath;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.comment.annotation.CommentArray;
import me.dablakbandit.core.config.path.*;

public class BankPermissionConfiguration extends CommentAdvancedConfiguration{
	
	private static final BankPermissionConfiguration configuration = new BankPermissionConfiguration(BankPlugin.getInstance());
	
	public static BankPermissionConfiguration getInstance(){
		return configuration;
	}
	
	@CommentArray({ "Edit additional permissions", "Permission node parameters", "Required: Permission, Check", })
	private static final Path						PERMISSION						= new EmptyPath();
	
	public static final BooleanPath					PERMISSION_SEND_MESSAGE			= new BooleanPath(true);
	
	public static final PermissionPath				PERMISSION_OPEN_SIGN			= new BankPermissionPath("bank.open.sign");
	public static final PermissionPath				PERMISSION_OPEN_CITIZENS		= new BankPermissionPath("bank.open.citizens");
	public static final PermissionPath				PERMISSION_OPEN_BLOCK			= new BankPermissionPath("bank.open.block");
	
	public static final PermissionPath				PERMISSION_SIGN_PLACE			= new BankPermissionPath("bank.sign.place");
	
	@CommentArray({ "Give a player the permission: bank.loan.interest.0.05 for 5% interest",
			"Lowest value will be taken from the user bank.loan.interest.0.05 & bank.loan.interest.0.01 will select 1% interest" })
	public static final BankDoublePermissionPath	PERMISSION_LOAN_INTEREST		= new BankDoublePermissionPath("bank.loan.interest.");
	
	@CommentArray({ "Give a player the permission: bank.loan.amount.1000.5 for a $1000.50 maximum loans",
			"Use main config to enable combined totals, bank.loan.amount.1000.5 & bank.loan.amount.10.5 would be a total of $1011.00" })
	public static final BankDoublePermissionPath	PERMISSION_LOAN_AMOUNT			= new BankDoublePermissionPath("bank.loan.amount.");
	
	@CommentArray({ "Give a player the permission: bank.item.slots.1000 for a 1000 slots",
			"Use main config to enable combined totals, bank.item.slots.1000 & bank.item.slots.10 would be a total of 1010 slots" })
	public static final BankIntegerPermissionPath	PERMISSION_SLOTS				= new BankIntegerPermissionPath("bank.item.slots.");

	@Comment("Permission to rename tabs in items inventory")
	public static final PermissionPath				PERMISSION_TAB_RENAME						= new PermissionPath("bank.item.tab.rename");

	@Comment("Permission to send money from inventory")
	public static final PermissionPath				PERMISSION_INVENTORY_MONEY_SEND	= new BankPermissionPath("bank.open.money.send");
	
	@Comment("Permission to send exp from inventory")
	public static final PermissionPath				PERMISSION_INVENTORY_EXP_SEND	= new BankPermissionPath("bank.open.exp.send");
	
	@Comment("Note this permission can't be disabled for security reasons.")
	public static final StringPath					PERMISSION_ADMIN_INVENTORY		= new StringPath("bank.admin.inventory.open");
	
	private BankPermissionConfiguration(JavaPlugin plugin){
		super(plugin, "permissions.yml");
	}
	
}
