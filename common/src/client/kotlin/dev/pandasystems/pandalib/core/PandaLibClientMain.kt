package dev.pandasystems.pandalib.core

import dev.pandasystems.pandalib.core.lifecycles.ClientLifecycle

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
