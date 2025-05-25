@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	java
	alias(libs.plugins.kotlinJvm)
	`maven-publish`
	`version-catalog`
	
	alias(libs.plugins.shadow) apply false
	alias(libs.plugins.architecturyPlugin)
	alias(libs.plugins.architecturyLoom)
}

version = prop("mod_version")
group = prop("maven_group")
base { archivesName = rootProject.name }

architectury {
	minecraft = libs.versions.minecraft.get()
	common("neoforge", "fabric")
}

loom {
	accessWidenerPath = file("src/main/resources/pandalib.accesswidener")
	runs {
		delete("client", "server")
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
	
	loom {
		silentMojangMappingsLicense()
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
			"mod_version" to prop("mod_version")
		)

		inputs.properties(props)
		filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
			expand(props)
		}
	}

	java {
		withSourcesJar()
	}

	publishing {
		publications {
			create<MavenPublication>("maven") {
				artifactId = project.base.archivesName.get()
				version = "${project.version}"
				from(components["java"])
			}
		}
	}
}

subprojects {
	apply(plugin = rootProject.libs.plugins.shadow.get().pluginId)
	
	base { archivesName = "${rootProject.base.archivesName.get()}-${project.name}" }
	version = "mc${rootProject.libs.versions.minecraft.get()}-${rootProject.version}"

	architectury {
		platformSetupLoomIde()
	}
	
	loom {
		accessWidenerPath = rootProject.loom.accessWidenerPath
		
		runs {
			named("client") {
				client()
				configName = "Client"
				runDir("../.runs/clinet")
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
