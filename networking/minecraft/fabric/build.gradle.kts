plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.easymodding)
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(project(":networking:minecraft"))
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

kotlin {
    jvmToolchain(21)
}

easyModding {
    configPath = project(":networking").file("easymodding.mod.json")
    minecraftVersion = "26.2"

    fabric()

    modDependencies {
        modImplementation("net.fabricmc:fabric-loader:0.19.3")
        modImplementation("net.fabricmc.fabric-api:fabric-api:0.155.2+26.2")
    }
}