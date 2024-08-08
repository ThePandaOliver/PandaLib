// gradle.properties
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project

val modmenuVersion: String by project

architectury {
	platformSetupLoomIde()
	fabric()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

configurations {
	getByName("developmentFabric").extendsFrom(configurations["common"])
}

repositories {
	maven { url = uri("https://maven.terraformersmc.com/releases/") }
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
	modApi("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

	modApi("com.terraformersmc:modmenu:${modmenuVersion}")

	"common"(project(":common", "namedElements")) { isTransitive = false }
	"shadowCommon"(project(":common", "transformProductionFabric")) { isTransitive = false }
}

tasks {
	base.archivesName.set(base.archivesName.get() + "-Fabric")

	remapJar {
		injectAccessWidener.set(true)
	}

	sourcesJar {
		val commonSources = project(":common").tasks.sourcesJar
		dependsOn(commonSources)
		from(commonSources.get().archiveFile.map { zipTree(it) })
	}
}

components {
	java.run {
		if (this is AdhocComponentWithVariants)
			withVariantsFromConfiguration(project.configurations.shadowRuntimeElements.get()) { skip() }
	}
}

publishing {
	publications.create<MavenPublication>("mavenFabric") {
		artifactId = "${project.properties["archives_base_name"]}" + "-Fabric"
		from(components["java"])
	}

	repositories {
		mavenLocal()
		maven {
			val releasesRepoUrl = "https://example.com/releases"
			val snapshotsRepoUrl = "https://example.com/snapshots"
			url = uri(if (project.version.toString().endsWith("SNAPSHOT") || project.version.toString().startsWith("0")) snapshotsRepoUrl else releasesRepoUrl)
			name = "ExampleRepo"
			credentials {
				username = project.properties["repoLogin"]?.toString()
				password = project.properties["repoPassword"]?.toString()
			}
		}
	}
}