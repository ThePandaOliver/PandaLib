plugins {
    kotlin("jvm")
}

version = "1.0.0"

dependencies {
    implementation(project(":wrappers"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}