pluginManagement {
	repositories {
		mavenLocal()
		maven("https://maven.architectury.dev/") { name = "Architectury" }
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.neoforged.net/releases/") { name = "NeoForge" }
		gradlePluginPortal()
	}
}

rootProject.name = "PandaLib"

fun includeMod(name: String) = include(name, "$name:fabric", "$name:neoforge")
include("core")
//includeMod("config")
//includeMod("base")
//includeMod("pandalib-kotlin")
