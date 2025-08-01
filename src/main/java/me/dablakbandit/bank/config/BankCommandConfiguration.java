package me.dablakbandit.bank.config;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.command.config.CommandConfiguration;
import me.dablakbandit.core.command.config.annotation.CommandBase;
import me.dablakbandit.core.config.comment.annotation.Comment;
import me.dablakbandit.core.config.comment.annotation.Delete;
import org.bukkit.plugin.java.JavaPlugin;

public class BankCommandConfiguration extends CommandConfiguration {

	private static BankCommandConfiguration config;

	public static void load(BankPlugin bankPlugin) {
		config = new BankCommandConfiguration(bankPlugin);
		config.load();
	}

	public static BankCommandConfiguration getInstance() {
		return config;
	}

	//@formatter:off
	@CommandBase
	public static final Command	BANK							= new Command("bank", null);
	
	public static Command		BANK_ADMIN						= new Command("admin", "bank.admin");
	public static Command		BANK_ADMIN_RELOAD				= new Command("reload", "bank.admin.reload");
	public static Command		BANK_ADMIN_BLOCK				= new Command("block", "bank.admin.block");
	public static Command		BANK_ADMIN_BLOCK_ADD			= new Command("add", "bank.admin.block.add");
	public static Command		BANK_ADMIN_BLOCK_REMOVE			= new Command("remove", "bank.admin.block.remove");
	public static Command		BANK_ADMIN_DEBUG				= new Command("debug", "bank.admin.debug");
	public static Command		BANK_ADMIN_DEBUG_ITEM			= new Command("item", "bank.admin.debug.item");
	public static Command 		BANK_ADMIN_EXP 					= new Command("exp", "bank.admin.exp");
	public static Command 		BANK_ADMIN_EXP_ADD 				= new Command("add", "bank.admin.exp.add", new String[0], new String[]{"<player> <amount>"});
	public static Command 		BANK_ADMIN_EXP_BALANCE 			= new Command("balance", "bank.admin.exp.balance", new String[0], new String[]{"<player>"});
	public static Command 		BANK_ADMIN_EXP_SUBTRACT 		= new Command("subtract", "bank.admin.exp.subtract", new String[0], new String[]{"<player> <amount>"});
	public static Command		BANK_ADMIN_RESET				= new Command("reset", "bank.admin.reset", new String[0], new String[]{ "<name>" });
	public static Command		BANK_ADMIN_FIX					= new Command("fix", "bank.admin.fix");
	public static Command		BANK_ADMIN_FIX_USERNAMES		= new Command("usernames", "bank.admin.fix.usernames");
	public static Command		BANK_ADMIN_PIN					= new Command("pin", "bank.admin.pin");
	public static Command		BANK_ADMIN_PIN_RESET			= new Command("reset", "bank.admin.pin.reset");
	public static Command		BANK_ADMIN_ITEM					= new Command("item", "bank.admin.item");
	public static Command		BANK_ADMIN_ITEM_BLACKLIST		= new Command("blacklist", "bank.admin.item.blacklist");
	public static Command		BANK_ADMIN_ITEM_TRASHBLACKLIST	= new Command("trashblacklist", "bank.admin.item.blacklist");
	public static Command		BANK_ADMIN_ITEM_DEFAULT			= new Command("default", "bank.admin.item.default");
	public static Command		BANK_ADMIN_PERMISSION			= new Command("permission", "bank.admin.permission");
	public static Command		BANK_ADMIN_PERMISSION_HISTORY	= new Command("history", "bank.admin.permission.history", new String[0], new String[]{ "<name>" });
	public static Command		BANK_ADMIN_PERMISSION_UPDATE	= new Command("update", "bank.admin.permission.update", new String[0], new String[]{ "<name>" });
	public static Command		BANK_ADMIN_LOOKUP				= new Command("lookup", "bank.admin.lookup", new String[0], new String[]{ "<name>" });
	public static Command 		BANK_ADMIN_MONEY 				= new Command("money", "bank.admin.money");
	public static Command 		BANK_ADMIN_MONEY_ADD 			= new Command("add", "bank.admin.money.add", new String[0], new String[]{"<player> <amount>"});
	public static Command 		BANK_ADMIN_MONEY_BALANCE 		= new Command("balance", "bank.admin.money.balance", new String[0], new String[]{"<player>"});
	public static Command 		BANK_ADMIN_MONEY_SUBTRACT 		= new Command("subtract", "bank.admin.money.subtract", new String[0], new String[]{"<player> <amount>"});
	public static Command		BANK_ADMIN_OPEN					= new Command("open", "bank.admin.open", new String[0], new String[]{ "<player>" });
	public static Command		BANK_ADMIN_FORCE				= new Command("force", "bank.admin.force");
	public static Command		BANK_ADMIN_FORCE_OPEN			= new Command("open", "bank.admin.force.open", new String[0], new String[]{ "<player>" });
	public static Command		BANK_ADMIN_LOCK					= new Command("lock", "bank.admin.lock", new String[0], new String[]{ "<player> <true/false>" });
	public static Command		BANK_ADMIN_SAVE					= new Command("save", "bank.admin.save");
	public static Command		BANK_ADMIN_SLOTS				= new Command("slots", "bank.admin.slots");
	public static Command		BANK_ADMIN_SLOTS_ADD			= new Command("add", "bank.admin.slots.add", new String[0], new String[]{ "<player> <amount>" });
	public static Command		BANK_ADMIN_SLOTS_SET			= new Command("set", "bank.admin.slots.set", new String[0], new String[]{ "<player> <amount>" });
	public static Command		BANK_ADMIN_TAX					= new Command("tax", "bank.admin.tax");
	public static Command		BANK_ADMIN_TAX_INFO				= new Command("withdraw", "bank.admin.tax.info");
	public static Command		BANK_ADMIN_TAX_WITHDRAW			= new Command("withdraw", "bank.admin.tax.withdraw", new String[0], new String[]{ "<player> <amount>" });
	public static Command		BANK_ADMIN_NPC					= new Command("npc", "bank.admin.npc");
	public static Command		BANK_ADMIN_NPC_SETOPENTYPES		= new Command("setopentypes", "bank.admin.npc.setopentypes", new String[0], new String[]{ "[types...]" });

