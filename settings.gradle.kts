pluginManagement {
    repositories {
        maven("https://maven.architectury.dev/") { name = "Architectury" }
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForge" }
        gradlePluginPortal()
    }
}

rootProject.name = "PandaLib"

include("config")
include("wrappers")

include("impl:21_11:fabric")

include("mod")
include("mod:fabric")
include("mod:neoforge")