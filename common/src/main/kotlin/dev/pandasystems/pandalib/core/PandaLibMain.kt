package dev.pandasystems.pandalib.core

import dev.pandasystems.pandalib.networking.NetworkSource

class PandaLibMain(
	val networkManager: NetworkSource
) {
	init {
		install(this)
	}

	companion object {
		lateinit var instance: PandaLibMain
			private set

		private fun install(main: PandaLibMain) {
			if (!::instance.isInitialized) {
				instance = main
			} else {
				logger.warn("""
					PandaLibMain instance already initialized.
					This is only intended for testing purposes.
				""".trimIndent())
			}
		}
	}
}