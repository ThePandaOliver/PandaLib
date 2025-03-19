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

package dev.pandasystems.pandalib.platform.services;

import dev.pandasystems.pandalib.registry.DeferredObject;
import dev.pandasystems.pandalib.registry.IdentifiableResourceReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.server.packs.PackType;

import java.util.function.Supplier;

public interface RegistrationHelper {
	<T> void register(DeferredObject<? extends T> deferredObject, Supplier<? extends T> supplier);
	<T> void registerNewRegistry(Registry<T> registry);

	void registerReloadListener(PackType packType, IdentifiableResourceReloadListener listener);
}
