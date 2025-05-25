@file:Suppress("UnstableApiUsage")
plugins {
	java
	alias(libs.plugins.kotlinJvm)
	`version-catalog`
	alias(libs.plugins.shadow) apply false
}

allprojects {
	apply(plugin = "java")
	apply(plugin = "kotlin")
	apply(plugin = "version-catalog")
	
	version = "${properties["mod_version"]}"
	group = properties["maven_group"] as String
	
	repositories {
		mavenCentral()
	}

	kotlin {
		jvmToolchain(21)
		compilerOptions {
			freeCompilerArgs = listOf("-Xjvm-default=all")
		}
	}
}

subprojects {
	base { archivesName = "${properties["mod_id"]}-${project.name}" }
	version = "mc${rootProject.libs.versions.minecraft.get()}-${rootProject.version}"

	repositories {
		maven("https://maven.architectury.dev/")
		maven("https://maven.parchmentmc.org/")
		maven("https://maven.fabricmc.net/")
		maven("https://maven.minecraftforge.net/")
		maven("https://maven.neoforged.net/releases/")
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

	tasks.processResources {
		val props = mutableMapOf(
			"mod_version" to properties["mod_version"]
		)

		inputs.properties(props)
		filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
			expand(props)
		}
	}
	
	java {
		withSourcesJar()
	}
}
