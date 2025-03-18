import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType

plugins {
	id("fabric-loom") version "1.10-SNAPSHOT"
}

repositories {
	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")
	}
}

@Suppress("UnstableApiUsage")
dependencies {
	minecraft("net.minecraft:minecraft:${properties["minecraft_version"]}")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${properties["parchment_minecraft_version"]}:${properties["parchment_mappings_version"]}@zip")
	})
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")

	modApi("dev.architectury:architectury:${properties["deps_architectury_version"]}")
}

tasks.withType<ShadowJar> {
	archiveClassifier.set("dev-shadow")
}

tasks.withType<RemapJarTask> {
	val shadowJar = tasks.getByName<ShadowJar>("shadowJar")
	inputFile.set(shadowJar.archiveFile)
}