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
	- Custom Serializer API
	- Premade formats: JSON
- Event/Listener API
- Deferred registration API
- Networking API
	- Play phase support
	- Configuration phase support
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
| Config Serializers (TOML, YAML, XML, Properties) |                        |
| Config Menu API                                  | Multi-Loader Framework |
| Wiki / Docs                                      |                        |
| Custom Model Rendering API                       |                        |

### Supported versions and mod loaders:

| Mod loader | Versions        |
|------------|-----------------|
| Fabric     | 1.20 – 1.21.8   |
| Forge      | 1.20 – 1.20.4   |
| NeoForge   | 1.20.5 – 1.21.8 |

Development is targeted 1.21.8

---

### Development:

#### Kotlin DSL

```kotlin
repositories {
	mavenCentral()
	maven {
		name = "Github"
		url = uri("https://maven.pkg.github.com/ThePandaOliver/PandaLib")
		credentials {
			username = System.getenv("GITHUB_ACTER")
			password = System.getenv("GITHUB_TOKEN")
		}
	}
}

dependencies {
	implementation("dev.pandasystems:pandalib-forge:<version>") // Forge
	implementation("dev.pandasystems:pandalib-neoforge:<version>") // NeoForge
	modApi("dev.pandasystems:pandalib-fabric:<version>") // Fabric

	// Only needed if you want to use the embedded Kotlin libraries in Forge-like environments.
	// "additionalRuntimeClasspath(...)" works for NeoGradle,
	// but for ForgeGradle, then you need to replace "additionalRuntimeClasspath(...)" with "minecraftLibrary(...)",
	// for Architectury Loom, then you need to replace "additionalRuntimeClasspath(...)" with "forgeRuntimeLibrary(...)"
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

> ### Thanks to **Jetbrains** for supporting this project with their open source program.
> [<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.svg" width=300px>](https://jb.gg/OpenSourceSupport)

> ### Thanks to **Kinetic Hosting** for supporting this project
> [![Partner Banner](https://github.com/ThePandaOliver/ThePandaOliver/blob/main/assets_for_readme/Support/kinetic_hosting_banner.png?raw=true)](https://t.ly/B1Kui)
>
> Every purchased server via my [affiliate link](https://t.ly/B1Kui) will help support me and my work.

## License

The project is licensed under the GNU LGPLv3