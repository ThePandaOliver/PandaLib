import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.shadow)
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
    compileOnlyApi(project(":common"))
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

kotlin {
    jvmToolchain(25)
}

easyModding {
    configPath = rootProject.file("easymodding.mod.json")
    minecraftVersion = "26.2"
    modId = "pandalib"

    neoForge {
        neoForgeVersion = "26.2.0.59"
    }

    runs {
        create("client") {
            client()
        }

        create("server") {
            server()
        }

        configureEach {
            workingDirectory = rootProject.file(".run")
        }
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

tasks.compileJava {
    source(project(":common").sourceSets.main.get().allSource)
}


tasks.compileKotlin {
    source(project(":common").sourceSets.main.get().allSource)
}

tasks.named<ProcessResources>("processResources") {
    from(project(":common").sourceSets.main.get().resources)
}