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
	
	implementation(libs.kotlin.reflect)
	implementation(libs.kotlinx.coroutines)
	implementation(libs.kotlinx.serialization)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.kotlinx.serialization.cbor)
	implementation(libs.kotlinx.datetime)
	implementation(libs.kotlinx.io)
	implementation(libs.kotlinx.io.bytestring)
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

kotlin {
	jvmToolchain(21)
}