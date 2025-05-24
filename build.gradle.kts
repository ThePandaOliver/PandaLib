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
			"java_version" to properties["java_version"],

			"maven_group" to properties["maven_group"],
			"mod_id" to properties["mod_id"],
			"mod_version" to properties["mod_version"],
			"mod_name" to properties["mod_name"],
			"mod_description" to properties["mod_description"],
			"mod_author" to properties["mod_author"],
			"mod_license" to properties["mod_license"],

			"project_curseforge_slug" to properties["project_curseforge_slug"],
			"project_modrinth_slug" to properties["project_modrinth_slug"],
			"project_github_repo" to properties["project_github_repo"],
		)

		if (properties["fabric_version_range"] != null)
			props["fabric_version_range"] = properties["fabric_version_range"] as String

		if (properties["forge_version_range"] != null)
			props["forge_version_range"] = properties["forge_version_range"] as String

		if (properties["neoforge_version_range"] != null)
			props["neoforge_version_range"] = properties["neoforge_version_range"] as String

		inputs.properties(props)
		filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
			expand(props)
		}
	}

	tasks.jar {
		manifest {
			attributes(mapOf(
					"Specification-Title" to properties["mod_name"],
					"Specification-Vendor" to properties["mod_author"],
					"Specification-Version" to properties["mod_version"],
					"Implementation-Title" to name,
					"Implementation-Vendor" to properties["mod_author"],
					"Implementation-Version" to archiveVersion
			))
		}
	}

	java {
		withSourcesJar()
	}
}
