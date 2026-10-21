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

val commonSourceSets = project(":common").extensions.getByType<SourceSetContainer>()
val commonTestmod = commonSourceSets["testmod"]
val commonTestmodClient = commonSourceSets["testmodClient"]

val client = sourceSets.create("client") {
	compileClasspath += sourceSets["main"].output + commonSourceSets["main"].output + commonSourceSets["client"].output
	runtimeClasspath += sourceSets["main"].output + commonSourceSets["main"].output + commonSourceSets["client"].output
}

val testmod = sourceSets.create("testmod") {
	compileClasspath += sourceSets["main"].output + commonSourceSets["main"].output + commonTestmod.output
	runtimeClasspath += sourceSets["main"].output + commonSourceSets["main"].output + commonTestmod.output
}

val testmodClient = sourceSets.create("testmodClient") {
	compileClasspath += sourceSets["main"].output + client.output + testmod.output +
		commonSourceSets["main"].output + commonSourceSets["client"].output + commonTestmod.output + commonTestmodClient.output
	runtimeClasspath += sourceSets["main"].output + client.output + testmod.output +
		commonSourceSets["main"].output + commonSourceSets["client"].output + commonTestmod.output + commonTestmodClient.output
}

neoForge {
	enable {
		version = project.property("neoforge_version") as String
		enabledSourceSets = setOf(client, testmodClient)
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
			sourceSet = testmodClient
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
			sourceSet(client)
			sourceSet(commonSourceSets.named("main").get())
			sourceSet(commonSourceSets.named("client").get())
		}
		create("pandalib_test") {
			sourceSet(testmod)
			sourceSet(testmodClient)
			sourceSet(commonTestmod)
			sourceSet(commonTestmodClient)
		}
	}
}

val clientOnlyPackagePrefixes = listOf(
	"net/minecraft/client/",
	"com/mojang/blaze3d/",
	"net/neoforged/neoforge/client/",
	"net/neoforged/fml/client/"
)

val createMinecraftArtifacts = tasks.named<CreateMinecraftArtifacts>("createMinecraftArtifacts")
val serverOnlyMinecraftJarFile = layout.buildDirectory.file("moddev/artifacts/minecraft-patched-${project.property("neoforge_version")}-server-only.jar")

val createServerOnlyMinecraftJar = tasks.register("createServerOnlyMinecraftJar") {
	dependsOn(createMinecraftArtifacts)
	val mergedJarProvider = createMinecraftArtifacts.flatMap { it.gameJarWithSourcesArtifact }
	val compiledJarProvider = createMinecraftArtifacts.flatMap { it.gameJarArtifact }
	inputs.files(mergedJarProvider, compiledJarProvider)
	outputs.file(serverOnlyMinecraftJarFile)

	doLast {
		val sourceJar = mergedJarProvider.orNull?.asFile?.takeIf { it.exists() }
			?: compiledJarProvider.get().asFile
		val outJar = serverOnlyMinecraftJarFile.get().asFile
		outJar.parentFile.mkdirs()

		ZipInputStream(sourceJar.inputStream().buffered()).use { zis ->
			ZipOutputStream(outJar.outputStream().buffered()).use { zos ->
				var entry = zis.nextEntry
				while (entry != null) {
					val name = entry.name
					val isClientOnly = clientOnlyPackagePrefixes.any { name.startsWith(it) }
					if (!isClientOnly) {
						zos.putNextEntry(entry)
						zis.copyTo(zos)
						zos.closeEntry()
					}
					zis.closeEntry()
					entry = zis.nextEntry
				}
			}
		}
	}
}

neoForge.ideSyncTask(createServerOnlyMinecraftJar)

val modDevServerCompileDependencies = configurations.create("modDevServerCompileDependencies") {
	isCanBeResolved = false
	isCanBeConsumed = false
	withDependencies {
		add(project.dependencies.create(files(createServerOnlyMinecraftJar)))
		configurations.getByName("modDevCompileDependencies").dependencies
			.withType<ModuleDependency>()
			.forEach { add(it.copy()) }
	}
}

