package me.dablakbandit.bank.inventory.money;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.player.info.transaction.BankMoneyTransactionInfo;
import me.dablakbandit.bank.player.info.transaction.Transaction;
import me.dablakbandit.bank.player.info.transaction.TransactionType;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.List;

import me.dablakbandit.bank.inventory.BankInventoriesManager;
import me.dablakbandit.bank.inventory.BankInventories;
import me.dablakbandit.bank.config.BankSoundConfiguration;

public class BankMoneyHistoryInventory extends BankInventoryHandler<BankMoneyInfo> {

	@Override
	public void init() {
		addInfo();
		addScrolls();
		addBack();
		int size = descriptor.getSize();
		addTransactions(size);
	}

	@Override
	protected boolean open(CorePlayers pl, Player player, int size, String title) {
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		if (bankInfo != null) {
			bankInfo.getMoneyTransactionInfo().setScrolled(0);
		}
		return super.open(pl, player, size, title);
	}

	private void addInfo() {
		ItemStack is = BankItemConfiguration.BANK_MONEY_BLANK.get();
		for (int i = 0; i < 9; i++) {
			setItem(i, () -> is);
		}
	}

	private void addScrolls() {
		setItem(BankItemConfiguration.BANK_MONEY_HISTORY_SCROLL_UP, (pl) -> addScroll(pl, -1));
		setItem(BankItemConfiguration.BANK_MONEY_HISTORY_SCROLL_DOWN, (pl) -> addScroll(pl, 1));
	}

	private void addScroll(CorePlayers pl, int add) {
		BankInfo bankInfo = pl.getInfo(BankInfo.class);
		if (bankInfo != null) {
			BankMoneyTransactionInfo txInfo = bankInfo.getMoneyTransactionInfo();
			txInfo.setScrolled(txInfo.getScrolled() + add);
		}
		pl.refreshInventory();
	}

	private void addBack() {
		setItem(BankItemConfiguration.BANK_MONEY_HISTORY_BACK, consumeSound(this::returnToMoney, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
	}

	protected void returnToMoney(CorePlayers pl) {
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MONEY);
	}

	private void addTransactions(int size) {
		int show = BankItemConfiguration.BANK_MONEY_HISTORY_LIST.getExtendValue("Slots", Integer.class);
		int start = BankItemConfiguration.BANK_MONEY_HISTORY_LIST.getExtendValue("Start", Integer.class);
		for (int item = 0; item < show; item++) {
			int slot = item + start;
			if (slot > size) {
				break;
			}
			int finalItem = item;
			setItem(slot, (info) -> getTransactionItemStack(finalItem, info));
		}
	}

	@Override
	public BankMoneyInfo getInvoker(CorePlayers pl) {
		return pl.getInfo(BankMoneyInfo.class);
	}

	private ItemStack getTransactionItemStack(int slot, BankMoneyInfo info) {
		BankInfo bankInfo = info.getPlayers().getInfo(BankInfo.class);
		if (bankInfo == null) return null;
		BankMoneyTransactionInfo txInfo = bankInfo.getMoneyTransactionInfo();
		List<Transaction> transactions = txInfo.getTransactions();
		int get = txInfo.getScrolled() * 9 + slot;
		if (get >= transactions.size()) return null;
		Transaction tx = transactions.get(get);
		ItemStack is = getTransactionTypeItem(tx.getType());
		String date = new java.text.SimpleDateFormat("HH:mm MMM dd, yyyy").format(tx.getDate());
		long deltaMillis = System.currentTimeMillis() - tx.getDate().getTime();
		String delta = formatTimeDelta(deltaMillis);
		String param = tx.getDescriptionParam() != null ? tx.getDescriptionParam() : "";
		String amount = Format.formatMoney(tx.getAmount());
		String desc = tx.getDescription().getMessage().get().replace("<player>", param).replace("<amount>", amount);
		return clone(is, ChatColor.AQUA + desc, ChatColor.GRAY + date + ChatColor.DARK_GRAY + " (" + delta + ")");
	}

	private String formatTimeDelta(long deltaMillis) {
		long seconds = deltaMillis / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		if (days > 0) return days + "d ago";
		if (hours > 0) return hours + "h ago";
		return minutes + "m ago";
	}

	private ItemStack getTransactionTypeItem(TransactionType type) {
		switch (type) {
			case DEPOSIT:
				return BankItemConfiguration.BANK_MONEY_HISTORY_DEPOSIT.get();
			case WITHDRAW:
				return BankItemConfiguration.BANK_MONEY_HISTORY_WITHDRAW.get();
			case SEND:
				return BankItemConfiguration.BANK_MONEY_HISTORY_SEND.get();
			case RECEIVE:
				return BankItemConfiguration.BANK_MONEY_HISTORY_RECEIVE.get();
			case INTEREST:
				return BankItemConfiguration.BANK_MONEY_HISTORY_INTEREST.get();
			case TAX:
				return BankItemConfiguration.BANK_MONEY_HISTORY_TAX.get();
			default:
				return BankItemConfiguration.BANK_MONEY_BLANK.get();
		}
	}
}
