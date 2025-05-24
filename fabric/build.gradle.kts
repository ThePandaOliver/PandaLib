@file:Suppress("UnstableApiUsage")

plugins {
	java
	alias(libs.plugins.architecturyPlugin)
	alias(libs.plugins.architecturyLoom)
}

architectury {
	minecraft = libs.versions.minecraft.get()
	platformSetupLoomIde()
	fabric()
}

loom {
	accessWidenerPath = project(":common").loom.accessWidenerPath
}

configurations {
//	getByName("developmentFabric").extendsFrom(configurations["common"])
}

repositories {
	mavenCentral()
	maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
	minecraft(libs.minecraft)
	mappings(loom.layered {
		officialMojangMappings()
		parchment("${libs.parchment.get()}@zip")
	})
	modImplementation(libs.fabricLoader)
	modApi(libs.fabricApi)

	modApi(libs.architectury.fabric)
	modApi(libs.modmenu)

	implementation(libs.bundles.kotlin)
	include(libs.bundles.kotlin)
}

tasks.remapJar {
	injectAccessWidener.set(true)
}