configurations {
	compileClasspath {
		extendsFrom(commonMain, modDevServerCompileDependencies)
	}
	runtimeClasspath {
		extendsFrom(commonMain, configurations["modDevRuntimeDependencies"])
	}
	named("clientCompileClasspath") {
		extendsFrom(configurations["api"], configurations["implementation"], commonMain, commonClient)
	}
	named("clientRuntimeClasspath") {
		extendsFrom(configurations["runtimeClasspath"], commonClient)
	}
	named("testmodCompileClasspath") {
		extendsFrom(configurations["compileClasspath"])
	}
	named("testmodRuntimeClasspath") {
		extendsFrom(configurations["runtimeClasspath"])
	}
	named("testmodClientCompileClasspath") {
		extendsFrom(configurations["clientCompileClasspath"], configurations["testmodImplementation"])
	}
	named("testmodClientRuntimeClasspath") {
		extendsFrom(configurations["clientRuntimeClasspath"], configurations["testmodRuntimeClasspath"])
	}
}

dependencies {
	commonMain(commonSourceSets["main"].output)
	commonClient(commonSourceSets["client"].output)
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
	"clientImplementation"(libs.autoService.annotations)
	ksp(libs.autoService.ksp)
	"kspClient"(libs.autoService.ksp)

	"testmodImplementation"(sourceSets["main"].output)
	"testmodImplementation"(commonTestmod.output)

	"testmodClientImplementation"(sourceSets["main"].output)
	"testmodClientImplementation"(client.output)
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
		client.output.classesDirs,
		testmod.output.classesDirs,
		commonSourceSets["main"].output.classesDirs,
		commonSourceSets["client"].output.classesDirs
	)
}

/**
 * Scans compiled .class constant pools in server/common source sets (`main` and `testmod`)
 * to prevent accidental references to client-only Minecraft or NeoForge classes.
 */
fun registerClientReferenceCheck(sourceSet: SourceSet) {
	val taskName = "verifyNoClientReferencesIn${sourceSet.name.replaceFirstChar { it.uppercase() }}"
	val checkTask = tasks.register(taskName) {
		dependsOn(sourceSet.classesTaskName)
		val classDirs = sourceSet.output.classesDirs
		inputs.files(classDirs)

		doLast {
			val violations = mutableListOf<String>()

			classDirs.asFileTree.matching { include("**/*.class") }.forEach { classFile ->
				val utf8Strings = readConstantPoolUtf8Strings(classFile)
				for (entry in utf8Strings) {
					val matchedPrefix = clientOnlyPackagePrefixes.firstOrNull { prefix ->
						entry.startsWith(prefix) || entry.contains("L$prefix")
					}
					if (matchedPrefix != null) {
						violations.add("${classFile.name} references client-only symbol '$entry'")
					}
				}
			}

			if (violations.isNotEmpty()) {
				error(
					"Client-only references detected in '${sourceSet.name}' source set (move this code to 'src/client'):\n" +
						violations.joinToString("\n") { "  - $it" }
				)
			}
		}
	}

	tasks.named(sourceSet.classesTaskName) {
		finalizedBy(checkTask)
	}
}

fun readConstantPoolUtf8Strings(classFile: File): List<String> {
	val strings = mutableListOf<String>()
	DataInputStream(classFile.inputStream().buffered()).use { input ->
		val magic = input.readInt()
		if (magic != 0xCAFEBABE.toInt()) return emptyList()
		input.readUnsignedShort() // minor
		input.readUnsignedShort() // major
		val cpCount = input.readUnsignedShort()
		var i = 1
		while (i < cpCount) {
			when (val tag = input.readUnsignedByte()) {
				1 -> strings.add(input.readUTF()) // CONSTANT_Utf8
				3, 4 -> input.skipBytes(4) // Integer, Float
				5, 6 -> {
					input.skipBytes(8) // Long, Double take 2 slots
					i++
				}
				7, 8, 16, 19, 20 -> input.skipBytes(2) // Class, String, MethodType, Module, Package
				9, 10, 11, 12, 17, 18 -> input.skipBytes(4) // Fieldref, Methodref, InterfaceMethodref, NameAndType, Dynamic, InvokeDynamic
				15 -> input.skipBytes(3) // MethodHandle
				else -> error("Unknown constant pool tag $tag in ${classFile.name}")
			}
			i++
		}
	}
	return strings
}

registerClientReferenceCheck(sourceSets["main"])
registerClientReferenceCheck(testmod)

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
	from(client.output)
	configurations = listOf(shadowCommon)
	archiveClassifier.set("")
	mergeServiceFiles()
}

tasks.named<Jar>("jar") {
	from(client.output)
	archiveClassifier.set("slim")
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
