plugins {
	id("net.neoforged.moddev") version("2.0.78")
}

neoForge {
	neoFormVersion = properties["neoform_version"] as String

	parchment {
		mappingsVersion = properties["parchment_mappings_version"] as String
		minecraftVersion = properties["parchment_minecraft_version"] as String
	}

	validateAccessTransformers = true
}

repositories {
	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")
	}
	maven {
		name = "Fabric"
		url = uri("https://maven.fabricmc.net/")
	}
}

@Suppress("UnstableApiUsage")
dependencies {
	compileOnly("org.spongepowered:mixin:0.8.5")
	compileOnly("io.github.llamalad7:mixinextras-common:0.4.1")
}

tasks.getByName("createMinecraftArtifacts").dependsOn(tasks.getByName("convertAW2AT"))