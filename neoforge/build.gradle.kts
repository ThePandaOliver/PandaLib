import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	id("net.neoforged.moddev") version("2.0.78")
}

neoForge {
	version = properties["neoforge_version"] as String

	parchment {
		mappingsVersion = properties["parchment_mappings_version"] as String
		minecraftVersion = properties["parchment_minecraft_version"] as String
	}

	validateAccessTransformers = true

	runs {
		create("client") {
			client()
			gameDirectory = file("../.runs/client")
			programArguments = listOf("--username", "dev")
		}

		create("server") {
			server()
			gameDirectory = file("../.runs/server")
			programArgument("--nogui")
		}
	}
}

@Suppress("unstableApiUsage")
configurations {
	configurations["additionalRuntimeClasspath"].extendsFrom(configurations["common"])
}

dependencies {
	common(project(":common")) { isTransitive = false }
	shadowBundle(project(":common"))
}

tasks.assemble {
	dependsOn(tasks.getByName<ShadowJar>("shadowJar"))
}

tasks.getByName("createMinecraftArtifacts").dependsOn(tasks.getByName("convertAW2AT"))