package dev.pandasystems.pandalib.forge.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.ModLoaderPlatform
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.ModList
import net.minecraftforge.forgespi.language.IModInfo
import kotlin.jvm.optionals.getOrNull

@AutoService(ModLoaderPlatform::class)
class ModLoaderImpl : ModLoaderPlatform {
	private val modMap = mutableMapOf<String, ModLoaderPlatform.Mod>()

	override fun isModLoaded(modId: String): Boolean {
		return ModList.get().isLoaded(modId)
	}

	override fun getMod(modId: String): ModLoaderPlatform.Mod? {
		if (modMap.containsKey(modId)) return modMap[modId]
		return ModList.get().getModContainerById(modId)
			.map { ModImpl(it) }.getOrNull()?.also {
				modMap[modId] = it
			}
	}

	override val mods by lazy {
		ModList.get().mods.forEach { getMod(it.modId) }
		modMap.values.toList()
	}

	override val modIds by lazy { ModList.get().mods.map { it.modId }.toList() }

	private class ModImpl(
		val container: ModContainer,
		val info: IModInfo = container.modInfo
	) : ModLoaderPlatform.Mod {
		override val id: String = info.modId

		override val displayName: String = info.displayName

		override val description: String = info.description

		override val authors: List<String> = info.config.getConfigElement<Any>("authors")
			.map { it.toString() }.map { listOf(it) }.orElseGet { emptyList<String>() }

		override val version: String = info.version.toString()
	}
}