package me.dablakbandit.bank.config.path;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(PathExtendedRepeat.class)
public @interface PathExtended {
	String key() default "";

	String value() default "";

	Class<?> classType();
}
