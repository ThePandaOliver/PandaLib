plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.ksp)
}

repositories {
	mavenLocal()
	mavenCentral()
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
	
	api(libs.universalSerializer)

	testImplementation(libs.kotlin.test)
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

kotlin {
	jvmToolchain(21)
}