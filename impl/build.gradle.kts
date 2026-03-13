plugins {
    kotlin("jvm")
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
        sourceCompatibility = JavaVersion.toVersion(21)
        targetCompatibility = JavaVersion.toVersion(21)
        withSourcesJar()
    }

    kotlin {
        jvmToolchain(21)
    }
}