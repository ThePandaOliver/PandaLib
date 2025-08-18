![banner.png](https://github.com/ThePandaOliver/Readme-Assets/blob/main/pandalib/banner.png?raw=true)

> [![Discord](https://img.shields.io/discord/1021703635178115122?style=for-the-badge&logo=discord&label=Discord&labelColor=black&color=lightblue)](https://discord.gg/wjPt4vEfXb)
> [![Modrinth](https://img.shields.io/modrinth/dt/mEEGbEIu?style=for-the-badge&logo=modrinth&label=Modrinth&labelColor=black&color=green)](https://modrinth.com/mod/pandalib)
> [![Curseforge](https://img.shields.io/curseforge/dt/975460?style=for-the-badge&logo=curseforge&label=Curseforge&labelColor=black&color=red)](https://www.curseforge.com/minecraft/mc-mods/pandalib)
> [![GitHub](https://img.shields.io/github/downloads/PandaDap2006/PandaLib/total?style=for-the-badge&logo=github&label=Github&labelColor=black&color=white)](https://github.com/PandaDap2006/PandaLib)
>
> [![Fabric API](https://img.shields.io/badge/Fabric%20API-REQUIRED%20for%20Fabric-1?style=for-the-badge&labelColor=black&color=gold)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## About:

PandaLib is a library mod for Minecraft that provides various APIs and utilities to simplify mod development.

The library is written in Kotlin, but it can be used for Java development as well.

### Current Features:

- Multi-Loader Framework (WIP)
- Config API
- Networking API
- Embedded Kotlin libraries

### Future Features:

| Planned                    | In development |
|----------------------------|----------------|
| Config Menu API            |                |
| Wiki / Docs                |                |
| Custom Model Rendering API |                |

### Targeted versions and mod loaders:

- Fabric and NeoForge 1.20.5 – 1.21.8
- Fabric and Forge 1.20 – 1.20.4

Development is done on 1.21.8

---

### Development:
<details>
<summary>Kotlin DSL</summary>

```kotlin
repositories {
	mavenCentral()
}

dependencies {
	// TODO: publish PandaLib to personel repository

	forgeRuntimeLibrary(kotlin("stdlib"))
	forgeRuntimeLibrary(kotlin("stdlib-jdk8"))
	forgeRuntimeLibrary(kotlin("stdlib-jdk7"))
	forgeRuntimeLibrary(kotlin("reflect", version = "2.2.0"))
	forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
	forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.10.2")
	forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.1")
	forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
	forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.8.1")
	forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
	forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-io-core:0.7.0")
	forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-io-bytestring:0.7.0")
}
```
</details>

---

## Advertisement:

> ### Thanks to **Jetbrains** for supporting this project with their open source program.\
> [<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.svg" width=300px>](https://jb.gg/OpenSourceSupport)

> ### Thanks to **Kinetic Hosting** for supporting this project
> [![Partner Banner](https://github.com/ThePandaOliver/Readme-Assets/blob/main/Support/Kinetic%20affiliate%20banner%20small.png?raw=true)](https://t.ly/B1Kui)
>
> Every purchased server via my [affiliate link](https://t.ly/B1Kui) will help support me and my work.
> On top of that, you can use code **PANDA** to get 15% off your first month.

## License

The project is licensed under the GNU LGPLv3