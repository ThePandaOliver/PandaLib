/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.`object`

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import java.util.UUID

class SyncableConfigObject<T>(
	resourceLocation: ResourceLocation,
	configClass: Class<T>
) : ConfigObject<T>(resourceLocation, configClass) {
	private val instances: MutableMap<UUID, T> = mutableMapOf()

	operator fun get(ownerUUID: UUID): T {
		return instances[ownerUUID] ?: throw IllegalStateException("ConfigObject instance not set for $resourceLocation and owner $ownerUUID")
	}

	operator fun get(player: Player): T {
		return get(player.uuid)
	}
}