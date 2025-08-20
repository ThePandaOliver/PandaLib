/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:Suppress("UnstableApiUsage")

import net.fabricmc.loom.task.RemapJarTask


plugins {
	id("architectury-plugin") apply false
	id("dev.architectury.loom") apply false
	id("com.gradleup.shadow") apply false
	id("io.github.pacifistmc.forgix") version "2.0.0-SNAPSHOT.5.1-FORK.3"

	id("me.modmuss50.mod-publish-plugin") version "0.8.4"
	id("com.google.devtools.ksp") version "2.2.0-2.0.2"
}

val modVersion: String by project
val modGroup: String by project

version = modVersion
group = modGroup

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.gradle.plugin.idea-ext")
	apply(plugin = "com.google.devtools.ksp")

	val nonModImplementation: Configuration by configurations.creating

	dependencies {
		nonModImplementation(kotlin("stdlib"))
		nonModImplementation(kotlin("stdlib-jdk8"))
		nonModImplementation(kotlin("stdlib-jdk7"))
		nonModImplementation(kotlin("reflect", version = "2.2.0"))
		nonModImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
		nonModImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.10.2")
		nonModImplementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.1")
		nonModImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
		nonModImplementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.8.1")
		nonModImplementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
		nonModImplementation("org.jetbrains.kotlinx:kotlinx-io-core:0.7.0")
		nonModImplementation("org.jetbrains.kotlinx:kotlinx-io-bytestring:0.7.0")

		runtimeOnly("com.google.auto.service:auto-service-annotations:1.1.1")
		compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
		ksp("dev.zacsweers.autoservice:auto-service-ksp:1.2.0")
	}
}

evaluationDependsOnChildren()

forgix {
	archiveClassifier = ""

	findProject(":fabric")?.let {
		fabric {
			inputJar = it.tasks.named<RemapJarTask>("remapJar").get().archiveFile
		}
	}

	findProject(":neoforge")?.let {
		neoforge {
			inputJar = it.tasks.named<RemapJarTask>("remapJar").get().archiveFile
		}
	}

	findProject(":forge")?.let {
		forge {
			inputJar = it.tasks.named<RemapJarTask>("remapJar").get().archiveFile
		}
	}

//	multiversion {
//		destinationDirectory
//		archiveVersion = modVersion
//
//		val versions =  file("versionProperties").listFiles { file ->
//			file.isFile && file.extension == "properties"
//		}!!.map { file -> file.nameWithoutExtension }
//
//		inputJars = project.files(
//			versions.map { version -> "build/forgix/${rootProject.name}-$modVersion+$version.jar" }
//		)
//	}
}