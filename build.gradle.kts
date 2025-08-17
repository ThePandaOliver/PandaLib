/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings

plugins {
	kotlin("jvm") version "2.2.0"
	id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.10"

	id("com.gradleup.shadow") version "9.0.2"
	id("architectury-plugin") version "3.4-SNAPSHOT"
	id("dev.architectury.loom") version "1.10-SNAPSHOT"
//	id("io.github.pacifistmc.forgix") version "2.0.0-SNAPSHOT.5.1-FORK.3"

	`maven-publish`
	id("me.modmuss50.mod-publish-plugin") version "0.8.4"
	id("com.google.devtools.ksp") version "2.2.0-2.0.2"
}

val javaVersion: String by extra
val mcVersion: String by extra
val buildFor: String by extra

val modVersion: String by extra
val modId: String by extra
val modGroup: String by extra

val modName: String by extra
val modDescription: String by extra
val modAuthors: String by extra
val modLicense: String by extra

val fabricLoaderVersion: String? by extra
val neoforgeLoaderVersion: String? by extra

val parchmentMinecraftVersion: String by extra
val parchmentMappingsVersion: String by extra

architectury {
	minecraft = mcVersion
	common(buildFor.split(",").map { it.trim() })
}

loom {
	accessWidenerPath = file("src/main/resources/pandalib.accesswidener")

	runs.all {
		ideConfigGenerated(false)
	}
}

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
}

allprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.gradle.plugin.idea-ext")
	apply(plugin = "architectury-plugin")
	apply(plugin = "dev.architectury.loom")
	apply(plugin = "maven-publish")
	apply(plugin = "com.google.devtools.ksp")
	apply(plugin = "com.gradleup.shadow")

	group = modGroup
	version = modVersion

	loom {
		silentMojangMappingsLicense()
		log4jConfigs.from(rootProject.file("log4j2.xml"))

		decompilers {
			get("vineflower").apply { // Adds names to lambdas - useful for mixins
				options.put("mark-corresponding-synthetics", "1")
			}
		}
	}

	repositories {
		mavenCentral()
		maven("https://maven.architectury.dev/")
		maven("https://maven.parchmentmc.org/")
		maven("https://maven.fabricmc.net/")
		maven("https://maven.minecraftforge.net/")
		maven("https://maven.neoforged.net/releases/")
	}

	val nonModImplementation by configurations.creating
	configurations.implementation.get().extendsFrom(nonModImplementation)

	dependencies {
		minecraft("com.mojang:minecraft:$mcVersion")
		mappings(loom.layered {
			officialMojangMappings()
			parchment("org.parchmentmc.data:parchment-$parchmentMinecraftVersion:$parchmentMappingsVersion@zip")
		})

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

	kotlin {
		jvmToolchain(21)
	}

	tasks {
		processResources {
			val props = mutableMapOf(
				"java_version" to javaVersion,
				"minecraft_version" to mcVersion,

				"mod_version" to modVersion,
				"mod_group" to modGroup,
				"mod_id" to modId,

				"mod_name" to modName,
				"mod_description" to modDescription,
				"mod_license" to modLicense,
				"mod_authors_fabric" to modAuthors.split(",").joinToString(", ") { "\"$it\"" },
				"mod_authors_forge" to modAuthors,
			)

			fabricLoaderVersion?.let { props["fabric_loader_version"] = it }
			neoforgeLoaderVersion?.let { props["neoforge_loader_version"] = it }

			inputs.properties(props)
			filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "fabric.mod.json", "**.mixins.json", "pack.mcmeta")) {
				expand(props)
			}
		}

		remapJar {
			isZip64 = true
		}

		shadowJar {
			isZip64 = true
		}
	}

	java {
		withSourcesJar()

	}

	idea {
		module {
			settings {
				val packagePrefixStr = "$modGroup.$modId".let {
					if (rootProject != project) "$it.${project.name.lowercase()}" else it
				}
				packagePrefix["src/main/kotlin"] = packagePrefixStr
				packagePrefix["src/main/java"] = packagePrefixStr
			}
		}
	}
}

subprojects {
	base { archivesName = "${rootProject.name}-${project.name}" }

	architectury {
		platformSetupLoomIde()
	}

	loom {
		accessWidenerPath = rootProject.loom.accessWidenerPath

		runs {
			named("client") {
				client()
				configName = "Client"
				runDir("../.runs/client")
				programArg("--username=Dev")
				ideConfigGenerated(true)
			}
			named("server") {
				server()
				configName = "Server"
				runDir("../.runs/server")
				ideConfigGenerated(true)
			}
		}
	}

	configurations {
		val common = create("common") {
			isCanBeResolved = true
			isCanBeConsumed = false
		}
		compileClasspath.get().extendsFrom(common)
		runtimeClasspath.get().extendsFrom(common)

		create("shadowBundle") {
			isCanBeResolved = true
			isCanBeConsumed = false
		}

		include.get().extendsFrom(configurations["nonModImplementation"])
	}

	tasks.getByName<ShadowJar>("shadowJar") {
		configurations = listOf(project.configurations["shadowBundle"])
		archiveClassifier.set("dev-shadow")

		exclude("architectury.common.json")
	}

	tasks.remapJar {
		injectAccessWidener.set(true)
		inputFile = tasks.getByName<ShadowJar>("shadowJar").archiveFile
	}
}

allprojects {
	publishing {
		publications {
			create<MavenPublication>("maven") {
				from(components["java"])

				artifactId = project.base.archivesName.get().lowercase()
				version = "mc${mcVersion}-${project.version}"
			}
		}
	}
}

publishMods {
	changelog = rootProject.file("CHANGELOG.md").readText()
	type = BETA
	dryRun = false

	val gameVerString = mcVersion
	val verString = project.version.toString()
	version = verString

	val cfOptions = curseforgeOptions {
		accessToken = providers.environmentVariable("CURSEFORGE_API_KEY")
		projectId = "975460"
		minecraftVersions.add("1.21.4")
		javaVersions.add(JavaVersion.VERSION_21)

		requires("architectury-api")
		changelogType = "markdown"
	}

	val mrOptions = modrinthOptions {
		accessToken = providers.environmentVariable("MODRINTH_API_KEY")
		projectId = "mEEGbEIu"
		minecraftVersions.add("1.21.4")

		requires("architectury-api")
	}

	curseforge("curseforgeNeoForge") {
		from(cfOptions)
		displayName = "[NeoForge $gameVerString] $verString"
		modLoaders.add("neoforge")
		file = project(":neoforge").tasks.remapJar.get().archiveFile
	}

	curseforge("curseforgeFabric") {
		from(cfOptions)
		displayName = "[Fabric $gameVerString] $verString"
		requires("fabric-api")
		modLoaders.add("fabric")
		file = project(":fabric").tasks.remapJar.get().archiveFile
	}

	modrinth("modrinthNeoForge") {
		from(mrOptions)
		displayName = "[NeoForge $gameVerString] $verString"
		modLoaders.add("neoforge")
		file = project(":neoforge").tasks.remapJar.get().archiveFile
	}

	modrinth("modrinthFabric") {
		from(mrOptions)
		displayName = "[Fabric $gameVerString] $verString"
		requires("fabric-api")
		modLoaders.add("fabric")
		file = project(":fabric").tasks.remapJar.get().archiveFile
	}
}