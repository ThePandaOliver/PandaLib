/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

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
