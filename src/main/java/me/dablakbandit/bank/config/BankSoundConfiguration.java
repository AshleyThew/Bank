package me.dablakbandit.bank.config;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.comment.CommentAdvancedConfiguration;
import me.dablakbandit.core.config.path.Path;
import me.dablakbandit.core.players.CorePlayers;

public class BankSoundConfiguration extends CommentAdvancedConfiguration{
	
	private static BankSoundConfiguration config = new BankSoundConfiguration(BankPlugin.getInstance(), "sounds.yml");
	
	public static BankSoundConfiguration getInstance(){
		return config;
	}
	
	private static Sound levelup, anvil;
	
	static{
		try{
			levelup = Enum.valueOf(Sound.class, "ENTITY_PLAYER_LEVELUP");
		}catch(Exception e){
			levelup = Enum.valueOf(Sound.class, "LEVEL_UP");
		}
		
		try{
			anvil = Enum.valueOf(Sound.class, "BLOCK_ANVIL_LAND");
		}catch(Exception e){
			anvil = Enum.valueOf(Sound.class, "ANVIL_LAND");
		}
	}
	
	public static SoundsPath	GLOBAL_ERROR							= new SoundsPath(anvil);
	
	public static SoundsPath	BLOCK_OPEN								= new SoundsPath(levelup);
	public static SoundsPath	CITIZENS_OPEN							= new SoundsPath(levelup);
	public static SoundsPath	SIGN_OPEN								= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_GLOBAL_BACK					= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_ITEMS_ADD_ALL					= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_ADD_HOTBAR				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_ADD_INVENTORY			= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_MONEY_WITHDRAW				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MONEY_WITHDRAW_ALL			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MONEY_DEPOSIT					= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MONEY_DEPOSIT_ALL				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MONEY_SEND_OTHER				= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_ITEMS_CHANGE_TAB				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_OPEN_TAB_ITEM_PICKER	= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_OPEN_BUY_SLOTS			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_OPEN_BUY_TABS			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_OPEN_ADD				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_OPEN_REMOVE				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_OPEN_TRASHCAN			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_OPEN_SORT				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_SCROLL_DOWN				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_SCROLL_UP				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_ITEM_ADD				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_ITEM_TAKE				= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_ITEMS_BUY_SLOTS_MINUS			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_BUY_SLOTS_BUY			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_BUY_SLOTS_ADD			= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_ITEMS_BUY_TABS_MINUS			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_BUY_TABS_BUY			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_BUY_TABS_ADD			= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_MENU_OPEN_PIN					= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MENU_OPEN_MONEY				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MENU_OPEN_ITEMS				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MENU_OPEN_EXP					= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MENU_OPEN_LOANS				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_MENU_OPEN_LOAN_VIEW			= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_EXP_WITHDRAW					= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_EXP_WITHDRAW_ALL				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_EXP_DEPOSIT					= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_EXP_DEPOSIT_ALL				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_EXP_SEND_OTHER				= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_LOANS_PAYBACK_ALL				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_LOANS_PAYBACK					= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_LOANS_OPEN_TAKE_OUT			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_LOANS_OPEN_VIEW				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_LOANS_TAKE_OUT				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_LOANS_VIEW_UP					= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_LOANS_VIEW_DOWN				= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_PIN_CLICK						= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_PIN_CLEAR						= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_PIN_SET						= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_PIN_REMOVE					= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_ITEMS_REMOVE_ALL				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_REMOVE_INVENTORY		= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_REMOVE_HOTBAR			= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_ITEMS_SORT_MATERIAL			= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_SORT_NAME				= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_SORT_AMOUNT				= new SoundsPath(levelup);
	
	public static SoundsPath	INVENTORY_ITEMS_TABITEMPICKER_RESET		= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_TABITEMPICKER_UP		= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_TABITEMPICKER_DOWN		= new SoundsPath(levelup);
	public static SoundsPath	INVENTORY_ITEMS_TABITEMPICKER_SELECT	= new SoundsPath(levelup);
	
	public static SoundsPath	MONEY_SEND_OTHER						= new SoundsPath(levelup);
	
	public static SoundsPath	EXP_SEND_OTHER							= new SoundsPath(levelup);
	
	private BankSoundConfiguration(JavaPlugin plugin, String file){
		super(plugin, file);
	}
	
	public static class SoundsPath extends Path<Sounds>{
		
		protected SoundsPath(Sound sound){
			super(new Sounds(sound));
		}
		
		protected SoundsPath(Sound sound, float volume, float pitch){
			super(new Sounds(sound, volume, pitch));
		}
		
		@Override
		protected Sounds get(RawConfiguration rawConfiguration, String s){
			Sounds sound = new Sounds(getDefault());
			sound.get(rawConfiguration, s);
			return sound;
		}
		
		@Override
		protected Object setAs(RawConfiguration config, Sounds sounds){
			String path = this.getActualPath();
			sounds.set(config, path);
			return null;
		}
		
		public void play(CorePlayers pl){
			get().play(pl);
		}
		
		public void play(Player player){
			get().play(player);
		}
	}
	
	public static class Sounds{
		
		private boolean	enabled	= true;
		private Sound	sound;
		private float	volume	= 1f, pitch = 1f;
		
		private Sounds(Sounds def){
			this.enabled = def.enabled;
		}
		
		private Sounds(Sound sound){
			this.sound = sound;
		}
		
		private Sounds(Sound sound, float volume, float pitch){
			this.sound = sound;
			this.volume = volume;
			this.pitch = pitch;
		}
		
		private void set(FileConfiguration config, String path){
			config.set(path + ".Enabled", enabled);
			config.set(path + ".Sound", sound.name());
			config.set(path + ".Pitch", pitch);
			config.set(path + ".Volume", volume);
		}
		
		private void get(FileConfiguration config, String path){
			if(config.isSet(path + ".Enabled")){
				enabled = config.getBoolean(path + ".Enabled");
			}
			if(config.isSet(path + ".Sound")){
				try{
					sound = Sound.valueOf(config.getString(path + ".Sound").toUpperCase());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(config.isSet(path + ".Pitch")){
				pitch = (float)config.getDouble(path + ".Pitch");
			}
			if(config.isSet(path + ".Volume")){
				volume = (float)config.getDouble(path + ".Volume");
			}
		}
		
		public void play(CorePlayers pl){
			play(pl.getPlayer());
		}
		
		public void play(Player player){
			if(BankPluginConfiguration.BANK_SOUNDS_ENABLED.get() && enabled){
				player.playSound(player.getLocation(), sound, volume * BankPluginConfiguration.BANK_SOUNDS_VOLUME_MODIFIER.get().floatValue(), pitch);
			}
		}
	}
}
