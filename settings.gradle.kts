pluginManagement {
	repositories {
		mavenLocal()
		maven("https://maven.architectury.dev/") { name = "Architectury" }
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.minecraftforge.net/") { name = "Forge" }
		maven("https://maven.neoforged.net/releases/") { name = "NeoForge" }
		gradlePluginPortal()
	}
}

include("fabric")
include("neoforge")
include("forge")

rootProject.name = "PandaLib"
