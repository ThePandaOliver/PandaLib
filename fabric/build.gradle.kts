/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:Suppress("UnstableApiUsage")

plugins {
	id("modloader")
	`maven-publish`
}

val mcVersion: String by project

val modId: String by project

val fabricLoaderVersion: String by project
val fabricApi: String by project

architectury {
	fabric()
	platformSetupLoomIde()
}

configurations {
	getByName("developmentFabric").extendsFrom(common.get())

	implementation.get().extendsFrom(nonModImplementation.get())
	include.get().extendsFrom(nonModImplementation.get())
}

repositories {
	maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApi")

	common(project(":common", configuration = "namedElements"))
	shadowBundle(project(":common", configuration = "transformProductionFabric"))
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])

			artifactId = "$modId-fabric"
			version = "mc${mcVersion}-${project.version}"
		}
	}
}