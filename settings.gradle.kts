/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

pluginManagement {
	repositories {
		mavenLocal()
		gradlePluginPortal()
		maven("https://maven.architectury.dev/") { name = "Architectury" }
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.minecraftforge.net/") { name = "Forge" }
		maven("https://maven.neoforged.net/releases/") { name = "NeoForge" }
		maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
		maven("https://maven.pkg.github.com/ThePandaOliver/Forgix") {
			name = "Github"
			credentials {
				username = System.getenv("GITHUB_USER")
				password = System.getenv("GITHUB_API_TOKEN")
			}
		}
	}
}

plugins {
	id("dev.kikugie.stonecutter") version "0.7.6"
}

rootProject.name = "PandaLib"

stonecutter {
	create(rootProject) {
		fun mc(mcVersion: String, name: String = mcVersion, loaders: Iterable<String>) {
			for (loader in loaders) {
				vers("$name-$loader", mcVersion)
			}
		}

		mc("1.21.10", loaders = listOf("fabric", "neoforge"))
//		mc("1.21.9", loaders = listOf("fabric", "neoforge"))
//		mc("1.21.8", loaders = listOf("fabric", "neoforge"))
	}
}
