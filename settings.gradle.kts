pluginManagement.repositories {
	maven {
		name = "Architectury"
		url = uri("https://maven.architectury.dev/")
	}
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
	gradlePluginPortal()
	mavenLocal()
}

plugins {
	id("org.ajoberstar.reckon.settings") version "0.19.2"
}

extensions.configure<org.ajoberstar.reckon.gradle.ReckonExtension> {
	setDefaultInferredScope("patch")
	stages("beta", "rc", "final")
	setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
	setStageCalc(calcStageFromProp())
}

include("fabric")
include("neoforge")

rootProject.name = "PandaLib"