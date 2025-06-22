package dev.pandasystems.pandalib.utils

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.api.platform.game
import java.util.function.Supplier

object EnvRunner {
	@JvmStatic
	fun runIf(env: Env, task: Supplier<Runnable>) {
		if (game.environment === env) task.get().run()
	}
}
