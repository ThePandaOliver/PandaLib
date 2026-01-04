/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:Suppress("UnstableApiUsage")

import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings

plugins {
	kotlin("jvm") version "2.3.0"
	id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.10"

	id("io.github.pacifistmc.forgix") version "2.0.0-SNAPSHOT"
	id("me.modmuss50.mod-publish-plugin") version "1.1.0"
	`maven-publish`
}

val modId: String by project

version = "1.0.0-ALPHA.2.1"
group = "dev.pandasystems"

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("stdlib-jdk7"))
	implementation(kotlin("reflect"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.10.2")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.9.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.9.0")
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
	implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.7.0")
	implementation("org.jetbrains.kotlinx:kotlinx-io-bytestring:0.8.2")
}

idea {
	module {
		settings {
			val packagePrefixStr = "dev.pandasystems.$modId"
			packagePrefix["src/main/kotlin"] = packagePrefixStr
			packagePrefix["src/main/java"] = packagePrefixStr
		}
	}
}

//publishMods {
//	type = ReleaseType.ALPHA
//	changelog = file("CHANGELOG.md").readText()
//
//	val cfOptions = curseforgeOptions {
//		accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
//		projectId = "975460"
//		minecraftVersions.add(mcVersion)
//	}
//
//	val mrOptions = modrinthOptions {
//		accessToken = providers.environmentVariable("MODRINTH_TOKEN")
//		projectId = "mEEGbEIu"
//		minecraftVersions.add(mcVersion)
//	}
//
//	curseforge("curseforgeFabric") {
//		displayName = "[$mcVersion Fabric] ${project.version}"
//		from(cfOptions)
//		file = project(":fabric").tasks.remapJar.get().archiveFile
//		modLoaders.add("fabric")
//		requires("fabric-api")
//	}
//
//	curseforge("curseforgeNeoForge") {
//		displayName = "[$mcVersion NeoForge] ${project.version}"
//		from(cfOptions)
//		file = project(":neoforge").tasks.remapJar.get().archiveFile
//		modLoaders.add("neoforge")
//	}
//
//	modrinth("modrinthFabric") {
//		displayName = "[$mcVersion Fabric] ${project.version}"
//		from(mrOptions)
//		file = project(":fabric").tasks.remapJar.get().archiveFile
//		modLoaders.add("fabric")
//		requires("fabric-api")
//	}
//
//	modrinth("modrinthNeoForge") {
//		displayName = "[$mcVersion NeoForge] ${project.version}"
//		from(mrOptions)
//		file = project(":neoforge").tasks.remapJar.get().archiveFile
//		modLoaders.add("neoforge")
//	}
//}