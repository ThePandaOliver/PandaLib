@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	java
	alias(libs.plugins.architecturyPlugin)
	alias(libs.plugins.architecturyLoom)
	alias(libs.plugins.shadow)
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
	getByName("developmentFabric").extendsFrom(common.get())
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

	common(project(":common", configuration = "namedElements")) { isTransitive = false }
	shadowBundle(project(":common", configuration = "transformProductionFabric"))
}

tasks.shadowJar {
	configurations = listOf(project.configurations.shadowBundle.get())
	archiveClassifier.set("dev-shadow")
	
	exclude("architectury.common.json")
}

tasks.remapJar {
	injectAccessWidener.set(true)
}