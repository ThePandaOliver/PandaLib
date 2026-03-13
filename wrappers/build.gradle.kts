plugins {
    kotlin("jvm")
}

version = "1.0.0"

dependencies {
    testImplementation(kotlin("test"))
    
    implementation("org.joml:joml:1.10.8")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}