/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.utils

enum class Environment {
	CLIENT,
	DEDICATED_SERVER;

	val isClient get() = this == CLIENT
	val isDedicatedServer get() = this == DEDICATED_SERVER
	
	val opposite get() = if (this == CLIENT) DEDICATED_SERVER else CLIENT
}