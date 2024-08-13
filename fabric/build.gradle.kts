import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import kotlin.text.set

// gradle.properties
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project

val modmenuVersion: String by project

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
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
	modApi("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

	modApi("com.terraformersmc:modmenu:${modmenuVersion}")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowBundle"(project(":common", "transformProductionFabric"))
}

tasks.remapJar {
	injectAccessWidener.set(true)
}