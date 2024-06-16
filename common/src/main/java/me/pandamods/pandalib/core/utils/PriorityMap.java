/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.core.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PriorityMap<K, V> implements Map<K, V> {
	private final Map<Integer, Map<K, V>> maps;

	private final Supplier<Map<K, V>> mapSupplier;

	public PriorityMap() {
		this.mapSupplier = HashMap::new;
		this.maps = new HashMap<>();
	}

	@Override
	public int size() {
		return maps.values().stream().mapToInt(Map::size).sum();
	}

	@Override
	public boolean isEmpty() {
		return maps.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return maps.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return maps.containsValue(value);
	}

	@Override
	public V get(Object key) {
		List<Integer> priorities = new ArrayList<>(maps.keySet());
		priorities.sort(Comparator.reverseOrder());
		for (Integer priority : priorities) {
			Map<K, V> map = maps.get(priority);
			return map.get(key);
		}
		return null;
	}

	public V put(int priority, K key, V value) {
		if (!maps.containsKey(priority)) {
			maps.put(priority, mapSupplier.get());
		}
		Map<K, V> map = maps.get(priority);
		return map.put(key, value);
	}

	@Nullable
	@Override
	public V put(K key, V value) {
		return put(0, key, value);
	}

	public V remove(int priority, Object key) {
		V value = null;
		if (maps.containsKey(priority)) {
			value = maps.get(priority).remove(key);
			if (maps.get(priority).isEmpty())
				maps.remove(priority);
		}
		return value;
	}

	@Override
	public V remove(Object key) {
		return remove(0, key);
	}

	public void putAll(int priority, @NotNull Map<? extends K, ? extends V> m) {
		if (!maps.containsKey(priority)) {
			maps.put(priority, mapSupplier.get());
		}
		maps.get(priority).putAll(m);
	}

	@Override
	public void putAll(@NotNull Map<? extends K, ? extends V> m) {
		putAll(0, m);
	}

	@Override
	public void clear() {
		maps.clear();
	}

	@NotNull
	@Override
	public Set<K> keySet() {
		Set<K> keys = new HashSet<>();
		List<Integer> priorities = new ArrayList<>(maps.keySet());
		priorities.sort(Comparator.reverseOrder());
		for (Integer priority : priorities) {
			Map<K, V> map = maps.get(priority);
			keys.addAll(map.keySet());
		}
		return keys;
	}

	@NotNull
	@Override
	public Collection<V> values() {
		List<V> values = new ArrayList<>();
		List<Integer> priorities = new ArrayList<>(maps.keySet());
		priorities.sort(Comparator.reverseOrder());
		for (Integer priority : priorities) {
			Map<K, V> map = maps.get(priority);
			values.addAll(map.values());
		}
		return values;
	}

	@NotNull
	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> entries = new HashSet<>();
		List<Integer> priorities = new ArrayList<>(maps.keySet());
		priorities.sort(Comparator.reverseOrder());
		for (Integer priority : priorities) {
			Map<K, V> map = maps.get(priority);
			entries.addAll(map.entrySet());
		}
		return entries;
	}
}
