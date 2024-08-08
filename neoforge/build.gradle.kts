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
	"shadowCommon"(project(":common", "transformProductionNeoForge")) { isTransitive = false }
}

tasks {
	base.archivesName.set(base.archivesName.get() + "-NeoForge")

	shadowJar {
		exclude("fabric.mod.json")
	}

	remapJar {
		injectAccessWidener = true
		atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
	}

	sourcesJar {
		val commonSources = project(":common").tasks.sourcesJar
		dependsOn(commonSources)
		from(commonSources.get().archiveFile.map { zipTree(it) })
	}
}