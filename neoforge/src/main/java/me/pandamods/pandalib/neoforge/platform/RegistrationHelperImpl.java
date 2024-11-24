/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.neoforge.platform;

import me.pandamods.pandalib.platform.services.IRegistrationHelper;
import me.pandamods.pandalib.registries.DeferredObject;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;

public class RegistrationHelperImpl implements IRegistrationHelper {
	private static final List<DeferredObject<Object, Object>> entries = new ArrayList<>();

	@Override
	public <T> void register(DeferredObject<T, ? extends T> deferredObject) {
		entries.add((DeferredObject<Object, Object>) deferredObject);
	}

	public static void registerObjects(final RegisterEvent event) {
		entries.forEach(deferredObject -> {
			event.register(deferredObject.getRegistryKey(), deferredObject.getLocation(), deferredObject);
		});
	}
}
