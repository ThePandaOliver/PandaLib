import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
	java
	idea

	id("architectury-plugin") version "3.4-SNAPSHOT"
	id("dev.architectury.loom") version "1.9-SNAPSHOT" apply false

	id("com.github.johnrengelman.shadow") version "8.1.1" apply false
	id("io.github.pacifistmc.forgix") version "1.2.9"
}

architectury {
	minecraft = properties["minecraft_version"] as String
}

allprojects {
	apply(plugin = "java")
	apply(plugin = "idea")

	base {
		archivesName = properties["mod_id"] as String
	}
	group = properties["maven_group"] as String
	version = "mc${properties["minecraft_version"]}-${properties["mod_version"]}"
}

forgix {
	group = "${properties["maven_group"]}.${properties["mod_id"]}"
	mergedJarName = "${properties["mod_id"]}-${version}.jar"
	outputDir = "build/libs/merged"

	if (findProject(":fabric") != null)
		fabricContainer = FabricContainer().apply {
			jarLocation = "build/libs/${properties["mod_id"]}-fabric-${version}.jar"
		}

	if (findProject(":forge") != null)
		forgeContainer = ForgeContainer().apply {
			jarLocation = "build/libs/${properties["mod_id"]}-forge-${version}.jar"
		}

	if (findProject(":neoforge") != null)
		neoForgeContainer = NeoForgeContainer().apply {
			jarLocation = "build/libs/${properties["mod_id"]}-neoforge-${version}.jar"
		}

	removeDuplicate("${properties["maven_group"]}.${properties["mod_id"]}")
}

subprojects {
	apply(plugin = "architectury-plugin")
	apply(plugin = "dev.architectury.loom")

	apply(plugin = "com.github.johnrengelman.shadow")

	base {
		archivesName = "${properties["mod_id"]}-${project.name}"
	}

	val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
	loom.silentMojangMappingsLicense()

	@Suppress("UnstableApiUsage")
	configurations {
		create("common") {
			isCanBeResolved = true
			isCanBeConsumed = false
		}
		compileClasspath.get().extendsFrom(configurations["common"])
		runtimeClasspath.get().extendsFrom(configurations["common"])

		// Files in this configuration will be bundled into your mod using the Shadow plugin.
		// Don't use the `shadow` configuration from the plugin itself as it's meant for excluding files.
		create("shadowBundle") {
			isCanBeResolved = true
			isCanBeConsumed = false
		}

		create("fullShadow") {
			isCanBeResolved = true
			isCanBeConsumed = false
		}
		implementation.get().extendsFrom(configurations["fullShadow"])
		configurations["shadowBundle"].extendsFrom(configurations["fullShadow"])
	}

	repositories {
		mavenCentral()
		mavenLocal()

		maven("https://maven.parchmentmc.org/")
		maven("https://maven.fabricmc.net/")
		maven("https://maven.minecraftforge.net/")
		maven("https://maven.neoforged.net/releases/")
	}

	@Suppress("UnstableApiUsage")
	dependencies {
		"minecraft"("com.mojang:minecraft:${properties["minecraft_version"]}") {
			exclude(group = "org.joml", module = "joml")
		}
		"mappings"(loom.layered {
			officialMojangMappings()
			parchment("org.parchmentmc.data:parchment-${properties["parchment_minecraft_version"]}:${properties["parchment_mappings_version"]}@zip")
		})

		"fullShadow"("org.lwjgl", "lwjgl-assimp", "${properties["deps_lwjgl_version"]}") {
			exclude(group = "org.lwjgl", module = "lwjgl")
		}
		for (native in arrayOf("natives-windows", "natives-linux", "natives-macos")) {
			"fullShadow"("org.lwjgl", "lwjgl-assimp", "${properties["deps_lwjgl_version"]}", classifier = native) {
				exclude(group = "org.lwjgl", module = "lwjgl")
			}
		}

		compileOnly("org.jetbrains:annotations:24.1.0")
	}

	tasks.withType<ShadowJar> {
		configurations = listOf(project.configurations.getByName("shadowBundle"))
		archiveClassifier.set("dev-shadow")

		exclude("architectury.common.json")
	}

	tasks.withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.release.set(JavaLanguageVersion.of(properties["java_version"] as String).asInt())
	}

	tasks.processResources {
		val props = mutableMapOf(
			"java_version" to properties["java_version"],
			"supported_mod_loaders" to properties["supported_mod_loaders"],

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

	tasks.build.get().finalizedBy(rootProject.tasks.getByName("mergeJars"))
	tasks.assemble.get().finalizedBy(rootProject.tasks.getByName("mergeJars"))
}