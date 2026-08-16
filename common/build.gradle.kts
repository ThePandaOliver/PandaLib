plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.easymodding)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

base {
    archivesName.set("pandalib")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.serialization)
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.serialization.cbor)
    api(libs.kotlinx.io)
    api(libs.kotlinx.io.bytestring)
    api(libs.kotlinx.datetime)

    testImplementation(libs.kotlin.test)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

kotlin {
    jvmToolchain(25)
}

easyModding {
    configPath = rootProject.file("easymodding.mod.json")
    minecraftVersion = "26.2"
    modId = "pandalib"

    modDependencies {
        modCompileOnly("net.fabricmc:fabric-loader:0.19.3")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "pandalib-common"
        }
    }

    repositories {
        maven {
            name = "LocalRepo"
            url = uri(providers.gradleProperty("LocalRepo"))
        }
    }
}