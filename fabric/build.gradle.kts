plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.easymodding)
    `maven-publish`
}

base {
    archivesName.set("pandalib-fabric")
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
    modId = "pandalib"

    fabric()

    modDependencies {
        modImplementation("net.fabricmc:fabric-loader:0.19.3")
        modImplementation("net.fabricmc.fabric-api:fabric-api:0.155.2+26.2")
    }

    runs {
        configureEach {
            workingDirectory = rootProject.file(".run")
        }

        create("client") {
            client()
        }

        create("server") {
            server()
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "pandalib-fabric"
        }
    }

    repositories {
        maven {
            name = "LocalRepo"
            url = uri(providers.gradleProperty("LocalRepo"))
        }
    }
}