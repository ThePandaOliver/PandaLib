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
