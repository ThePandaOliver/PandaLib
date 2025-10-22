/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.client

import dev.pandasystems.pandalib.client.config.ClientConfigSynchronizer
import dev.pandasystems.pandalib.pandalibLogger
import dev.pandasystems.pandalib.utils.InternalPandaLibApi


@InternalPandaLibApi
fun initializePandaLibClient() {
	pandalibLogger.debug("PandaLib Client is initializing...")

	ClientConfigSynchronizer.init()

	pandalibLogger.debug("PandaLib initialized successfully.")
}