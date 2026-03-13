plugins {
    kotlin("jvm") version "2.3.0"
}

val modVersion: String by project
val modGroup: String by project

allprojects {
    version = modVersion
    group = modGroup
    
    repositories {
        mavenCentral()
    }
}