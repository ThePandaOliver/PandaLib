import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.shadow)
	`maven-publish`
}

evaluationDependsOn(":common")

base {
	archivesName.set("pandalib-fabric")
}

repositories {
	mavenLocal()
	mavenCentral()
}

loom {
	splitEnvironmentSourceSets()
}

val commonMain = configurations.create("commonMain") {
	isCanBeResolved = true
	isCanBeConsumed = false
}

val commonClient = configurations.create("commonClient") {
	isCanBeResolved = true
	isCanBeConsumed = false
}

val shadowCommon = configurations.create("shadowCommon") {
	isCanBeResolved = true
	isCanBeConsumed = false
}

configurations.compileClasspath.get().extendsFrom(commonMain)
configurations.runtimeClasspath.get().extendsFrom(commonMain)
configurations.named("clientCompileClasspath").get().extendsFrom(commonClient)
configurations.named("clientRuntimeClasspath").get().extendsFrom(commonClient)

val commonSourceSets = project(":common").extensions.getByType<SourceSetContainer>()
val commonTestmod = commonSourceSets["testmod"]
val commonTestmodClient = commonSourceSets["testmodClient"]

val testmod = sourceSets.create("testmod") {
	compileClasspath += sourceSets["main"].output + commonSourceSets["main"].output + commonTestmod.output
	runtimeClasspath += sourceSets["main"].output + commonSourceSets["main"].output + commonTestmod.output
}

val testmodClient = sourceSets.create("testmodClient") {
	compileClasspath += sourceSets["main"].output + sourceSets["client"].output + testmod.output +
		commonSourceSets["main"].output + commonSourceSets["client"].output + commonTestmod.output + commonTestmodClient.output
	runtimeClasspath += sourceSets["main"].output + sourceSets["client"].output + testmod.output +
		commonSourceSets["main"].output + commonSourceSets["client"].output + commonTestmod.output + commonTestmodClient.output
}

configurations {
	named("testmodCompileClasspath") {
		extendsFrom(configurations["compileClasspath"])
	}
	named("testmodRuntimeClasspath") {
		extendsFrom(configurations["runtimeClasspath"])
	}
	named("testmodClientCompileClasspath") {
		extendsFrom(configurations["clientCompileClasspath"], configurations["testmodCompileClasspath"])
	}
	named("testmodClientRuntimeClasspath") {
		extendsFrom(configurations["clientRuntimeClasspath"], configurations["testmodRuntimeClasspath"])
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

	commonMain(commonSourceSets["main"].output)
	commonClient(commonSourceSets["client"].output)
	shadowCommon(project(":common")) { isTransitive = false }

	implementation(libs.autoService.annotations)
	"clientImplementation"(libs.autoService.annotations)
	ksp(libs.autoService.ksp)
	"kspClient"(libs.autoService.ksp)

	"testmodImplementation"(sourceSets["main"].output)
	"testmodImplementation"(commonTestmod.output)

	"testmodClientImplementation"(sourceSets["main"].output)
	"testmodClientImplementation"(sourceSets["client"].output)
	"testmodClientImplementation"(testmod.output)
	"testmodClientImplementation"(commonTestmod.output)
	"testmodClientImplementation"(commonTestmodClient.output)
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(25))
	}
}

kotlin {
	jvmToolchain(25)
	target {
		val mainCompilation = compilations.getByName("main")
		val clientCompilation = compilations.getByName("client")
		val testmodCompilation = compilations.getByName("testmod")
		val testmodClientCompilation = compilations.getByName("testmodClient")

		clientCompilation.associateWith(mainCompilation)
		testmodCompilation.associateWith(mainCompilation)
		testmodClientCompilation.associateWith(mainCompilation)
		testmodClientCompilation.associateWith(clientCompilation)
		testmodClientCompilation.associateWith(testmodCompilation)
	}
}

tasks.named<KotlinCompile>("compileClientKotlin") {
	friendPaths.setFrom(
		sourceSets["main"].output.classesDirs,
		commonSourceSets["main"].output.classesDirs,
		commonSourceSets["client"].output.classesDirs
	)
}

tasks.named<KotlinCompile>("compileTestmodKotlin") {
	friendPaths.setFrom(
		sourceSets["main"].output.classesDirs,
		commonSourceSets["main"].output.classesDirs
	)
}

tasks.named<KotlinCompile>("compileTestmodClientKotlin") {
	friendPaths.setFrom(
		sourceSets["main"].output.classesDirs,
		sourceSets["client"].output.classesDirs,
		testmod.output.classesDirs,
		commonSourceSets["main"].output.classesDirs,
		commonSourceSets["client"].output.classesDirs
	)
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
			source(testmodClient)
			configName = "Test Fabric Client"
			ideConfigFolder = "Test Fabric"
			ideConfigGenerated(true)
			runDir("../.runs/client")
		}
		create("testmodServer") {
			server()
			source(testmod)
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
	from(sourceSets["client"].output)
	configurations = listOf(shadowCommon)
	archiveClassifier.set("")
	mergeServiceFiles()
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
