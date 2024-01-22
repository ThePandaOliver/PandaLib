package me.pandamods.pandalib.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedHashMap<K, V> extends LinkedHashMap<K, V> {
	private static final int DEFAULT_CAPACITY = 10;
	private final int maxCapacity;

	public LimitedHashMap(int maxCapacity) {
		super(DEFAULT_CAPACITY, 0.75f, true);
		this.maxCapacity = maxCapacity;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > maxCapacity;
	}
}

