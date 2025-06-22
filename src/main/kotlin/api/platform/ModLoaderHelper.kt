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
