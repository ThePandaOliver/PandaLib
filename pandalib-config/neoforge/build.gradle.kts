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
	neoForge(libs.neoforge)
	api(project(":pandalib-config"))

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