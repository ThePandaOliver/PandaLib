plugins {
	id("dev.kikugie.stonecutter")
	kotlin("jvm") version "2.2.0" apply false
	id("architectury-plugin") version "3.4-SNAPSHOT" apply false
	id("dev.architectury.loom") version "1.11-SNAPSHOT" apply false
	id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.10" apply false
	id("io.github.pacifistmc.forgix") version "2.0.0-SNAPSHOT.5.1-FORK.3"

	id("me.modmuss50.mod-publish-plugin") version "0.8.4"
	id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
}
stonecutter active "1.21.10-fabric"