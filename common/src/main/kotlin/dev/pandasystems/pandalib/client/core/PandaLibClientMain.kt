package dev.pandasystems.pandalib.client.core

import dev.pandasystems.pandalib.client.core.lifecycles.ClientLifecycle

class PandaLibClientMain {
	init {
		instance = this
		ClientLifecycle.initialize()
	}

	companion object {
		lateinit var instance: PandaLibClientMain
			private set
	}
}
