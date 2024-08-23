// gradle.properties
val modId: String by project
val projectGroup: String by project

val forgeVersion: String by project

architectury {
	platformSetupLoomIde()
	forge()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)

	forge {
		convertAccessWideners.set(true)
		extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)

		// Fixes Mixin Patcher issue with Forge
		useCustomMixin.set(true)

		mixinConfig("${modId}-common.mixins.json")
		mixinConfig("${modId}.mixins.json")
	}
}

configurations {
	getByName("developmentForge").extendsFrom(configurations["common"])
	// Required for embedding libraries into the jar because Forge is weird.
	getByName("forgeRuntimeLibrary").extendsFrom(configurations["jarShadow"])
}

dependencies {
	forge("net.minecraftforge:forge:${forgeVersion}")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionForge"))
}

tasks.shadowJar {
	exclude("fabric.mod.json")
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}