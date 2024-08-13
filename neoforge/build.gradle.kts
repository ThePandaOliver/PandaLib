import org.gradle.kotlin.dsl.tasks

// gradle.properties
val neoForgeVersion: String by project

architectury {
	platformSetupLoomIde()
	neoForge()
}

configurations {
	getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

dependencies {
	neoForge("net.neoforged:neoforge:${neoForgeVersion}")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionNeoForge"))
}

tasks.shadowJar {
	exclude("fabric.mod.json")
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}