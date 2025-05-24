@file:Suppress("UnstableApiUsage")

plugins {
	java
	`maven-publish`
	alias(libs.plugins.architecturyPlugin)
	alias(libs.plugins.architecturyLoom)
}

architectury {
	minecraft = libs.versions.minecraft.get()
	common("neoforge", "fabric")
}

loom {
	accessWidenerPath = file("src/main/resources/${properties["mod_id"]}.accesswidener")
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

	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation(libs.fabricLoader)

	modApi(libs.architectury.common)
	api(libs.bundles.kotlin)
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			artifactId = project.base.archivesName.get()
			version = "${project.version}-SNAPSHOT"
			from(components["java"])
		}
	}
}