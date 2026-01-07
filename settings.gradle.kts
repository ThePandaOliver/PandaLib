pluginManagement {
	repositories {
		mavenLocal()
		maven("https://maven.architectury.dev/") { name = "Architectury" }
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.minecraftforge.net/") { name = "Forge" }
		maven("https://maven.neoforged.net/releases/") { name = "NeoForge" }
		maven("https://repo.pandasystems.dev/repository/maven-public/") { name = "PandasRepository" }
		gradlePluginPortal()
	}
}

include("fabric")

rootProject.name = "PandaLib"
