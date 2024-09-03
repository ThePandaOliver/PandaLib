// gradle.properties
val neoForgeVersion: String by project

architectury {
	platformSetupLoomIde()
	neoForge()
}

configurations {
	getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

dependencies {
	neoForge("net.neoforged:neoforge:${neoForgeVersion}")

	implementation(project(":neoforge", "namedElements"))
	"common"(project(":common", "namedElements")) { isTransitive = false }
	"common"(project(":testmod-common", "namedElements")) { isTransitive = false }
}

tasks.shadowJar {
	exclude("fabric.mod.json")
}

tasks.remapJar {
	injectAccessWidener = true
}