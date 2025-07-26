package me.dablakbandit.bank.inventory.admin.permission;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.inventory.admin.BankAdminInventory;
import me.dablakbandit.bank.player.info.admin.BankAdminInfo;
import me.dablakbandit.bank.player.permission.PermissionCheck;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BankPermissionHistoryInventory extends BankInventoryHandler<CorePlayers> implements BankAdminInventory {

	private int scrolled;

	@Override
	public void init() {
		addInfo();
		addScrolls();
		int size = descriptor.getSize();
		addChecks(size);
	}

	@Override
	protected boolean open(CorePlayers pl, Player player, int size, String title) {
		this.scrolled = 0;
		return super.open(pl, player, size, title);
	}

	private void addInfo() {
		ItemStack is = BankItemConfiguration.BANK_ADMIN_ITEM_BLANK.get();
		for (int i = 0; i < 9; i++) {
			setItem(i, () -> is);
		}
	}

	private void addScrolls() {
		setItem(BankItemConfiguration.BANK_PERMISSION_HISTORY_SCROLL_UP, (pl) -> addScroll(pl, -1));
		setItem(BankItemConfiguration.BANK_PERMISSION_HISTORY_SCROLL_DOWN, (pl) -> addScroll(pl, 1));
	}

	private void addScroll(CorePlayers pl, int add) {
		this.scrolled = Math.max(0, this.scrolled + add);
		pl.refreshInventory();
	}

	private void addChecks(int size) {
		int show = BankItemConfiguration.BANK_PERMISSION_HISTORY_LIST.getExtendValue("Slots", Integer.class);
		int start = BankItemConfiguration.BANK_PERMISSION_HISTORY_LIST.getExtendValue("Start", Integer.class);
		for (int item = 0; item < show; item++) {
			int slot = item + start;
			if (slot > size) {
				break;
			}
			int finalItem = item;
			setItem(slot, (i) -> getItemStack(finalItem, i));
		}
	}

	@Override
	public CorePlayers getInvoker(CorePlayers pl) {
		return pl;
	}

	private ItemStack getItemStack(int slot, CorePlayers pl) {
		int get = scrolled * 9 + slot;
		List<PermissionCheck> list = pl.getInfo(BankAdminInfo.class).getPermissionsCheck();
		if (get >= list.size()) {
			return null;
		}
		PermissionCheck item = list.get(get);
		ItemStack is = (item.has() ? BankItemConfiguration.BANK_PERMISSION_HISTORY_TRUE : BankItemConfiguration.BANK_PERMISSION_HISTORY_FALSE).get();
		return clone(is, (item.has() ? ChatColor.GREEN : ChatColor.RED) + item.getPermission());
	}
}
