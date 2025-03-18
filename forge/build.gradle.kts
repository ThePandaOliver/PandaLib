import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.getByName

plugins {
	id("net.minecraftforge.gradle") version("6.0.26")
	id("org.spongepowered.mixin") version("0.7.+")
	id("org.parchmentmc.librarian.forgegradle") version("1.+")
}

minecraft {
	mappings("parchment", "${properties["parchment_mappings_version"]}-${properties["parchment_minecraft_version"]}")
	reobf = false
	copyIdeResources.set(true)

	runs {
		create("client") {
			taskName("Client")
			workingDirectory(file("../.runs/client"))
			ideaModule("${rootProject.name}.${project.name}.main")
			args("--username", "Dev")
		}

		create("server") {
			taskName("Server")
			workingDirectory(file("../.runs/server"))
			ideaModule("${rootProject.name}.${project.name}.main")
			args("--nogui")
		}
	}
}

configurations {
	configurations["minecraftLibrary"].extendsFrom(configurations["common"])
}

repositories {
	maven("https://maven.minecraftforge.net/")
}

dependencies {
	minecraft("net.minecraftforge:forge:${properties["minecraft_version"]}-${properties["forge_version"]}")

	common(project(":common")) { isTransitive = false }
	shadowBundle(project(":common"))

	// Mixin for forge
	annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
	// Mixin Extras for forge
	annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")
	compileOnly("io.github.llamalad7:mixinextras-common:0.4.1")
	implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.4.1")) {
		jarJar.ranged(this, "[0.4.1,)")
	}
}

mixin {
	add(sourceSets.main.get(), "mixins.${project.name}.refmap.json")
	config("${project.name}.mixins.json")

	add(sourceSets.main.get(), "mixins.${project.name}-common.refmap.json")
	config("${project.name}-common.mixins.json")
}

sourceSets.forEach {
	val outputDir = layout.buildDirectory.file("sourcesSets/${it.name}").get().asFile
	it.output.setResourcesDir(outputDir)
	it.java.destinationDirectory.set(outputDir)
}

tasks.assemble {
	dependsOn("shadowJar")
}

tasks.getByName("downloadMcpConfig").dependsOn(tasks.getByName("convertAW2AT"))