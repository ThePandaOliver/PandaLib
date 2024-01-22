package me.pandamods.pandalib.config.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
	String modId();
	String name();
	String parentDirectory() default "";
	boolean synchronize() default false;
}