	public static Command		BANK_ECO						= new Command("eco", "bank.admin.eco");
	public static Command		BANK_ECO_GIVE					= new Command("give", "bank.admin.give", new String[0], new String[]{ "<name> <amount>" });
	public static Command		BANK_ECO_SET					= new Command("set", "bank.admin.set", new String[0], new String[]{ "<name> <amount>" });
	public static Command		BANK_ECO_TAKE					= new Command("take", "bank.admin.take", new String[0], new String[]{ "<name> <amount>" });
	
	public static Command		BANK_EXP						= new Command("exp", "bank.exp");
	@Comment("Enable command opentypes subset in config.yml")
	public static Command		BANK_EXP_OPEN					= new Command("open", "bank.exp.open");
	public static Command		BANK_EXP_BALANCE				= new Command("balance", "bank.exp.balance");
	public static Command		BANK_EXP_DEPOSIT				= new Command("deposit", "bank.exp.deposit", new String[0], new String[]{ "<amount>" });
	public static Command		BANK_EXP_DEPOSIT_ALL			= new Command("all", "bank.exp.deposit.all");
	public static Command		BANK_EXP_WITHDRAW				= new Command("withdraw", "bank.exp.withdraw", new String[0], new String[]{ "<amount>" });
	public static Command		BANK_EXP_WITHDRAW_ALL			= new Command("all", "bank.exp.withdraw.all");
	public static Command		BANK_EXP_SEND					= new Command("send", "bank.exp.send");
	
	public static Command		BANK_MONEY						= new Command("money", "bank.money");
	@Comment("Enable command opentypes subset in config.yml")
	public static Command		BANK_MONEY_OPEN					= new Command("open", "bank.money.open");
	public static Command		BANK_MONEY_BALANCE				= new Command("balance", "bank.money.balance");
	public static Command		BANK_MONEY_DEPOSIT				= new Command("deposit", "bank.money.deposit", new String[0], new String[]{ "<amount>" });
	public static Command		BANK_MONEY_DEPOSIT_ALL			= new Command("all", "bank.money.deposit.all");
	public static Command		BANK_MONEY_PAY					= new Command("pay", "bank.money.pay", new String[0], new String[]{ "<name> <amount>" });
	public static Command		BANK_MONEY_WITHDRAW				= new Command("withdraw", "bank.money.withdraw", new String[0], new String[]{ "<amount>" });
	public static Command		BANK_MONEY_WITHDRAW_ALL			= new Command("all", "bank.money.withdraw.all");

	public static Command		BANK_CHEQUE						= new Command("cheque", "bank.cheque");
	public static Command		BANK_CHEQUE_CREATE				= new Command("create", "bank.cheque.create", new String[0], new String[]{ "<amount> [player]" });
	public static Command		BANK_CHEQUE_BOOK				= new Command("book", "bank.cheque.book");

	@Delete(".Info")
	public static Command		BANK_OPEN						= new Command("open", "bank.open.command");
	@Comment("Enable command advanced in config.yml")
	public static Command		BANK_OPEN_EXP					= new Command("exp", "bank.open.command.exp");
	@Comment("Enable command advanced in config.yml")
	public static Command		BANK_OPEN_ITEMS					= new Command("items", "bank.open.command.items");
	@Comment("Enable command advanced in config.yml")
	public static Command		BANK_OPEN_LOANS					= new Command("loans", "bank.open.command.loans");
	@Comment("Enable command advanced in config.yml")
	public static Command		BANK_OPEN_MENU					= new Command("menu", "bank.open.command.menu");
	@Comment("Enable command advanced in config.yml")
	public static Command		BANK_OPEN_MONEY					= new Command("money", "bank.open.command.money");
	@Comment("Enable command advanced in config.yml")
	public static Command		BANK_OPEN_PIN					= new Command("pin", "bank.open.command.pin");
	
	public static Command		BANK_INFO						= new Command("info", "bank.info");

	
	private BankCommandConfiguration(JavaPlugin plugin){
		super(plugin, "conf/commands.yml");
	}
}
