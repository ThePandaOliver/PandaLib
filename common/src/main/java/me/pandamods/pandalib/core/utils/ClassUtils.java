package me.pandamods.pandalib.core.utils;

import java.lang.reflect.Constructor;

public class ClassUtils {
	public static <V> V constructUnsafely(Class<V> cls) {
		try {
			Constructor<V> constructor = cls.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
}
