@file:Suppress("UnstableApiUsage")

architectury {
	fabric()
}

configurations {
	getByName("developmentFabric").extendsFrom(common.get())
}

repositories {
	maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
	modImplementation(libs.fabricLoader)
	modApi(libs.fabricApi)
	modApi(libs.architectury.fabric)
	modApi(libs.modmenu)

	include(libs.bundles.kotlin)
	common(project(":", configuration = "namedElements")) { isTransitive = false }
	shadowBundle(project(":", configuration = "transformProductionFabric"))
}