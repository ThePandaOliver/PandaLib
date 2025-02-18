/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.utils;

import java.util.Collection;

@SuppressWarnings("unused")
public class CollectionsUtils {
	public static int findIndexOf(Collection<?> collection, Object object) {
		for (int i = 0; i < collection.size(); i++) {
			if (collection.toArray()[i].equals(object)) return i;
		}
		return -1;
	}

	public static <T> T getEntryByIndex(Collection<T> collection, int index) {
		int i = 0;
		for (T t : collection) {
			if (i == index) return t;
			i++;
		}
		return null;
	}
}
