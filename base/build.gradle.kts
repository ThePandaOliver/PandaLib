plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.architectury.loom)

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
	maven("https://maven.fabricmc.net/")
}

dependencies {
	minecraft(libs.minecraft)
	@Suppress("UnstableApiUsage")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-1.21.10:2025.10.12@zip")
	})
	modCompileOnly(libs.fabric.loader)

	api(project(":pandalib-kotlin"))
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

kotlin {
	jvmToolchain(21)
}