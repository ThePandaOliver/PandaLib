package dev.pandasystems.pandalib.impl.platform

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.impl.platform.services.GameHelper
import dev.pandasystems.pandalib.impl.platform.services.ModLoaderHelper
import dev.pandasystems.pandalib.impl.platform.services.NetworkHelper
import dev.pandasystems.pandalib.impl.platform.services.RegistrationHelper
import org.slf4j.Logger
import java.util.*

@Suppress("unused")
object Services {
	private val LOGGER: Logger = LogUtils.getLogger()

	@JvmField
	val NETWORK: NetworkHelper = load(NetworkHelper::class.java)

	@JvmField
	val REGISTRATION: RegistrationHelper = load(RegistrationHelper::class.java)

	@JvmField
	val GAME: GameHelper = load(GameHelper::class.java)

	@JvmField
	val MOD_LOADER: ModLoaderHelper = load(ModLoaderHelper::class.java)

	private fun <T> load(serviceClass: Class<T>): T {
		val loadedService = ServiceLoader.load(serviceClass).findFirst()
			.orElseThrow { NullPointerException("Failed to load service for " + serviceClass.getName()) }
		LOGGER.debug("Loaded {} for service {}", loadedService, serviceClass)
		return loadedService
	}
}
