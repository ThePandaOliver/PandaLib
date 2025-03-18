import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

plugins {
	id("fabric-loom") version "1.10-SNAPSHOT"
}

loom {
	runs {
		named("client") {
			client()
			configName = "Client"
			ideConfigGenerated(true)
			runDir("../.runs/client")
			source(sourceSets["main"])
			programArgs("--username=Dev")
		}
		named("server") {
			server()
			configName = "Server"
			ideConfigGenerated(true)
			runDir("../.runs/server")
			source(sourceSets["main"])
		}
	}
}

repositories {
	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")
	}
	maven("https://maven.terraformersmc.com/releases/")
}

@Suppress("UnstableApiUsage")
dependencies {
	minecraft("net.minecraft:minecraft:${properties["minecraft_version"]}")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${properties["parchment_minecraft_version"]}:${properties["parchment_mappings_version"]}@zip")
	})
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")
	modApi("net.fabricmc.fabric-api:fabric-api:${properties["fabric_api_version"]}")

	modApi("dev.architectury:architectury-fabric:${properties["deps_architectury_version"]}")
	modApi("com.terraformersmc:modmenu:${properties["deps_modmenu_version"]}")

	common(project(":common", "namedElements")) { isTransitive = false }
	shadowBundle(project(":common"))
}

tasks.withType<ShadowJar> {
	archiveClassifier.set("dev-shadow")
}

tasks.withType<RemapJarTask> {
	val shadowJar = tasks.getByName<ShadowJar>("shadowJar")
	inputFile.set(shadowJar.archiveFile)
}