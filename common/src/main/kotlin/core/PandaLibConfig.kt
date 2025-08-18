/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.core

import dev.pandasystems.pandalib.impl.config.Configuration

@Configuration(PandaLib.MOD_ID, "pandalib")
class PandaLibConfig {
	var commonDebug: Boolean = false
}
