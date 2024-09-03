// gradle.properties
val modId: String by project

val forgeVersion: String by project

architectury {
	platformSetupLoomIde()
	forge()
}

loom {
	forge {
		convertAccessWideners.set(true)
	}
}

configurations {
	getByName("developmentForge").extendsFrom(configurations["common"])
	// Required for embedding libraries into the jar because Forge is weird.
	getByName("forgeRuntimeLibrary").extendsFrom(configurations["jarShadow"])
}

dependencies {
	forge("net.minecraftforge:forge:${forgeVersion}")

	implementation(project(":forge", "namedElements"))
	"common"(project(":common", "namedElements")) { isTransitive = false }
	"common"(project(":testmod-common", "namedElements")) { isTransitive = false }
}

tasks.shadowJar {
	exclude("fabric.mod.json")
}

tasks.remapJar {
	injectAccessWidener = true
}