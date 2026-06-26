plugins {
	alias(libs.plugins.kotlin.jvm)
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	api(project(":pandalib-kotlin"))

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