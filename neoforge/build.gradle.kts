@file:Suppress("UnstableApiUsage")

plugins {
	java
	`maven-publish`
	alias(libs.plugins.architecturyPlugin)
	alias(libs.plugins.architecturyLoom)
	alias(libs.plugins.shadow)
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
	getByName("developmentNeoForge").extendsFrom(common.get())
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

	api(libs.bundles.kotlin)
	include(libs.bundles.kotlin)

	common(project(":common", configuration = "namedElements")) { isTransitive = false }
	shadowBundle(project(":common", configuration = "transformProductionNeoForge"))
}

tasks.shadowJar {
	configurations = listOf(project.configurations.shadowBundle.get())
	archiveClassifier.set("dev-shadow")

	exclude("architectury.common.json")
}

tasks.remapJar {
	injectAccessWidener = true
	atAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
	inputFile = tasks.shadowJar.get().archiveFile
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			artifactId = project.base.archivesName.get()
			version = "${project.version}"
			from(components["java"])
		}
	}
	
	repositories {
		maven {
			name = "local"
			url = file("C:\\Users\\Olive\\.m2\\repository").toURI()
		}
	}
}