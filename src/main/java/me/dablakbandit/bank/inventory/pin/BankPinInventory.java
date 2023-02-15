package me.dablakbandit.bank.inventory.pin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.players.CorePlayers;

public abstract class BankPinInventory extends BankInventoryHandler<BankInfo>{
	
	protected static int[]					pins;
	protected static final List<Integer>	pin_nums	= Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
	protected static final List<BankItemPath> itemPaths = Arrays.asList(
			BankItemConfiguration.BANK_PIN_NUMBER_ONE,
			BankItemConfiguration.BANK_PIN_NUMBER_TWO,
			BankItemConfiguration.BANK_PIN_NUMBER_THREE,
			BankItemConfiguration.BANK_PIN_NUMBER_FOUR,
			BankItemConfiguration.BANK_PIN_NUMBER_FIVE,
			BankItemConfiguration.BANK_PIN_NUMBER_SIX,
			BankItemConfiguration.BANK_PIN_NUMBER_SEVEN,
			BankItemConfiguration.BANK_PIN_NUMBER_EIGHT,
			BankItemConfiguration.BANK_PIN_NUMBER_NINE
	);

	protected static final List<BankItemPath> progressPaths = Arrays.asList(
			BankItemConfiguration.BANK_PIN_PROGRESS_SLOT_1,
			BankItemConfiguration.BANK_PIN_PROGRESS_SLOT_2,
			BankItemConfiguration.BANK_PIN_PROGRESS_SLOT_3,
			BankItemConfiguration.BANK_PIN_PROGRESS_SLOT_4
	);

	@Override
	public void init(){
		int size = descriptor.getSize();
		setAll(size, BankItemConfiguration.BANK_ITEM_BLANK);


		for (int i = 0; i < progressPaths.size(); i++) {
			int finalI = i;
			setItem(progressPaths.get(i).getSlot(), (bi) -> getProgress(bi, finalI));
		}

		pins = itemPaths.stream().map(path -> path.getSlot()).mapToInt(Integer::intValue).toArray();
		Arrays.stream(pins).forEach(pin -> setItem(pin, BankItemConfiguration.BANK_ITEM_BLANK, this::onClick));
		setItem(BankItemConfiguration.BANK_PIN_CLEAR, this::clear);
		setItem(BankItemConfiguration.BANK_PIN_NUMBER_ZERO, this::getZero, this::onClick);
		
	}
	
	public ItemStack getProgress(BankInfo info, int set){
		int current = info.getPinInfo().getTempPin().length();
		BankItemPath path = set < current ? BankItemConfiguration.BANK_PIN_PROGRESS_FINISHED : progressPaths.get(set);
		return clone(path.get(), path.getName().replaceAll("<length>", "" + current));
	}
	
	public ItemStack getZero(BankItemPath path){
		return path.get().clone();
	}
	
	public ItemStack getNumber(int i){
		BankItemPath path = itemPaths.get(i-1);
		return replaceNameLore(path, "<number>", "" + i);
	}
	
	public void clear(CorePlayers pl, BankInfo bankInfo){
		BankSoundConfiguration.INVENTORY_PIN_CLEAR.play(pl);
		bankInfo.getPinInfo().clearTempPin();
		pl.refreshInventory();
	}
	
	public abstract void onClick(CorePlayers pl, BankInfo bankInfo, Inventory inventory, InventoryClickEvent event);
	
	@Override
	public void set(CorePlayers pl, Player player, Inventory inv){
		super.set(pl, player, inv);
		List<Integer> nums = new ArrayList<>(pin_nums);
		Collections.shuffle(nums);
		for(int index = 0; index < 9; index++){
			int number = nums.get(index);
			ItemStack is = getNumber(number);
			is.setAmount(number);
			inv.setItem(pins[index], is);
		}
	}
	
	@Override
	public BankInfo getInvoker(CorePlayers pl){
		return pl.getInfo(BankInfo.class);
	}
	
	@Override
	public boolean open(CorePlayers pl, Player player){
		getInvoker(pl).getPinInfo().clearTempPin();
		return super.open(pl, player);
	}
	
	public int getClick(InventoryClickEvent event){
		return event.getRawSlot() == BankItemConfiguration.BANK_PIN_NUMBER_ZERO.getSlot() ? 0 : event.getCurrentItem().getAmount();
	}
	
}
