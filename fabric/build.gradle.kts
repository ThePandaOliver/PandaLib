import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.shadow)
	`maven-publish`
}

base {
	archivesName.set("pandalib-fabric")
}

repositories {
	mavenLocal()
	mavenCentral()
}

val common = configurations.create("common") {
	isCanBeResolved = true
	isCanBeConsumed = false
}

val shadowCommon = configurations.create("shadowCommon") {
	isCanBeResolved = true
	isCanBeConsumed = false
}

configurations.compileClasspath.get().extendsFrom(common)
configurations.runtimeClasspath.get().extendsFrom(common)

val commonSourceSets = project(":common").extensions.getByType<SourceSetContainer>()
val commonTestmod = commonSourceSets["testmod"]


val testmod = sourceSets.create("testmod") {
	// 1. Add compiled outputs from both fabric:main and common:testmod
	compileClasspath += sourceSets["main"].output + commonTestmod.output
	runtimeClasspath += sourceSets["main"].output + commonTestmod.output
}

configurations {
	named("testmodCompileClasspath") {
		extendsFrom(configurations["compileClasspath"])
	}
	named("testmodRuntimeClasspath") {
		extendsFrom(configurations["runtimeClasspath"])
	}
}


dependencies {
	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
	implementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version")}")
	implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")

	api(libs.kotlin.reflect)
	api(libs.kotlinx.coroutines)
	api(libs.kotlinx.serialization)
	api(libs.kotlinx.serialization.json)
	api(libs.kotlinx.serialization.cbor)
	api(libs.kotlinx.io)
	api(libs.kotlinx.io.bytestring)
	api(libs.kotlinx.datetime)

	common(project(":common")) { isTransitive = false }
	shadowCommon(project(":common")) { isTransitive = false }

	implementation(libs.autoService.annotations)
	ksp(libs.autoService.ksp)

	"testmodImplementation"(sourceSets["main"].output)
	"testmodImplementation"(commonTestmod.output)
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(25))
	}
}

kotlin {
	jvmToolchain(25)
}

loom {
	runs {
		named("client") {
			client()
			configName = "Fabric Client"
			ideConfigFolder = "Fabric"
			ideConfigGenerated(true)
			runDir("../.runs/client")
		}
		named("server") {
			server()
			configName = "Fabric Server"
			ideConfigFolder = "Fabric"
			ideConfigGenerated(true)
			runDir("../.runs/server")
		}
		create("testmodClient") {
			client()
			source("testmod")
			configName = "Test Fabric Client"
			ideConfigFolder = "Test Fabric"
			ideConfigGenerated(true)
			runDir("../.runs/client")
		}
		create("testmodServer") {
			server()
			source("testmod")
			configName = "Test Fabric Server"
			ideConfigFolder = "Test Fabric"
			ideConfigGenerated(true)
			runDir("../.runs/server")
		}
	}
}

tasks.processResources {
	val replaceProperties = mapOf(
		"mod_id" to project.property("mod_id"),
		"mod_name" to project.property("mod_name"),
		"mod_version" to project.property("mod_version"),
		"mod_description" to project.property("mod_description"),
		"mod_authors" to project.property("mod_authors"),
		"mod_license" to project.property("mod_license"),
		"minecraft_version" to project.property("minecraft_version"),
		"fabric_loader_version" to project.property("fabric_loader_version"),
		"fabric_api_version" to project.property("fabric_api_version")
	)
	inputs.properties(replaceProperties)
	filesMatching("fabric.mod.json") {
		expand(replaceProperties)
	}
}

tasks.named<ShadowJar>("shadowJar") {
	configurations = listOf(shadowCommon)
	archiveClassifier.set("")
}

tasks.named<Jar>("jar") {
	archiveClassifier.set("slim")
}

tasks.named("assemble") {
	dependsOn(tasks.named("shadowJar"))
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
			artifactId = "pandalib-fabric"
		}
	}

	repositories {
		maven {
			name = "LocalRepo"
			url = uri(providers.gradleProperty("LocalRepo").getOrElse("${rootProject.layout.buildDirectory.get()}/repo"))
		}
	}
}
