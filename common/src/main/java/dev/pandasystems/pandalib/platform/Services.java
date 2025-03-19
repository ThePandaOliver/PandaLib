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

package dev.pandasystems.pandalib.platform;

import com.mojang.logging.LogUtils;
import dev.pandasystems.pandalib.platform.services.GameHelper;
import dev.pandasystems.pandalib.platform.services.ModLoaderHelper;
import dev.pandasystems.pandalib.platform.services.NetworkHelper;
import dev.pandasystems.pandalib.platform.services.RegistrationHelper;
import org.slf4j.Logger;

import java.util.ServiceLoader;

@SuppressWarnings("unused")
public class Services {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final NetworkHelper NETWORK = load(NetworkHelper.class);
	public static final RegistrationHelper REGISTRATION = load(RegistrationHelper.class);
	public static final GameHelper GAME = load(GameHelper.class);
	public static final ModLoaderHelper MOD_LOADER = load(ModLoaderHelper.class);

	private static <T> T load(Class<T> serviceClass) {
		final T loadedService = ServiceLoader.load(serviceClass).findFirst().orElseThrow(() ->
				new NullPointerException("Failed to load service for " + serviceClass.getName()));
		LOGGER.debug("Loaded {} for service {}", loadedService, serviceClass);
		return loadedService;
	}
}
