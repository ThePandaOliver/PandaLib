pluginManagement {
	repositories {
		mavenLocal()
		maven(providers.gradleProperty("LocalRepo"))
		maven("https://maven.architectury.dev/") { name = "Architectury" }
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.neoforged.net/releases/") { name = "NeoForge" }
		gradlePluginPortal()
	}
}

rootProject.name = "PandaLib"

fun includeMod(name: String) = include("$name:common", "$name:fabric")
include("core")
include("config")
includeMod("networking")
//includeMod("base")
//includeMod("pandalib-kotlin")
