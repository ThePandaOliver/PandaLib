plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.easymodding)
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	implementation(project(":core"))

	testImplementation(libs.kotlin.test)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

tasks.test {
	failOnNoDiscoveredTests = false
}

kotlin {
	jvmToolchain(21)
}

easyModding {
	fabric()
	neoForge()
	forge()
}
