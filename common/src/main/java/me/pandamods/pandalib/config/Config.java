package me.pandamods.pandalib.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
	String modId();
	String name();
	String parentDirectory() default "";
	ConfigType type() default ConfigType.COMMON;
	boolean synchronise() default false;

	@Retention(RetentionPolicy.RUNTIME)
	@Environment(EnvType.CLIENT)
	@interface Gui {
		@Retention(RetentionPolicy.RUNTIME)
		@interface Category {
			String value();
			boolean isObject() default false;
		}

		@Retention(RetentionPolicy.RUNTIME)
		@interface CollapsableObject { }
	}
}
