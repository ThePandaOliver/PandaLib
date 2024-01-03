package me.pandamods.pandalib.client.config;

import java.lang.reflect.Field;

public class Option {
	private final Field field;
	private final Object originClass;

	public Option(Field field, Object originClass) {
		this.field = field;
		this.originClass = originClass;
	}

	public String getName() {
		return this.field.getName();
	}

	public Field getField() {
		return field;
	}

	public Object getOriginClass() {
		return originClass;
	}

	public Object get() {
		try {
			return field.get(originClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public int getAsInteger() {
		try {
			return field.getInt(originClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public float getAsFloat() {
		try {
			return field.getFloat(originClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean getAsBoolean() {
		try {
			return field.getBoolean(originClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public String getAsString() {
		try {
			return (String) field.get(originClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void set(Object object) {
		try {
			field.set(originClass, object);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean is(Class<?> instance) {
		return this.field.getType().isInstance(instance);
	}

	public boolean isInteger() {
		return is(Integer.class) || is(int.class);
	}

	public boolean isFloat() {
		return is(Float.class) || is(float.class);
	}

	public boolean isBoolean() {
		return is(Boolean.class) || is(boolean.class);
	}

	public boolean isString() {
		return is(String.class);
	}
}
