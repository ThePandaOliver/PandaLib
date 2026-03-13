plugins {
    kotlin("jvm")
    id("dev.architectury.loom") version "1.13-SNAPSHOT"
    id("com.gradleup.shadow") version "9.0.2" apply false
    id("com.google.devtools.ksp") version "2.3.0"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "com.google.devtools.ksp")

    val mcVersion: String by project
    val loaderName = properties["loom.platform"] as? String

    group = "dev.pandasystems"
    version = "1.0.0-ALPHA.3"
    base { archivesName = "pandalib-$loaderName-$mcVersion" }

    loom {
        decompilers {
            get("vineflower").apply { // Adds names to lambdas - useful for mixins
                options.put("mark-corresponding-synthetics", "1")
            }
        }

        if (loaderName != null) {
            runs {
                val path = project.projectDir.toPath().relativize(rootProject.file(".runs").toPath())

                named("client") {
                    client()
                    configName = "Client"
                    runDir("$path/client")
                    programArg("--username=Dev")
                }
                named("server") {
                    server()
                    configName = "Server"
                    runDir("$path/server")
                }
            }
        }

        runs.configureEach { ideConfigGenerated(false) }
    }

    val common: Configuration by configurations.creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    configurations.compileClasspath.get().extendsFrom(common)
    configurations.runtimeClasspath.get().extendsFrom(common)

    val shadowBundle: Configuration by configurations.creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    
    repositories {
        maven("https://maven.parchmentmc.org/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
    }

    val parchmentMinecraftVersion: String by project
    val parchmentMappingsVersion: String by project
    
    val fabricLoaderVersion: String? by project
    val fabricApiVersion: String? by project
    val neoforgeLoaderVersion: String? by project

    dependencies {
        runtimeOnly("com.google.auto.service:auto-service-annotations:1.1.1")
        compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
        ksp("dev.zacsweers.autoservice:auto-service-ksp:1.2.0")

        minecraft("com.mojang:minecraft:$mcVersion")
        @Suppress("UnstableApiUsage")
        mappings(loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-$parchmentMinecraftVersion:$parchmentMappingsVersion@zip")
        })

        when (loaderName) {
            "fabric" -> {
                modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
                modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

                common(project(":impl:21_11", configuration = "namedElements")) { isTransitive = false }
                shadowBundle(project(":impl:21_11", configuration = "transformProductionFabric"))
            }

            "neoforge" -> {
                "neoForge"("net.neoforged:neoforge:$neoforgeLoaderVersion")

                common(project(":impl:21_11", configuration = "namedElements")) { isTransitive = false }
                shadowBundle(project(":impl:21_11", configuration = "transformProductionNeoForge"))
            }

            else -> {
                // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
                // Do NOT use other classes from fabric loader
                modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
            }
        }

        fun dependNoneMod(dependencyNotation: Any?): Any? {
            if (dependencyNotation == null) return null
            implementation(dependencyNotation)
            if (loaderName == "neoforge") "forgeRuntimeLibrary"(dependencyNotation)
            return dependencyNotation
        }

        fun nestNoneMod(dependencyNotation: Any?): Any? {
            if (dependencyNotation == null) return null
            if (loaderName != null) include(dependencyNotation)
            return dependencyNotation
        }

        dependNoneMod(nestNoneMod(kotlin("stdlib")))
        dependNoneMod(nestNoneMod(kotlin("stdlib-jdk8")))
        dependNoneMod(nestNoneMod(kotlin("stdlib-jdk7")))
        dependNoneMod(nestNoneMod(kotlin("reflect")))
        dependNoneMod(nestNoneMod("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2"))
        dependNoneMod(nestNoneMod("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.10.2"))
        dependNoneMod(nestNoneMod("org.jetbrains.kotlinx:kotlinx-serialization-core:1.9.0"))
        dependNoneMod(nestNoneMod("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0"))
        dependNoneMod(nestNoneMod("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.9.0"))
        dependNoneMod(nestNoneMod("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1"))
        dependNoneMod(nestNoneMod("org.jetbrains.kotlinx:kotlinx-io-core:0.8.2"))
        dependNoneMod(nestNoneMod("org.jetbrains.kotlinx:kotlinx-io-bytestring:0.8.2"))
        
        dependNoneMod(nestNoneMod(project(":wrappers")))
        dependNoneMod(nestNoneMod(project(":config")))
    }
}