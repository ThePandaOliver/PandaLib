/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import net.minecraft.network.protocol.common.custom.CustomPacketPayload

fun interface PacketHandler<T : CustomPacketPayload> {
	fun handle(payload: T)
}