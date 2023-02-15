package me.dablakbandit.bank.inventory.head;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.players.CorePlayers;
import me.dablakbandit.core.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerHead {

    private static PlayerHead instance = new PlayerHead();
    public static PlayerHead getInstance() {
        return instance;
    }
    
    private PlayerHead(){
        
    }

    private Material headMaterial;
    private Object namespaceKey;

    private void setup(){
        if(headMaterial == null){
            headMaterial = ItemUtils.getInstance().getMaterial("SKULL_ITEM", "PLAYER_SKULL", "PLAYER_HEAD");
        }
        try{
            namespaceKey = new NamespacedKey(BankPlugin.getInstance(), "player-head");
        }catch (Exception e){
            return;
        }
    }

    public void set(ItemStack itemStack){
        setup();
        if(namespaceKey == null){
            return;
        }

        if(itemStack != null && itemStack.hasItemMeta()){
            itemStack.setType(headMaterial);
            ItemMeta itemMeta = itemStack.getItemMeta();
            try{
                PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
                persistentDataContainer.set(((NamespacedKey) namespaceKey), PersistentDataType.STRING, "true");
                itemStack.setItemMeta(itemMeta);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void replace(CorePlayers pl, ItemStack itemStack){
        if(namespaceKey == null){
            return;
        }
        checkKey(pl, itemStack);
    }

    private void checkKey(CorePlayers pl, ItemStack itemStack){
        if(itemStack != null && itemStack.getType() == headMaterial && itemStack.hasItemMeta()){
            ItemMeta itemMeta = itemStack.getItemMeta();
            try{
                PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
                if (persistentDataContainer.has(((NamespacedKey) namespaceKey), PersistentDataType.STRING)) {
                    String value = persistentDataContainer.get(((NamespacedKey) namespaceKey), PersistentDataType.STRING);
                    if(value.equalsIgnoreCase("true")){
                        SkullMeta skullMeta = (SkullMeta) itemMeta;
                        skullMeta.setOwningPlayer(pl.getPlayer());
                        itemStack.setItemMeta(skullMeta);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
