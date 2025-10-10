/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.event.serverevents.blockBreakPostEvent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import org.slf4j.Logger

object PandaLib {
	const val MOD_ID = "pandalib"

	init {
		logger.debug("PandaLib is initializing...")

		pandalibConfig.load()

		ConfigSynchronizer.init()

		blockBreakPostEvent.register { level, pos, state, entity ->
			if (entity is Player) {
				println("Server value: ${pandalibConfig.get().debugging.syncedValue}")
				println("Player ${entity.name} value: ${pandalibConfig.get().debugging[entity.uuid]}")
			}
		}

		logger.debug("PandaLib initialized successfully.")
	}
}

val logger: Logger = LogUtils.getLogger()