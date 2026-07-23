pluginManagement {
	repositories {
		mavenLocal()
		maven("E:\\MavenRepo")
		maven("https://maven.architectury.dev/") { name = "Architectury" }
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.neoforged.net/releases/") { name = "NeoForge" }
		gradlePluginPortal()
	}
}

rootProject.name = "PandaLib"

fun includeMod(name: String) = include(name, "$name:fabric", "$name:neoforge")
include("core")
include("config")
//includeMod("base")
//includeMod("pandalib-kotlin")
