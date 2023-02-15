package me.dablakbandit.bank.config.path;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BankItemBase{
	//@formatter:off
	String[] value() default {
		"This config file handles all gui items for this plugin allowing all items to be dynamically changed",
		"",
		"~Item node parameters~",
		"Required: Material, Durability, Amount",
		"Additional: Name, Lore, Unbreakable",
		"~Special~", 
		"Slot: <slot> Set the slot in the inventory, can be used in some cases to specify length, if default -1 then changing will do nothing.",
		"Slots: Duplicate this item to additional slots. Integer list.",
		"HeadURL: <url> / Head url from minecraft player skin, requires Item.Material to be PLAYER_HEAD or PLAYER_SKULL (Premium)\"",
		"HeadDB: <headdb-id> / HeadDB identifier",
		"PlayerHead: <true/false> If this should be the players own head.",
		"Enchant: <enchant_name>",	
		"CustomModelData: <integer> (1.14+ only!)", 
		"",
		"~Example with all~",
		"Blank:",
		"  Material: BLACK_STAINED_GLASS_PANE",
		"  Durability: 0",
		"  Amount: 1",
		"  Slot: -1",
		"  Name: 'Example name'",
		"  Lore:",
		"  - '&cExample lore'",
		"  - '&aExample second lore'",
		"  Unbreakable: true",
		"  Slots:",
		"  - 2",
		"  - 3",
		"  HeadURL: 'http://textures.minecraft.net/texture/1998e3668a6ca62eb75f36fa4bb7f5ecdb26b170aacd3377db53f62f188b38'",
		"  HeadDB: 'burger'",
		"  Enchant: 'THORNS'",
		"  CustomModelData: 69",
		"",
	};
	//@formatter:on
}
