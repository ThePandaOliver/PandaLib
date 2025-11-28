![banner.png](https://github.com/ThePandaOliver/Readme-Assets/blob/main/pandalib/banner.png?raw=true)

> [![Discord](https://img.shields.io/discord/1021703635178115122?style=for-the-badge&logo=discord&label=Discord&labelColor=black&color=lightblue)](https://discord.gg/wjPt4vEfXb)
> [![Modrinth](https://img.shields.io/modrinth/dt/mEEGbEIu?style=for-the-badge&logo=modrinth&label=Modrinth&labelColor=black&color=green)](https://modrinth.com/mod/pandalib)
> [![Curseforge](https://img.shields.io/curseforge/dt/975460?style=for-the-badge&logo=curseforge&label=Curseforge&labelColor=black&color=red)](https://www.curseforge.com/minecraft/mc-mods/pandalib)
> [![GitHub](https://img.shields.io/github/downloads/PandaDap2006/PandaLib/total?style=for-the-badge&logo=github&label=Github&labelColor=black&color=white)](https://github.com/PandaDap2006/PandaLib)
>
> [![Fabric API](https://img.shields.io/badge/Fabric%20API-REQUIRED%20for%20Fabric-1?style=for-the-badge&labelColor=black&color=gold)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## About:

PandaLib is a library mod for Minecraft that provides various APIs and utilities to simplify mod development.

The library is written in Kotlin, and some APIs might not be fully supported by Java.

### Current Features:

- Multi-Loader Framework (WIP)
- Config API
  - Synchronization API
  - Premade formats: JSON
- Event/Listener API
- Deferred registration API
- Networking API
  - Play phase support
  - Configuration phase support
- Embedded libraries
  - [Universal Serializer](https://github.com/ThePandaOliver/universal-serializer)
  - Embedded Kotlin libraries
    - Kotlin Standard Library
    - Reflect
    - Coroutines
    - Serialization
    - IO
    - DateTime

### Future Features:

| Planned                                          | In development         |
|--------------------------------------------------|------------------------|
| Config Serializers (TOML, YAML, XML, Properties) | Multi-Loader Framework |
| Config Menu API                                  |                        |
| Wiki / Docs                                      |                        |

### Supported versions and mod loaders:

| Mod loader | Versions          |
|------------|-------------------|
| Fabric     | 1.20 – 1.21.10    |
| NeoForge   | 1.20.5 – 1.21.10  |
| Forge      | Support has ended |

Development is targeted 1.21.10

---

### Development:

#### Looking for a specific version's codebase

- **1.21**
  - [1.21.10](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.10)
  - [1.21.9](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.9)
  - [1.21.8](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.8)
  - [1.21.7](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.7)
  - [1.21.6](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.6)
  - [1.21.5](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.5)
  - [1.21.4](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.4)
  - [1.21.3](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.3)
  - [1.21.2](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.2)
  - [1.21.1](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21.1)
  - [1.21.0](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.21)
- **1.20**
  - [1.20.6](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.20.6)
  - [1.20.5](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.20.5)
  - [1.20.4](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.20.4)
  - [1.20.3](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.20.3)
  - [1.20.2](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.20.2)
  - [1.20.1](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.20.1)
  - [1.20.0](https://github.com/ThePandaOliver/PandaLib/tree/versions/1.20)

#### Kotlin DSL

```kotlin
repositories {
	mavenCentral()
	maven {
		name = "Github"
		url = uri("https://repo.pandasystems.dev/repository/maven-public/")
	}
}

dependencies {
	modApi("dev.pandasystems:pandalib:<version>") // Common
	implementation("dev.pandasystems:pandalib-neoforge:<version>") // NeoForge
	modApi("dev.pandasystems:pandalib-fabric:<version>") // Fabric

	// Only needed if you want to use the embedded Kotlin libraries in NeoForge environments.
	additionalRuntimeClasspath(kotlin("stdlib"))
	additionalRuntimeClasspath(kotlin("stdlib-jdk8"))
	additionalRuntimeClasspath(kotlin("stdlib-jdk7"))
	additionalRuntimeClasspath(kotlin("reflect", version = "2.2.0"))
	additionalRuntimeClasspath("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
	additionalRuntimeClasspath("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.10.2")
	additionalRuntimeClasspath("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.1")
	additionalRuntimeClasspath("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
	additionalRuntimeClasspath("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.8.1")
	additionalRuntimeClasspath("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
	additionalRuntimeClasspath("org.jetbrains.kotlinx:kotlinx-io-core:0.7.0")
	additionalRuntimeClasspath("org.jetbrains.kotlinx:kotlinx-io-bytestring:0.7.0")
}
```

---

## Advertisement:

> ### Thanks to **Kinetic Hosting** for supporting this project
> [![Partner Banner](https://github.com/ThePandaOliver/ThePandaOliver/blob/main/assets_for_readme/Support/kinetic_hosting_banner.png?raw=true)](https://billing.kinetichosting.com/aff.php?aff=476)
>
> Every purchased server via my [affiliate link](https://billing.kinetichosting.com/aff.php?aff=476) will help support me and my work.

## License

The project is licensed under the GNU LGPLv3