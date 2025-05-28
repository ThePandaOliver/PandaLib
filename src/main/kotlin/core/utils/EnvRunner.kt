package dev.pandasystems.pandalib.core.utils

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.impl.platform.Services
import java.util.function.Supplier

object EnvRunner {
	@JvmStatic
	fun runIf(env: Env, task: Supplier<Runnable>) {
		if (Services.GAME.environment === env) task.get().run()
	}
}
