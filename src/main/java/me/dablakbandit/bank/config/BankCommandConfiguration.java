package me.dablakbandit.bank.config;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.command.config.annotation.CommandBase;

public class BankCommandConfiguration extends CommandConfiguration{
	
	private static BankCommandConfiguration config = new BankCommandConfiguration(BankPlugin.getInstance(), "commands.yml");
	
	public static BankCommandConfiguration getInstance(){
		return config;
	}
	
	@CommandBase
	public static Command	BANK							= new Command("bank", null);
	
	public static Command	BANK_ADMIN						= new Command("admin", "bank.admin");
	public static Command	BANK_ADMIN_RELOAD				= new Command("reload", "bank.admin.reload");
	public static Command	BANK_ADMIN_ADD					= new Command("add", "bank.admin.add");
	public static Command	BANK_ADMIN_ADD_BLOCK			= new Command("block", "bank.admin.add.block");
	public static Command	BANK_ADMIN_REMOVE				= new Command("remove", "bank.admin.remove");
	public static Command	BANK_ADMIN_REMOVE_BLOCK			= new Command("block", "bank.admin.remove.block");
	public static Command	BANK_ADMIN_RESET				= new Command("reset", "bank.admin.reset", new String[0], new String[]{ "<name>" });
	public static Command	BANK_ADMIN_FIX					= new Command("fix", "bank.admin.fix");
	public static Command	BANK_ADMIN_FIX_USERNAMES		= new Command("usernames", "bank.admin.fix.usernames");
	public static Command	BANK_ADMIN_PIN					= new Command("pin", "bank.admin.pin");
	public static Command	BANK_ADMIN_PIN_RESET			= new Command("reset", "bank.admin.pin.reset");
	public static Command	BANK_ADMIN_ITEM					= new Command("item", "bank.admin.item");
	public static Command	BANK_ADMIN_ITEM_BLACKLIST		= new Command("blacklist", "bank.admin.item.blacklist");
	public static Command	BANK_ADMIN_PERMISSION			= new Command("permission", "bank.admin.permission");
	public static Command	BANK_ADMIN_PERMISSION_HISTORY	= new Command("history", "bank.admin.permission.history", new String[0], new String[]{ "<name>" });
	public static Command	BANK_ADMIN_PERMISSION_UPDATE	= new Command("update", "bank.admin.permission.update", new String[0], new String[]{ "<name>" });
	public static Command	BANK_ADMIN_LOOKUP				= new Command("lookup", "bank.admin.lookup", new String[0], new String[]{ "<name>" });
	public static Command	BANK_ADMIN_OPEN					= new Command("open", "bank.admin.open", new String[0], new String[]{ "<player>" });
	public static Command	BANK_ADMIN_LOCK					= new Command("lock", "bank.admin.lock", new String[0], new String[]{ "<player> <true/false>" });
	public static Command	BANK_ADMIN_SAVE					= new Command("save", "bank.admin.save");
	public static Command	BANK_ADMIN_SLOTS				= new Command("slots", "bank.admin.slots");
	public static Command	BANK_ADMIN_SLOTS_ADD			= new Command("add", "bank.admin.slots.add", new String[0], new String[]{ "<player> <amount>" });
	public static Command	BANK_ADMIN_SLOTS_SET			= new Command("set", "bank.admin.slots.set", new String[0], new String[]{ "<player> <amount>" });
	
	public static Command	BANK_ECO						= new Command("eco", "bank.admin.eco");
	public static Command	BANK_ECO_GIVE					= new Command("give", "bank.admin.give", new String[0], new String[]{ "<name> <amount>" });
	public static Command	BANK_ECO_SET					= new Command("set", "bank.admin.set", new String[0], new String[]{ "<name> <amount>" });
	public static Command	BANK_ECO_TAKE					= new Command("take", "bank.admin.take", new String[0], new String[]{ "<name> <amount>" });
	
	public static Command	BANK_EXP						= new Command("exp", "bank.exp");
	public static Command	BANK_EXP_BALANCE				= new Command("balance", "bank.exp.balance");
	public static Command	BANK_EXP_DEPOSIT				= new Command("deposit", "bank.exp.deposit", new String[0], new String[]{ "<amount>" });
	public static Command	BANK_EXP_DEPOSIT_ALL			= new Command("all", "bank.exp.deposit.all");
	public static Command	BANK_EXP_WITHDRAW				= new Command("withdraw", "bank.exp.withdraw", new String[0], new String[]{ "<amount>" });
	public static Command	BANK_EXP_WITHDRAW_ALL			= new Command("all", "bank.exp.withdraw.all");
	public static Command	BANK_EXP_SEND					= new Command("send", "bank.exp.send");
	
	public static Command	BANK_MONEY						= new Command("money", "bank.money");
	public static Command	BANK_MONEY_BALANCE				= new Command("balance", "bank.money.balance");
	public static Command	BANK_MONEY_DEPOSIT				= new Command("deposit", "bank.money.deposit", new String[0], new String[]{ "<amount>" });
	public static Command	BANK_MONEY_DEPOSIT_ALL			= new Command("all", "bank.money.deposit.all");
	public static Command	BANK_MONEY_PAY					= new Command("pay", "bank.money.pay", new String[0], new String[]{ "<name> <amount>" });
	public static Command	BANK_MONEY_WITHDRAW				= new Command("withdraw", "bank.money.withdraw", new String[0], new String[]{ "<amount>" });
	public static Command	BANK_MONEY_WITHDRAW_ALL			= new Command("all", "bank.money.withdraw.all");
	
	public static Command	BANK_OPEN						= new Command("open", "bank.open.command");
	
	public static Command	BANK_INFO						= new Command("info", "bank.info");
	
	// public static Command BANK_MONEY = new Command("money", "bank.money");
	// public static Command BANK_MONEY_PAY = new Command("pay", "bank.money.pay", new String[0], new String[]{ "<name> <amount>" });
	
	private BankCommandConfiguration(JavaPlugin plugin, String filename){
		super(plugin, filename);
	}
}
