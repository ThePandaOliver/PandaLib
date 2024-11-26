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

package me.pandamods.pandalib.fabric.platform;

import me.pandamods.pandalib.platform.services.IRegistrationHelper;
import me.pandamods.pandalib.registry.DeferredObject;
import net.minecraft.core.Registry;

import java.util.function.Supplier;

public class RegistrationHelperImpl implements IRegistrationHelper {
	@Override
	@SuppressWarnings("unchecked")
	public <T> void register(DeferredObject<? extends T> deferredObject, Supplier<? extends T> supplier) {
		Registry.register((Registry<T>) deferredObject.getRegistry(), deferredObject.getId(), supplier.get());
	}
}
