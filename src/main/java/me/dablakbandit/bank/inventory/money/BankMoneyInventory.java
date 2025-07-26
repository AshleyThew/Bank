package me.dablakbandit.bank.inventory.money;

import me.dablakbandit.bank.config.*;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.inventory.*;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.vault.Eco;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BankMoneyInventory extends BankInventoryHandler<BankMoneyInfo> {

	@Override
	public void init() {
		addBack();
		setItem(BankItemConfiguration.BANK_MONEY_BLANK);
		setItem(BankItemConfiguration.BANK_MONEY_WITHDRAWALL, consumeSound(this::withdrawAll, BankSoundConfiguration.INVENTORY_MONEY_WITHDRAW_ALL));
		setItem(BankItemConfiguration.BANK_MONEY_WITHDRAW, consumeSound(this::withdraw, BankSoundConfiguration.INVENTORY_MONEY_WITHDRAW));
		setItem(BankItemConfiguration.BANK_MONEY_BALANCE, this::getBalance);
		setItem(BankItemConfiguration.BANK_MONEY_DEPOSIT, consumeSound(this::deposit, BankSoundConfiguration.INVENTORY_MONEY_DEPOSIT));
		setItem(BankItemConfiguration.BANK_MONEY_DEPOSITALL, consumeSound(this::depositAll, BankSoundConfiguration.INVENTORY_MONEY_DEPOSIT_ALL));
		setItem(BankItemConfiguration.BANK_MONEY_SEND, consumePermissions(BankPermissionConfiguration.PERMISSION_INVENTORY_MONEY_SEND, consumeSound(this::sendMoney, BankSoundConfiguration.MONEY_SEND_OTHER)));
		setItem(BankItemConfiguration.BANK_MONEY_HISTORY_OPEN, consumeSound((pl, info) -> openHistory(pl, info), BankSoundConfiguration.INVENTORY_MENU_OPEN_MONEY));
	}

	private ItemStack getBalance(BankItemPath path, BankMoneyInfo info) {
		return replaceNameLore(path, "<money>", Format.formatMoney(info.getMoney()));
	}

	private void withdrawAll(CorePlayers pl, BankMoneyInfo info) {
		double withdraw = info.getMoney();
		if (BankPluginConfiguration.BANK_MONEY_FULL_DOLLARS.get()) {
			withdraw = Math.floor(withdraw);
		}
		info.withdrawMoney(pl, withdraw);
		pl.refreshInventory();
	}

	private void withdraw(CorePlayers pl, BankMoneyInfo info) {
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_MONEY_WTIHDRAW.get(), Format.round(info.getMoney(), 2)) {
			@Override
			public void cancel(CorePlayers pl) {
				pl.setOpenInventory(BankMoneyInventory.this);
			}

			@Override
			public void close(CorePlayers pl) {
				pl.setOpenInventory(BankMoneyInventory.this);
			}

			@Override
			public void onClick(CorePlayers pl, String value) {
				double withdraw;
				try {
					withdraw = Double.parseDouble(value);
				} catch (Exception e) {
					pl.setOpenInventory(BankMoneyInventory.this);
					return;
				}
				withdraw = Math.max(0, withdraw);
				withdraw = Math.min(info.getMoney(), withdraw);
				if (info.withdrawMoney(pl, withdraw)) {
					// if(PlayerManager.hasLocationChanged(bi.getLast(), player.getLocation())){ return; }
					// bi.openMoney(player);
				} else {
					// (PlayerManager.hasLocationChanged(bi.getLast(), player.getLocation())){ return; }
					// bi.openMoney(player);
				}
				pl.setOpenInventory(BankMoneyInventory.this);
			}
		});
	}

	private void deposit(CorePlayers pl, BankMoneyInfo info) {
		double balance = 0;
		if (Eco.getInstance().getEconomy() != null) {
			balance = Eco.getInstance().getEconomy().getBalance(pl.getPlayer());
		}
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_MONEY_DEPOSIT.get(), "" + Format.round(balance, 2)) {
			@Override
			public void cancel(CorePlayers pl) {
				pl.setOpenInventory(BankMoneyInventory.this);
			}

			@Override
			public void close(CorePlayers pl) {
				pl.setOpenInventory(BankMoneyInventory.this);
			}

			@Override
			public void onClick(CorePlayers pl, String value) {
				double deposit;
				try {
					deposit = Double.parseDouble(value);
				} catch (Exception e) {
					pl.setOpenInventory(BankMoneyInventory.this);
					return;
				}
				deposit = Math.max(0d, deposit);
				double max = 0;
				if (Eco.getInstance().getEconomy() != null) {
					max = Eco.getInstance().getEconomy().getBalance(pl.getPlayer());
				}
				deposit = Math.min(max, deposit);
				info.deposit(pl.getPlayer(), pl, deposit);
				pl.setOpenInventory(BankMoneyInventory.this);
			}
		});
	}

	private void depositAll(CorePlayers pl, BankMoneyInfo info) {
		double deposit = 0;
		if (Eco.getInstance().getEconomy() != null) {
			deposit = Eco.getInstance().getEconomy().getBalance(pl.getPlayer());
		}
		info.deposit(pl.getPlayer(), pl, deposit);
		pl.refreshInventory();
	}

	private void sendMoney(CorePlayers pl, BankMoneyInfo info) {
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_MONEY_SEND_NAME.get(), " ") {
			@Override
			public void cancel(CorePlayers pl) {
				pl.setOpenInventory(BankMoneyInventory.this);
			}

			@Override
			public void close(CorePlayers pl) {
				pl.setOpenInventory(BankMoneyInventory.this);
			}

			@Override
			public void onClick(CorePlayers pl, String value) {
				if (value.startsWith(" ")) {
					value = value.substring(1);
				}
				if (Bukkit.getPlayerExact(value) == null) {
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", value));
					return;
				}
				sendMoneyAmount(pl, info, value);
			}
		});
	}

	private void sendMoneyAmount(CorePlayers pl, BankMoneyInfo info, String name) {
		pl.setOpenInventory(new AnvilInventory(BankLanguageConfiguration.ANVIL_MONEY_SEND_AMOUNT.get(), "1") {
			@Override
			public void cancel(CorePlayers pl) {
				pl.setOpenInventory(BankMoneyInventory.this);
			}

			@Override
			public void close(CorePlayers pl) {
				pl.setOpenInventory(BankMoneyInventory.this);
			}

			@Override
			public void onClick(CorePlayers from, String value) {
				double amount;
				try {
					amount = Double.parseDouble(value);
				} catch (Exception e) {
					e.printStackTrace();
					from.setOpenInventory(BankMoneyInventory.this);
					return;
				}
				Player p = Bukkit.getPlayerExact(name);
				final CorePlayers payTo = CorePlayerManager.getInstance().getPlayer(p);
				if (p == null || payTo == null || payTo.getInfo(BankInfo.class).isLocked(false)) {
					BankSoundConfiguration.GLOBAL_ERROR.play(pl.getPlayer());
					BankLanguageConfiguration.sendFormattedMessage(pl, BankLanguageConfiguration.COMMAND_UNKNOWN_PLAYER.get().replaceAll("<player>", name));
					return;
				}
				info.send(payTo, amount);
				pl.setOpenInventory(BankMoneyInventory.this);
			}
		});
	}

	public ItemStack getBack(BankItemPath path, BankMoneyInfo bankMoneyInfo) {
		BankInfo bankInfo = bankMoneyInfo.getPlayers().getInfo(BankInfo.class);
		if (containsAnyOpenType(bankInfo, OpenTypes.ALL, OpenTypes.MENU)) {
			return path.get();
		}
		return BankItemConfiguration.BANK_MONEY_BLANK.get();
	}

	private void addBack() {
		if (!BankPluginConfiguration.BANK_MONEY_ONLY.get()) {
			setItem(BankItemConfiguration.BANK_MONEY_BACK, this::getBack, consumeSound(this::returnToMainMenu, BankSoundConfiguration.INVENTORY_GLOBAL_BACK));
		}
	}

	protected void returnToMainMenu(CorePlayers pl) {
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MAIN_MENU);
	}

	protected void openHistory(CorePlayers pl, BankMoneyInfo info) {
		BankInventoriesManager.getInstance().open(pl, BankInventories.BANK_MONEY_HISTORY);
	}

	@Override
	public BankMoneyInfo getInvoker(CorePlayers pl) {
		return pl.getInfo(BankMoneyInfo.class);
	}

}
