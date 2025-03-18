pluginManagement.repositories {
	maven {
		name = "Fabric"
		url = uri("https://maven.fabricmc.net/")
	}
	maven {
		name = "Forge"
		url = uri("https://maven.minecraftforge.net/")
	}
	maven {
		name = "NeoForge"
		url = uri("https://maven.neoforged.net/releases/")
	}
	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")
	}
	gradlePluginPortal()
}

include("common")
include("fabric")
include("neoforge")
