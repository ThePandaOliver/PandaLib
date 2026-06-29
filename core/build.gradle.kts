plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
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
    api(libs.kotlinx.datetime)
    api(libs.kotlinx.io)
    api(libs.kotlinx.io.bytestring)

    testImplementation(libs.kotlin.test)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

kotlin {
    jvmToolchain(21)
}
