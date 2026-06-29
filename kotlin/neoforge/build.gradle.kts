plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.architectury.loom)
	alias(libs.plugins.shadow)

	alias(libs.plugins.publish.mod)
	alias(libs.plugins.ksp)
}

loom {
	silentMojangMappingsLicense()

	decompilers {
		get("vineflower").apply { // Adds names to lambdas - useful for mixins
			options.put("mark-corresponding-synthetics", "1")
		}
	}
}

repositories {
	mavenLocal()
	mavenCentral()
	maven("https://maven.parchmentmc.org/")
	maven("https://maven.neoforged.net/releases/")

	maven("https://repo.pandasystems.dev/repository/maven-public/")
}

dependencies {
	minecraft(libs.minecraft)
	@Suppress("UnstableApiUsage")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-1.21.10:2025.10.12@zip")
	})
	api(project(":pandalib-kotlin"))
	neoForge(libs.neoforge)

	ksp(libs.autoService.ksp)
	runtimeOnly(libs.autoService.annotations)
	compileOnly(libs.autoService.annotations)
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

kotlin {
	jvmToolchain(21)
}

val mcVersion: String by project
val modVersion: String by project
val modGroup: String by project
val modName: String by project
val modDescription: String by project
val modLicense: String by project
val modAuthors: String by project

tasks.processResources {
	val props = mutableMapOf(
		"minecraft_version" to mcVersion,

		"mod_version" to modVersion,
		"mod_group" to modGroup,
		"mod_id" to "pandalib-kotlin",

		"mod_name" to modName,
		"mod_description" to modDescription,
		"mod_license" to modLicense,
		"mod_authors" to modAuthors,
	)

	inputs.properties(props)
	filesMatching(
		listOf(
			"META-INF/neoforge.mods.toml",
			"**.mixins.json",
			"pack.mcmeta"
		)
	) {
		expand(props)
	}
}