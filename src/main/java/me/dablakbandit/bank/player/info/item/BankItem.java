package me.dablakbandit.bank.player.info.item;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.core.utils.NMSUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BankItem {

    private static boolean hasCustomModelData = false;

    static {
        try {
            hasCustomModelData = NMSUtils.getMethod(ItemMeta.class, "getCustomModelData") != null;
        } catch (Exception e) {

        }
    }

    private final ItemStack itemStack;
    private int amount;

    public BankItem(ItemStack itemStack) {
        this(itemStack, itemStack.getAmount());
        itemStack.setAmount(1);
    }

    public BankItem(ItemStack itemStack, int amount) {
        this.itemStack = itemStack;
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean compares(Material material, Integer damage, Integer modelData) {
        if (itemStack.getType() != material) {
            return false;
        }
        if (damage != null && itemStack.getDurability() != damage) {
            return false;
        }
        if (modelData != null && hasCustomModelData) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null || !meta.hasCustomModelData() || modelData != meta.getCustomModelData()) {
                return false;
            }
        }
        return true;
    }


    public int getMax(int max) {
        return BankPluginConfiguration.BANK_ITEMS_SLOTS_MERGE_ENABLED.get() ? max : Math.max(itemStack.getMaxStackSize(), amount);
    }
}
