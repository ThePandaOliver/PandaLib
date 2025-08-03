/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

@file:Suppress("UnstableApiUsage")

val fabricLoaderVersion: String by project
val fabricApi: String by project

architectury {
	fabric()
}

configurations {
	getByName("developmentFabric").extendsFrom(common.get())
}

repositories {
	maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApi")

	common(project(":", configuration = "namedElements"))
	shadowBundle(project(":", configuration = "transformProductionFabric"))
}