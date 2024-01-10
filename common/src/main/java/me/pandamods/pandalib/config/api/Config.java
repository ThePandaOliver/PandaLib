package me.pandamods.pandalib.config.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
	String modId();
	String name();
	String parentDirectory() default "";
	ConfigType type() default ConfigType.COMMON;
	boolean synchronize() default false;
}
