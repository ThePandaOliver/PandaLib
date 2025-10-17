/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.client

import dev.pandasystems.pandalib.client.initializePandaLibClient
import dev.pandasystems.pandalib.pandalibModid
import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@OptIn(InternalPandaLibApi::class)
@Mod(value = pandalibModid, dist = [Dist.CLIENT])
class PandaLibClientNeoForge(eventBus: IEventBus) {
	init {
		initializePandaLibClient()
	}
}
