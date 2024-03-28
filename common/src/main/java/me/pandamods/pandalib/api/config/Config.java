package me.pandamods.pandalib.api.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
	String modId();
	String name();
	String parentDirectory() default "";
	boolean synchronize() default false;
}
