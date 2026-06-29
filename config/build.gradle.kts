plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.ksp)
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

kotlin {
	jvmToolchain(21)
}