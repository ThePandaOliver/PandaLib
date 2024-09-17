architectury {
	platformSetupLoomIde()
	fabric()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

configurations {
	getByName("developmentFabric").extendsFrom(configurations["common"])
}

repositories {
	maven { url = uri("https://maven.terraformersmc.com/releases/") }
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")
	modApi("net.fabricmc.fabric-api:fabric-api:${properties["fabric_api_version"]}")

	modApi("com.terraformersmc:modmenu:${properties["deps_modmenu_version"]}")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionFabric"))
}

tasks.remapJar {
	injectAccessWidener.set(true)
}