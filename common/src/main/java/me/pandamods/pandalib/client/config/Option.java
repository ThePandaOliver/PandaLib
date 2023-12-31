package me.pandamods.pandalib.client.config;

import java.lang.reflect.Field;

public class Option<T> {
	private final Field field;
	private final Object originClass;

	public Option(Field field, Object originClass) {
		this.field = field;
		this.originClass = originClass;
	}

	public String getName() {
		return this.field.getName();
	}

	@SuppressWarnings("unchecked")
	public T get() {
		try {
			return (T) field.get(originClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void set(T t) {
		try {
			field.set(originClass, t);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
