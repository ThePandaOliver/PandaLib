@file:Suppress("UnstableApiUsage")

plugins {
	java
	alias(libs.plugins.architecturyPlugin)
	alias(libs.plugins.architecturyLoom)
}

architectury {
	minecraft = libs.versions.minecraft.get()
	platformSetupLoomIde()
	neoForge()
}

loom {
	accessWidenerPath = project(":common").loom.accessWidenerPath
}

configurations {
//	getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

repositories {
	mavenCentral()
}

dependencies {
	minecraft(libs.minecraft)
	mappings(loom.layered {
		officialMojangMappings()
		parchment("${libs.parchment.get()}@zip")
	})
	neoForge(libs.neoforgeLoader)

	modApi(libs.architectury.neoforge)

	implementation(libs.bundles.kotlin)
	include(libs.bundles.kotlin)
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
}