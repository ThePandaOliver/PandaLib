package dev.pandasystems.pandalib.core

import dev.pandasystems.pandalib.core.lifecycles.ClientLifecycle
import dev.pandasystems.pandalib.core.lifecycles.ServerLifecycle


class PandaLibMain {
	init {
		instance = this
		ClientLifecycle.initialize()
		ServerLifecycle.initialize()
	}

	companion object {
		lateinit var instance: PandaLibMain
			private set
	}
}