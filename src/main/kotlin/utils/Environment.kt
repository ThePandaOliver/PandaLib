/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.utils

enum class Environment {
	CLIENT,
	SERVER;

	val isClient get() = this == CLIENT
	val isServer get() = this == SERVER
	
	val opposite get() = if (this == CLIENT) SERVER else CLIENT
}