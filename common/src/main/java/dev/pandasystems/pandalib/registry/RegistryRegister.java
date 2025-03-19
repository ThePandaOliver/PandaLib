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

package dev.pandasystems.pandalib.registry;

import dev.pandasystems.pandalib.platform.Services;
import net.minecraft.core.Registry;

@SuppressWarnings("unused")
public class RegistryRegister<T> {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Registry<T> register(Registry<T> registry) {
		Services.REGISTRATION.registerNewRegistry(registry);
		return registry;
	}
}
