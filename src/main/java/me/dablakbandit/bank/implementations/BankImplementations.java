package me.dablakbandit.bank.implementations;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.implementations.blacklist.ItemBlacklistImplementation;
import me.dablakbandit.bank.implementations.citizens.CitizensType;
import me.dablakbandit.bank.implementations.headdb.HeadDBImplementation;
import me.dablakbandit.bank.implementations.lock.LockType;
import me.dablakbandit.bank.implementations.other.BlockType;
import me.dablakbandit.bank.implementations.other.SignType;
import me.dablakbandit.bank.implementations.placeholder.MVDWPlaceholderImplementation;
import me.dablakbandit.bank.implementations.placeholder.PlaceholderAPIImplementation;
import me.dablakbandit.bank.implementations.skript.SkriptImplementation;
import me.dablakbandit.bank.implementations.vault.VaultImplementation;
import me.dablakbandit.core.config.path.BooleanPath;
import me.dablakbandit.core.utils.NMSUtils;

public enum BankImplementations{
	//@formatter:off
	LOCK(LockType.class, BankPluginConfiguration.BANK_SAVE_LOCK_TIME),
	CITIZENS(CitizensType.class, BankPluginConfiguration.BANK_TYPE_CITIZENS_ENABLED),
	BLOCK(BlockType.class, BankPluginConfiguration.BANK_TYPE_BLOCK_ENABLED),
	SIGN(SignType.class, BankPluginConfiguration.BANK_TYPE_SIGN_ENABLED),
	SKRIPT(SkriptImplementation.class, BankPluginConfiguration.BANK_IMPLEMENTATION_SKRIPT_ENABLED),
	VAULT_OVERRIDE(VaultImplementation.class, BankPluginConfiguration.BANK_IMPLEMENTATION_VAULT_OVERRIDE),
	MVDW_PLACEHOLDER(MVDWPlaceholderImplementation.class, BankPluginConfiguration.BANK_IMPLEMENTATION_PLACEHOLDER_MVDW),
	PLACEHOLDER_API(PlaceholderAPIImplementation.class, BankPluginConfiguration.BANK_IMPLEMENTATION_PLACEHOLDER_API),
	ITEM_BLACKLIST(ItemBlacklistImplementation.class, BankPluginConfiguration.BANK_ITEMS_BLACKLIST_ENABLED),
	HEADDB(HeadDBImplementation.class, null),
	
    //@formatter:on
	;
	
	private BankImplementation							object;
	private final Class<? extends BankImplementation>	supplier;
	private final Supplier<Boolean>						booleanPath;
	private boolean										loaded;
	
	BankImplementations(Class<? extends BankImplementation> supplier, BooleanPath booleanPath){
		this.supplier = supplier;
		this.booleanPath = booleanPath;
	}
	
	public void load(){
		if(booleanPath != null && !booleanPath.get()){ return; }
		try{
			object = (BankImplementation)NMSUtils.getMethod(supplier, "getInstance").invoke(null);
		}catch(IllegalAccessException | InvocationTargetException e){
			e.printStackTrace();
		}
		object.load();
		loaded = true;
	}
	
	public void enable(){
		if(!loaded){ return; }
		object.enable();
	}
	
	public void disable(){
		if(!loaded){ return; }
		object.disable();
		loaded = false;
	}
	
}
