package dev.pandasystems.pandalib.core

import dev.pandasystems.pandalib.core.lifecycles.ClientLifecycle
import dev.pandasystems.pandalib.core.lifecycles.ServerLifecycle
import dev.pandasystems.pandalib.core.utils.client
import dev.pandasystems.pandalib.core.utils.server


class PandaLibMain {
	init {
		instance = this
		client { ClientLifecycle.initialize() }
		server { ServerLifecycle.initialize() }
	}

	companion object {
		lateinit var instance: PandaLibMain
			private set
	}
}