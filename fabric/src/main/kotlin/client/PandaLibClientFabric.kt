/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.client

import dev.pandasystems.pandalib.client.PandaLibClient
import net.fabricmc.api.ClientModInitializer

class PandaLibClientFabric : ClientModInitializer {
	override fun onInitializeClient() {
		PandaLibClient
	}
}
