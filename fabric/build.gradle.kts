import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

architectury {
	platformSetupLoomIde()
	fabric()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)

	runs {
		named("client") {
			client()
			configName = "Client"
			ideConfigGenerated(true)
			runDir("../.runs/client")
			source(sourceSets["main"])
			programArgs("--username=Dev")
		}
		named("server") {
			server()
			configName = "Server"
			ideConfigGenerated(true)
			runDir("../.runs/server")
			source(sourceSets["main"])
		}
	}
}

@Suppress("UnstableApiUsage")
configurations {
	getByName("developmentFabric").extendsFrom(configurations["common"])
}

repositories {
	maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")
	modApi("net.fabricmc.fabric-api:fabric-api:${properties["fabric_api_version"]}")

	modApi("com.terraformersmc:modmenu:${properties["deps_modmenu_version"]}")

	common(project(":common", "namedElements")) { isTransitive = false }
	shadowBundle(project(":common", "transformProductionFabric"))
}

tasks.remapJar {
	injectAccessWidener.set(true)
}

tasks.withType<RemapJarTask> {
	val shadowJar = tasks.getByName<ShadowJar>("shadowJar")
	inputFile.set(shadowJar.archiveFile)
}