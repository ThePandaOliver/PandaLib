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
    api(project(":core"))
    api(libs.kotlinx.serialization)
    api(libs.kotlinx.serialization.cbor)

    testImplementation(libs.kotlin.test)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

kotlin {
    jvmToolchain(21)
}

easyModding {
    fabric()
    neoForge()
    forge()

    dependencies {

    }
}
