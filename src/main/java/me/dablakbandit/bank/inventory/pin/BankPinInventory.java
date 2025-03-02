package me.dablakbandit.bank.inventory.pin;

import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankSoundConfiguration;
import me.dablakbandit.bank.config.path.impl.BankItemPath;
import me.dablakbandit.bank.inventory.BankInventoryHandler;
import me.dablakbandit.bank.player.info.BankInfo;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class BankPinInventory extends BankInventoryHandler<BankInfo>{
	
	protected static Integer[]					pins;
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
	
	private final Map<String, Integer[]>		pinMap			= new HashMap<>();

	@Override
	public void init(){

		if (BankItemConfiguration.BANK_PIN_NUMBER_BLANK.getSlots().length > 0) {
			setItem(BankItemConfiguration.BANK_PIN_NUMBER_BLANK);
		} else {
			int size = descriptor.getSize();
			setAll(size, BankItemConfiguration.BANK_PIN_NUMBER_BLANK);
		}

		for (int i = 0; i < progressPaths.size(); i++) {
			int finalI = i;
			setItem(progressPaths.get(i).getSlot(), (bi) -> getProgress(bi, finalI));
		}

		pins = itemPaths.stream().mapToInt(BankItemPath::getSlot).boxed().toArray(Integer[]::new);
		Arrays.stream(pins).forEach(pin -> setItem(pin, BankItemConfiguration.BANK_ADMIN_ITEM_BLANK, this::onClick));
		setItem(BankItemConfiguration.BANK_PIN_CLEAR, this::clear);
		setItem(BankItemConfiguration.BANK_PIN_NUMBER_ZERO, this::getZero, this::onClick);
	}
	
	public ItemStack getProgress(BankInfo info, int set){
		int current = info.getPinInfo().getTempPin().length();
		BankItemPath path = set < current ? BankItemConfiguration.BANK_PIN_PROGRESS_FINISHED : progressPaths.get(set);
		return clone(path.get(), path.getName().replaceAll("<length>", String.valueOf(current)));
	}
	
	public ItemStack getZero(BankItemPath path){
		return path.get().clone();
	}
	
	public ItemStack getNumber(int i){
		BankItemPath path = itemPaths.get(i-1);
		return replaceNameLore(path, "<number>", String.valueOf(i));
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
		pinMap.put(pl.getUUIDString(), nums.toArray(new Integer[0]));
		for(int index = 0; index < 9; index++){
			int number = nums.get(index);
			ItemStack is = getNumber(number);
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
		if(event.getRawSlot() == BankItemConfiguration.BANK_PIN_NUMBER_ZERO.getSlot()){
			return 0;
		}else{
			Integer[] integers = pinMap.get(event.getWhoClicked().getUniqueId().toString());
			int index = Arrays.asList(pins).indexOf(event.getRawSlot());
			return integers[index];
		}
	}
	
	@Override
	public void onClose(CorePlayers pl, Player player){
		super.onClose(pl, player);
		pinMap.remove(pl.getUUIDString());
	}
}
