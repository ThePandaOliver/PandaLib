/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:Suppress("UnstableApiUsage")

plugins {
	id("modloader")
}

val neoforgeLoaderVersion: String by project

architectury {
	neoForge()
	platformSetupLoomIde()
}

configurations {
	getByName("developmentNeoForge").extendsFrom(common.get())

	implementation.get().extendsFrom(nonModImplementation.get())
	include.get().extendsFrom(nonModImplementation.get())
	forgeRuntimeLibrary.get().extendsFrom(nonModImplementation.get())
}

dependencies {
	neoForge("net.neoforged:neoforge:$neoforgeLoaderVersion")

	common(project(":common", configuration = "namedElements"))
	shadowBundle(project(":common", configuration = "transformProductionNeoForge"))
}

tasks.remapJar {
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}