/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

@file:Suppress("UnstableApiUsage")

val neoforgeLoaderVersion: String by project

architectury {
	neoForge()
}

configurations {
	getByName("developmentNeoForge").extendsFrom(common.get())
	forgeRuntimeLibrary.get().extendsFrom(nonModImplementation.get())
}

dependencies {
	neoForge("net.neoforged:neoforge:$neoforgeLoaderVersion")

	common(project(":", configuration = "namedElements"))
	shadowBundle(project(":", configuration = "transformProductionNeoForge"))
}

tasks.remapJar {
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}