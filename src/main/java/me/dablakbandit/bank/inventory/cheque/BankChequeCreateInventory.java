package me.dablakbandit.bank.inventory.cheque;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.implementations.cheque.ChequeImplementation;
import me.dablakbandit.bank.implementations.cheque.TempCheque;
import me.dablakbandit.bank.inventory.*;
import me.dablakbandit.bank.inventory.path.BankItemPathReplacer;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BankChequeCreateInventory extends BankInventoryHandler<BankInfo> {

	private static BankItemPathReplacer<TempCheque> replacer = new BankItemPathReplacer<TempCheque>().addMoney("<amount>", TempCheque::getAmount).add("<recipient>", TempCheque::getRecipientDisplayName).addMoney("<cost>", (tempCheque) -> BankPluginConfiguration.BANK_CHEQUES_CREATE_COST.get()).addMoney("<total>", (tempCheque) -> tempCheque.getAmount() + BankPluginConfiguration.BANK_CHEQUES_CREATE_COST.get());

	@Override
	public void init() {
		addBack();
		setItem(BankItemConfiguration.BANK_CHEQUE_CREATE_BLANK);
		setItem(BankItemConfiguration.BANK_CHEQUE_CREATE_AMOUNT, this::getInfo, this::setAmount);
		setItem(BankItemConfiguration.BANK_CHEQUE_CREATE_RECIPIENT, this::getInfo, this::setRecipient);
		setItem(BankItemConfiguration.BANK_CHEQUE_CREATE_CONFIRM, this::getInfo, this::confirmCheque);
	}

	public ItemStack getInfo(BankItemPath path, BankInfo bankInfo) {
		TempCheque tempCheque = bankInfo.getTempCheque();
		return replacer.apply(path, tempCheque);
	}

	@Override
	protected boolean open(CorePlayers pl, Player player, int size, String title) {
		BankInfo bankInfo = getInvoker(pl);
		if (bankInfo.getTempCheque() == null) {
			TempCheque tempCheque = new TempCheque(BankPluginConfiguration.BANK_CHEQUES_MINIMUM_AMOUNT.get());
			bankInfo.setTempCheque(tempCheque);
		}
		return super.open(pl, player, size, title);
	}

	private void setAmount(CorePlayers pl, BankInfo bankInfo) {
		TempCheque tempCheque = bankInfo.getTempCheque();
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_CHEQUE_AMOUNT.get(), Format.round(tempCheque.getAmount(), 2)) {
			@Override
			public void cancel(CorePlayers pl) {
				pl.setOpenInventory(BankChequeCreateInventory.this);
			}

			@Override
			public void close(CorePlayers pl) {
				pl.setOpenInventory(BankChequeCreateInventory.this);
			}

			@Override
			public void onClick(CorePlayers pl, String s) {
				if (s == null || s.trim().isEmpty()) {
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_INVALID_INPUT.get());
					return;
				}

				try {
					double amount = Double.parseDouble(s.trim());

					if (amount < BankPluginConfiguration.BANK_CHEQUES_MINIMUM_AMOUNT.get()) {
						BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_AMOUNT_TOO_LOW.get());
						return;
					}

					if (amount > BankPluginConfiguration.BANK_CHEQUES_MAXIMUM_AMOUNT.get()) {
						BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_AMOUNT_TOO_HIGH.get());
						return;
					}

					tempCheque.setAmount(amount);
					pl.setOpenInventory(BankChequeCreateInventory.this);
				} catch (NumberFormatException e) {
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_INVALID_AMOUNT.get());
				}
			}
		});
	}

	private void setRecipient(CorePlayers pl, BankInfo bankInfo) {
		TempCheque tempCheque = bankInfo.getTempCheque();
		String currentRecipient = tempCheque.getRecipientName();
		if (currentRecipient == null || currentRecipient.isEmpty()) {
			currentRecipient = "Anyone";
		}

		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_CHEQUE_RECIPIENT.get(), currentRecipient) {
			@Override
			public void cancel(CorePlayers pl) {
				pl.setOpenInventory(BankChequeCreateInventory.this);
			}

			@Override
			public void close(CorePlayers pl) {
				pl.setOpenInventory(BankChequeCreateInventory.this);
			}

			@Override
			public void onClick(CorePlayers pl, String s) {
				if (s == null || s.trim().isEmpty()) {
					// Clear recipient - make it payable to anyone
					tempCheque.setRecipient(null);
					tempCheque.setRecipientName(null);
					pl.setOpenInventory(BankChequeCreateInventory.this);
					return;
				}

				String recipientName = s.trim();

				// Check if recipient exists and is online
				Player recipientPlayer = Bukkit.getPlayer(recipientName);
				if (recipientPlayer == null) {
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_RECIPIENT_NOT_FOUND.get());
					return;
				}

				tempCheque.setRecipient(recipientPlayer.getUniqueId());
				tempCheque.setRecipientName(recipientName);
				pl.setOpenInventory(BankChequeCreateInventory.this);
			}
		});
	}

	private void confirmCheque(CorePlayers pl, BankInfo bankInfo) {
		TempCheque tempCheque = bankInfo.getTempCheque();

		if (tempCheque.getAmount() <= 0) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_INVALID_AMOUNT.get());
			return;
		}

		// Check if player has enough money for the cheque amount plus creation cost
		double createCost = BankPluginConfiguration.BANK_CHEQUES_CREATE_COST.get();
		double totalCost = tempCheque.getAmount() + createCost;
		double playerMoney = pl.getInfo(me.dablakbandit.bank.player.info.BankMoneyInfo.class).getMoney();

		if (playerMoney < totalCost) {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_INSUFFICIENT_FUNDS.get());
			return;
		}

		boolean success = ChequeImplementation.getInstance().createCheque(pl, tempCheque.getAmount(), tempCheque.getRecipient(), tempCheque.isFromChequeBook());

		if (success) {
			finished(pl);
		} else {
			BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.MESSAGE_CHEQUE_CREATION_FAILED.get());
		}
	}

	private void addBack() {
		setItem(BankItemConfiguration.BANK_CHEQUE_CREATE_BACK, (path, info) -> {
			if (containsAnyOpenType(info, OpenTypes.ALL, OpenTypes.CHEQUES)) {
				return path.get();
			}
			return BankItemConfiguration.BANK_CHEQUE_MENU_BLANK.get();
		}, (pl) -> {
			if (containsAnyOpenType(pl.getInfo(BankInfo.class), OpenTypes.ALL, OpenTypes.CHEQUES)) {
				consumeSound(this::finished, BankSoundConfiguration.INVENTORY_MENU_OPEN_MONEY).accept(pl);
			}
		});
	}

	private void finished(CorePlayers pl) {
		if (getInvoker(pl).getTempCheque().isFromChequeBook()) {
			pl.setOpenInventory(null);
		} else {
			BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_CHEQUE_MENU);
		}
	}

	@Override
	public BankInfo getInvoker(CorePlayers pl) {
		return pl.getInfo(BankInfo.class);
	}
}
