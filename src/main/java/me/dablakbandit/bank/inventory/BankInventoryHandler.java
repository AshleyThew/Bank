package me.dablakbandit.bank.inventory;

import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.implementations.placeholder.BankPlaceholderManager;
import me.dablakbandit.bank.inventory.head.PlayerHead;
import me.dablakbandit.bank.player.info.BankPermissionInfo;
import me.dablakbandit.bank.utils.format.BankColorUtil;
import me.dablakbandit.core.config.path.PermissionPath;
import me.dablakbandit.core.inventory.InventoryHandler;
import me.dablakbandit.core.inventory.handler.ItemInfoHandler;
import me.dablakbandit.core.inventory.handler.ItemInfoInventoryEventHandler;
import me.dablakbandit.core.inventory.handler.ItemInfoInventoryHandler;
import me.dablakbandit.core.players.CorePlayerManager;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BankInventoryHandler<T> extends InventoryHandler<T> {

	public void setItem(BankItemPath path, BiFunction<BankItemPath, T, ItemStack> supplier, ItemInfoInventoryEventHandler<T> handler) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path, t), handler));
	}

	public void setItem(BankItemPath path, BiFunction<BankItemPath, T, ItemStack> supplier, ItemInfoInventoryHandler<T> handler) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path, t), (a, b, c, d) -> handler.onClick(a, b, c)));
	}

	public void setItem(BankItemPath path, BiFunction<BankItemPath, T, ItemStack> supplier, ItemInfoHandler<T> handler) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path, t), (a, b, c, d) -> handler.onClick(a, b)));
	}

	public void setItem(BankItemPath path, BiFunction<BankItemPath, T, ItemStack> supplier, Consumer<CorePlayers> handler) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path, t), (a, b, c, d) -> handler.accept(a)));
	}

	public void setItem(BankItemPath path, Function<BankItemPath, ItemStack> supplier, ItemInfoInventoryEventHandler<T> handler) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path), handler));
	}

	public void setItem(BankItemPath path, Function<BankItemPath, ItemStack> supplier, ItemInfoInventoryHandler<T> handler) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path), (a, b, c, d) -> handler.onClick(a, b, c)));
	}

	public void setItem(BankItemPath path, Function<BankItemPath, ItemStack> supplier, ItemInfoHandler<T> handler) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path), (a, b, c, d) -> handler.onClick(a, b)));
	}

	public void setItem(BankItemPath path, Function<BankItemPath, ItemStack> supplier, Consumer<CorePlayers> handler) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path), (a, b, c, d) -> handler.accept(a)));
	}

	public void setItem(BankItemPath path, BiFunction<BankItemPath, T, ItemStack> supplier) {
		consumeSlots(path, (slot) -> setItem(slot, (t) -> supplier.apply(path, t)));
	}

	public void setItem(BankItemPath path, ItemInfoInventoryHandler<T> handler) {
		consumeSlots(path, (slot) -> setItem(slot, path, (a, b, c, d) -> handler.onClick(a, b, c)));
	}

	public void setItem(BankItemPath path, ItemInfoHandler<T> handler) {
		consumeSlots(path, (slot) -> setItem(slot, path, (a, b, c, d) -> handler.onClick(a, b)));
	}

	public void setItem(BankItemPath path, Consumer<CorePlayers> handler) {
		consumeSlots(path, (slot) -> setItem(slot, path, handler));
	}

	public void setItem(BankItemPath path, Runnable handler) {
		setItem(path, (pl) -> handler.run());
	}

	public void setItem(BankItemPath path) {
		consumeSlots(path, (slot) -> setItem(slot, path));
	}

	protected void consumeSlots(BankItemPath bankItemPath, Consumer<Integer> apply) {
		apply.accept(bankItemPath.getSlot());
		for (int slot : bankItemPath.getSlots()) {
			apply.accept(slot);
		}
	}


	public Consumer<CorePlayers> consumeSound(Consumer<CorePlayers> consumer, BankSoundConfiguration.SoundsPath sounds) {
		return (pl) -> {
			consumer.accept(pl);
			sounds.play(pl);
		};
	}

	public ItemInfoHandler<T> consumeSound(ItemInfoHandler<T> consumer, BankSoundConfiguration.SoundsPath sounds) {
		return (pl, t) -> {
			consumer.onClick(pl, t);
			sounds.play(pl);
		};
	}

	public ItemInfoHandler<T> consumePermissions(PermissionPath permissionPath, ItemInfoHandler<T> consumer) {
		return (pl, t) -> {
			if (permissionPath.has(pl.getPlayer())) {
				consumer.onClick(pl, t);
			}
		};
	}

	@Override
	public boolean hasPermission(Player player) {
		return !this.descriptor.hasPermission() || CorePlayerManager.getInstance().getPlayer(player).getInfo(BankPermissionInfo.class).checkPermission(this.descriptor.getPermission(), true);
	}

	protected static ItemStack replaceNameLore(BankItemPath bankItemPath, String... replacements) {
		return replaceCloneNameLore(bankItemPath.get(), bankItemPath.getName(), bankItemPath.getLore(), replacements);
	}


	@Override
	protected boolean open(CorePlayers pl, Player player, int size, String title) {
		return super.open(pl, player, size, BankColorUtil.hex(BankPlaceholderManager.getInstance().replace(pl, title)));
	}

	@Override
	public void set(CorePlayers pl, Player player, Inventory inventory) {
		super.set(pl, player, inventory);
		for (ItemStack itemStack : inventory.getContents()) {
			PlayerHead.getInstance().replace(pl, itemStack);
		}
	}
}
