/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.client

import dev.pandasystems.pandalib.client.initializePandaLibClient
import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import net.fabricmc.api.ClientModInitializer

@OptIn(InternalPandaLibApi::class)
class PandaLibClientFabric : ClientModInitializer {
	override fun onInitializeClient() {
		initializePandaLibClient()
	}
}
