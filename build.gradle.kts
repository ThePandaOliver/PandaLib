@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	java
	idea
	alias(libs.plugins.kotlinJvm)
	`maven-publish`
	`version-catalog`

	alias(libs.plugins.shadow) apply false
	alias(libs.plugins.architecturyPlugin)
	alias(libs.plugins.architecturyLoom)
	alias(libs.plugins.modPublish)
}

architectury {
	minecraft = libs.versions.minecraft.get()
	common("neoforge", "fabric")
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
	modImplementation(libs.fabricLoader)
	modApi(libs.architectury.common)
}

allprojects {
	apply(plugin = "java")
	apply(plugin = "kotlin")
	apply(plugin = "maven-publish")
	apply(plugin = "version-catalog")
	apply(plugin = rootProject.libs.plugins.architecturyPlugin.get().pluginId)
	apply(plugin = rootProject.libs.plugins.architecturyLoom.get().pluginId)
	
	group = "dev.pandasystems"

	loom {
		silentMojangMappingsLicense()
		log4jConfigs.from(rootProject.file("log4j2.xml"))
	}

	repositories {
		mavenCentral()
		maven("https://maven.architectury.dev/")
		maven("https://maven.parchmentmc.org/")
		maven("https://maven.fabricmc.net/")
		maven("https://maven.minecraftforge.net/")
		maven("https://maven.neoforged.net/releases/")
	}

	dependencies {
		minecraft(rootProject.libs.minecraft)
		mappings(loom.layered {
			officialMojangMappings()
			parchment("${rootProject.libs.parchment.get()}@zip")
		})

		api(rootProject.libs.bundles.kotlin)
	}

	kotlin {
		jvmToolchain(21)
		compilerOptions {
			freeCompilerArgs = listOf("-Xjvm-default=all")
		}
	}

	tasks.processResources {
		val props = mutableMapOf(
			"mod_version" to version.toString(),
		)

		inputs.properties(props)
		filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
			expand(props)
		}
	}

	java {
		withSourcesJar()
	}
}

subprojects {
	apply(plugin = rootProject.libs.plugins.shadow.get().pluginId)

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
				version = "mc${rootProject.libs.versions.minecraft.get()}-${project.version}"
			}
		}
	}
}

publishMods {
	changelog = rootProject.file("CHANGELOG.md").readText()
	type = BETA
	dryRun = false
	
	val gameVerString = rootProject.libs.versions.minecraft.get()
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