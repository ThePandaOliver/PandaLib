/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.platform

interface ModLoaderHelper {
	fun isModLoaded(modId: String): Boolean
	fun getMod(modId: String): Mod?
	val mods: List<Mod>
	val modIds: List<String>

	interface Mod {
		val id: String
		val displayName: String
		val description: String
		val authors: List<String>
		val version: String
	}
}
