package dev.pandasystems.pandalib.fabric.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.ModLoaderPlatform
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata
import kotlin.jvm.optionals.getOrNull

@AutoService(ModLoaderPlatform::class)
class ModLoaderImpl : ModLoaderPlatform {
	private val modMap = mutableMapOf<String, ModLoaderPlatform.Mod>()

	override fun isModLoaded(modId: String): Boolean {
		return FabricLoader.getInstance().isModLoaded(modId)
	}

	override fun getMod(modId: String): ModLoaderPlatform.Mod? {
		if (modMap.containsKey(modId)) return modMap[modId]
		return FabricLoader.getInstance().getModContainer(modId)
			.map { ModImpl(it) }.getOrNull()?.also {
				modMap[modId] = it
			}
	}

	override val mods by lazy {
		FabricLoader.getInstance().allMods.forEach { getMod(it.metadata.id) }
		modMap.values.toList()
	}

	override val modIds by lazy { FabricLoader.getInstance().allMods.map { it.metadata.id } }

	private class ModImpl(
		container: ModContainer,
		metadata: ModMetadata = container.metadata
	) : ModLoaderPlatform.Mod {
		override val id: String = metadata.id

		override val displayName: String = metadata.name

		override val description: String = metadata.description

		override val authors: List<String> = metadata.authors.map { it.name }

		override val version: String = metadata.version.friendlyString
	}
}