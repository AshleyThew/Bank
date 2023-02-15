package me.dablakbandit.bank.config.path;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BankItemDelete{
	String[] value() default { "Slot", "Slots", "HeadURL", "HeadDB", "Name", "Lore", "Unbreakable", "Enchant", "CustomModelData", "Material", "Durability", "Amount" };
}
