import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.architectury.plugin.ArchitectPluginExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings

plugins {
	kotlin("jvm")
	id("architectury-plugin") version "3.4-SNAPSHOT" apply false
	id("dev.architectury.loom") version "1.13-SNAPSHOT" apply false
	id("com.gradleup.shadow") version "9.0.2" apply false
	id("org.jetbrains.gradle.plugin.idea-ext")
	id("com.google.devtools.ksp") version "2.3.4" apply false
	`maven-publish`
}

group = "dev.pandasystems"
version = "1.0.0-ALPHA.2.1"

val modId: String by rootProject

val modName: String by rootProject
val modDescription: String by rootProject
val modAuthors: String by rootProject
val modLicense: String by rootProject

repositories {
	mavenCentral()
}

subprojects {
	val javaVersion: String by project

	val fabricLoaderVersion: String? by project
	val neoforgeLoaderVersion: String? by project

	val parchmentMinecraftVersion: String by project
	val parchmentMappingsVersion: String by project

	val fabricApiVersion: String by project

	val loaderName = project.name.substringAfter('-')
	val mcVersion = project.name.substringBefore('-')

	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "architectury-plugin")
	apply(plugin = "dev.architectury.loom")
	apply(plugin = "com.gradleup.shadow")
	apply(plugin = "com.google.devtools.ksp")
	apply(plugin = "maven-publish")

	project.extensions.getByType(ArchitectPluginExtension::class.java).apply {
		when (loaderName) {
			"fabric" -> {
				fabric()
				platformSetupLoomIde()
			}

			"neoforge" -> {
				neoForge()
				platformSetupLoomIde()
			}
		}
	}

	val loom = project.extensions.getByType(LoomGradleExtensionAPI::class.java).apply {
		silentMojangMappingsLicense()
		log4jConfigs.from(rootProject.file("log4j2.xml"))
		accessWidenerPath = rootProject.file("src/main/resources/$modId.accesswidener")

		decompilers {
			get("vineflower").apply { // Adds names to lambdas - useful for mixins
				options.put("mark-corresponding-synthetics", "1")
			}
		}

		runs {
			val path = project.projectDir.toPath().relativize(rootProject.file(".runs").toPath())

			named("client") {
				client()
				configName = "Client $mcVersion"
				runDir("$path/client")
				programArg("--username=Dev")
			}
			named("server") {
				server()
				configName = "Server $mcVersion"
				runDir("$path/server")
			}
		}

		runs.configureEach { ideConfigGenerated(false) }
	}

	val common: Configuration by configurations.creating {
		isCanBeResolved = true
		isCanBeConsumed = false
	}
	configurations.compileClasspath.get().extendsFrom(common)
	configurations.runtimeClasspath.get().extendsFrom(common)

	val shadowBundle: Configuration by configurations.creating {
		isCanBeResolved = true
		isCanBeConsumed = false
	}

	val nonModImplementation: Configuration by configurations.creating

	configurations {
		implementation.get().extendsFrom(nonModImplementation)
		getByName("include").extendsFrom(nonModImplementation)

		when (loaderName) {
			"fabric" -> {
				getByName("developmentFabric").extendsFrom(common)
			}

			"neoforge" -> {
				getByName("developmentNeoForge").extendsFrom(common)
				getByName("forgeRuntimeLibrary").extendsFrom(nonModImplementation)
			}
		}
	}

	repositories {
		mavenCentral()
		maven("https://maven.architectury.dev/")
		maven("https://maven.parchmentmc.org/")
		maven("https://maven.fabricmc.net/")
		maven("https://maven.neoforged.net/releases/")

		maven("https://repo.pandasystems.dev/repository/maven-snapshots/") {
			name = "PandasRepository"
			mavenContent {
				snapshotsOnly()
			}
		}
	}

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

		nonModImplementation("dev.pandasystems:universal-serializer:0.1.0-SNAPSHOT") { isChanging = true }

		runtimeOnly("com.google.auto.service:auto-service-annotations:1.1.1")
		compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
		"ksp"("dev.zacsweers.autoservice:auto-service-ksp:1.2.0")

		"minecraft"("com.mojang:minecraft:$mcVersion")
		@Suppress("UnstableApiUsage")
		"mappings"(loom.layered {
			officialMojangMappings()
			parchment("org.parchmentmc.data:parchment-$parchmentMinecraftVersion:$parchmentMappingsVersion@zip")
		})

		when (loaderName) {
			"fabric" -> {
				"modImplementation"("net.fabricmc:fabric-loader:$fabricLoaderVersion")
				"modImplementation"("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

				common(project(":"))
				shadowBundle(project(":"))
			}

			"neoforge" -> {
				"neoForge"("net.neoforged:neoforge:$neoforgeLoaderVersion")

				common(project(":"))
				shadowBundle(project(":"))
			}

			else -> {
				// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
				// Do NOT use other classes from fabric loader
				"modImplementation"("net.fabricmc:fabric-loader:$fabricLoaderVersion")
			}
		}
	}

	java {
		withSourcesJar()
	}

	kotlin {
		jvmToolchain(21)
	}

	tasks {
		processResources {
			val props = mutableMapOf(
				"java_version" to javaVersion,
				"minecraft_version" to mcVersion,

				"mod_version" to rootProject.version,
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

		getByName<ShadowJar>("shadowJar") {
			configurations = listOf(shadowBundle)
			archiveClassifier.set("dev-shadow")

			exclude("architectury.common.json")
		}

		getByName<RemapJarTask>("remapJar") {
			injectAccessWidener.set(true)
			inputFile = getByName<ShadowJar>("shadowJar").archiveFile
			archiveClassifier.set("")
			if (loaderName == "neoforge")
				atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
		}


		val remapSlimJar by registering(RemapJarTask::class) {
			dependsOn(getByName<ShadowJar>("shadowJar"))
			injectAccessWidener.set(true)
			inputFile = getByName<ShadowJar>("shadowJar").archiveFile
			archiveClassifier.set("slim")
			addNestedDependencies.set(false)
			if (loaderName == "neoforge")
				atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
		}

		val copyBuildModFile by registering(Copy::class) {
			from("build/libs/${base.archivesName.get()}-$version.jar")
			into(rootDir.resolve("build/mod-build"))
		}

		build {
			dependsOn("remapSlimJar")
			finalizedBy(copyBuildModFile)
		}

		val copyLicense by register("copyLicense",Copy::class) {
			from(rootDir.resolve("LICENSE.md"))
			destinationDir = project.layout.buildDirectory.file("resources/main").get().asFile
		}

		processResources {
			dependsOn(copyLicense)
		}
	}

	idea {
		module {
			settings {
				val mcVersionString = mcVersion.replace('.', '_')
				val packagePrefixStr = "dev.pandasystems.$modId.mc$mcVersionString.$loaderName"
				packagePrefix["src/main/kotlin"] = packagePrefixStr
				packagePrefix["src/main/java"] = packagePrefixStr
			}
		}
	}

	publishing {
		publications {
			create<MavenPublication>("maven") {
				from(components["java"])
				artifactId = base.archivesName.get()

				artifact(tasks.named("remapSlimJar")) { classifier = "slim" }
			}
		}

		repositories {
			if (version.toString().endsWith("SNAPSHOT")) {
				maven("https://repo.pandasystems.dev/repository/maven-snapshots/") {
					name = "Snapshot"
					credentials {
						username = System.getenv("NEXUS_USERNAME")
						password = System.getenv("NEXUS_PASSWORD")
					}
				}
			} else {
				maven("https://repo.pandasystems.dev/repository/maven-releases/") {
					name = "Release"
					credentials {
						username = System.getenv("NEXUS_USERNAME")
						password = System.getenv("NEXUS_PASSWORD")
					}
				}
			}
		}
	}
}