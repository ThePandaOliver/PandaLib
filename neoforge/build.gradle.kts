import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.neoforged.nfrtgradle.CreateMinecraftArtifacts
import org.gradle.api.artifacts.ModuleDependency
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.DataInputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.shadow)
	alias(libs.plugins.neoforge.moddev)
	`maven-publish`
}

evaluationDependsOn(":common")

base {
	archivesName.set("pandalib-neoforge")
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

val commonSourceSets = project(":common").extensions.getByType<SourceSetContainer>()
val commonTestmod = commonSourceSets["testmod"]

val testmod = sourceSets.create("testmod") {
	compileClasspath += sourceSets["main"].output + commonSourceSets["main"].output + commonTestmod.output
	runtimeClasspath += sourceSets["main"].output + commonSourceSets["main"].output + commonTestmod.output
}

neoForge {
	enable {
		version = project.property("neoforge_version") as String
	}

	runs {
		create("client") {
			client()
			ideName = "NeoForge Client"
			ideFolderName = "NeoForge"
			gameDirectory.set(rootProject.file(".runs/client"))
		}
		create("server") {
			server()
			ideName = "NeoForge Server"
			ideFolderName = "NeoForge"
			gameDirectory.set(rootProject.file(".runs/server"))
		}
		create("testmodClient") {
			client()
			sourceSet = testmod
			ideName = "Test NeoForge Client"
			ideFolderName = "Test NeoForge"
			gameDirectory.set(rootProject.file(".runs/client"))
		}
		create("testmodServer") {
			server()
			sourceSet = testmod
			ideName = "Test NeoForge Server"
			ideFolderName = "Test NeoForge"
			gameDirectory.set(rootProject.file(".runs/server"))
		}
	}

	mods {
		create("pandalib") {
			sourceSet(sourceSets.main.get())
			sourceSet(commonTestmod)
		}
		create("pandalib_test") {
			sourceSet(testmod)
			sourceSet(commonTestmod)
		}
	}
}

configurations {
	compileClasspath {
		extendsFrom(common)
	}
	runtimeClasspath {
		extendsFrom(common)
	}
	named("testmodCompileClasspath") {
		extendsFrom(configurations["compileClasspath"])
	}
	named("testmodRuntimeClasspath") {
		extendsFrom(configurations["runtimeClasspath"])
	}
}

dependencies {
	common(project(":common")) { isTransitive = false }
	shadowCommon(project(":common")) { isTransitive = false }

	api(libs.kotlin.reflect)
	api(libs.kotlinx.coroutines)
	api(libs.kotlinx.serialization)
	api(libs.kotlinx.serialization.json)
	api(libs.kotlinx.serialization.cbor)
	api(libs.kotlinx.io)
	api(libs.kotlinx.io.bytestring)
	api(libs.kotlinx.datetime)

	testImplementation(libs.kotlin.test)

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

tasks.processResources {
	val replaceProperties = mapOf(
		"mod_id" to project.property("mod_id"),
		"mod_name" to project.property("mod_name"),
		"mod_version" to project.property("mod_version"),
		"mod_description" to project.property("mod_description"),
		"mod_authors" to project.property("mod_authors"),
		"mod_license" to project.property("mod_license"),
		"minecraft_version" to project.property("minecraft_version"),
		"neoforge_version" to project.property("neoforge_version")
	)
	inputs.properties(replaceProperties)
	filesMatching(listOf("META-INF/neoforge.mods.toml", "pack.mcmeta", "pandalib-neoforge.mixins.json")) {
		expand(replaceProperties)
	}
}

tasks.named<ShadowJar>("shadowJar") {
	configurations = listOf(shadowCommon)
	archiveClassifier.set("")
	mergeServiceFiles()
}

tasks.named("assemble") {
	dependsOn(tasks.named("shadowJar"))
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
			artifactId = "pandalib-neoforge"
		}
	}

	repositories {
		maven {
			name = "LocalRepo"
			url = uri(providers.gradleProperty("LocalRepo").getOrElse("${rootProject.layout.buildDirectory.get()}/repo"))
		}
	}
}
