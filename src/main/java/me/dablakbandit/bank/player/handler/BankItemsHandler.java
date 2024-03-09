package me.dablakbandit.bank.player.handler;

import me.dablakbandit.bank.config.BankPermissionConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.bank.player.info.BankItemsInfo;
import me.dablakbandit.bank.player.info.item.BankItem;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.ItemUtils;
import me.dablakbandit.core.utils.Version;
import me.dablakbandit.core.utils.itemutils.IItemUtils;
import me.dablakbandit.core.vault.Eco;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BankItemsHandler {

    private static final IItemUtils itemUtils = ItemUtils.getInstance();
    private final BankItemsInfo bankItemsInfo;

    private int buySlots, buyTabs;

    public BankItemsHandler(BankItemsInfo bankItemsInfo) {
        this.bankItemsInfo = bankItemsInfo;
    }

    public void addInventoryToBank(Player player, boolean force) {
        addToBank(player, 9, 36, force);
    }

    public void addAllInventoryToBank(Player player, boolean force) {
        addToBank(player, 0, 36, force);
        addOffhandToBank(player, force);
    }

    public void addHotbarToBank(Player player, boolean force) {
        addToBank(player, 0, 9, force);
        addOffhandToBank(player, force);
    }

    public void addOffhandToBank(Player player, boolean force) {
        if (!Version.isAtleastNine()) {
            return;
        }
        ItemStack is = player.getInventory().getItemInOffHand();
        if (isEmpty(is)) {
            return;
        }
        player.getInventory().setItemInOffHand(addBankItem(player, is, force));
    }

    protected void addToBank(Player player, int x, int z, boolean force) {
        Inventory inv = player.getInventory();
        for (int i = x; i < z; i++) {
            ItemStack is = inv.getItem(i);
            if (isEmpty(is)) {
                continue;
            }
            is = addBankItem(player, is, force);
            inv.setItem(i, is);
            if (is != null) {
                return;
            }
        }
    }

    public ItemStack addBankItem(Player player, ItemStack is, boolean force) {
        return addBankItem(player, is, bankItemsInfo.getOpenTab(), force);
    }

    public ItemStack addBankItem(Player player, ItemStack is, int tab, boolean force) {
        if (!BankPermissionConfiguration.PERMISSION_ITEMS_DEPOSIT.has(player)) {
            return is;
        }
        if (isEmpty(is)) {
            return is;
        }
        if (ItemBlacklistImplementation.getInstance().isBlacklisted(is)) {
            // pl.getPlayer().sendMessage(LanguageConfiguration.MESSAGE_ITEM_IS_BLACKLISTED.getMessage());
            return is;
        }
        if (!force && tab > bankItemsInfo.getTotalTabCount()) {
            return is;
        }
        int itemSize = is.getAmount();
        is = mergeBank(is, tab);
        if (isEmpty(is)) {
            bankItemsInfo.save(BankPluginConfiguration.BANK_SAVE_ITEM_DEPOSIT);
            return null;
        }
        int size = getTabSize(bankItemsInfo.getOpenTab());
        if (force || size < BankPluginConfiguration.BANK_ITEMS_TABS_SIZE_MAX.get()) {
            if (force || getTotalBankSize(tab) < getBankSlots(tab)) {
                getTab(tab).add(new BankItem(is));
                bankItemsInfo.save(BankPluginConfiguration.BANK_SAVE_ITEM_DEPOSIT);
                return null;
            }
        }
        if (is.getAmount() != itemSize) {
            bankItemsInfo.save(BankPluginConfiguration.BANK_SAVE_ITEM_DEPOSIT);
        }
        // player.sendMessage(LanguageConfiguration.MESSAGE_BANK_IS_FULL.getMessage());
        return is;
    }

    protected ItemStack mergeBank(ItemStack is, int tab) {
        int add = is.getAmount();

        for (BankItem bankItem : getTab(tab)) {
            if (!canMerge(is, bankItem)) {
                continue;
            }
            int max = bankItem.getMax(bankItemsInfo.getPermissionMergeMax());
            int maxAdd = max - bankItem.getAmount();
            if (maxAdd > add) {
                bankItem.setAmount(bankItem.getAmount() + add);
                return null;
            } else {
                add -= maxAdd;
                bankItem.setAmount(max);
            }
        }
        is.setAmount(add);
        return is;
    }

    public boolean canMerge(ItemStack from, BankItem to) {
        if (from == null || to == null) {
            return false;
        }
        if (!itemUtils.canMerge(from, to.getItemStack())) {
            return false;
        }
        int max = to.getMax(bankItemsInfo.getPermissionMergeMax());
        return to.getAmount() < max;
    }

    public boolean canMerge(BankItem from, ItemStack to) {
        if (from == null || to == null) {
            return false;
        }
        if (!itemUtils.canMerge(from.getItemStack(), to)) {
            return false;
        }
        int max = to.getMaxStackSize();
        return to.getAmount() < max;
    }

    private boolean isEmpty(ItemStack is) {
        return is == null || is.getType() == Material.AIR;
    }

    public int getTotalBankSize(int tab) {
        if (BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_PER_TAB.get()) {
            return getTab(tab).size();
        }
        return bankItemsInfo.getBankItems().stream().map(List::size).mapToInt(Integer::intValue).sum();
    }

    public int getTabSize(int tab) {
        return getTab(tab).size();
    }

    public int getBoughtSlots(int tab) {
        return BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_PER_TAB.get() ? bankItemsInfo.getBoughtSlotsMap().getOrDefault(tab, 0) : bankItemsInfo.getBoughtSlotsMap().getOrDefault(1, 0);
    }

    public int getBankSlots(int tab) {
        return BankPluginConfiguration.BANK_ITEMS_SLOTS_DEFAULT.get() + getBoughtSlots(tab) + bankItemsInfo.getCommandSlots() + bankItemsInfo.getPermissionSlots();
    }

    public int getBankSlots() {
        return getBankSlots(bankItemsInfo.getOpenTab());
    }

    public int getTotalSlots() {
        if (BankPluginConfiguration.BANK_ITEMS_TABS_ENABLED.get() && BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_PER_TAB.get()) {
            int t = 0;
            for (int i = 1; i <= 9; i++) {
                t += getBankSlots(i);
            }
            return t;
        } else {
            return getBankSlots(1);
        }
    }

    public int getTotalUsedSlots() {
        if (BankPluginConfiguration.BANK_ITEMS_TABS_ENABLED.get()) {
            int t = 0;
            for (int tab = 1; tab <= 9; tab++) {
                t += getTab(tab).size();
            }
            return t;
        } else {
            return getTab(1).size();
        }
    }

    public BankItem getBankItemAtSlot(int slot, int tab) {
        List<BankItem> listItems = getTab(tab);
        if (slot >= listItems.size()) {
            return null;
        }
        return listItems.get(slot);
    }

    public List<BankItem> getTab(int tab) {
        return bankItemsInfo.getTabBankItems(tab);
    }

    private void removeBankItemAtInt(int slot, int tab) {
        List<BankItem> listItems = getTab(tab);
        listItems.remove(slot);
    }

    public void removeBankToInventory(Player player) {
        boolean taken = removeItemTo(player, 9, 36);
        if (taken) {
            bankItemsInfo.save(BankPluginConfiguration.BANK_SAVE_ITEM_WITHDRAW);
        }
    }

    public void removeAllBankToInventory(Player player) {
        boolean taken = removeItemTo(player, 0, 36);
        taken |= removeBankToOffhand(player);
        if (taken) {
            bankItemsInfo.save(BankPluginConfiguration.BANK_SAVE_ITEM_WITHDRAW);
        }
    }

    public void removeBankToHotbar(Player player) {
        boolean taken = false;
        taken |= removeItemTo(player, 0, 9);
        taken |= removeBankToOffhand(player);
        if (taken) {
            bankItemsInfo.save(BankPluginConfiguration.BANK_SAVE_ITEM_WITHDRAW);
        }
    }

    protected boolean removeItemTo(BankItem bankItem, Supplier<ItemStack> supplier, Consumer<ItemStack> consumer) {
        ItemStack item = supplier.get();
        if (isEmpty(item)) {
            ItemStack cloned = bankItem.getItemStack().clone();
            int taken = Math.min(bankItem.getAmount(), cloned.getMaxStackSize());
            cloned.setAmount(taken);
            bankItem.setAmount(bankItem.getAmount() - taken);
            consumer.accept(cloned);
            return true;
        }
        if (canMerge(bankItem, item)) {
            int size = bankItem.getAmount();
            int max = item.getMaxStackSize();
            int maxAdd = max - item.getAmount();
            if (maxAdd > size) {
                item.setAmount(item.getAmount() + size);
                bankItem.setAmount(0);
                return true;
            } else {
                size -= maxAdd;
                item.setAmount(max);
                bankItem.setAmount(size);
                return true;
            }
        }
        return false;
    }

    protected boolean removeItemTo(Player player, int x, int z) {
        Inventory inv = player.getInventory();
        List<BankItem> items = getTab(bankItemsInfo.getOpenTab());
        Iterator<BankItem> iterator = items.iterator();
        boolean taken = false;
        while (iterator.hasNext()) {
            BankItem item = iterator.next();
            for (int i = x; i < z; i++) {
                int finalI = i;
                if (removeItemTo(item, () -> inv.getItem(finalI), is -> inv.setItem(finalI, is))) {
                    taken = true;
                    if (item.getAmount() == 0) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        return taken;
    }

    private boolean removeBankToOffhand(Player player) {
        if (!Version.isAtleastNine()) {
            return false;
        }
        List<BankItem> items = getTab(bankItemsInfo.getOpenTab());
        Iterator<BankItem> it = items.iterator();
        boolean taken = false;
        while (it.hasNext()) {
            BankItem is = it.next();
            if (removeItemTo(is, player.getInventory()::getItemInOffHand, player.getInventory()::setItemInOffHand)) {
                taken = true;
                if (is.getAmount() == 0) {
                    it.remove();
                }
            }
        }
        return taken;
    }

    private void mergeIntoInventory(Player player, BankItem merge) {
        if (merge == null) {
            return;
        }
        Inventory inv = player.getInventory();
        for (int pSlot = 0; pSlot < 36; pSlot++) {
            int finalPSlot = pSlot;
            removeItemTo(merge, () -> inv.getItem(finalPSlot), is -> inv.setItem(finalPSlot, is));
            if (merge.getAmount() == 0) {
                break;
            }
        }
    }

    private boolean takeBankItem(Player player, int tab, int slot, int take) {
        if (!BankPermissionConfiguration.PERMISSION_ITEMS_WITHDRAW.has(player)) {
            return false;
        }
        BankItem is = getBankItemAtSlot(slot, tab);
        if (is == null) {
            return false;
        }
        int original = is.getAmount();
        int toTake = Math.min(is.getAmount(), take);
        int left = is.getAmount() - toTake;

        BankItem give = new BankItem(is.getItemStack(), toTake);
        mergeIntoInventory(player, give);
        left += give.getAmount();

        if (left <= 0) {
            removeBankItemAtInt(slot, tab);
            return true;
        } else {
            is.setAmount(left);
            return left != original;
        }
    }

    public boolean takeBankItemAt(Player player, int tab, int slot, int amount) {
        boolean taken = takeBankItem(player, tab, slot, amount);
        if (taken) {
            bankItemsInfo.save(BankPluginConfiguration.BANK_SAVE_ITEM_WITHDRAW);
        }
        return taken;
    }

    public void sort(Comparator<BankItem> comparator) {
        sort(bankItemsInfo.getOpenTab(), comparator);
    }

    public void sort(int tab, Comparator<BankItem> comparator) {
        List<BankItem> list = getTab(tab);
        list.sort(comparator);
    }

    public long countTotal(Material material, Integer damage, Integer model) {
        return bankItemsInfo.getBankItems().stream().flatMap(List::stream).filter(item -> item.compares(material, damage, model)).mapToLong(BankItem::getAmount).sum();
    }

    @SuppressWarnings("deprecation")
    public boolean buySlots(CorePlayers pl, int slots) {
        if (slots == 0) {
            return false;
        }
        double d = slots * BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_COST.get();
        if (Eco.getInstance().getEconomy().has(pl.getName(), d) && Eco.getInstance().getEconomy().withdrawPlayer(pl.getName(), d).transactionSuccess()) {
            // player.sendMessage(LanguageConfiguration.MESSAGE_SLOTS_BOUGHT.getMessage().replace("<i>", "" + buy_slot_amount).replace("<p>", Format.formatMoney(d)));
            int slot = BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_PER_TAB.get() ? bankItemsInfo.getOpenTab() : 1;
            bankItemsInfo.getBoughtSlotsMap().put(slot, getBoughtSlots(slot) + slots);
            return true;
        } else {
            // player.sendMessage(LanguageConfiguration.MESSAGE_SLOTS_FAILED.getMessage());
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean buyTabs(CorePlayers pl) {
        if (buyTabs == 0) {
            return false;
        }
        double cost = buyTabs * BankPluginConfiguration.BANK_ITEMS_TABS_BUY_COST.get();
        if (Eco.getInstance().getEconomy().has(pl.getName(), cost) && Eco.getInstance().getEconomy().withdrawPlayer(pl.getName(), cost).transactionSuccess()) {
            // player.sendMessage(LanguageConfiguration.MESSAGE_SLOTS_BOUGHT.getMessage().replace("<i>", "" + buy_slot_amount).replace("<p>", Format.formatMoney(d)));
            bankItemsInfo.setBoughtTabs(bankItemsInfo.getBoughtTabs() + buyTabs);
            buyTabs = 0;
            if (bankItemsInfo.getPlayers().getPlayer() != null) {
                bankItemsInfo.checkPermissions(bankItemsInfo.getPlayers().getPlayer(), false);
            }
            return true;
        } else {
            // player.sendMessage(LanguageConfiguration.MESSAGE_SLOTS_FAILED.getMessage());
        }
        return false;
    }

    public void incrementBuySlots() {
        if (getBoughtSlots(bankItemsInfo.getOpenTab()) + buySlots < BankPluginConfiguration.BANK_ITEMS_SLOTS_BUY_MAX.get()) {
            this.buySlots++;
        }
    }

    public void decrementBuySlots() {
        this.buySlots = Math.max(0, this.buySlots - 1);
    }

    public int getBuySlots() {
        return buySlots;
    }


    public void resetBuySlots() {
        this.buySlots = 0;
    }

    public int getBuyTabs() {
        return buyTabs;
    }

    public void incrementBuyTabs() {
        if (bankItemsInfo.getBoughtTabs() + buyTabs < BankPluginConfiguration.BANK_ITEMS_TABS_BUY_MAX.get()) {
            this.buyTabs++;
        }
    }

    public void decrementBuyTabs() {
        this.buyTabs = Math.max(0, this.buyTabs - 1);
    }
}
