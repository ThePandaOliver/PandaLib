@file:Suppress("UnstableApiUsage")

architectury {
	neoForge()
}

configurations {
	getByName("developmentNeoForge").extendsFrom(common.get())
}

dependencies {
	neoForge(libs.neoforgeLoader)
	modApi(libs.architectury.neoforge)

	include(libs.bundles.kotlin)
	forgeRuntimeLibrary(libs.bundles.kotlin)
	include(libs.bundles.jackson)
	forgeRuntimeLibrary(libs.bundles.jackson)
	common(project(":", configuration = "namedElements")) { isTransitive = false }
	shadowBundle(project(":", configuration = "transformProductionNeoForge"))
}

tasks.remapJar {
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}