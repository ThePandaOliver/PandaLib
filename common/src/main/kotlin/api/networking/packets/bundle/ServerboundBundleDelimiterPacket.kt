/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
package dev.pandasystems.pandalib.api.networking.packets.bundle

import net.minecraft.network.protocol.BundleDelimiterPacket
import net.minecraft.network.protocol.game.ServerGamePacketListener

class ServerboundPLBundleDelimiterPacket : BundleDelimiterPacket<ServerGamePacketListener>()

