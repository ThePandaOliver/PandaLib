// gradle.properties
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project

val modmenuVersion: String by project

architectury {
	platformSetupLoomIde()
	fabric()
}

configurations {
	getByName("developmentFabric").extendsFrom(configurations["common"])
}

repositories {
	maven { url = uri("https://maven.terraformersmc.com/releases/") }
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
	modApi("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

	modApi("com.terraformersmc:modmenu:${modmenuVersion}")

	implementation(project(":fabric", "namedElements"))
	"common"(project(":common", "namedElements")) { isTransitive = false }
	"common"(project(":testmod-common", "namedElements")) { isTransitive = false }
}

tasks.remapJar {
	injectAccessWidener.set(true)
}