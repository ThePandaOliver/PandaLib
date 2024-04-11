package me.pandamods.pandalib.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ClassUtils {
	public static <T> T constructUnsafely(Class<T> cls) {
		try {
			Constructor<T> constructor = cls.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> void setFieldUnsafely(Object parentObject, Field field, T value) {
		try {
			field.set(parentObject, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFieldUnsafely(Object parentObject, Field field) {
		try {
			return (T) field.get(parentObject);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
