import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

architectury {
	platformSetupLoomIde()
	neoForge()
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
	getByName("developmentNeoForge").extendsFrom(configurations["common"])
	forgeRuntimeLibrary.get().extendsFrom(configurations["fullShadow"])
}

dependencies {
	neoForge("net.neoforged:neoforge:${properties["neoforge_version"]}")

	common(project(":common", "namedElements")) { isTransitive = false }
	shadowBundle(project(":common", "transformProductionNeoForge"))
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}

tasks.withType<RemapJarTask> {
	val shadowJar = tasks.getByName<ShadowJar>("shadowJar")
	inputFile.set(shadowJar.archiveFile)
}