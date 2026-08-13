plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.easymodding)
    `maven-publish`
}

base {
    archivesName.set("pandalib-neoforge")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(project(":common"))
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

kotlin {
    jvmToolchain(25)
}

easyModding {
    configPath = rootProject.file("easymodding.mod.json")
    minecraftVersion = "26.2"

    neoForge {
        neoForgeVersion = "26.2.0.59"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "pandalib-neoforge"
        }
    }

    repositories {
        maven {
            name = "LocalRepo"
            url = uri(providers.gradleProperty("LocalRepo"))
        }
    }
}